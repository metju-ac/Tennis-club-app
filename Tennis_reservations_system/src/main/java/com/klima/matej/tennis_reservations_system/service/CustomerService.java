package com.klima.matej.tennis_reservations_system.service;

import com.klima.matej.tennis_reservations_system.entity.Customer;

/**
 * Service interface for customer.
 */
public interface CustomerService extends GenericService<Customer>{
    /**
     * Finds customer by phone number.
     * @param phoneNumber phone number of the customer
     * @return customer with the given phone number
     */
    Customer findByPhoneNumber(String phoneNumber);
}
