package sk.stuba.fei.uim.vsa.pr2.rest.car;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.rest.user.user_Factory;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR_TYPE;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.USER;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/cars")
public class car_Resource {

    public static final String EMPTY_RESPONSE = "{}";

    private final ObjectMapper json = new ObjectMapper();
    private final CarParkService carParkService = new CarParkService();
    private final car_Factory factory = new car_Factory();


    /**
     * OK
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Object getCars(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization, @QueryParam("user") Long user, @QueryParam("vrp") String vrp) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }



    if (user == null && vrp == null) {
            try {
                if (carParkService.getCars() == null) return NotFound();
                List<CAR> cars = carParkService.getCars();
                List<car_ResponseDto> carsResponseDtos = cars.stream().map(factory::transformToDto).collect(Collectors.toList());
                return json.writeValueAsString(carsResponseDtos);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return NoContent();
            }
        }
        /**
         * OK
         */
        if (user != null && vrp == null) {
            try {
                if (carParkService.getCars(user) == null) return NotFound();
                List<CAR> cars = carParkService.getCars(user);
                List<car_ResponseDto> carsResponseDtos = cars.stream().map(factory::transformToDto).collect(Collectors.toList());
                return json.writeValueAsString(carsResponseDtos);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return NoContent();
            }
        }

        /**
         * OK
         */
        if (user == null && vrp != null) {
            try {
                if (carParkService.getCar(vrp) == null) return NotFound();
                CAR car = carParkService.getCar(vrp);
                car_ResponseDto carsResponseDtos = factory.transformToDto(car);
                return json.writeValueAsString(carsResponseDtos);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return NoContent();
            }
        }

        /**
         * OK
         */
        if (user != null && vrp != null) {
            try {
                if (carParkService.getCar(vrp) == null) return NotFound();
                if (carParkService.getCar(user) == null) return NotFound();

                CAR car = carParkService.getCar(vrp);
                CAR car2 = carParkService.getCar(user);
                if (car.getOwner().getId() == car2.getOwner().getId()) {
                    car_ResponseDto carsResponseDtos = factory.transformToDto(car);
                    return json.writeValueAsString(carsResponseDtos);
                }else{
                    return BadRequest();
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return NoContent();
            }
        }
        return BadRequest();
    }


    /**
     * OK
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getCar(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id) {
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
            if (carParkService.getCar(id) == null) return NotFound();
            CAR car = carParkService.getCar(id);
            car_ResponseDto cpr = factory.transformToDto(car);
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
    public Object postCar(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,String requestBody) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }

        try {
            car_Request dto = json.readValue(requestBody, car_Request.class);


            if (dto.getOwner().getEmail()==null || dto.getOwner().getFirstName()==null ||dto.getOwner().getLastName()==null )return BadRequest();

            if (dto.getOwner().getEmail() != null) {

                if (carParkService.getUser(dto.getOwner().getId()) != null) {
                    USER user = carParkService.getUser(dto.getOwner().getId());

                    if (carParkService.getCarType(dto.getType().getName()) == null) {
                        return BadRequest();
                    }
                    CAR_TYPE carType = carParkService.getCarType(dto.getType().getName());
                    CAR car = carParkService.createCar(user.getId(), dto.getBrand(), dto.getModel(), dto.getColour(), dto.getVrp(), carType.getId());
                    if (dto.getId() != null) car.setId(dto.getId());
                    if (car == null) return BadRequest();
                    List<CAR> cars=new ArrayList<>();
                    cars.add(car);
                    user.setCars(cars);

                    car_ResponseDto responseDto = factory.transformToDto(car);
                    return Response.status(Response.Status.CREATED).entity(json.writeValueAsString(responseDto)).build();
                } else {
                    USER user = carParkService.createUser(dto.getOwner().getId(), dto.getOwner().getFirstName(), dto.getOwner().getLastName(), dto.getOwner().getEmail());
                    if (carParkService.getCarType(dto.getType().getName()) == null) {
                        carParkService.createCarType(dto.getType().getName());
                    }
                    CAR_TYPE carType = carParkService.getCarType(dto.getType().getName());
                    CAR car = carParkService.createCar(user.getId(), dto.getBrand(), dto.getModel(), dto.getColour(), dto.getVrp(), carType.getId());
                    if (dto.getId() != null) car.setId(dto.getId());
                    if (car == null) return BadRequest();
                    List<CAR> cars=new ArrayList<>();
                    cars.add(car);
                    user.setCars(cars);
                    carParkService.updateUser(user);
                    car_ResponseDto responseDto = factory.transformToDto(car);
                    return Response.status(Response.Status.CREATED).entity(json.writeValueAsString(responseDto)).build();
                }
            }

            if (dto.getOwner().getId() == null) {
                if (carParkService.getUser(dto.getOwner().getEmail()) != null) {
                    USER user = carParkService.getUser(dto.getOwner().getEmail());
                    if (carParkService.getCarType(dto.getType().getName()) == null) {
                        carParkService.createCarType(dto.getType().getName());
                    }
                    CAR_TYPE carType = carParkService.getCarType(dto.getType().getName());
                    CAR car = carParkService.createCar(user.getId(), dto.getBrand(), dto.getModel(), dto.getColour(), dto.getVrp(), carType.getId());
                    if (dto.getId() != null) car.setId(dto.getId());
                    if (car == null) return BadRequest();
                    List<CAR> cars=new ArrayList<>();
                    cars.add(car);
                    user.setCars(cars);
                    carParkService.updateUser(user);
                    car_ResponseDto responseDto = factory.transformToDto(car);
                    return Response.status(Response.Status.CREATED).entity(json.writeValueAsString(responseDto)).build();
                } else {
                    USER user = carParkService.createUser(dto.getOwner().getFirstName(), dto.getOwner().getLastName(), dto.getOwner().getEmail());
                    if (carParkService.getCarType(dto.getType().getName()) == null) {
                        carParkService.createCarType(dto.getType().getName());
                    }
                    CAR_TYPE carType = carParkService.getCarType(dto.getType().getName());
                    CAR car = carParkService.createCar(user.getId(), dto.getBrand(), dto.getModel(), dto.getColour(), dto.getVrp(), carType.getId());
                    if (dto.getId() != null) car.setId(dto.getId());
                    if (car == null) return BadRequest();
                    List<CAR> cars=new ArrayList<>();
                    cars.add(car);
                    user.setCars(cars);
                    carParkService.updateUser(user);
                    car_ResponseDto responseDto = factory.transformToDto(car);
                    return Response.status(Response.Status.CREATED).entity(json.writeValueAsString(responseDto)).build();

                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return EMPTY_RESPONSE;
        }
        return 0;
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object putCarId(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }
        return BadRequest();
    }


    /**
     * OK
     */
    @DELETE
    @Path("/{id}")
    public Object deleteCarId(@HeaderParam(HttpHeaders.AUTHORIZATION)String authorization,@PathParam("id") Long id) {
        String pouzivatel=carParkService.getEmail(authorization);
        Long heslo=carParkService.getPassword(authorization);
        if (carParkService.getUser(pouzivatel)==null)return Unauthorized();
        if (carParkService.getUser(pouzivatel)!=null){
            if (carParkService.getUser(pouzivatel).getId()!=(heslo)){
                return Unauthorized();
            }
        }

        if (id == null) return BadRequest();
        if (carParkService.deleteCar(id) == null) return BadRequest();
        carParkService.deleteCar(id);
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
