package sk.stuba.fei.uim.vsa.pr2.rest.parkingSpot;

import sk.stuba.fei.uim.vsa.pr2.rest.Dto;

public class ps_ResponseDto extends Dto {


    private Long id;

    private String identifier;

    private String type;

    private String reservation;

    public String getReservation() {
        return reservation;
    }

    public void setReservation(String reservation) {
        this.reservation = reservation;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
