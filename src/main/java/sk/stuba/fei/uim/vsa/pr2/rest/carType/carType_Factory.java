package sk.stuba.fei.uim.vsa.pr2.rest.carType;

import sk.stuba.fei.uim.vsa.pr2.rest.ResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR_TYPE;

public class carType_Factory implements ResponseFactory<CAR_TYPE, carType_ResponseDto,carType_Request> {

    private Long id;

    private String carType;

    @Override
    public carType_ResponseDto transformToDto(CAR_TYPE entity) {
        carType_ResponseDto carTypeResponseDto = new carType_ResponseDto();
        carTypeResponseDto.setId(entity.getId());
        carTypeResponseDto.setName(entity.getName());
        return carTypeResponseDto;
    }

    @Override
    public CAR_TYPE transformToEntity(carType_Request dto) {
        CAR_TYPE carType = new CAR_TYPE();
        if (dto.getId() != null) {
            carType.setId(dto.getId());
        }
        carType.setName(dto.getName());
        return carType;
    }


}
