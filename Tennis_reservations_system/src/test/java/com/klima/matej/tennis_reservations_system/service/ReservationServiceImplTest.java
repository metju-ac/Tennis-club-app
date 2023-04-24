package com.klima.matej.tennis_reservations_system.service;

import com.klima.matej.tennis_reservations_system.entity.Court;
import com.klima.matej.tennis_reservations_system.entity.Customer;
import com.klima.matej.tennis_reservations_system.entity.Reservation;
import com.klima.matej.tennis_reservations_system.entity.Surface;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceImplTest {
    private final ReservationService reservationService = new ReservationServiceImpl();
    private static Customer CUSTOMER_1;
    private static Customer CUSTOMER_2;
    private static Court COURT_1;
    private static Court COURT_2;

    @BeforeAll
    public static void setUpCustomerAndCourt() {
        CustomerService customerService = new CustomerServiceImpl();
        CourtService courtService = new CourtServiceImpl();
        SurfaceService surfaceService = new SurfaceServiceImpl();

        Customer customer1 = new Customer();
        customer1.setName("Reservation Test Customer");
        customer1.setPhoneNumber("Reservation Test Phone Number");
        customerService.save(customer1);

        Customer customer2 = new Customer();
        customer2.setName("Reservation Test Customer 2");
        customer2.setPhoneNumber("Reservation Test Phone Number 2");
        customerService.save(customer2);

        Surface surface = new Surface();
        surface.setName("Reservation Test Surface");
        surface.setMinutePrice(1);
        surfaceService.save(surface);

        Court court1 = new Court();
        court1.setName("Test Court 1");
        court1.setSurface(surface);
        courtService.save(court1);

        Court court2 = new Court();
        court2.setName("Test Court 2");
        court2.setSurface(surface);
        courtService.save(court2);

        CUSTOMER_1 = customer1;
        CUSTOMER_2 = customer2;
        COURT_1 = court1;
        COURT_2 = court2;
    }

    @AfterAll
    public static void tearDownCustomerAndCourt() {
        CustomerService customerService = new CustomerServiceImpl();
        CourtService courtService = new CourtServiceImpl();
        SurfaceService surfaceService = new SurfaceServiceImpl();

        customerService.deleteById(CUSTOMER_1.getId());
        customerService.deleteById(CUSTOMER_2.getId());
        courtService.deleteById(COURT_1.getId());
        courtService.deleteById(COURT_2.getId());
        surfaceService.deleteById(COURT_1.getSurface().getId());
    }


    private Reservation saveReservation() {
        Reservation reservation = new Reservation(false, CUSTOMER_1, COURT_1,
                Timestamp.valueOf("2023-01-01 12:00:00"),
                Timestamp.valueOf("2023-01-01 13:00:00"));

        reservationService.save(reservation);

        return reservation;
    }

    private Reservation saveReservation(boolean isDoubles, Customer customer, Court court) {
        Reservation reservation = new Reservation(isDoubles, customer, court,
                Timestamp.valueOf("2023-01-01 12:00:00"),
                Timestamp.valueOf("2023-01-01 13:00:00"));

        reservationService.save(reservation);

        return reservation;
    }

    private Reservation saveReservation(boolean isDoubles, Customer customer, Court court, Timestamp startsAt, Timestamp endsAt) {
        Reservation reservation = new Reservation(isDoubles, customer, court, startsAt, endsAt);

        reservationService.save(reservation);

        return reservation;
    }

    @Test
    public void testSaveReservation() {
        Reservation reservation = new Reservation();
        reservation.setCustomer(CUSTOMER_1);
        reservation.setCourt(COURT_1);

        reservation.setDoubles(true);
        reservation.setStartsAt(Timestamp.valueOf("2023-01-01 12:00:00"));
        reservation.setEndsAt(Timestamp.valueOf("2023-01-01 13:00:00"));

        Reservation saved = reservationService.save(reservation);

        assertNotNull(saved);
        assertNotEquals(reservation.getId(), 0);

        reservationService.deleteById(reservation.getId());
    }

    @Test
    public void testSaveReservationPrice() {
        Reservation reservation = saveReservation(false, CUSTOMER_1, COURT_1);
        assertEquals(60, reservation.getPrice());

        reservationService.deleteById(reservation.getId());
    }

    @Test
    public void testSaveReservationPriceDoubles() {
        Reservation reservation = saveReservation(true, CUSTOMER_1, COURT_1);
        assertEquals(90, reservation.getPrice());

        reservationService.deleteById(reservation.getId());
    }

    @Test
    public void testDeleteByIdReservation() {
        Reservation reservation = saveReservation();

        boolean deleted = reservationService.deleteById(reservation.getId());
        assertTrue(deleted);

        Reservation foudReservation = reservationService.findById(reservation.getId());
        assertNull(foudReservation);
    }

    @Test
    public void findAllReservation() {
        Reservation reservation1 = saveReservation(false, CUSTOMER_1, COURT_1);
        Reservation reservation2 = saveReservation(true, CUSTOMER_2, COURT_2);

        assertEquals(2, reservationService.findAll().size());

        reservationService.deleteById(reservation1.getId());
        reservationService.deleteById(reservation2.getId());
    }

    @Test
    public void testFindByIdReservation() {
        Reservation reservation = saveReservation();
        Reservation foundReservation = reservationService.findById(reservation.getId());
        assertEquals(reservation, foundReservation);

        reservationService.deleteById(reservation.getId());
    }

    @Test
    public void testFindByInReservationNotFound() {
        Reservation foundReservation = reservationService.findById(-1);
        assertNull(foundReservation);
    }

    @Test
    public void testFindAllByCourtId() {
        Reservation reservation1 = saveReservation(false, CUSTOMER_1, COURT_1);
        Reservation reservation2 = saveReservation(true, CUSTOMER_2, COURT_1);
        Reservation reservation3 = saveReservation(false, CUSTOMER_1, COURT_2);

        assertEquals(2, reservationService.findAllByCourtId(COURT_1.getId()).size());

        reservationService.deleteById(reservation1.getId());
        reservationService.deleteById(reservation2.getId());
        reservationService.deleteById(reservation3.getId());
    }

    @Test
    public void testFindAllByPhoneNumber() {
        Reservation reservation1 = saveReservation(false, CUSTOMER_1, COURT_1);
        Reservation reservation2 = saveReservation(true, CUSTOMER_1, COURT_2);
        Reservation reservation3 = saveReservation(false, CUSTOMER_2, COURT_1);

        assertEquals(2, reservationService.findAllByPhoneNumber(CUSTOMER_1.getPhoneNumber()).size());

        reservationService.deleteById(reservation1.getId());
        reservationService.deleteById(reservation2.getId());
        reservationService.deleteById(reservation3.getId());
    }

    @Test
    public void testFindFutureByPhoneNumber() {
        Timestamp future = new Timestamp(System.currentTimeMillis() + 1000000000);
        Timestamp past = new Timestamp(System.currentTimeMillis() - 1000000000);

        Reservation reservation1 = saveReservation(false, CUSTOMER_1, COURT_1, future, future);
        Reservation reservation2 = saveReservation(true, CUSTOMER_1, COURT_2, future, future);
        Reservation reservation3 = saveReservation(false, CUSTOMER_1, COURT_1, past, past);

        assertEquals(2, reservationService.findFutureByPhoneNumber(CUSTOMER_1.getPhoneNumber()).size());

        reservationService.deleteById(reservation1.getId());
        reservationService.deleteById(reservation2.getId());
        reservationService.deleteById(reservation3.getId());
    }

    @Test
    public void testFindFutureByPhoneNumberNotFound() {
        Timestamp past = new Timestamp(System.currentTimeMillis() - 1000000000);

        Reservation reservation1 = saveReservation(false, CUSTOMER_1, COURT_1, past, past);
        Reservation reservation2 = saveReservation(true, CUSTOMER_1, COURT_2, past, past);
        Reservation reservation3 = saveReservation(false, CUSTOMER_1, COURT_1, past, past);

        assertEquals(0, reservationService.findFutureByPhoneNumber(CUSTOMER_1.getPhoneNumber()).size());

        reservationService.deleteById(reservation1.getId());
        reservationService.deleteById(reservation2.getId());
        reservationService.deleteById(reservation3.getId());
    }

    @Test
    public void testUpdateReservation() {
        Reservation reservation = saveReservation();

        reservation.setDoubles(true);
        reservation.setStartsAt(Timestamp.valueOf("2023-01-01 12:00:00"));
        reservation.setEndsAt(Timestamp.valueOf("2023-01-01 13:00:00"));

        Reservation updated = reservationService.update(reservation);
        assertNotNull(updated);

        Reservation foundReservation = reservationService.findById(reservation.getId());
        assertEquals(reservation, foundReservation);

        reservationService.deleteById(reservation.getId());
    }
}