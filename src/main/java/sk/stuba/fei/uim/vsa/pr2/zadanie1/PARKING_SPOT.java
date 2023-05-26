package sk.stuba.fei.uim.vsa.pr2.zadanie1;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "PARKING_SPOT")
public class PARKING_SPOT implements Serializable {
    @Id
    @Column(name = "idMiesta", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String identifier;

    @OneToOne
    private CAR_TYPE type;

    private String carParkFloor;

    @ManyToOne
    @JoinColumns(
            {
                    @JoinColumn(name = "idParkingFloor", referencedColumnName = "floorIdentifier"),
                    @JoinColumn(name = "idCarParku", referencedColumnName = "idPark")
            }
    )
    private CAR_PARK_FLOOR cpf;


    @OneToOne(cascade = CascadeType.MERGE, mappedBy = "spot")
    private RESERVATION reservations;

    public CAR_TYPE getType() {
        return type;
    }

    public void setType(CAR_TYPE carType) {
        this.type = carType;
    }

    public RESERVATION getReservations() {
        return reservations;
    }

    public void setReservations(RESERVATION reservation) {
        this.reservations = reservation;
    }

    public String getCarParkFloor() {
        return carParkFloor;
    }

    public void setCarParkFloor(String carParkFloor) {
        this.carParkFloor = carParkFloor;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String spotIdentifier) {
        this.identifier = spotIdentifier;
    }

    public CAR_PARK_FLOOR getCpf() {
        return cpf;
    }

    public void setCpf(CAR_PARK_FLOOR car_park_floor) {
        this.cpf = car_park_floor;
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
                ", spotIdentifier='" + identifier + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PARKING_SPOT that = (PARKING_SPOT) o;
        return Objects.equals(id, that.id) && Objects.equals(identifier, that.identifier) && Objects.equals(cpf, that.cpf) && Objects.equals(reservations, that.reservations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, cpf, reservations);
    }
}
