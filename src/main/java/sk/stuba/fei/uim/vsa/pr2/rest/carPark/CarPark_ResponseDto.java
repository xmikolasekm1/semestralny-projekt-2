package sk.stuba.fei.uim.vsa.pr2.rest.carPark;

import sk.stuba.fei.uim.vsa.pr2.rest.Dto;
import sk.stuba.fei.uim.vsa.pr2.rest.carParkFloor.cpf_ResponseDto;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR_PARK_FLOOR;

import java.util.List;
import java.util.Objects;


public class CarPark_ResponseDto extends Dto {


    private Long id;

    private String name;

    private String address;

    private Integer prices;

    private List<String> floors;

    public List<String> getFloors() {
        return floors;
    }

    public void setFloors(List<String> floors) {
        this.floors = floors;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarPark_ResponseDto that = (CarPark_ResponseDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
