package com.klima.matej.tennis_reservations_system.service;

import com.klima.matej.tennis_reservations_system.entity.Surface;

/**
 * Service interface for surface.
 */
public interface SurfaceService extends GenericService<Surface>{
    /**
     * Finds surface by name.
     * @param name name of the surface
     * @return surface with the given name
     */
    Surface findByName(String name);
}
