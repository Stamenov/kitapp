package mensa.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
        	
        	System.out.println("1");
        	new Configuration();
        	System.out.println("2");
        	new Configuration().configure();
        	System.out.println("3");
        	new StandardServiceRegistryBuilder();
        	System.out.println("4");
        	new StandardServiceRegistryBuilder().build();
        	System.out.println("5");
        	System.out.println("6");
        	//Crashes after this line, why??
            return new Configuration().configure().buildSessionFactory(
			    new StandardServiceRegistryBuilder().build() );
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

	public static void shutdown() {
		// Close caches and connection pools
		getSessionFactory().close();
	}
}