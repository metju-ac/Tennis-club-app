package com.klima.matej.tennis_reservations_system.service;

import com.klima.matej.tennis_reservations_system.entity.Reservation;

import java.util.List;

/**
 * Service interface for reservation.
 */
public interface ReservationService extends GenericService<Reservation> {
    /**
     * Finds all reservations for the given court.
     * @param courtId ID of the court
     * @return list of all reservations for the given court
     */
    List<Reservation> findAllByCourtId(long courtId);

    /**
     * Finds all reservations for the given customer.
     * @param phoneNumber phone number of the customer
     * @return list of all reservations for the given customer
     */
    List<Reservation> findAllByPhoneNumber(String phoneNumber);

    /**
     * Finds all reservations in the future for the given customer.
     * @param phoneNumber phone number of the customer
     * @return list of all reservations in the future for the given customer
     */
    List<Reservation> findFutureByPhoneNumber(String phoneNumber);

}
