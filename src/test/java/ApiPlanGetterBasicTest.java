import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

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

public class ApiPlanGetterBasicTest {

	private SessionFactory sessionFactory;
    private Session session = null;
    private int testTimestamp;
    private String mealName;
    private Offer testOffer = null;
    
	@Before
	public void before() {
		testTimestamp = (int) new Date().getTime();
		mealName = "mealForOffer";
		
		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}
	
	@Test
	public void basicTest() {
		Meal testMeal1 = new Meal();
		testMeal1.setName(mealName);
		
		session.beginTransaction();
		session.save(testMeal1);
		session.getTransaction().commit();
		
		testOffer = new Offer();
		testOffer.setMeal(testMeal1);
		testOffer.setTimestamp(testTimestamp);
		testOffer.setId(1);

		session.beginTransaction();
		session.save(testOffer);
		session.getTransaction().commit();
		
		ApiPlanGetter getPlan = new ApiPlanGetter();
		
		Response planResponse = getPlan.getPlanByTimestamps(testTimestamp, testTimestamp + 10);
		ArrayList<Offer> planFromApi = (ArrayList) planResponse.getEntity();
		
	    assertNotNull(planFromApi);
	    assertEquals(planFromApi.size(), 1);
	    assertEquals(planFromApi.get(0).getMeal().getName(), mealName);
	}

	@After
    public void after() {
		session.beginTransaction();
		if (testOffer != null) {
			session.delete(testOffer);
		}
		session.getTransaction().commit();
		session.close();
    }
}
