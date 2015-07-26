import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import mensa.api.ApiPlanGetter;
import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.Offer;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetPlanTest {

	private SessionFactory sessionFactory;
    private Session session = null;
    private int testMealid;
    private int testOfferid;
    private int testTimestamp;
    
	@Before
	public void before() {
		testMealid = 1;
		testOfferid = 1;
		//timestamp for 27.07 00:00
		testTimestamp = 1437955200;
		
		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
		session.beginTransaction();
	}
	
	@Test
	public void testGetPlan() {
		String name = "test";
		Meal testMeal1 = new Meal();
		testMeal1.setMealid(testMealid);
		testMeal1.setName(name);
		session.save(testMeal1);
		
		Offer testOffer = new Offer();
		testOffer.setMeal(testMeal1);
		testOffer.setId(testOfferid);
		testOffer.setTimestamp(testTimestamp);
		
		session.save(testOffer);
		session.getTransaction().commit();
		
		ApiPlanGetter getPlan = new ApiPlanGetter();
		
		Response planResponse = getPlan.getPlanByTimestamps(testTimestamp, testTimestamp + 10);
		ArrayList<Offer> a = (ArrayList) planResponse.getEntity();
		assertEquals(a.get(0).getMeal().getName(), name);
		assertEquals(a.get(0).getMeal().getMealid(), testMealid);
		assertEquals(a.get(0).getId(), testMealid);
	}

	@After
    public void after() {
     sessionFactory.getCurrentSession().close();
    }

}
