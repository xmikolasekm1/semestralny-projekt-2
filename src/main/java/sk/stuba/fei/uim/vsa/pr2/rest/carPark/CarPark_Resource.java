package sk.stuba.fei.uim.vsa.pr2.rest.carPark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.rest.carParkFloor.cpf_Factory;
import sk.stuba.fei.uim.vsa.pr2.rest.carParkFloor.cpf_Request;
import sk.stuba.fei.uim.vsa.pr2.rest.carParkFloor.cpf_ResponseDto;
import sk.stuba.fei.uim.vsa.pr2.rest.parkingSpot.ps_Factory;
import sk.stuba.fei.uim.vsa.pr2.rest.parkingSpot.ps_Request;
import sk.stuba.fei.uim.vsa.pr2.rest.parkingSpot.ps_ResponseDto;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.*;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/carparks")
public class CarPark_Resource {

    public static final String EMPTY_RESPONSE = "{}";

    private final ObjectMapper json = new ObjectMapper();
    private final CarParkService carParkService = new CarParkService();
    private final CarPark_Factory factory = new CarPark_Factory();
    private final cpf_Factory factory2 = new cpf_Factory();
    private final ps_Factory factory3 = new ps_Factory();


    /**
     * OK
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Object getCarParks(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@QueryParam("name") String name) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        if (name == null) {
            try {
                List<CAR_PARK> carParks = carParkService.getCarParks();
                List<CarPark_ResponseDto> cpr = carParks.stream().map(factory::transformToDto).collect(Collectors.toList());
                return json.writeValueAsString(cpr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return EMPTY_RESPONSE;
            }
        }
        if (name != null) {
            try {
                if (carParkService.getCarPark(name) == null) return NotFound();
                Object cp = carParkService.getCarPark(name);
                CAR_PARK carPark = (CAR_PARK) cp;
                CarPark_ResponseDto cpr = factory.transformToDto(carPark);
                return json.writeValueAsString(cpr);
            } catch (JsonProcessingException e) {
                return EMPTY_RESPONSE;
            }
        }
        return EMPTY_RESPONSE;
    }

    /**
     * OK
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getCarParksId(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id) {
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
            if (carParkService.getCarPark(id) == null) return NotFound();
            Object cp = carParkService.getCarPark(id);
            CAR_PARK carPark = (CAR_PARK) cp;
            CarPark_ResponseDto cpr = factory.transformToDto(carPark);
            return json.writeValueAsString(cpr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.noContent().build();
        }

    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Object postCarParks(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,String requestBody) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        try {
            CarPark_Request dto = json.readValue(requestBody, CarPark_Request.class);

            if (dto.getFloors() == null) {
                CAR_PARK carPark = carParkService.createCarPark(dto.getName(), dto.getAddress(), dto.getPrices(), dto.getId());
                if (dto.getId()!=null)carPark.setId(dto.getId());
                CarPark_ResponseDto responseDto = factory.transformToDto(carPark);
                return Response.status(Response.Status.CREATED).entity(json.writeValueAsString(responseDto)).build();
            } else {
                if (carParkService.getCarPark(dto.getId()) != null) return BadRequest();
                CAR_PARK carPark = carParkService.createCarPark(dto.getName(), dto.getAddress(), dto.getPrices(), dto.getId());
                if (dto.getId()!=null)carPark.setId(dto.getId());

                List<CAR_PARK_FLOOR> carParkFloors = dto.getFloors();
                List<CAR_PARK_FLOOR> poschodia = new ArrayList<>();

                for (CAR_PARK_FLOOR carParkFloor : carParkFloors) {
                    if (carParkService.getCarParkFloor(carPark.getId(), carParkFloor.getIdentifier()) != null) {
                        carParkService.deleteCarPark(carPark.getId());
                        return BadRequest();
                    }
                    CAR_PARK_FLOOR cpf = carParkService.createCarParkFloor(carPark.getId(), carParkFloor.getIdentifier());

                    List<PARKING_SPOT> parkingSpotList = carParkFloor.getSpots();
                    if (parkingSpotList == null || parkingSpotList.isEmpty()) {
                        carParkFloor.setSpots(Collections.emptyList());
                    } else {
                        List<PARKING_SPOT> parkingSpots = carParkFloor.getSpots();
                        if (parkingSpots.isEmpty() == false) {
                            List<PARKING_SPOT> miesta = new ArrayList<>();

                            for (PARKING_SPOT ps : parkingSpots) {
                                if (ps.getType()==null){
                                    carParkService.deleteCarPark(carPark.getId());
                                    return BadRequest();
                                }

                                if (carParkService.getCarType(ps.getType().getName()) == null) {
                                    carParkService.deleteCarPark(carPark.getId());
                                    return BadRequest();
                                }

                                if (carParkService.getParkingSpot(ps.getCarParkFloor(), ps.getIdentifier()) != null) {
                                    carParkService.deleteCarPark(carPark.getId());
                                    return BadRequest();
                                }

                                CAR_TYPE carType = carParkService.getCarType(ps.getType().getName());
                                PARKING_SPOT parkingSpot = carParkService.createParkingSpot(carPark.getId(), carParkFloor.getIdentifier(), ps.getIdentifier(), carType.getId());

                                if (ps.getId() != null) parkingSpot.setId(ps.getId());


                                miesta.add(parkingSpot);
                            }
                            cpf.setSpots(miesta);
                        }
                        if (parkingSpots.isEmpty() || parkingSpots == null) {
                            cpf.setSpots(Collections.emptyList());
                        }
                    }
                    poschodia.add(cpf);
                }
                carPark.setFloors(poschodia);


                CarPark_ResponseDto responseDto = factory.transformToDto(carPark);
                return Response.status(Response.Status.CREATED).entity(json.writeValueAsString(responseDto)).build();
            }


        } catch (JsonProcessingException e) {
            return NoContent();
        }
    }


    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Object putCarParksId(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id,String requestBody) {
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
            CarPark_Request dto = json.readValue(requestBody, CarPark_Request.class);
            CAR_PARK cp=carParkService.getCarPark(id);
            if (cp==null){
                return NotFound();
            }

            if (dto.getAddress() == null || dto.getName() == null || dto.getPrices() == null) {
                return BadRequest();
            }

            cp.setName(dto.getName());
            cp.setAddress(dto.getAddress());
            cp.setPrices(dto.getPrices());

            carParkService.updateCarPark(cp);

            cp = carParkService.getCarPark(id);
            return Response.ok((cp)).build();

        }catch (JsonProcessingException e){
            return BadRequest();
        }



    }


    /**
     * OK
     */
    @DELETE
    @Path("/{id}")
    public Object deleteCarParksId(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        if (id == null) return BadRequest();
        if (carParkService.getCarPark(id) == null) return BadRequest();
        carParkService.deleteCarPark(id);
        return NoContent();

    }

