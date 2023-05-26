package sk.stuba.fei.uim.vsa.pr2.rest.parkingSpot;

import sk.stuba.fei.uim.vsa.pr2.rest.ResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR_TYPE;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.PARKING_SPOT;

import java.util.Collections;

public class ps_Factory implements ResponseFactory<PARKING_SPOT, ps_ResponseDto, ps_Request> {

    @Override
    public ps_ResponseDto transformToDto(PARKING_SPOT entity) {
        ps_ResponseDto psResponseDto = new ps_ResponseDto();
        psResponseDto.setId(entity.getId());
        psResponseDto.setIdentifier(entity.getIdentifier());
        psResponseDto.setType(entity.getType().toString());

        if (entity.getReservations() != null) {
            psResponseDto.setReservation(entity.getReservations().toString());
        } else {
            psResponseDto.setReservation(Collections.emptyList().toString());
        }
        return psResponseDto;
    }

    @Override
    public PARKING_SPOT transformToEntity(ps_Request dto) {
        PARKING_SPOT parkingSpot = new PARKING_SPOT();
        parkingSpot.setId(dto.getId());
        parkingSpot.setIdentifier(dto.getIdentifier());
        parkingSpot.setType(dto.getType());

        if (dto.getReservations() != null) {
            parkingSpot.setReservations(dto.getReservations());
        }


        return parkingSpot;
    }


}
