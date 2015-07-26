package mensa.api.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Util class for hibernate (db) operations.
 * @author Petar Vutov / Hibernate tuts
 */
public final class HibernateUtil {

	/**
	* Utility class, do not initialize.
	*/
	private HibernateUtil() {
	}

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
        	
        	Configuration config = new Configuration().configure();
            config.addAnnotatedClass(mensa.api.hibernate.domain.User.class);
        	config.addAnnotatedClass(mensa.api.hibernate.domain.Image.class);
        	config.addAnnotatedClass(mensa.api.hibernate.domain.ImageProposal.class);
        	config.addAnnotatedClass(mensa.api.hibernate.domain.Rating.class);
        	config.addAnnotatedClass(mensa.api.hibernate.domain.RatingCollection.class);
        	config.addAnnotatedClass(mensa.api.hibernate.domain.Tags.class);
        	config.addAnnotatedClass(mensa.api.hibernate.domain.MealData.class);
        	config.addAnnotatedClass(mensa.api.hibernate.domain.Meal.class);
        	config.addAnnotatedClass(mensa.api.hibernate.domain.Price.class);
        	config.addAnnotatedClass(mensa.api.hibernate.domain.Offer.class);
        	
            return config.buildSessionFactory(
			    new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build());
        } catch (Throwable e) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + e);
            throw new ExceptionInInitializerError(e);
        }
    }
	
	/**
	* Get the session factory.
	* @return SessionFactory the factory.
	*/
    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

	/**
	* Close caches and connection pools
	*/
	public static void shutdown() {
		getSessionFactory().close();
	}
}