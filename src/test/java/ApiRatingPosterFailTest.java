import static org.junit.Assert.assertNull;

import java.util.Date;

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


public class ApiRatingPosterFailTest {

	private SessionFactory sessionFactory;
    private Session session = null;
    private int offerid;
    private int mealid;
    private int testTimestamp;
    private String mealName = "mealForOffer";
    
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
		testOffer.setId(offerid);
		testOffer.setTimestamp(testTimestamp);
		testOffer.setId(1);

		session.beginTransaction();
		session.save(testOffer);
		session.getTransaction().commit();
		
		offerid = testOffer.getId();
		mealid = testMeal.getMealid();
	}	

	
	@Test
	public void invalidRatingTest() {

		int ratingValue = 6;
		new ApiRatingPoster().doRate("1", mealid, ratingValue);
		
		session.beginTransaction();
		Meal meal = session.get(Meal.class, mealid);
		session.getTransaction().commit();
		meal.setCurrentUser("1");
		assertNull(meal.getData().getRatings().getCurrentUserRating());
		
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
