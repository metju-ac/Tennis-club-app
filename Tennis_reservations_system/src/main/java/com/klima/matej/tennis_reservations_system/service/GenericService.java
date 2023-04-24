package com.klima.matej.tennis_reservations_system.service;

import java.util.List;

/**
 * Generic service interface.
 * @param <T> type of the object
 */
public interface GenericService<T>{
    /**
     * Saves object to the database.
     * @param object object to be saved
     * @return saved object
     */
    T save(T object);

    /**
     * Finds all objects in the database.
     * @return list of all objects
     */
    List<T> findAll();

    /**
     * Finds object by ID.
     * @param id ID of the object
     * @return object with the given ID
     */
    T findById(long id);

    /**
     * Updates object in the database.
     * @param object object to be updated
     * @return updated object
     */
    T update(T object);

    /**
     * Deletes object from the database.
     * @param id ID of the object to be deleted
     * @return true if object was deleted, false otherwise
     */
    boolean deleteById(long id);
}
