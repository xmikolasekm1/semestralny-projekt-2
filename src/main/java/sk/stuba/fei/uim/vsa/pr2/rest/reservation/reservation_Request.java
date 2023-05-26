package sk.stuba.fei.uim.vsa.pr2.rest.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.CAR;
import sk.stuba.fei.uim.vsa.pr2.zadanie1.PARKING_SPOT;

import javax.persistence.OneToOne;
import java.util.Date;


public class reservation_Request {

    private Long id;
    private Long idUser;
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING, timezone = "Europe/Bratislava")
    private Date start;
    private Date end;

    private Double prices;
    private PARKING_SPOT spot;
    private CAR car;

    public PARKING_SPOT getSpot() {
        return spot;
    }

    public void setSpot(PARKING_SPOT spot) {
        this.spot = spot;
    }

    public CAR getCar() {
        return car;
    }

    public void setCar(CAR car) {
        this.car = car;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Double getPrices() {
        return prices;
    }

    public void setPrices(Double prices) {
        this.prices = prices;
    }
}
