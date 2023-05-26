package sk.stuba.fei.uim.vsa.pr2;
import jakarta.ws.rs.core.Application;
import sk.stuba.fei.uim.vsa.pr2.rest.car.car_Resource;
import sk.stuba.fei.uim.vsa.pr2.rest.carPark.CarPark_Resource;
import sk.stuba.fei.uim.vsa.pr2.rest.carType.carType_Resource;
import sk.stuba.fei.uim.vsa.pr2.rest.parkingSpot.ps_Resource;
import sk.stuba.fei.uim.vsa.pr2.rest.reservation.reservation_Resource;
import sk.stuba.fei.uim.vsa.pr2.rest.user.user_Resource;

import java.util.HashSet;
import java.util.Set;


public class CP_Application extends Application {


    static final Set<Class<?>> appClasses = new HashSet<>();

    static {
        appClasses.add(CarPark_Resource.class);
        appClasses.add(car_Resource.class);
        appClasses.add(carType_Resource.class);
        appClasses.add(ps_Resource.class);
        appClasses.add(reservation_Resource.class);
        appClasses.add(user_Resource.class);
//        appClasses.add(CORSFilter.class);
//        appClasses.add(BasicAuthRequestFilter.class);

    }

    @Override
    public Set<Class<?>> getClasses(){return appClasses;}
}
