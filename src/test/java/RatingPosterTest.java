import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;

import javax.ws.rs.core.Response;

import mensa.api.ApiPlanGetter;
import mensa.api.ApiRatingPoster;
import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;
import mensa.api.hibernate.domain.Offer;
import mensa.api.hibernate.domain.RatingCollection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RatingPosterTest {

	private SessionFactory sessionFactory;
    private Session session = null;
    private int testMealid;
    private int testOfferid;
    private int testTimestamp;
    private String mealName;
    
	@Before
	public void before() {
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
	public void testRatingPoster() {
		MealData testData = new MealData();
		testData.setRatings(new RatingCollection());
		
		Meal testMeal1 = new Meal();
		testMeal1.setMealid(testMealid);
		testMeal1.setName(mealName);
		testMeal1.setData(testData);
		session.save(testMeal1);
		
		Offer testOffer = new Offer();
		testOffer.setMeal(testMeal1);
		testOffer.setId(testOfferid);
		testOffer.setTimestamp(testTimestamp);
		testOffer.setId(1);
		
		session.save(testOffer);
		session.getTransaction().commit();

		int ratingValue = 4;
		new ApiRatingPoster().doRate("1", testMealid, ratingValue);
		
		session.beginTransaction();
		Meal meal = session.get(Meal.class, testMealid);
		session.getTransaction().commit();
		meal.setCurrentUser("1");
		assertEquals(meal.getData().getRatings().getCurrentUserRating().getValue(), ratingValue);
	}
	
	@Test
	public void testRatingPoster2() {
		MealData testData = new MealData();
		testData.setRatings(new RatingCollection());
		
		Meal testMeal1 = new Meal();
		testMeal1.setMealid(testMealid);
		testMeal1.setName(mealName);
		testMeal1.setData(testData);
		session.save(testMeal1);
		
		Offer testOffer = new Offer();
		testOffer.setMeal(testMeal1);
		testOffer.setId(testOfferid);
		testOffer.setTimestamp(testTimestamp);
		testOffer.setId(1);
		
		session.save(testOffer);
		session.getTransaction().commit();


		int ratingValue = 6;
		new ApiRatingPoster().doRate("1", testMealid, ratingValue);
		
		session.beginTransaction();
		Meal meal = session.get(Meal.class, testMealid);
		session.getTransaction().commit();
		meal.setCurrentUser("1");
		assertNull(meal.getData().getRatings().getCurrentUserRating());
		
	}

	@After
    public void after() {
     sessionFactory.getCurrentSession().close();
    }
}
