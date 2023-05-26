package sk.stuba.fei.uim.vsa.pr2.rest.carParkFloor;

import sk.stuba.fei.uim.vsa.pr2.rest.ResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR_PARK_FLOOR;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.PARKING_SPOT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class cpf_Factory implements ResponseFactory<CAR_PARK_FLOOR,cpf_ResponseDto,cpf_Request> {

    @Override
    public cpf_ResponseDto transformToDto(CAR_PARK_FLOOR entity) {
        cpf_ResponseDto cpfResponseDto=new cpf_ResponseDto();
        cpfResponseDto.setId(entity.getidPark());
        cpfResponseDto.setIdentifier(entity.getIdentifier());

        List<PARKING_SPOT> spots=entity.getSpots();
        List<String> spoty= new ArrayList<>();
        for (PARKING_SPOT ps: spots){
            spoty.add(ps.toString());
        }
        cpfResponseDto.setSpots(spoty);

        cpfResponseDto.setCarPark(entity.getCarPark().getId());
        return cpfResponseDto;
    }

    @Override
    public CAR_PARK_FLOOR transformToEntity(cpf_Request dto) {
        CAR_PARK_FLOOR carParkFloor=new CAR_PARK_FLOOR();
        carParkFloor.setidPark(dto.getId());
        carParkFloor.setIdentifier(dto.getIdentifier());
        return null;
    }


}
