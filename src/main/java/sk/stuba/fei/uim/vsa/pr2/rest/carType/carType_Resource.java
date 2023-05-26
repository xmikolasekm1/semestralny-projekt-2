package sk.stuba.fei.uim.vsa.pr2.rest.carType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR_TYPE;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CarParkService;

import java.util.List;
import java.util.stream.Collectors;

@Path("/cartypes")

public class carType_Resource {

    public static final String EMPTY_RESPONSE = "{}";

    private final ObjectMapper json = new ObjectMapper();
    private final CarParkService carParkService = new CarParkService();
    private final carType_Factory factory = new carType_Factory();

    /**
     * OK
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Object getCarTypes(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@QueryParam("name") String name) {
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
                List<CAR_TYPE> carTypes = carParkService.getCarTypes();
                List<carType_ResponseDto> carTypeResponseDtos = carTypes.stream().map(factory::transformToDto).collect(Collectors.toList());
                return json.writeValueAsString(carTypeResponseDtos);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return NoContent();
            }
        } else {
            try {
                if (carParkService.getCarType(name) == null) return NotFound();
                CAR_TYPE carType = carParkService.getCarType(name);
                carType_ResponseDto carTypeResponseDto = factory.transformToDto(carType);
                return json.writeValueAsString(carTypeResponseDto);
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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getCarType(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id) {
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
            if (carParkService.getCarType(id) == null) return NotFound();
            Object ct = carParkService.getCarType(id);
            CAR_TYPE carType = (CAR_TYPE) ct;
            if (carType==null)return BadRequest();
            carType_ResponseDto cpr = factory.transformToDto(carType);
            return json.writeValueAsString(cpr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return NoContent();
        }


    }

    /**
     * OK
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Object postCarType(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,String requestBody) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        try {
            carType_Request dto = json.readValue(requestBody, carType_Request.class);

            if (dto.getId() != null) {
                CAR_TYPE type=factory.transformToEntity(dto);
                if (carParkService.getCarType(type.getId()) != null)
                    return BadRequest();
                CAR_TYPE carType = carParkService.createCarType(factory.transformToEntity(dto).getId(), factory.transformToEntity(dto).getName());
                carType_ResponseDto responseDto = factory.transformToDto(carType);
                return Response.status(Response.Status.CREATED).entity(json.writeValueAsString(responseDto)).build();
            }

            if (dto.getId() == null) {
                CAR_TYPE type=factory.transformToEntity(dto);
                if (carParkService.getCarType(type.getName()) != null)
                    return BadRequest();
                CAR_TYPE carType = carParkService.createCarType(factory.transformToEntity(dto).getName());
                carType_ResponseDto responseDto = factory.transformToDto(carType);
                return Response.status(Response.Status.CREATED).entity(json.writeValueAsString(responseDto)).build();
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return NoContent();
        }
        return BadRequest();
    }

    /**
     * OK
     */
    @DELETE
    @Path("/{id}")
    public Object deleteCarTypeId(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        if (id == null) return BadRequest();
        if (carParkService.getCarType(id)==null)return NotFound();
        if (carParkService.deleteCarType(id) == null) return BadRequest();
        carParkService.deleteCarType(id);
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

    private Response Unauthorized(){return Response.status(Response.Status.UNAUTHORIZED).build();}
}
