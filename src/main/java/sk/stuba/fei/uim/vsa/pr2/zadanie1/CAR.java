package sk.stuba.fei.uim.vsa.pr2.zadanie1;

import java.lang.String;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CAR")
public class CAR implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "znacka")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "ecv",unique = true)
    private String vrp;

    @Column(name = "farba")
    private String colour;


    @ManyToOne
    private USER owner;

    @OneToOne
    private CAR_TYPE type;

    @OneToOne(mappedBy = "car")
    private RESERVATION reservations;

    public RESERVATION getReservations() {
        return reservations;
    }

    public void setReservations(RESERVATION reservation) {
        this.reservations = reservation;
    }

    public void setType(CAR_TYPE carType) {
        this.type = carType;
    }

    public CAR_TYPE getType() {
        return type;
    }

    public USER getOwner() {
        return owner;
    }

    public void setOwner(USER user) {
        this.owner = user;
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

    public void setBrand(String znacka) {
        this.brand = znacka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVrp() {
        return vrp;
    }

    public void setVrp(String ecv) {
        this.vrp = ecv;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String farba) {
        this.colour = farba;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", vrp='" + vrp + '\'' +
                ", colour='" + colour + '\'' +
                ", owner=" + owner +
                ", type=" + type +
                '}';
    }
}
