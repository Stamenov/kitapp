import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

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
    private String mealName;
    
	@Before
	public void before(){
		testMealid = 1;
		testOfferid = 1;
		//timestamp for 27.07 00:00
		testTimestamp = 1437955200;
		mealName = "mealForOffer";
		
		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
		session.beginTransaction();
	}
	
	@Test
	public void testGetPlan(){
		Meal testMeal1 = new Meal();
		testMeal1.setMealid(testMealid);
		testMeal1.setMealid(testOfferid);
		testMeal1.setName(mealName);
		session.save(testMeal1);
		
		Offer testOffer = new Offer();
		testOffer.setMeal(testMeal1);
		testOffer.setId(testOfferid);
		testOffer.setTimestamp(testTimestamp);
		testOffer.setId(1);
		
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
     sessionFactory.getCurrentSession().close();
    }
}
