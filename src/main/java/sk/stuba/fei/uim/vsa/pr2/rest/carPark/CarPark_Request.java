package sk.stuba.fei.uim.vsa.pr2.rest.carPark;

import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR_PARK_FLOOR;

import java.util.List;

public class CarPark_Request {

    private Long id;

    private String name;

    private String address;

    private Integer prices;

    private List<CAR_PARK_FLOOR> floors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPrices() {
        return prices;
    }

    public void setPrices(Integer prices) {
        this.prices = prices;
    }

    public List<CAR_PARK_FLOOR> getFloors() {
        return floors;
    }

    public void setFloors(List<CAR_PARK_FLOOR> floors) {
        this.floors = floors;
    }
}
