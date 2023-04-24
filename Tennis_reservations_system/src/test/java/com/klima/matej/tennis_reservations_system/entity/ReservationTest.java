package com.klima.matej.tennis_reservations_system.entity;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {
    private static List<Reservation> RESERVATIONS;

    @BeforeAll
    public static void setUpReservations() {
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();

        reservation1.setStartsAt(Timestamp.valueOf("2023-01-01 10:00:00"));
        reservation1.setEndsAt(Timestamp.valueOf("2023-01-01 11:00:00"));

        reservation2.setStartsAt(Timestamp.valueOf("2023-01-01 12:00:00"));
        reservation2.setEndsAt(Timestamp.valueOf("2023-01-01 13:00:00"));

        RESERVATIONS = List.of(reservation1, reservation2);
    }

    @Test
    public void testIsNotOverlappingBefore() {
        assertFalse(Reservation.isOverlapping(
                Timestamp.valueOf("2023-01-01 08:00:00"),
                Timestamp.valueOf("2023-01-01 09:00:00"),
                RESERVATIONS));
    }

    @Test
    public void testIsNotOverlappingRightBefore() {
        assertFalse(Reservation.isOverlapping(
                Timestamp.valueOf("2023-01-01 09:00:00"),
                Timestamp.valueOf("2023-01-01 10:00:00"),
                RESERVATIONS));
    }

    @Test
    public void testIsNotOverlappingBetween() {
        assertFalse(Reservation.isOverlapping(
                Timestamp.valueOf("2023-01-01 11:00:00"),
                Timestamp.valueOf("2023-01-01 12:00:00"),
                RESERVATIONS));
    }

    @Test
    public void testIsNotOverlappingAfter() {
        assertFalse(Reservation.isOverlapping(
                Timestamp.valueOf("2023-01-01 13:00:00"),
                Timestamp.valueOf("2023-01-01 14:00:00"),
                RESERVATIONS));
    }

    @Test
    public void testIsOverlappingStart() {
        assertTrue(Reservation.isOverlapping(
                Timestamp.valueOf("2023-01-01 09:00:00"),
                Timestamp.valueOf("2023-01-01 10:30:00"),
                RESERVATIONS));
    }

    @Test
    public void testIsOverlappingEnd() {
        assertTrue(Reservation.isOverlapping(
                Timestamp.valueOf("2023-01-01 10:30:00"),
                Timestamp.valueOf("2023-01-01 11:30:00"),
                RESERVATIONS));
    }

    @Test
    public void testIsOverlappingWhole() {
        assertTrue(Reservation.isOverlapping(
                Timestamp.valueOf("2023-01-01 09:30:00"),
                Timestamp.valueOf("2023-01-01 11:30:00"),
                RESERVATIONS));
    }

    @Test
    public void testIsOverlappingMore() {
        assertTrue(Reservation.isOverlapping(
                Timestamp.valueOf("2023-01-01 09:00:00"),
                Timestamp.valueOf("2023-01-01 16:00:00"),
                RESERVATIONS));
    }
}