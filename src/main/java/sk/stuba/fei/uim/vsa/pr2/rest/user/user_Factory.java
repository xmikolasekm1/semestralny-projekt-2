package sk.stuba.fei.uim.vsa.pr2.rest.user;

import sk.stuba.fei.uim.vsa.pr2.rest.ResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.rest.car.car_Factory;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.USER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class user_Factory implements ResponseFactory<USER, user_ResponseDto,user_Request> {

    @Override
    public user_ResponseDto transformToDto(USER entity) {
        user_ResponseDto user_responseDto = new user_ResponseDto();
        user_responseDto.setId(entity.getId());
        user_responseDto.setFirstName(entity.getFirstName());
        user_responseDto.setLastName(entity.getLastName());
        user_responseDto.setEmail(entity.getEmail());

        List<CAR> auta= entity.getCars();
        List<String> cars=new ArrayList<>();
        for (CAR car:auta){
            cars.add(car.toString());
        }

        if (!cars.isEmpty()){
        user_responseDto.setCars(cars);}
        else{user_responseDto.setCars(Collections.emptyList());}

        return user_responseDto;
    }

    @Override
    public USER transformToEntity(user_Request dto) {
        USER user = new USER();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());

        if (dto.getCars() != null) {
            List<CAR> cars=dto.getCars();
            user.setCars(cars);
        }

        return user;
    }


}
