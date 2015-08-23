import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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


public class PopulateSpeculativeTest {

	private SessionFactory sessionFactory;
    private Session session = null;

	@Before
	public void before() {		
		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}

	// This test checks if populate generated a reasonable number of offers.
	@Test
	public void speculativeTest() {
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

		/**
		 * During vacation, 250 is a normal offer count. Outside vacation, a bit more. These 
		 * sanity-check values should only fail if the mensa is serving nothing or almost nothing 
		 * for the time period covered by the API.
		 */
		if (offers.size() < 100) {
			fail("Populate returned less offers than sanity-check value.");
		}
		
		if (offers.size() > 2000) {
			fail("Populate returned more offers than sanity-check value.");
		}
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
