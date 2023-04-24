package com.klima.matej.tennis_reservations_system.service;

import com.klima.matej.tennis_reservations_system.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Objects;

/**
 * Generic abstract service implementation.
 * @param <T> type of the object
 */
public abstract class GenericServiceImpl<T> implements GenericService<T>{
    /**
     * Session factory used to create sessions.
     */
    private final SessionFactory sessionFactory;

    /**
     * Type of the object.
     */
    private final Class<T> classType;

    public GenericServiceImpl(Class<T> classType) {
        this.sessionFactory = HibernateUtil.getSessionFactory();
        this.classType = classType;
    }

    /**
     * {@inheritDoc}
     */
    public T save(T object) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(object);
            transaction.commit();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List<T> findAll(){
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM " + classType.getName(), classType).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public T findById(long id) {
        try (Session session = sessionFactory.openSession()) {
            T object = session.get(classType, id);
            if (Objects.nonNull(object)) {
                return object;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public T update(T object) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(object);
            transaction.commit();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean deleteById(long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            T object = session.get(classType, id);
            if (Objects.nonNull(object)) {
                session.remove(object);
            }
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }
}