    /**
     * OK
     */
    @GET
    @Path("/{id}/spots")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getCarParksSpots(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id, @QueryParam("free") Boolean free) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        if (id == null) return BadRequest();
        if (carParkService.getCarPark(id) == null) return BadRequest();
        if (free == null) {
            try {
                ps_Factory factory = new ps_Factory();
                if (carParkService.getParkingSpots(id) == null) return BadRequest();
                Map<String, List<PARKING_SPOT>> parkingSpotsMap = carParkService.getParkingSpots(id);
                List<PARKING_SPOT> parkingSpots = new ArrayList<>();
                for (Map.Entry<String, List<PARKING_SPOT>> map : parkingSpotsMap.entrySet()) {
                    List<PARKING_SPOT> poschodia = map.getValue();
                    for (PARKING_SPOT p : poschodia) {
                        parkingSpots.add(p);
                    }
                }

                List<ps_ResponseDto> cpr = parkingSpots.stream().map(factory::transformToDto).collect(Collectors.toList());
                return json.writeValueAsString(cpr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return NoContent();
            }
        }
        if (free == true) {
            try {
                ps_Factory factory = new ps_Factory();
                CAR_PARK car_park = carParkService.getCarPark(id);
                if (carParkService.getAvailableParkingSpots(car_park.getName()) == null) return BadRequest();
                Map<String, List<PARKING_SPOT>> parkingSpotsMap = carParkService.getAvailableParkingSpots(car_park.getName());
                List<PARKING_SPOT> parkingSpots = new ArrayList<>();
                for (Map.Entry<String, List<PARKING_SPOT>> map : parkingSpotsMap.entrySet()) {
                    List<PARKING_SPOT> poschodia = map.getValue();
                    for (PARKING_SPOT p : poschodia) {
                        parkingSpots.add(p);
                    }
                }

                List<ps_ResponseDto> cpr = parkingSpots.stream().map(factory::transformToDto).collect(Collectors.toList());
                return json.writeValueAsString(cpr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return NoContent();
            }
        } else {
            try {
                ps_Factory factory = new ps_Factory();
                CAR_PARK car_park = carParkService.getCarPark(id);
                if (carParkService.getOccupiedParkingSpots(car_park.getName()) == null) return BadRequest();
                Map<String, List<PARKING_SPOT>> parkingSpotsMap = carParkService.getOccupiedParkingSpots(car_park.getName());
                List<PARKING_SPOT> parkingSpots = new ArrayList<>();
                for (Map.Entry<String, List<PARKING_SPOT>> map : parkingSpotsMap.entrySet()) {
                    List<PARKING_SPOT> poschodia = map.getValue();
                    for (PARKING_SPOT p : poschodia) {
                        parkingSpots.add(p);
                    }
                }

                List<ps_ResponseDto> cpr = parkingSpots.stream().map(factory::transformToDto).collect(Collectors.toList());
                return json.writeValueAsString(cpr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return NoContent();
            }
        }
    }

    /**
     * OK
     */
    @GET
    @Path("/{id}/floors/{identifier}/spots")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getParkingSpots(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id, @PathParam("identifier") String identifier) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        if (id == null || identifier == null) return BadRequest();
        try {
            ps_Factory factory = new ps_Factory();
            if (carParkService.getParkingSpots(id, identifier) == null) return BadRequest();
            if (carParkService.getCarParkFloor(id, identifier) == null) return BadRequest();
            if (carParkService.getCarPark(id) == null) return BadRequest();
            List<PARKING_SPOT> parkingSpots = carParkService.getParkingSpots(id, identifier);
            List<ps_ResponseDto> cpr = parkingSpots.stream().map(factory::transformToDto).collect(Collectors.toList());
            return json.writeValueAsString(cpr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return NoContent();
        }

    }


    @GET
    @Path("/{id}/floors")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getCarParksFloorsId(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id) {
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
            if (carParkService.getCarParkFloors(id) == null) return BadRequest();
            List<CAR_PARK_FLOOR> carParkFloor = carParkService.getCarParkFloors(id);
            List<cpf_ResponseDto> cpr = carParkFloor.stream().map(factory2::transformToDto).collect(Collectors.toList());
            return json.writeValueAsString(cpr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return NoContent();
        }
    }


    /**
     * OK
     */
    @GET
    @Path("/{id}/floors/{identifier}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getCarParksFloors(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id, @PathParam("identifier") String identifier) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        if (id == null || identifier == null) return BadRequest();
        try {
            if (carParkService.getCarParkFloor(id, identifier) == null) return NotFound();
            CAR_PARK_FLOOR carParkFloor = carParkService.getCarParkFloor(id, identifier);
            cpf_ResponseDto cpr = factory2.transformToDto(carParkFloor);
            return json.writeValueAsString(cpr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return NoContent();
        }
    }

    @POST
    @Path("/{id}/floors")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Object postCarParksFloors(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id, String requestBody) {
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
            cpf_Request dto = json.readValue(requestBody, cpf_Request.class);
            List<PARKING_SPOT> parkingSpotList = dto.getSpots();
            if (parkingSpotList == null) {
                if (carParkService.getCarParkFloor(id, dto.getIdentifier()) != null) return BadRequest();
                CAR_PARK_FLOOR cpf = carParkService.createCarParkFloor(id, dto.getIdentifier());
                if (dto.getId()!=null)cpf.setId(dto.getId());
                cpf_ResponseDto responseDto = factory2.transformToDto(cpf);
                return Response.status(Response.Status.CREATED).entity(json.writeValueAsString(responseDto)).build();
            } else {
                if (carParkService.getCarParkFloor(id, dto.getIdentifier()) != null) return BadRequest();
                CAR_PARK_FLOOR cpf = carParkService.createCarParkFloor(id, dto.getIdentifier());
                List<PARKING_SPOT> ps = cpf.getSpots();
                List<PARKING_SPOT> spots = new ArrayList<>();
                for (PARKING_SPOT pspot : ps) {
                    if (carParkService.getParkingSpots(id, cpf.getIdentifier()) != null) return BadRequest();
                    PARKING_SPOT parkingSpot = carParkService.createParkingSpot(id, cpf.getIdentifier(), pspot.getIdentifier(), pspot.getType().getId());
                    spots.add(parkingSpot);
                }
                cpf.setSpots(spots);
                cpf_ResponseDto responseDto = factory2.transformToDto(cpf);
                return Response.status(Response.Status.CREATED).entity(json.writeValueAsString(responseDto)).build();
            }
        } catch (JsonProcessingException e) {
            return NoContent();
        }

    }

    @PUT
    @Path("/{id}/floors/{identifier}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object putCarParksId(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        return NoContent();
    }

    /**
     * OK
     */
    @DELETE
    @Path("/{id}/floors/{identifier}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object deleteCarParkFloors(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id, @PathParam("identifier") String identifier) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        if (id == null || identifier == null) return BadRequest();
        if (carParkService.deleteCarParkFloor(id, identifier) == null) return BadRequest();
        carParkService.deleteCarParkFloor(id, identifier);
        return NoContent();
    }

    @POST
    @Path("/{id}/floors/{identifier}/spots")
    @Produces(MediaType.APPLICATION_JSON)
    public Object postParkingSpot(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id, @PathParam("identifier") String identifier, String requestBody) {
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
            ps_Request dto = json.readValue(requestBody, ps_Request.class);
            if (carParkService.getCarPark(id)==null)return BadRequest();
            if (carParkService.getCarParkFloor(id,identifier)==null)return BadRequest();
            if (carParkService.getParkingSpot(dto.getId())!=null)return BadRequest();
            if (carParkService.getParkingSpot(identifier,dto.getIdentifier())!=null)return BadRequest();

            CAR_TYPE carType=carParkService.getCarType(dto.getType().getName());
            if (carType==null)return BadRequest();
            PARKING_SPOT parkingSpot=carParkService.createParkingSpot(id,identifier,dto.getIdentifier(),carType.getId());
            if (parkingSpot==null)return BadRequest();
            if(dto.getId()!=null)parkingSpot.setId(dto.getId());
            ps_ResponseDto responseDto=factory3.transformToDto(parkingSpot);
            return Response.status(Response.Status.CREATED).entity(json.writeValueAsString(responseDto)).build();
        }catch (JsonProcessingException e){
            return NoContent();
        }
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

    private Response Unauthorized(){return Response.status(Response.Status.UNAUTHORIZED).build();}
}
