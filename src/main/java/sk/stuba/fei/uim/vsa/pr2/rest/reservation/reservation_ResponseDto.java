package sk.stuba.fei.uim.vsa.pr2.rest.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import sk.stuba.fei.uim.vsa.pr2.rest.Dto;

import java.util.Date;
import java.sql.Timestamp;

public class reservation_ResponseDto extends Dto {

    private Long id;
    private String idSpot;
    private String car;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "Europe/Bratislava")
    private Date start;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "Europe/Bratislava")
    private Date end;
    private Double prices;

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdSpot() {
        return idSpot;
    }

    public void setIdSpot(String idSpot) {
        this.idSpot = idSpot;
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
