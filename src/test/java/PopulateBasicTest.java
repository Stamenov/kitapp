import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Iterator;
import java.util.List;

import mensa.api.Populate;
import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Offer;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class PopulateBasicTest {
	private SessionFactory sessionFactory;
    private Session session = null;

	@Before
	public void before() {		
		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}
	
	@Test
	public void basicTest() {
		// Make extra sure we're working with an empty database. 
		// Other tests should be cleaning up but you never know.
		session.beginTransaction();
		List<Offer> offers = session.createCriteria(Offer.class).list();
		session.getTransaction().commit();
		assertEquals(0, offers.size());
		
		new Populate().run();
		session.beginTransaction();
		offers = session.createCriteria(Offer.class).list();
		session.getTransaction().commit();

		assertNotEquals(0, offers.size());
		System.out.println("Populate test returned " + offers.size() + " offers.");
	}

	@After
    public void after() {
		session.beginTransaction();
		Iterator<Offer> it = session.createCriteria(Offer.class).list().iterator();
		while (it.hasNext()) {
			session.delete(it.next());
		}
		session.getTransaction().commit();
		session.close();
    }
}
