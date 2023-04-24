package com.klima.matej.tennis_reservations_system.service;

import com.klima.matej.tennis_reservations_system.entity.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceImplTest {
    private final CustomerService customerService = new CustomerServiceImpl();

    private Customer saveCustomer(String phoneNumber, String name) {
        Customer customer = new Customer();
        customer.setPhoneNumber(phoneNumber);
        customer.setName(name);
        customerService.save(customer);

        return customer;
    }

    @Test
    public void testSaveCustomer() {
        Customer customer = new Customer();
        customer.setPhoneNumber("001");
        customer.setName("Test Customer");

        Customer saved = customerService.save(customer);

        assertNotNull(saved);
        assertNotEquals(customer.getId(), 0);

        customerService.deleteById(customer.getId());
    }

    @Test
    public void testDeleteByIdCustomer() {
        Customer customer = saveCustomer("002", "Test Customer");
        boolean deleted = customerService.deleteById(customer.getId());

        assertTrue(deleted);

        Customer foundCustomer = customerService.findById(customer.getId());
        assertNull(foundCustomer);
    }

    @Test
    public void testSaveCustomerNullPhoneNumber() {
        Customer customer = new Customer();
        customer.setName("Test Customer");

        assertThrows(IllegalStateException.class, () -> customerService.save(customer));
    }

    @Test
    public void testSaveCustomerDuplicatePhoneNumber() {
        Customer customer = saveCustomer("003", "Test Customer");
        assertThrows(IllegalStateException.class, () -> saveCustomer("003", "Test Customer 2"));

        customerService.deleteById(customer.getId());
    }

    @Test
    public void testFindAllCustomers() {
        Customer customer = saveCustomer("004", "Test Customer");
        Customer customer2 = saveCustomer("005", "Test Customer 2");
        assertEquals(2, customerService.findAll().size());

        customerService.deleteById(customer.getId());
        customerService.deleteById(customer2.getId());
    }

    @Test
    public void testFindByIdCustomer() {
        Customer customer = saveCustomer("006", "Test Customer");
        Customer foundCustomer = customerService.findById(customer.getId());
        assertEquals(customer, foundCustomer);

        customerService.deleteById(customer.getId());
    }

    @Test
    public void testFindByIdCustomerNotFound() {
        Customer foundCustomer = customerService.findById(-1);
        assertNull(foundCustomer);
    }

    @Test
    public void testFindByPhoneNumberCustomer() {
        Customer customer = saveCustomer("007", "Test Customer");
        Customer foundCustomer = customerService.findByPhoneNumber("007");
        assertEquals(customer, foundCustomer);

        customerService.deleteById(customer.getId());
    }

    @Test
    public void testFindByPhoneNumberCustomerNotFound() {
        Customer foundCustomer = customerService.findByPhoneNumber("0");
        assertNull(foundCustomer);
    }

    @Test
    public void testUpdateCustomer() {
        Customer customer = saveCustomer("008", "Test Customer");
        customer.setName("Test Customer 2");
        Customer updated = customerService.update(customer);
        assertNotNull(updated);

        Customer foundCustomer = customerService.findById(customer.getId());
        assertEquals(customer, foundCustomer);

        customerService.deleteById(customer.getId());
    }

    @Test
    public void testUpdateCustomerDuplicatePhoneNumber() {
        Customer customer = saveCustomer("009", "Test Customer");

        Customer customer2 = saveCustomer("010", "Test Customer 2");
        customer2.setPhoneNumber("009");
        Customer updated = customerService.update(customer2);
        assertNull(updated);

        customerService.deleteById(customer.getId());
        customerService.deleteById(customer2.getId());
    }

    @Test
    public void testCustomerEmptyReservations() {
        Customer customer = saveCustomer("011", "Test Customer");
        Customer foundCustomer = customerService.findById(customer.getId());
        assertEquals(0, foundCustomer.getReservations().size());

        customerService.deleteById(customer.getId());
    }
}