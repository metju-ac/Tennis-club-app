package com.klima.matej.tennis_reservations_system.params;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

/**
 * Class used to store parameters for patching reservation.
 * All attributes are optional.
 */
@Getter
@Builder
public class ReservationPatchParams {
    /**
     * ID of the court of the reservation.
     */
    private long courtId = -1;

    /**
     * Phone number of the customer creating the reservation.
     */
    private String customerPhoneNumber;

    /**
     * Name of the customer creating the reservation.
     */
    private String customerName;

    /**
     * Whether the reservation is doubles or not.
     */
    private Boolean isDoubles;

    /**
     * Timestamp of the start of the reservation.
     */
    private Timestamp startsAt;

    /**
     * Timestamp of the end of the reservation.
     */
    private Timestamp endsAt;

    /**
     * Timestamp of the creation of the reservation.
     */
    private Timestamp createdAt;
}
