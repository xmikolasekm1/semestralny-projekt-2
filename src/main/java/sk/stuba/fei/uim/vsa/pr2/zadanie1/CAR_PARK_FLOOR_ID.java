package sk.stuba.fei.uim.vsa.pr2.zadanie1;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
@Embeddable
public class CAR_PARK_FLOOR_ID implements Serializable {

    private Long id;

    private String identifier;

    public CAR_PARK_FLOOR_ID() {
    }

    public CAR_PARK_FLOOR_ID(Long idPoschodia, String floorIdentifier) {
        this.id = idPoschodia;
        this.identifier = floorIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CAR_PARK_FLOOR_ID that = (CAR_PARK_FLOOR_ID) o;
        return Objects.equals(id, that.id) && Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier);
    }
}
