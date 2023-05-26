package sk.stuba.fei.uim.vsa.pr2.rest.user;

import sk.stuba.fei.uim.vsa.pr2.rest.Dto;
import java.lang.String;
import java.util.List;

public class user_ResponseDto extends Dto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private List<String> cars;

    public List<String> getCars() {
        return cars;
    }

    public void setCars(List<String> cars) {
        this.cars = cars;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                '}';
    }
}
