package com.klima.matej.tennis_reservations_system.service;

import com.klima.matej.tennis_reservations_system.entity.Court;
import com.klima.matej.tennis_reservations_system.util.HibernateUtil;

import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service implementation for court.
 */
@Service
public class CourtServiceImpl extends GenericServiceImpl<Court> implements CourtService {
    /**
     * Session factory used to create sessions.
     */
    private final SessionFactory sessionFactory;

    /**
     * Constructor.
     */
    public CourtServiceImpl() {
        super(Court.class);
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Court findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Court court =  session.createQuery("SELECT c FROM Court c LEFT JOIN FETCH c.reservations WHERE c.id = :id", Court.class)
                    .setParameter("id", id)
                    .getSingleResult();
            if (Objects.nonNull(court)) {
                return court;
            }
        } catch (NoResultException ignored) {}
        return null;
    }
}
