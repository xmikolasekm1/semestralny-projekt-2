package sk.stuba.fei.uim.vsa.pr2.rest.parkingSpot;

import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR_PARK_FLOOR;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR_TYPE;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.RESERVATION;

public class ps_Request {

    //nepovinny
    private Long id;

    private String identifier;

    private String carParkFloor;

    //nepovinny
    private Long carPark;

    private CAR_TYPE type;

    //nepovinny
    private boolean free;

    //nepovinny
    private RESERVATION reservations;

    public Long getCarPark() {
        return carPark;
    }

    public void setCarPark(Long carPark) {
        this.carPark = carPark;
    }

    public boolean isFree() {
        return free;
    }

    public void setCarParkFloor(String carParkFloor) {
        this.carParkFloor = carParkFloor;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public RESERVATION getReservations() {
        return reservations;
    }

    public void setReservations(RESERVATION reservations) {
        this.reservations = reservations;
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

    public CAR_TYPE getType() {
        return type;
    }

    public void setType(CAR_TYPE type) {
        this.type = type;
    }

    public String getCarParkFloor() {
        return carParkFloor;
    }
}
