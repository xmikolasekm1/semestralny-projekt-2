package sk.stuba.fei.uim.vsa.pr2.zadanie1;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "RESERVATION")
public class RESERVATION implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long idUser;

    @Column(nullable = false)
    private Date start;


    private Date end;
    private Double prices;

    @OneToOne
    private PARKING_SPOT spot;

    @OneToOne
    private CAR car;

    public CAR getCar() {
        return car;
    }

    public void setCar(CAR car) {
        this.car = car;
    }

    public PARKING_SPOT getSpot() {
        return spot;
    }

    public void setSpot(PARKING_SPOT parkingSpot) {
        this.spot = parkingSpot;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }


    public Double getPrices() {
        return prices;
    }

    public void setPrices(Double celkovaCena) {
        this.prices = celkovaCena;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", datum=" + start +
                ", koniec=" + end +
                ", celkovaCena=" + prices +
                ", parkingSpot=" + spot +
                ", car=" + car +
                '}';
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date datum) {
        this.start = datum;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date koniec) {
        this.end = koniec;
    }
}
