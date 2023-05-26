package sk.stuba.fei.uim.vsa.pr2.rest.car;

import java.lang.String;

import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR_TYPE;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.RESERVATION;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.USER;

public class car_Request {

    private Long id;

    private String brand;

    private String model;

    private String vrp;

    private String colour;

    private CAR_TYPE type;

    private USER owner;

    private RESERVATION reservations;

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

    public String getBrand() {
        return brand;
    }



    public String getModel() {
        return model;
    }



    public String getVrp() {
        return vrp;
    }


    public String getColour() {
        return colour;
    }



    public CAR_TYPE getType() {
        return type;
    }

    public void setType(CAR_TYPE type) {
        this.type = type;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setVrp(String vrp) {
        this.vrp = vrp;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public USER getOwner() {
        return owner;
    }

    public void setOwner(USER owner) {
        this.owner = owner;
    }
}
