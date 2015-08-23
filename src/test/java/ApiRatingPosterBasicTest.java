import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import mensa.api.ApiRatingPoster;
import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;
import mensa.api.hibernate.domain.Offer;
import mensa.api.hibernate.domain.Rating;
import mensa.api.hibernate.domain.RatingCollection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ApiRatingPosterBasicTest {

	private SessionFactory sessionFactory;
    private Session session = null;
    private int testTimestamp;
    private int offerid;
    private int mealid;
    private String mealName = "mealForOffer";;
    
	@Before
	public void before() {
		testTimestamp = (int) new Date().getTime();
		
		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
		
		// data
		MealData testData = new MealData();
		testData.setRatings(new RatingCollection());
		
		Meal testMeal = new Meal();
		testMeal.setName(mealName);
		testMeal.setData(testData);
		
		session.beginTransaction();
		session.save(testMeal);
		session.getTransaction().commit();
		
		Offer testOffer = new Offer();
		testOffer.setMeal(testMeal);
		testOffer.setTimestamp(testTimestamp);

		session.beginTransaction();
		session.save(testOffer);
		session.getTransaction().commit();
		offerid = testOffer.getId();
		mealid = testMeal.getMealid();
	}
	
	@Test
	public void basicTest() {

		int ratingValue = 4;
		Meal response = (Meal) new ApiRatingPoster().doRate("1", mealid, ratingValue).getEntity();
		
		session.close();
		
		session = sessionFactory.openSession();				
		session.beginTransaction();
		Meal meal = session.get(Meal.class, mealid);
		session.getTransaction().commit();
		
		assertEquals(meal.getMealid(), response.getMealid());
		
		RatingCollection r = meal.getData().getRatings();
		assertEquals((int) r.getAverage(), ratingValue);
		
		meal.setCurrentUser("1");
		
		assertNotNull(r.getCurrentUserRating());
		assertEquals(r.getCurrentUserRating().getValue(), ratingValue);
		
		// test if rating updates correctly:
		response = (Meal) new ApiRatingPoster().doRate("1", mealid, ratingValue - 1).getEntity();
		assertEquals(response.getData().getRatings().getCurrentUserRating().getValue(), ratingValue - 1);
		}

	@After
    public void after() {
		session.beginTransaction();
		Offer offer = session.get(Offer.class, offerid);
		session.delete(offer);
		session.getTransaction().commit();
		session.close();
    }
}
