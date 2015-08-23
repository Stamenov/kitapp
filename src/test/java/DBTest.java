
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DBTest {
	
    private SessionFactory sessionFactory;
    private Session session = null;
	String testMealName;
	private int mealid = 0;
    
	@Before
	public void before() {
		testMealName = "testName";
		
		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();		
	}
	
	@Test
	public void testDatabaseConnection() {

		Meal testMeal = new Meal();
		testMeal.setName(testMealName);

		session.beginTransaction();
		session.save(testMeal);
		session.getTransaction().commit();
		mealid = testMeal.getMealid();
		
		Meal mealFromDb = (Meal) session.get(Meal.class, testMeal.getMealid());
	    assertNotNull(mealFromDb);

	}
    @After
    public void after() {
    	if (mealid != 0) {
			session.beginTransaction();
			session.delete(session.get(Meal.class, mealid));
			session.getTransaction().commit();
		}
    	session.close();
    }

	
	
}
