package sk.stuba.fei.uim.vsa.pr2.rest.carType;

import sk.stuba.fei.uim.vsa.pr2.rest.Dto;

public class carType_ResponseDto extends Dto {

    private Long id;

    private String name;


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
}
