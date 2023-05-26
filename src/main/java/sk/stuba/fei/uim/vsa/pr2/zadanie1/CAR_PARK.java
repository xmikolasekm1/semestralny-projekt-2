package sk.stuba.fei.uim.vsa.pr2.zadanie1;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CAR_PARK")
public class CAR_PARK implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAZOV", nullable = false,unique = true)
    private String name;
    @Column(name = "ADRESA", nullable = false)
    private String address;
    @Column(name = "CENA", nullable = false)
    private Integer prices;


    @OneToMany(mappedBy = "carPark",cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "idPoschodia",referencedColumnName = "idPoschodia")
    private List<CAR_PARK_FLOOR> floors =new ArrayList<>();


    public List<CAR_PARK_FLOOR> getFloors() {
        return floors;
    }

    public void setFloors(List<CAR_PARK_FLOOR> CAR_PARK_FLOOR) {
        this.floors = CAR_PARK_FLOOR;
    }


    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                '}';
    }

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

    public void setAddress(String adress) {
        this.address = adress;
    }

    public Integer getPrices() {
        return prices;
    }

    public void setPrices(Integer pricePerHour) {
        this.prices = pricePerHour;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof CAR_PARK)) {
            return false;
        }
        CAR_PARK other = (CAR_PARK) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }
}
