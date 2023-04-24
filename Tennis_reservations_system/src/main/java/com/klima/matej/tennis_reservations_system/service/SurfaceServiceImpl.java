package com.klima.matej.tennis_reservations_system.service;

import com.klima.matej.tennis_reservations_system.entity.Surface;
import com.klima.matej.tennis_reservations_system.util.HibernateUtil;

import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service implementation for surface.
 */
@Service
public class SurfaceServiceImpl extends GenericServiceImpl<Surface> implements SurfaceService{
    /**
     * Session factory used to create sessions.
     */
    private final SessionFactory sessionFactory;

    /**
     * Constructor.
     */
    public SurfaceServiceImpl() {
        super(Surface.class);
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Surface findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            Surface surface =  session.createQuery("SELECT s FROM Surface s LEFT JOIN FETCH s.courts WHERE s.id = :id", Surface.class)
                    .setParameter("id", id)
                    .getSingleResult();
            if (Objects.nonNull(surface)) {
                return surface;
            }
        } catch (NoResultException ignored) {}
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Surface findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Surface surface =  session.createQuery("FROM Surface WHERE name = :name", Surface.class)
                    .setParameter("name", name)
                    .getSingleResult();
            if (Objects.nonNull(surface)) {
                return surface;
            }
        } catch (NoResultException ignored) {}
        return null;
    }
}
