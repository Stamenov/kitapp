package mensa.api.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
        	
        	Configuration config = new Configuration().configure();
        	config.addAnnotatedClass(mensa.api.hibernate.domain.Image.class);
        	config.addAnnotatedClass(mensa.api.hibernate.domain.ImageList.class);
        	config.addAnnotatedClass(mensa.api.hibernate.domain.RatingList.class);
        	config.addAnnotatedClass(mensa.api.hibernate.domain.MealData.class);
        	config.addAnnotatedClass(mensa.api.hibernate.domain.Meal.class);

            return config.buildSessionFactory(
			    new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build() );
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
    	System.out.println("6");
        return sessionFactory;
    }

	public static void shutdown() {
		// Close caches and connection pools
		getSessionFactory().close();
	}
}