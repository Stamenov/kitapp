
import static org.junit.Assert.assertEquals;

import java.util.List;

import mensa.api.ApiNamesGetter;
import mensa.api.ApiNamesGetter.StringMealidPair;
import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;
import mensa.api.hibernate.domain.RatingCollection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ApiNamesGetterBasicTest {

	private SessionFactory sessionFactory;
    private Session session = null;
    
    private String mealName = "gdhsgh";
	private String mealName2 = "fhgsh";
    
    private int mealid = 0;
    private int mealid2 = 0;

	@Before
	public void before() {		
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
		mealid = testMeal.getMealid();
		
		MealData testData2 = new MealData();
		testData.setRatings(new RatingCollection());
		
		Meal testMeal2 = new Meal();
		testMeal2.setName(mealName2);
		testMeal2.setData(testData2);
		
		session.beginTransaction();
		session.save(testMeal2);
		session.getTransaction().commit();
		mealid2 = testMeal2.getMealid();
	}

	@Test
	public void basicTest() {
		
		List<StringMealidPair> pairs = (List<StringMealidPair>) new ApiNamesGetter().getNames().getEntity();
		
		assertEquals(2, pairs.size());
		
		StringMealidPair first = pairs.get(0);
		StringMealidPair second = pairs.get(1);
		
		assertEquals(first.getName(), mealName);
		assertEquals(second.getName(), mealName2);
		assertEquals(first.getMealid(), mealid);
		assertEquals(second.getMealid(), mealid2);
	}

	@After
    public void after() {
		session.beginTransaction();
		
		if (mealid != 0) {
			session.delete(session.get(Meal.class, mealid));
		}
		
		if (mealid2 != 0) {
			session.delete(session.get(Meal.class, mealid2));
		}
		
		session.getTransaction().commit();
		session.close();
    }
}
