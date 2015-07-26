
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

	int testMealId;
	String testMealName;
    
	@Before
	public void before(){
		testMealId = 1;
		testMealName = "testName";
		
		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
		session.beginTransaction();
		
	}
	
	@Test
	public void testDatabaseConnection() {

		Meal testMeal = new Meal();
		testMeal.setMealid(testMealId);
		testMeal.setName(testMealName);
		
		session.save(testMeal);
		session.getTransaction().commit();
		
		Meal mealFromDb = (Meal) session.get(Meal.class, testMealId);
	    assertNotNull(mealFromDb);
	    assertEquals(testMealId, mealFromDb.getMealid());

	}
    @After
    public void after() {
     sessionFactory.getCurrentSession().close();
    }

	
	
}
