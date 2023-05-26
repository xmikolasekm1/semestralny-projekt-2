package sk.stuba.fei.uim.vsa.pr2.zadanie1;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "USER")
public class USER implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false,unique = true)
    private String email;


    @OneToMany(mappedBy = "owner",cascade = CascadeType.REMOVE)
    @JoinColumn(name = "idUser")
    private List<CAR> cars;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                '}';
    }

    public List<CAR> getCars() {
        return cars;
    }

    public void setCars(List<CAR> auto) {
        this.cars = auto;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstname) {
        this.firstName = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastname) {
        this.lastName = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
