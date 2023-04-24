package com.klima.matej.tennis_reservations_system.util;

        import org.hibernate.SessionFactory;
        import org.hibernate.boot.Metadata;
        import org.hibernate.boot.MetadataSources;
        import org.hibernate.boot.registry.StandardServiceRegistry;
        import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Hibernate Utility class with a convenient method to get Session Factory object.
 */
public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    /**
     * Returns Session Factory object.
     * @return Session Factory object.
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder().configure().build();

                MetadataSources sources = new MetadataSources(registry);

                Metadata metadata = sources.getMetadataBuilder().build();

                sessionFactory = metadata.getSessionFactoryBuilder().build();

            } catch (Exception e) {
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }
}