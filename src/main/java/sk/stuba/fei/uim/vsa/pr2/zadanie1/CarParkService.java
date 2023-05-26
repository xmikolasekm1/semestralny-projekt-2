package sk.stuba.fei.uim.vsa.pr2.zadanie1;

import org.glassfish.grizzly.compression.lzma.impl.Base;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class CarParkService extends AbstractCarParkService {
    /**
     * OK
     */
    @Override
    public CAR_PARK createCarPark(String name, String address, Integer pricePerHour, Long id) {
        if (name == null || address == null || pricePerHour == null) return null;
        CAR_PARK car_park = new CAR_PARK();
        if (id != null) {
            car_park.setId(id);
        }
        car_park.setName(name);
        car_park.setAddress(address);
        car_park.setPrices(pricePerHour);
        persist(car_park);
        return car_park;
    }

    /**
     * OK
     */
    @Override
    public CAR_PARK getCarPark(Long carParkId) {
        EntityManager em = emf.createEntityManager();
        if (carParkId != null && checkCarPark(carParkId)) {
            return em.find(CAR_PARK.class, carParkId);
        }
        return null;
    }

    /**
     * OK
     */
    @Override
    public CAR_PARK getCarPark(String carParkName) {
        EntityManager em = emf.createEntityManager();
        if (carParkName == null) return null;
        Query q = em.createNativeQuery("select * from CAR_PARK where NAZOV='" + (carParkName) + "'", CAR_PARK.class);
        try {
            CAR_PARK car_parks = (CAR_PARK) q.getSingleResult();
            return car_parks;
        } catch (NoResultException e) {
            return null;
        }

    }

    /**
     * OK
     *
     * @return
     */
    @Override
    public List<CAR_PARK> getCarParks() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery("select * from CAR_PARK", CAR_PARK.class);
        List<CAR_PARK> car_parks = q.getResultList();

        return car_parks;
    }

    /**
     * OK
     */
    @Override
    public CAR_PARK updateCarPark(Object carPark) {
        EntityManager em = emf.createEntityManager();
        CAR_PARK cp = (CAR_PARK) carPark;
        if (cp == null) return null;
        em.getTransaction().begin();
        CAR_PARK car_park = em.find(CAR_PARK.class, cp.getId());

        if (car_park == null) {
            em.getTransaction().rollback();
            em.close();
            return null;
        }
        try {
            car_park.setAddress(cp.getAddress());
            car_park.setName(cp.getName());
            car_park.setPrices(cp.getPrices());
            em.getTransaction().commit();
            em.close();
            return car_park;
        } catch (NoResultException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
        em.close();
        return null;

    }

    /**
     * OK
     */
    @Override
    public CAR_PARK deleteCarPark(Long carParkId) {
        EntityManager em = emf.createEntityManager();
        if (carParkId == null || checkCarPark(carParkId) == false) return null;
        CAR_PARK carPark = em.getReference(CAR_PARK.class, carParkId);
        em.getTransaction().begin();
        em.remove(carPark);
        em.getTransaction().commit();
        close();
        return carPark;
    }

    /**
     * OK
     */
    @Override
    public CAR_PARK_FLOOR createCarParkFloor(Long carParkId, String floorIdentifier) {
        EntityManager em = emf.createEntityManager();

        if (carParkId == null || floorIdentifier == null || checkCarPark(carParkId) == false) return null;

        if (getCarParkFloor(carParkId, floorIdentifier) != null) return null;

        CAR_PARK car_park = em.find(CAR_PARK.class, carParkId);
        if (car_park == null) {
            return null;
        }

        CAR_PARK_FLOOR car_park_floor = new CAR_PARK_FLOOR();

        car_park_floor.setidPark(carParkId);
        car_park_floor.setIdentifier(floorIdentifier);
        car_park_floor.setCarPark(car_park);

        List<CAR_PARK_FLOOR> car_park_floors = car_park.getFloors();
        car_park_floors.add(car_park_floor);

        car_park.setFloors(car_park_floors);

        em.getTransaction().begin();
        try {

            em.persist(car_park);
            em.persist(car_park_floor);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return car_park_floor;
    }

    /**
     * OK
     */
    @Override
    public CAR_PARK_FLOOR getCarParkFloor(Long carParkId, String floorIdentifier) {
        EntityManager em = emf.createEntityManager();
        if (carParkId == null || floorIdentifier == null) return null;
        Query q = em.createNativeQuery("select * from CAR_PARK_FLOOR where idPark='" + (carParkId) + "' and floorIdentifier='" + (floorIdentifier) + "'", CAR_PARK_FLOOR.class);
        try {
            CAR_PARK_FLOOR cpf = (CAR_PARK_FLOOR) q.getSingleResult();
            return cpf;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * OK
     */
    @Override
    public CAR_PARK_FLOOR getCarParkFloor(Long carParkFloorId) {
        return null;
    }

    /**
     * OK
     */
    @Override
    public List<CAR_PARK_FLOOR> getCarParkFloors(Long carParkId) {
        EntityManager em = emf.createEntityManager();
        if (carParkId == null || checkCarPark(carParkId) == false) return null;
        Query q = em.createNativeQuery("select * from CAR_PARK_FLOOR where idPark='" + (carParkId) + "'", CAR_PARK_FLOOR.class);
        try {
            List<CAR_PARK_FLOOR> cpf = q.getResultList();
            return cpf;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();

        }

    }

    /**
     * OK
     */
    @Override
    public Object updateCarParkFloor(Object carParkFloor) {

        return null;
    }

    /**
     * OK
     */
    @Override
    public CAR_PARK_FLOOR deleteCarParkFloor(Long carParkId, String floorIdentifier) {
        EntityManager em = emf.createEntityManager();
        if (carParkId == null || floorIdentifier == null || checkCarPark(carParkId) == false) return null;
        Query q = em.createNativeQuery("select * from CAR_PARK_FLOOR where idPark='" + (carParkId) + "' and floorIdentifier='" + (floorIdentifier) + "'", CAR_PARK_FLOOR.class);
        try {
            CAR_PARK_FLOOR object = (CAR_PARK_FLOOR) q.getSingleResult();
            em.getTransaction().begin();
            em.remove(object);
            em.getTransaction().commit();
            return object;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }


    }

    /**
     * OK
     */
    @Override
    public CAR_PARK_FLOOR deleteCarParkFloor(Long carParkFloorId) {
        return null;
    }

    /**
     * OK
     */
    @Override
    public PARKING_SPOT createParkingSpot(Long carParkId, String floorIdentifier, String spotIdentifier) {
        EntityManager em = emf.createEntityManager();
        if (carParkId == null || floorIdentifier == null || spotIdentifier == null || checkCarPark(carParkId) == false) {
            return null;
        }
        if (checkParkSpotName(carParkId, spotIdentifier) == false) return null;
        Query q = em.createNativeQuery("select * from CAR_PARK_FLOOR where idPark='" + (carParkId) + "' and floorIdentifier='" + (floorIdentifier) + "'", CAR_PARK_FLOOR.class);
        try {
            CAR_PARK_FLOOR cpf = (CAR_PARK_FLOOR) q.getSingleResult();
            PARKING_SPOT parking_spot = new PARKING_SPOT();
            parking_spot.setIdentifier(spotIdentifier);
            parking_spot.setCpf(cpf);
            List<PARKING_SPOT> ps = cpf.getSpots();
            ps.add(parking_spot);
            cpf.setSpots(ps);

            if (getCarType("benzin") == null) createCarType("benzin");
            CAR_TYPE carType = getCarType("benzin");
            parking_spot.setType(carType);
            em.getTransaction().begin();
            em.persist(parking_spot);
            em.persist(cpf);
            em.getTransaction().commit();
            return parking_spot;
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return null;
    }

    /**
     * OK
     */
    @Override
    public PARKING_SPOT getParkingSpot(Long parkingSpotId) {
        EntityManager em = emf.createEntityManager();
        if (parkingSpotId == null) return null;
        Query q = em.createNativeQuery("select * from PARKING_SPOT where idMiesta='" + (parkingSpotId) + "'", PARKING_SPOT.class);
        try {
            PARKING_SPOT ps = (PARKING_SPOT) q.getSingleResult();
            return ps;
        } catch (NoResultException e) {
            return null;
        }

    }

    /**
     * OK
     */
    @Override
    public List<PARKING_SPOT> getParkingSpots(Long carParkId, String floorIdentifier) {
        EntityManager em = emf.createEntityManager();
        if (carParkId == null || floorIdentifier == null || checkCarPark(carParkId) == false) return null;
        Query q = em.createNativeQuery("select * from PARKING_SPOT where idCarParku='" + (carParkId) + "' and idParkingFloor='" + (floorIdentifier) + "'", PARKING_SPOT.class);
        try {
            List<PARKING_SPOT> ps = q.getResultList();
            return ps;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }

    }

    /**
     * OK
     */
    @Override
    public Map<String, List<PARKING_SPOT>> getParkingSpots(Long carParkId) {
        EntityManager em = emf.createEntityManager();
        if (carParkId == null || checkCarPark(carParkId) == false) return null;
        CAR_PARK cp = em.find(CAR_PARK.class, carParkId);
        if (cp == null) return null;
        Map<String, List<PARKING_SPOT>> map = new HashMap<>();
        List<CAR_PARK_FLOOR> cpf = cp.getFloors();
        for (CAR_PARK_FLOOR c : cpf) {
            String menoP = c.getIdentifier();
            List<PARKING_SPOT> o = c.getSpots();
            map.put(menoP, o);
        }
        return map;
    }

    /**
     * OK
     */
    @Override
    public Map<String, List<PARKING_SPOT>> getAvailableParkingSpots(String carParkName) {
        EntityManager em = emf.createEntityManager();
        if (carParkName == null) return null;
        Query q = em.createNativeQuery("select * from CAR_PARK where NAZOV='" + (carParkName) + "';", CAR_PARK.class);
        Map<String, List<PARKING_SPOT>> map = new HashMap<>();
        try {
            CAR_PARK car_park = (CAR_PARK) q.getSingleResult();
            List<CAR_PARK_FLOOR> carParkFloors = car_park.getFloors();
            for (CAR_PARK_FLOOR cpf : carParkFloors) {
                if (!cpf.getSpots().isEmpty()) {
                    Query q2 = em.createQuery("select park from PARKING_SPOT park where park.cpf.identifier='" + (cpf.getIdentifier()) + "'", PARKING_SPOT.class);
                    List<PARKING_SPOT> parkingSpots = q2.getResultList();
                    List<PARKING_SPOT> ps = new ArrayList<>();
                    for (PARKING_SPOT spot : parkingSpots) {
                        RESERVATION reservation = spot.getReservations();
                        if (reservation == null) {
                            ps.add(spot);
                        }
                    }

                    map.put(cpf.getIdentifier(), ps);
                }
            }
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
        return map;

    }

    /**
     * OK
     */
    @Override
    public Map<String, List<PARKING_SPOT>> getOccupiedParkingSpots(String carParkName) {
        EntityManager em = emf.createEntityManager();
        if (carParkName == null) return null;
        Query q = em.createNativeQuery("select * from CAR_PARK where NAZOV='" + (carParkName) + "';", CAR_PARK.class);
        Map<String, List<PARKING_SPOT>> map = new HashMap<>();
        try {
            CAR_PARK car_park = (CAR_PARK) q.getSingleResult();
            List<CAR_PARK_FLOOR> carParkFloors = car_park.getFloors();
            for (CAR_PARK_FLOOR cpf : carParkFloors) {
                if (!cpf.getSpots().isEmpty()) {
                    Query q2 = em.createQuery("select park from PARKING_SPOT park where park.cpf.identifier='" + (cpf.getIdentifier()) + "'", PARKING_SPOT.class);
                    List<PARKING_SPOT> parkingSpots = q2.getResultList();
                    List<PARKING_SPOT> ps = new ArrayList<>();
                    for (PARKING_SPOT spot : parkingSpots) {
                        RESERVATION reservation = spot.getReservations();
                        if (reservation != null) {
                            ps.add(spot);
                        }
                    }

                    map.put(cpf.getIdentifier(), (ps));
                }
            }
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
        return map;
    }

    @Override
    public PARKING_SPOT updateParkingSpot(Object parkingSpot) {
        return null;
    }

    /**
     * OK
     */
    @Override
    public PARKING_SPOT deleteParkingSpot(Long parkingSpotId) {
        EntityManager em = emf.createEntityManager();
        if (parkingSpotId == null || checkParkSpot(parkingSpotId) == false) return null;
        Query q = em.createNativeQuery("select * from PARKING_SPOT where idMiesta='" + (parkingSpotId) + "'", PARKING_SPOT.class);
        try {
            PARKING_SPOT ps = (PARKING_SPOT) q.getSingleResult();
            em.getTransaction().begin();
            em.remove(ps);
            em.getTransaction().commit();
            return ps;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * OK
     */
    @Override
    public CAR createCar(Long userId, String brand, String model, String colour, String vehicleRegistrationPlate) {
        EntityManager em = emf.createEntityManager();

        if (userId == null || brand == null || model == null || colour == null || vehicleRegistrationPlate == null)
            return null;

        USER user = em.find(USER.class, userId);
        if (user == null) {
            return null;
        }

        List<CAR> carList = user.getCars();
        CAR car = new CAR();

        car.setBrand(brand);
        car.setModel(model);
        car.setColour(colour);
        car.setVrp(vehicleRegistrationPlate);
        car.setOwner(user);


        carList.add(car);
        user.setCars(carList);

        em.getTransaction().begin();
        try {

            em.persist(user);
            em.persist(car);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return car;
    }

    /**
     * OK
     */
    @Override
    public CAR getCar(Long carId) {
        EntityManager em = emf.createEntityManager();
        if (carId == null || checkCar(carId) == false) return null;
        CAR car = em.find(CAR.class, carId);
        if (car != null) return car;
        return null;
    }

    /**
     * OK
     */
    @Override
    public CAR getCar(String vehicleRegistrationPlate) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery("select * from CAR where ecv='" + (vehicleRegistrationPlate) + "'", CAR.class);
        try {
            CAR car = (CAR) q.getSingleResult();
            return car;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * OK
     */
    @Override
    public List<CAR> getCars(Long userId) {
        EntityManager em = emf.createEntityManager();
        if (userId == null || checkUser(userId) == false) return null;
        Query q = em.createQuery("select car from CAR car where car.owner.id='" + (userId) + "'", CAR.class);
        try {
            List<CAR> car = q.getResultList();
            return car;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Object updateCar(Object car) {

        return null;
    }

    /**
     * OK
     */
    @Override
    public Object deleteCar(Long carId) {
        EntityManager em = emf.createEntityManager();
        if (carId == null || checkCar(carId)) return null;
        Query q = em.createNativeQuery("select * from CAR where id='" + (carId) + "'", CAR.class);
        try {
            CAR auto = (CAR) q.getSingleResult();
            endReservation(auto.getReservations().getId());
            em.getTransaction().begin();
            em.remove(auto);
            em.getTransaction().commit();
            return auto;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * OK
     */
    @Override
    public USER createUser(String firstname, String lastname, String email) {
        if (firstname == null || lastname == null || email == null) return null;

        USER user = new USER();
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setEmail(email);
        persist(user);
        return user;
    }

    @Override
    public USER createUser(Long id, String firstname, String lastname, String email) {
        if (firstname == null || lastname == null || email == null) return null;

        USER user = new USER();
        user.setId(id);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setEmail(email);
        persist(user);
        return user;
    }


    /**
     * OK
     */
    @Override
    public USER getUser(Long userId) {
        EntityManager em = emf.createEntityManager();
        if (userId == null) return null;
        USER user = em.find(USER.class, userId);
        if (user == null) return null;
        return user;
    }

    /**
     * OK
     */
    @Override
    public USER getUser(String email) {
        EntityManager em = emf.createEntityManager();
        if (email == null) return null;
        Query q = em.createNativeQuery("select * from USER", USER.class);
        List<USER> users = q.getResultList();
        for (USER c : users) {
            if (c.getEmail().matches(email)) {
                return c;
            }
        }
        return null;
    }

    /**
     * OK
     */
    @Override
    public List<USER> getUsers() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery("select * from USER", USER.class);
        try {
            List<USER> user = q.getResultList();
            return user;
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * OK
     */
    @Override
    public Object updateUser(Object user) {
        if (user == null) return null;
        USER u = (USER) user;
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        USER databaseUser = em.find(USER.class, u.getId());
        if (databaseUser == null) {
            em.getTransaction().rollback();
            em.close();
            return null;
        }
        try {
            databaseUser.setEmail(u.getEmail());
            databaseUser.setFirstName(u.getFirstName());
            databaseUser.setLastName(u.getLastName());
            em.merge(databaseUser);
            em.getTransaction().commit();
            em.close();
            return databaseUser;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            em.close();
            return null;
        }
    }

    /**
     * OK
     */
    @Override
    public Object deleteUser(Long userId) {
        EntityManager em = emf.createEntityManager();
        if (userId == null || checkUser(userId) == false) return null;
        Query q = em.createNativeQuery("select * from USER where id='" + (userId) + "'", USER.class);
        Query q2 = em.createNativeQuery("select * from RESERVATION where IDUSER='" + (userId) + "'", RESERVATION.class);
        try {
            USER user = (USER) q.getSingleResult();
            List<RESERVATION> r = q2.getResultList();
            for (RESERVATION re : r) {
                endReservation(re.getId());
            }
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
            return user;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }


    }

    /**
     * OK
     */
    @Override
    public RESERVATION createReservation(Long parkingSpotId, Long cardId) {
        EntityManager em = emf.createEntityManager();

        if (parkingSpotId == null || cardId == null) return null;

        RESERVATION reservation = new RESERVATION();
        if (checkId(parkingSpotId)) {
            if (checkType(parkingSpotId, cardId)) {
                if (checkCarAndSpot(cardId)) {
                    CAR c = em.find(CAR.class, cardId);
                    reservation.setIdUser(c.getOwner().getId());

                    PARKING_SPOT ps = em.find(PARKING_SPOT.class, parkingSpotId);
                    ps.setReservations(reservation);

                    c.setReservations(reservation);
                    reservation.setCar(c);


//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                    Date date = new Date();
                    //reservation.setStart(Timestamp.valueOf(sdf.format(timestamp)));
                    reservation.setStart(date);
                    reservation.setSpot(ps);


                    em.getTransaction().begin();
                    try {
                        em.persist(reservation);
                        em.merge(ps);
                        em.getTransaction().commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                        em.getTransaction().rollback();
                    } finally {
                        em.close();
                    }

                    return reservation;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
        return null;
    }

    /**
     * OK
     */
    @Override
    public RESERVATION endReservation(Long reservationId) {
        EntityManager em = emf.createEntityManager();
        if (reservationId == null) return null;
        Query q = em.createNativeQuery("select * from RESERVATION where id='" + (reservationId) + "';", RESERVATION.class);
        try {
            RESERVATION reservation = (RESERVATION) q.getSingleResult();


            Date date = new Date();

            reservation.setEnd(date);


            long difference = reservation.getEnd().getTime() - reservation.getStart().getTime();
            double hours = (double) ((difference / (1000 * 60 * 60)) % 24);
            System.out.println(hours);
            double cena = 0;


            Query q1 = em.createNativeQuery("select * from CAR_PARK_FLOOR;", CAR_PARK_FLOOR.class);
            List<CAR_PARK_FLOOR> cpf = q1.getResultList();

            for (CAR_PARK_FLOOR cp : cpf) {
                List<PARKING_SPOT> parkingSpots = cp.getSpots();
                for (PARKING_SPOT ps : parkingSpots) {
                    if (ps.getId().equals(reservation.getSpot().getId())) {
                        cena = cp.getCarPark().getPrices();
                    }
                }
            }

            reservation.setPrices(cena * hours);
            CAR car = em.find(CAR.class, reservation.getCar().getId());
            car.setReservations(null);

            PARKING_SPOT parking_spot = em.find(PARKING_SPOT.class, reservation.getSpot().getId());
            parking_spot.setReservations(null);

            em.getTransaction().begin();
            em.merge(reservation);
            em.getTransaction().commit();

            return reservation;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }

    }

    /**
     * OK
     */
    @Override
    public List<RESERVATION> getReservations(Long parkingSpotId, Date date) {
        EntityManager em = emf.createEntityManager();
        if (parkingSpotId == null || date == null) return null;

        long den = date.getTime();
        //System.out.println(den);
        Query q = em.createQuery("select reservation from RESERVATION reservation where reservation.spot.id='" + (parkingSpotId) + "'", RESERVATION.class);
        try {
            List<RESERVATION> reservations = q.getResultList();
            for (RESERVATION r : reservations) {
                List<RESERVATION> reservationList = new ArrayList<>();
                if (r.getStart().getTime() >= den && r.getStart().getTime() <= (den + 86400000)) {
                    reservationList.add(r);
                }
                if (reservationList.isEmpty() == false) return reservationList;
            }
            return Collections.emptyList();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }


    }

    /**
     * OK
     */
    @Override
    public List<RESERVATION> getMyReservations(Long userId) {
        EntityManager em = emf.createEntityManager();
        if (userId == null || checkUser(userId) == false) return null;
        Query q = em.createNativeQuery("select * from RESERVATION where IDUSER='" + (userId) + "'", RESERVATION.class);
        try {
            List<RESERVATION> reservations = q.getResultList();
            return reservations;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }

    }

    /**
     * OK
     */
    @Override
    public Object updateReservation(Object reservation) {


        return null;
    }

    /**
     * OK
     */
    @Override
    public CAR_TYPE createCarType(String name) {
        if (name == null) return null;
        EntityManager em = emf.createEntityManager();
        CAR_TYPE ct = getCarType(name);
        if (ct != null) {
            return null;
        }

        CAR_TYPE car_type = new CAR_TYPE();
        car_type.setName(name);
        persist(car_type);
        return car_type;


    }

    @Override
    public CAR_TYPE createCarType(Long id, String name) {
        if (name == null) return null;
        EntityManager em = emf.createEntityManager();
        CAR_TYPE ct = em.find(CAR_TYPE.class, id);
        if (ct != null) return null;

        CAR_TYPE car_type = new CAR_TYPE();
        car_type.setId(id);
        car_type.setName(name);
        persist(car_type);
        return car_type;


    }

    /**
     * OK
     *
     * @return
     */
    @Override
    public List<CAR_TYPE> getCarTypes() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery("select * from CAR_TYPE;", CAR_TYPE.class);
        try {
            List<CAR_TYPE> carTypes = q.getResultList();
            return carTypes;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }

    }

    /**
     * OK
     */
    @Override
    public CAR_TYPE getCarType(Long carTypeId) {
        EntityManager em = emf.createEntityManager();
        if (carTypeId == null) return null;
        Query q = em.createNativeQuery("select * from CAR_TYPE where id='" + (carTypeId) + "';", CAR_TYPE.class);
        try {
            CAR_TYPE carTypes = (CAR_TYPE) q.getSingleResult();
            return carTypes;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * OK
     */
    @Override
    public CAR_TYPE getCarType(String name) {
        EntityManager em = emf.createEntityManager();
        if (name == null) return null;
        Query q = em.createNativeQuery("select * from CAR_TYPE where carType='" + (name) + "';", CAR_TYPE.class);
        try {
            CAR_TYPE carTypes = (CAR_TYPE) q.getSingleResult();
            return carTypes;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    /**
     * OK
     */
    @Override
    public Object deleteCarType(Long carTypeId) {
        EntityManager em = emf.createEntityManager();
        if (carTypeId == null) return null;
        Query q = em.createNativeQuery("select * from CAR_TYPE where id='" + (carTypeId) + "'", CAR_TYPE.class);
        try {
            CAR_TYPE auto = (CAR_TYPE) q.getSingleResult();
            em.getTransaction().begin();
            em.remove(auto);
            em.getTransaction().commit();
            return auto;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }

    }

    /**
     * OK
     */
    @Override
    public CAR createCar(Long userId, String brand, String model, String colour, String vehicleRegistrationPlate, Long carTypeId) {
        EntityManager em = emf.createEntityManager();

        if (userId == null || brand == null || model == null || colour == null || vehicleRegistrationPlate == null || carTypeId == null)
            return null;

        USER user = em.find(USER.class, userId);
        if (user == null) {
            return null;
        }

        List<CAR> carList = user.getCars();
        CAR car = new CAR();

        car.setBrand(brand);
        car.setModel(model);
        car.setColour(colour);
        car.setVrp(vehicleRegistrationPlate);
        car.setOwner(user);

        CAR_TYPE carType = em.find(CAR_TYPE.class, carTypeId);
        car.setType(carType);
        carType.setCar(car);


        carList.add(car);
        user.setCars(carList);

        em.getTransaction().begin();
        try {

            em.persist(user);
            em.persist(car);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return car;
    }

    /**
     * OK
     */
    @Override
    public PARKING_SPOT createParkingSpot(Long carParkId, String floorIdentifier, String spotIdentifier, Long carTypeId) {
        EntityManager em = emf.createEntityManager();
        if (carParkId == null || floorIdentifier == null || spotIdentifier == null || carParkId == null) return null;
        if (checkParkSpotName(carParkId, spotIdentifier) == false) return null;
        Query q = em.createNativeQuery("select * from CAR_PARK_FLOOR where idPark='" + (carParkId) + "' and floorIdentifier='" + (floorIdentifier) + "'", CAR_PARK_FLOOR.class);
        try {
            CAR_PARK_FLOOR cpf = (CAR_PARK_FLOOR) q.getSingleResult();
            PARKING_SPOT parking_spot = new PARKING_SPOT();
            parking_spot.setIdentifier(spotIdentifier);

            CAR_TYPE car_type = getCarType(carTypeId);
            if (car_type == null) return null;
            parking_spot.setType(car_type);


            parking_spot.setCpf(cpf);
            List<PARKING_SPOT> ps = cpf.getSpots();
            ps.add(parking_spot);
            cpf.setSpots(ps);
            em.getTransaction().begin();
            em.persist(parking_spot);
            em.persist(cpf);
            em.getTransaction().commit();
            return parking_spot;
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        return null;
    }

    public boolean checkId(Long parkSpotId) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery("select * from PARKING_SPOT where idMiesta='" + (parkSpotId) + "'", PARKING_SPOT.class);
        try {
            PARKING_SPOT ps = (PARKING_SPOT) q.getSingleResult();
            if (ps != null && ps.getReservations() == null) {
                return true;
            }
        } catch (NoResultException e) {
            return false;
        } finally {
            em.close();
        }
        return false;

    }

    public boolean checkType(Long parkSpotId, Long carId) {
        EntityManager em = emf.createEntityManager();
        CAR car = em.find(CAR.class, carId);
        CAR_TYPE type = car.getType();
        PARKING_SPOT ps = em.find(PARKING_SPOT.class, parkSpotId);
        if (ps.getType() == (type)) {
            return true;
        }
        return false;
    }

    public boolean checkCarAndSpot(Long carId) {
        EntityManager em = emf.createEntityManager();
        CAR car = em.find(CAR.class, carId);
        if (car.getReservations() == null) {
            return true;
        } else {
            return false;
        }

    }

    public static void persist(Object object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public boolean checkCarPark(Long carparkid) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery("select * from CAR_PARK where ID='" + (carparkid) + "';", CAR_PARK.class);
        try {
            CAR_PARK cp = (CAR_PARK) q.getSingleResult();
            if (cp != null) {
                return true;
            }
            return false;
        } catch (NoResultException e) {
            return false;
        }

    }

    public boolean checkParkSpot(Long parkspotid) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery("select * from PARKING_SPOT where idMiesta='" + (parkspotid) + "'", PARKING_SPOT.class);
        PARKING_SPOT cp = (PARKING_SPOT) q.getSingleResult();
        if (cp != null) {
            return true;
        }
        return false;
    }

    public boolean checkUser(Long userId) {
        EntityManager em = emf.createEntityManager();
        USER user = getUser(userId);
        if (user == null) return false;
        return true;
    }

    public boolean checkCar(Long carId) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery("select * from CAR where id='" + (carId) + "'", CAR.class);
        try {
            CAR cp = (CAR) q.getSingleResult();
            if (cp != null) {
                return true;
            }
            return false;
        } catch (NoResultException e) {
            return false;
        }
    }

    public boolean checkParkSpotName(Long cpId, String name) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery("select * from PARKING_SPOT where idCarParku='" + (cpId) + "' and identifier='" + (name) + "'", PARKING_SPOT.class);
        try {
            PARKING_SPOT ps = (PARKING_SPOT) q.getSingleResult();
            if (ps != null) {
                return false;
            } else {
                return true;
            }
        } catch (NoResultException e) {
            return true;
        } finally {
            em.close();
        }
    }

    public List<CAR> getCars() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery("select * from CAR", CAR.class);
        try {
            List<CAR> car = q.getResultList();
            return car;
        } catch (NoResultException e) {
            return null;
        }
    }

    public RESERVATION getReservation(Long id) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("select reservation from RESERVATION reservation where reservation.id='" + (id) + "'", RESERVATION.class);
        try {
            RESERVATION reservation = (RESERVATION) q.getSingleResult();
            return reservation;
        } catch (NoResultException e) {
            return null;
        }
    }

    public PARKING_SPOT getParkingSpot(String floorIdentifier, String parkingSpotIdentifier) {
        EntityManager em = emf.createEntityManager();
        Query q = em.createQuery("select parkingSpot from PARKING_SPOT parkingSpot where parkingSpot.cpf.identifier='" + (floorIdentifier) + "' and parkingSpot.identifier='" + (parkingSpotIdentifier) + "' ", PARKING_SPOT.class);
        try {
            PARKING_SPOT parkingSpot = (PARKING_SPOT) q.getSingleResult();
            if (parkingSpot == null) return null;
            return parkingSpot;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<RESERVATION> getAllReservations() {
        EntityManager em = emf.createEntityManager();
        Query q = em.createNativeQuery("select * from RESERVATION", RESERVATION.class);
        try {
            List<RESERVATION> reservations = q.getResultList();
            if (reservations == null) {
                return null;
            }
            return reservations;
        } catch (NoResultException e) {
            return null;
        }
    }

    public String getEmail(String authHeader) {
        String base64Encoded = authHeader.substring("Basic ".length());
        String decoded = new String(Base64.getDecoder().decode(base64Encoded));
        return decoded.split(":")[0];
    }

    public Long getPassword(String authHeader) {
        String base64Encoded = authHeader.substring("Basic ".length());
        String decoded = new String(Base64.getDecoder().decode(base64Encoded));
        return Long.valueOf(decoded.split(":")[1]);
    }

}
