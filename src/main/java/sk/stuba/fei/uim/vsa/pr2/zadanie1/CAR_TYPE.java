package sk.stuba.fei.uim.vsa.pr2.zadanie1;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CAR_TYPE")
public class CAR_TYPE implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "carType", nullable = false, unique = true)
    private String name;

    @OneToOne(mappedBy = "type")
    private CAR car;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CAR getCar() {
        return car;
    }

    public void setCar(CAR car) {
        this.car = car;
    }

    public String getName() {
        return name;
    }

    public void setName(String carType) {
        this.name = carType;
    }


    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                '}';
    }
}
