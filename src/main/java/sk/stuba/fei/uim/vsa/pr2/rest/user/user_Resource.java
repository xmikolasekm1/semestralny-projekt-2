package sk.stuba.fei.uim.vsa.pr2.rest.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.rest.carPark.CarPark_Request;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR_PARK;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.USER;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users")
public class user_Resource {

    public static final String EMPTY_RESPONSE = "{}";

    private final ObjectMapper json = new ObjectMapper();
    private final CarParkService carParkService = new CarParkService();
    private final user_Factory factory = new user_Factory();

    /**
     * OK
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Object getUsers(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@QueryParam("email") String email) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        if (email == null) {
            try {
                List<USER> users = carParkService.getUsers();
                List<user_ResponseDto> usersResponseDtos = users.stream().map(factory::transformToDto).collect(Collectors.toList());
                return json.writeValueAsString(usersResponseDtos);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return NoContent();
            }
        } else {
            try {
                if (carParkService.getUser(email) == null) return NotFound();
                USER user = carParkService.getUser(email);
                user_ResponseDto userResponseDto = factory.transformToDto(user);
                return json.writeValueAsString(userResponseDto);
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
    public Object getUsers(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id) {
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
            if (carParkService.getUser(id) == null) return NotFound();
            USER user = carParkService.getUser(id);
            user_ResponseDto userResponseDto = factory.transformToDto(user);
            return json.writeValueAsString(userResponseDto);
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
    public Object postUser(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,String requestBody) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId() != heslo){
                return Unauthorized();
            }
        }


        try {
            user_Request dto = json.readValue(requestBody, user_Request.class);
            if (dto.getCars() == null) {
                if (carParkService.getUser(dto.getEmail()) != null) return BadRequest();
                if (carParkService.getUser(dto.getId()) != null) return BadRequest();
                USER user = carParkService.createUser((dto).getFirstName(), (dto).getLastName(), (dto).getEmail());
                if (dto.getId() != null) {
                    user.setId(dto.getId());
                }
                user_ResponseDto responseDto = factory.transformToDto(user);
                return Response.status(Response.Status.CREATED).entity(json.writeValueAsString(responseDto)).build();
            } else {

                if (carParkService.getUser(dto.getEmail()) != null) return BadRequest();
                if (carParkService.getUser(dto.getId()) != null) return BadRequest();
                USER user = carParkService.createUser(dto.getFirstName(), (dto).getLastName(), (dto).getEmail());
                if (dto.getId() != null) {
                    user.setId(dto.getId());
                }

                List<CAR> cars = dto.getCars();
                List<CAR> auta = user.getCars();

                for (CAR c : cars) {
                    if (c.getOwner() == null) return BadRequest();

                    if (carParkService.getCar(c.getVrp()) != null) {
                        carParkService.deleteUser(user.getId());
                        return BadRequest();
                    }


                    if (carParkService.getCarType(c.getType().getName()) == null) {
                        carParkService.createCarType(c.getType().getName());
                    }

                    CAR car = carParkService.createCar(user.getId(), c.getBrand(), c.getModel(), c.getColour(), c.getVrp(), carParkService.getCarType(c.getType().getName()).getId());
                    auta.add(car);
                }
                user.setCars(auta);

                user_ResponseDto responseDto = factory.transformToDto(user);
                return Response.status(Response.Status.CREATED).entity(json.writeValueAsString(responseDto)).build();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return EMPTY_RESPONSE;
        }

    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Object putUser(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id,String requestBody) {
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
            user_Request dto = json.readValue(requestBody, user_Request.class);
            USER cp=carParkService.getUser(id);
            if (cp==null){
                return NotFound();
            }

            if (dto.getFirstName() == null || dto.getLastName() == null || dto.getEmail() == null) {
                return BadRequest();
            }

            cp.setFirstName(dto.getFirstName());
            cp.setLastName(dto.getLastName());
            cp.setEmail(dto.getEmail());

            carParkService.updateCarPark(cp);

            cp = carParkService.getUser(id);
            return Response.ok((cp)).build();

        }catch (JsonProcessingException e){
            return BadRequest();
        }

    }

    @DELETE
    @Path("/{id}")
    public Object deleteUser(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization, @PathParam("id") Long id) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }


        if (id == null) return BadRequest();
        if (carParkService.deleteUser(id) == null) return BadRequest();
        carParkService.deleteUser(id);
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
