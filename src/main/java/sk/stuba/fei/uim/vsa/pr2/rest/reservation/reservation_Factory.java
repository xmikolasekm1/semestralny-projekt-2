package sk.stuba.fei.uim.vsa.pr2.rest.reservation;

import sk.stuba.fei.uim.vsa.pr2.rest.ResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.RESERVATION;

import java.sql.Timestamp;

public class reservation_Factory implements ResponseFactory<RESERVATION, reservation_ResponseDto,reservation_Request> {

    @Override
    public reservation_ResponseDto transformToDto(RESERVATION entity) {
        reservation_ResponseDto responseDto=new reservation_ResponseDto();
        responseDto.setId(entity.getId());
        responseDto.setIdSpot(entity.getSpot().toString());
        responseDto.setCar(entity.getCar().toString());

        responseDto.setStart(entity.getStart());


        responseDto.setEnd(entity.getEnd());
        responseDto.setPrices(entity.getPrices());
        return responseDto;
    }

    @Override
    public RESERVATION transformToEntity(reservation_Request dto) {
        RESERVATION reservation=new RESERVATION();
        reservation.setId(dto.getId());
        reservation.setIdUser(dto.getIdUser());
        reservation.setStart(dto.getStart());
        reservation.setEnd(dto.getEnd());
        reservation.setPrices(dto.getPrices());
        reservation.setCar(dto.getCar());
        reservation.setSpot(dto.getSpot());

        return reservation;
    }


}
