package com.klima.matej.tennis_reservations_system.params;

import lombok.Builder;
import lombok.Getter;

/**
 * Class used to store parameters for patching court.
 * All attributes are optional.
 */
@Getter
@Builder
public class CourtPatchParams {
    /**
     * Name of the court.
     */
    private String name;

    /**
     * ID of the surface of the court.
     */
    private long surfaceId = -1;
}

