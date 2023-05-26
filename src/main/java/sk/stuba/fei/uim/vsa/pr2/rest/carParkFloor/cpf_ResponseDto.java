package sk.stuba.fei.uim.vsa.pr2.rest.carParkFloor;

import sk.stuba.fei.uim.vsa.pr2.rest.Dto;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR_PARK;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.PARKING_SPOT;

import java.util.List;

public class cpf_ResponseDto extends Dto {

    private Long id;
    private String identifier;
    private List<String> spots;
    private Long carPark;

    public List<String> getSpots() {
        return spots;
    }

    public void setSpots(List<String> spots) {
        this.spots = spots;
    }

    public Long getCarPark() {
        return carPark;
    }

    public void setCarPark(Long carPark) {
        this.carPark = carPark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
