package sk.stuba.fei.uim.vsa.pr2.rest.car;

import sk.stuba.fei.uim.vsa.pr2.rest.ResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR;

import java.util.Collections;

public class car_Factory implements ResponseFactory<CAR, car_ResponseDto,car_Request> {

    @Override
    public car_ResponseDto transformToDto(CAR entity) {
        car_ResponseDto carResponseDto = new car_ResponseDto();
        carResponseDto.setId(entity.getId());
        carResponseDto.setModel(entity.getModel());
        carResponseDto.setBrand(entity.getBrand());
        carResponseDto.setColour(entity.getColour());
        carResponseDto.setVrp(entity.getVrp());

        carResponseDto.setOwner(entity.getOwner().toString());

        carResponseDto.setType(entity.getType().toString());

        if(entity.getReservations()!=null){
            carResponseDto.setReservations(entity.getReservations().toString());
        }else {
            carResponseDto.setReservations(Collections.emptyList().toString());
        }



        return carResponseDto;
    }

    @Override
    public CAR transformToEntity(car_Request dto) {
        CAR car = new CAR();
        if (dto.getId() != null) {
            car.setId(dto.getId());
        }
        car.setBrand(dto.getBrand());
        car.setModel(dto.getModel());
        car.setColour(dto.getColour());
        car.setVrp(dto.getVrp());

        car.setOwner(dto.getOwner());

        car.setType(dto.getType());

        car.setReservations(dto.getReservations());


        return car;
    }







}
