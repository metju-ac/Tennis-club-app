package com.klima.matej.tennis_reservations_system.service;

import com.klima.matej.tennis_reservations_system.entity.Customer;
import com.klima.matej.tennis_reservations_system.util.HibernateUtil;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Objects;

/**
 * Service implementation for customer.
 */
@Service
public class CustomerServiceImpl extends GenericServiceImpl<Customer> implements CustomerService {
    /**
     * Session factory used to create sessions.
     */
    private final SessionFactory sessionFactory;

    /**
     * Constructor.
     */
    public CustomerServiceImpl() {
        super(Customer.class);
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Customer customer =  session.createQuery("SELECT c FROM Customer c LEFT JOIN FETCH c.reservations WHERE c.id = :id", Customer.class)
                    .setParameter("id", id)
                    .getSingleResult();
            if (Objects.nonNull(customer)) {
                return customer;
            }
        } catch (NoResultException ignored) {}
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer findByPhoneNumber(String phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            Customer customer =  session.createQuery("SELECT c FROM Customer c LEFT JOIN FETCH c.reservations WHERE phoneNumber = :phoneNumber", Customer.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
            if (Objects.nonNull(customer)) {
                return customer;
            }
        } catch (NoResultException ignored) {}
        return null;
    }
}
