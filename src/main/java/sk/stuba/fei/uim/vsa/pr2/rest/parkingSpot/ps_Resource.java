package sk.stuba.fei.uim.vsa.pr2.rest.parkingSpot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.rest.car.car_ResponseDto;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.PARKING_SPOT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/parkingspots")
public class ps_Resource {
    public static final String EMPTY_RESPONSE = "{}";

    private final ObjectMapper json = new ObjectMapper();
    private final CarParkService carParkService = new CarParkService();
    private final ps_Factory factory = new ps_Factory();


    /**
     * OK
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getParkingSpot(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization, @PathParam("id") Long id) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        if (id == null) return BadRequest();
        try {
            if (carParkService.getParkingSpot(id) == null) return NotFound();
            PARKING_SPOT ps = carParkService.getParkingSpot(id);
            ps_ResponseDto cpr = factory.transformToDto(ps);
            return json.writeValueAsString(cpr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return NoContent();
        }
    }


    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object putParkingSpot(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        return id;
    }

    /**
     * OK
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object deleteParkingSpot(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        if (id == null) {
            return BadRequest();
        }
        if (carParkService.getParkingSpot(id) == null) {
            return NotFound();
        }
        carParkService.deleteParkingSpot(id);
        return NoContent();
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
    private Response Unauthorized(){return Response.status(Response.Status.UNAUTHORIZED).build();}
}
