package sk.stuba.fei.uim.vsa.pr2.rest.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.rest.carPark.CarPark_Request;
import sk.stuba.fei.uim.vsa.pr2.rest.carParkFloor.cpf_ResponseDto;
import sk.stuba.fei.uim.vsa.pr2.rest.carType.carType_Factory;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.jar.JarException;
import java.util.stream.Collectors;

@Path("/reservations")
public class reservation_Resource {
    public static final String EMPTY_RESPONSE = "{}";

    private final ObjectMapper json = new ObjectMapper();
    private final CarParkService carParkService = new CarParkService();
    private final reservation_Factory factory = new reservation_Factory();


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Object getReservations(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization,
                                  @QueryParam("user") Long id,
                                  @QueryParam("spot") Long idSpot,
                                  @QueryParam("date") String datum) {
        String pouzivatel = carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        Date date1 = null;
        if (datum != null) {
            try {
                date1 = new SimpleDateFormat("yyyy-MM-dd").parse(datum);
            } catch (ParseException e) {
                return BadRequest();
            }
        }
        if (id == null && idSpot == null && date1 == null) {
            try {
                List<RESERVATION> reservations = carParkService.getAllReservations();
                List<reservation_ResponseDto> cpr = reservations.stream().map(factory::transformToDto).collect(Collectors.toList());
                return json.writeValueAsString(cpr);
            } catch (JsonProcessingException e) {
                return NoContent();
            }
        }
        if (id != null && idSpot == null && date1 == null) {
            try {
                if (carParkService.getUser(id) == null) return NotFound();
                List<RESERVATION> reservations = carParkService.getMyReservations(id);
                if (reservations == null) return NotFound();
                List<reservation_ResponseDto> cpr = reservations.stream().map(factory::transformToDto).collect(Collectors.toList());
                return json.writeValueAsString(cpr);

            } catch (JsonProcessingException e) {
                return NoContent();
            }
        }
        if (id == null && idSpot != null && date1 != null) {
            try {
                if (carParkService.getParkingSpot(idSpot) == null) return BadRequest();
                List<RESERVATION> reservations = carParkService.getReservations(idSpot, date1);
                if (reservations == null) return NotFound();
                List<reservation_ResponseDto> cpr = reservations.stream().map(factory::transformToDto).collect(Collectors.toList());
                return json.writeValueAsString(cpr);
            } catch (JsonProcessingException e) {
                return NoContent();
            }
        }
        if (id == null && idSpot == null && date1 != null) {
            return BadRequest();
        }
        if (id == null && idSpot != null && date1 == null) {
            return BadRequest();
        }

        return BadRequest();
    }

    /**
     * OK
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getReservation(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization, @PathParam("id") Long id) {
        String pouzivatel = carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        if (id == null) return null;
        try {
            if (carParkService.getReservation(id) == null) return BadRequest();
            RESERVATION reservation = carParkService.getReservation(id);
            reservation_ResponseDto responseDto = factory.transformToDto(reservation);
            return json.writeValueAsString(responseDto);
        } catch (JsonProcessingException e) {
            return NoContent();
        }

    }

    /**
     * OK
     */
    @POST
    @Path("/{id}/end")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Object postReservationEnd(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization, @PathParam("id") Long id) {
        String pouzivatel = carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        if (id == null) return BadRequest();
        try {
            RESERVATION reservation = carParkService.getReservation(id);
            if (reservation == null) return NotFound();

            reservation = carParkService.endReservation(id);
            reservation_ResponseDto responseDto = factory.transformToDto(reservation);
            return (json.writeValueAsString(responseDto));
        } catch (JsonProcessingException e) {
            return NoContent();
        }
    }

    /**
     * OK
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Object postReservation(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization, String requestBody) {
        String pouzivatel = carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        try {
            reservation_Request dto = json.readValue(requestBody, reservation_Request.class);
            PARKING_SPOT spot = carParkService.getParkingSpot(dto.getSpot().getCarParkFloor(), dto.getSpot().getIdentifier());
            CAR car = carParkService.getCar(dto.getCar().getVrp());
            if (car == null) return BadRequest();
            if (spot == null) return NotFound();
            if (car.getReservations() != null) return BadRequest();
            if (spot.getReservations() != null) return BadRequest();

            CAR_TYPE auto = car.getType();
            CAR_TYPE miesto = spot.getType();
            if (auto.equals(miesto)) return BadRequest();

            RESERVATION reservation = carParkService.createReservation(spot.getId(), car.getId());
            if (dto.getId() != null) reservation.setId(dto.getId());
            if (reservation == null) return BadRequest();
            reservation_ResponseDto responseDto = factory.transformToDto(reservation);
            return Response.status(Response.Status.CREATED).entity(json.writeValueAsString(responseDto)).build();

        } catch (JsonProcessingException e) {
            return NoContent();
        }

    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object putReservation(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization, @PathParam("id") Long id) {
        String pouzivatel = carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        return "ahoj";
    }


    private Response NoContent() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    private Response NotFound() {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private Response BadRequest() {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    private Response Created() {
        return Response.status(Response.Status.CREATED).build();
    }

    private Response Unauthorized() {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
