package com.klima.matej.tennis_reservations_system.params;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

/**
 * Class used to store parameters for creating or updating reservation.
 * All attributes are required.
 */
@Getter
@Builder
public class ReservationCreateParams {
    /**
     * ID of the court of the reservation.
     */
    @NotNull(message = "courtId is required")
    private long courtId;

    /**
     * Phone number of the customer creating the reservation.
     */
    @NotNull(message = "customerPhoneNumber is required")
    private String customerPhoneNumber;

    /**
     * Name of the customer creating the reservation.
     */
    @NotNull(message = "customerName is required")
    private String customerName;

    /**
     * Whether the reservation is doubles or not.
     */
    @NotNull(message = "isDoubles is required")
    private boolean isDoubles;

    /**
     * Timestamp of the start of the reservation.
     */
    @NotNull(message = "startsAt is required")
    private Timestamp startsAt;

    /**
     * Timestamp of the end of the reservation.
     */
    @NotNull(message = "endsAt is required")
    private Timestamp endsAt;
}
