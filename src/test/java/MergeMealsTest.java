import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import mensa.api.ApiMergePoster;
import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;
import mensa.api.hibernate.domain.Tags;
import mensa.api.hibernate.domain.User;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;

public class MergeMealsTest {
	private SessionFactory sessionFactory;
    private Session session = null;
    private int mealid1 = 1;
    private int mealid2 = 2;
    private int mealdataid1 = 1;
    private int mealdataid2 = 2;

    @Before
	public void before(){
		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
		session.beginTransaction();
    }
    
    @Test
    public void testMealsMerge(){
    	
    	User testUser = new User();
    	testUser.setUserid("1");
    	
    	Tags sameTags1 = new Tags();
    	sameTags1.setBio(true);
    	sameTags1.setAdd("add");
    	sameTags1.setCow(false);
    	sameTags1.setCow_aw(false);
    	sameTags1.setFish(false);
    	sameTags1.setPork(false);
    	sameTags1.setVeg(false);
    	sameTags1.setVegan(false);
    	
    	Tags sameTags2 = sameTags1.clone();
    	
    	Meal testMeal1 = new Meal();
    	testMeal1.setMealid(mealid1);
    	Meal testMeal2 = new Meal();
    	testMeal2.setMealid(mealid2);
    	
    	MealData sameData1 = new MealData(testMeal1, sameTags1, true);
    	MealData sameData2 = new MealData(testMeal2, sameTags2, true);
    	
    	testMeal1.setData(sameData1);
    	testMeal2.setData(sameData2);
    	
    	session.save(testUser);
    	session.save(testMeal1);
    	session.save(testMeal2);
    	session.getTransaction().commit();
    	
    	ApiMergePoster mergeMeals = new ApiMergePoster();
    	mergeMeals.doMerge(testUser.getUserid(), mealid1, mealid2);
    	
		session.beginTransaction();
		Meal meal1FromDb = (Meal) session.get(Meal.class, mealid1);
		Meal meal2FromDb = (Meal) session.get(Meal.class, mealid2);
		
		assertEquals(mealdataid1, meal1FromDb.getData().getId());
		assertEquals(mealdataid2, meal2FromDb.getData().getId());
		
		Criteria inactiveMealData = session.createCriteria(MealData.class);
		inactiveMealData.add(Restrictions.eq("active", Boolean.FALSE));
		List<MealData> resultList = inactiveMealData.list();
		HashSet<MealData> set = new HashSet<MealData>(resultList);

		//one inactive should be there
		assertEquals(1, set.size());
		
		//meals in the inactive mealdata same meals as set
		Iterator<MealData> it = set.iterator();
		MealData data;
		while(it.hasNext()){
			data = it.next();
			for (Meal meal : data.getMeals()){
				assertTrue(mealid1 == meal.getMealid() || mealdataid2 == meal.getMealid());
			}
		}

		
    }
}
