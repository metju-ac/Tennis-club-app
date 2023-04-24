package com.klima.matej.tennis_reservations_system.service;

import com.klima.matej.tennis_reservations_system.entity.Reservation;
import com.klima.matej.tennis_reservations_system.util.HibernateUtil;

import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Service implementation for reservation.
 */
@Service
public class ReservationServiceImpl extends GenericServiceImpl<Reservation> implements ReservationService {
    /**
     * Session factory used to create sessions.
     */
    private final SessionFactory sessionFactory;

    /**
     * Constructor.
     */
    public ReservationServiceImpl() {
        super(Reservation.class);
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Reservation> findAllByCourtId(long courtId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT r FROM Reservation r WHERE r.court.id = :courtId", Reservation.class)
                    .setParameter("courtId", courtId)
                    .getResultList();
        } catch (NoResultException ignored) {}
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Reservation> findAllByPhoneNumber(String phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT r FROM Reservation r WHERE r.customer.phoneNumber = :phoneNumber", Reservation.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getResultList();
        } catch (NoResultException ignored) {}
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Reservation> findFutureByPhoneNumber(String phoneNumber) {
        List<Reservation> reservations = findAllByPhoneNumber(phoneNumber);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        return reservations.stream()
                .filter(reservation -> reservation.getStartsAt().after(currentTime))
                .toList();
    }
}
