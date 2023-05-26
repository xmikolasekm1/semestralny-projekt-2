package sk.stuba.fei.uim.vsa.pr2.rest.carPark;

import sk.stuba.fei.uim.vsa.pr2.rest.ResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR_PARK;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR_PARK_FLOOR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarPark_Factory implements ResponseFactory<CAR_PARK, CarPark_ResponseDto,CarPark_Request> {

    @Override
    public CarPark_ResponseDto transformToDto(CAR_PARK entity) {
        CarPark_ResponseDto carPark_response = new CarPark_ResponseDto();
        carPark_response.setId(entity.getId());
        carPark_response.setName(entity.getName());
        carPark_response.setAddress(entity.getAddress());
        carPark_response.setPrices(entity.getPrices());

        if (entity.getFloors()==null){
            carPark_response.setFloors(Collections.emptyList());
        }
        else{
            List<CAR_PARK_FLOOR> cpf=entity.getFloors();
            List<String> poschodia=new ArrayList<>();
            for(CAR_PARK_FLOOR c:cpf){
                poschodia.add(c.toString());
            }
            carPark_response.setFloors(poschodia);
        }
        return carPark_response;
    }


    @Override
    public CAR_PARK transformToEntity(CarPark_Request dto) {
        CAR_PARK car_park = new CAR_PARK();
        car_park.setId(dto.getId());
        car_park.setName(dto.getName());
        car_park.setAddress(dto.getAddress());
        car_park.setPrices(dto.getPrices());

        if(dto.getFloors()!=null){
        List<CAR_PARK_FLOOR> carParkFloors=dto.getFloors();
        car_park.setFloors(carParkFloors);}
        else {
            car_park.setFloors(Collections.emptyList());
        }

        return car_park;
    }



}
