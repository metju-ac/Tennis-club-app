package com.klima.matej.tennis_reservations_system.params;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * Class used to store parameters for creating or updating court.
 * All attributes are required.
 */
@Getter
@Builder
public class CourtCreateParams {
    /**
     * Name of the court.
     */
    @NotNull(message = "name is required")
    private String name;

    /**
     * ID of the surface of the court.
     */
    @NotNull(message = "surfaceId is required")
    private long surfaceId;
}
