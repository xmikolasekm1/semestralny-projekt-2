package sk.stuba.fei.uim.vsa.pr2.zadanie1;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@IdClass(CAR_PARK_FLOOR_ID.class)
@Table(name = "CAR_PARK_FLOOR")
public class CAR_PARK_FLOOR implements Serializable {
    @Id
    @Column(name = "idPark", nullable = false)
    private Long id;

    @Id
    @Column(name = "floorIdentifier", nullable = false)
    private String identifier;

    @ManyToOne
    private CAR_PARK carPark;

    @OneToMany(mappedBy = "cpf",cascade = CascadeType.REMOVE, orphanRemoval = true,fetch=FetchType.EAGER)
    @JoinColumn(name = "idMiesta",referencedColumnName = "idMiesta")
    private List<PARKING_SPOT> spots =new ArrayList<>();

    public List<PARKING_SPOT> getSpots() {
        return spots;
    }

    public void setSpots(List<PARKING_SPOT> parking_spots) {
        this.spots = parking_spots;
    }

    public Long getidPark() {
        return id;
    }

    public void setidPark(Long idPark) {
        this.id = idPark;
    }

    public CAR_PARK getCarPark() {
        return carPark;
    }

    public void setCarPark(CAR_PARK carPark) {
        this.carPark = carPark;
    }

    public CAR_PARK_FLOOR() {
    }



    @Override
    public boolean equals(Object object) {

        if (!(object instanceof CAR_PARK_FLOOR)) {
            return false;
        }
        CAR_PARK_FLOOR other = (CAR_PARK_FLOOR) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String floorIdentifier) {
        this.identifier = floorIdentifier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +'\''+
                "idPark=" + id +'\''+
                ", floorIdentifier='" + identifier + '\'' +
                ", carPark=" + carPark +'\''+
                ", parking_spots=" + spots +'\''+
                '}';
    }
}
