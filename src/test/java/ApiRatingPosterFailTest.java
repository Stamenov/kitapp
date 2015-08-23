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
    private int testOfferid;
    private int testTimestamp;
    private String mealName;
    
	@Before
	public void before() {
		testTimestamp = (int) new Date().getTime();
		mealName = "mealForOffer";
		
		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}	

	
	@Test
	public void invalidRatingTest() {
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
		testOffer.setId(testOfferid);
		testOffer.setTimestamp(testTimestamp);
		testOffer.setId(1);

		session.beginTransaction();
		session.save(testOffer);
		session.getTransaction().commit();
		
		testOfferid = testOffer.getId();

		int ratingValue = 6;
		new ApiRatingPoster().doRate("1", testMeal.getMealid(), ratingValue);
		
		session.beginTransaction();
		Meal meal = session.get(Meal.class, testMeal.getMealid());
		session.getTransaction().commit();
		meal.setCurrentUser("1");
		assertNull(meal.getData().getRatings().getCurrentUserRating());
		
	}
	
	@After
    public void after() {
		session.beginTransaction();
		Offer offer = session.get(Offer.class, testOfferid);
		session.delete(offer);
		session.getTransaction().commit();
		session.close();
    }
}
