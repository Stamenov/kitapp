import static org.junit.Assert.assertEquals;

import mensa.api.ApiMealGetter;
import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;
import mensa.api.hibernate.domain.RatingCollection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ApiMealGetterBasicTest {
	
	private SessionFactory sessionFactory;
    private Session session = null;
    private int mealid;
    private String mealName;
    
	@Before
	public void before() {
		mealName = "mea;jkl";
		
		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}
	
	@Test
	public void basicTest() {
		MealData testData = new MealData();
		testData.setRatings(new RatingCollection());
		
		Meal testMeal = new Meal();
		testMeal.setName(mealName);
		testMeal.setData(testData);
		
		session.beginTransaction();
		session.save(testMeal);
		session.getTransaction().commit();
		
		mealid = testMeal.getMealid();
		
		session.beginTransaction();
		Meal meal = (Meal) new ApiMealGetter().doGet(mealid, "fgsfg").getEntity();
		session.getTransaction().commit();

		assertEquals(meal.getName(), testMeal.getName());
		assertEquals(meal.getMealid(), mealid);
	}

	@After
    public void after() {
		session.beginTransaction();
		Meal meal = session.get(Meal.class, mealid);
		session.delete(meal);
		session.getTransaction().commit();
		session.close();
    }
}
