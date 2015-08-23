import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mensa.api.ApiMergePoster;
import mensa.api.admin.InactiveMealData;
import mensa.api.admin.Merge;
import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;
import mensa.api.hibernate.domain.Tags;
import mensa.api.hibernate.domain.User;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ApiMergePosterBasicTest {
	private SessionFactory sessionFactory;
    private Session session = null;
    
    private int mealid1;
    private int mealid2;
    
    private String userid = "1";
    
    private MealData mergeResult = null;

    @Before
	public void before() {
		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
		
		// Populate db
    	
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
    	Meal testMeal2 = new Meal();
    	
    	MealData sameData1 = new MealData(testMeal1, sameTags1, true);
    	MealData sameData2 = new MealData(testMeal2, sameTags2, true);
    	
    	testMeal1.setData(sameData1);
    	testMeal2.setData(sameData2);

		session.beginTransaction();
    	session.save(testMeal1);
    	session.save(testMeal2);
    	session.getTransaction().commit();
    	
    	mealid1 = testMeal1.getMealid();
    	mealid2 = testMeal2.getMealid();
    }
    
    /**
     * App-side test of merging.
     */
    @Test
    public void basicTest() {
    	
    	ApiMergePoster mergeMeals = new ApiMergePoster();
    	mergeMeals.doMerge(userid, mealid1, mealid2);
    	
		session.beginTransaction();
		Meal meal1FromDb = (Meal) session.get(Meal.class, mealid1);
		Meal meal2FromDb = (Meal) session.get(Meal.class, mealid2);
    	session.getTransaction().commit();
    	
    	userid = userid;
    	
		session.beginTransaction();
		Criteria inactiveMealData = session.createCriteria(MealData.class);
		inactiveMealData.add(Restrictions.eq("active", Boolean.FALSE));
		List<MealData> resultList = inactiveMealData.list();
		HashSet<MealData> set = new HashSet<MealData>(resultList);
    	session.getTransaction().commit();

		//one inactive should be there
		assertEquals(1, set.size());
		
		// Test if duplicate merges show up, they shouldn't:
    	mergeMeals.doMerge(userid, mealid1, mealid2);
    	
		assertEquals(1, set.size());
		
		mergeResult = resultList.get(0);
		
		//meals in the inactive mealdata same meals as set
		Iterator<MealData> it = set.iterator();
		MealData data;
		while (it.hasNext()) {
			data = it.next();
			for (Meal meal : data.getMeals()) {
				assertTrue(mealid1 == meal.getMealid() || mealid2 == meal.getMealid());
			}
		}
		
    }
    
    /**
     * Test the admin side of merging.
     */
    @Test
    public void adminTestAccept() {
    	ApiMergePoster mergeMeals = new ApiMergePoster();
    	mergeMeals.doMerge(userid, mealid1, mealid2);
    	
    	// get meals that will be displayed in adminpanel
    	List<List<Meal>> meals = (List<List<Meal>>) new InactiveMealData().getInactiveMealDatas().getEntity();
    	
    	assertEquals(1, meals.size());
    	
    	// check if merge is successfully finalized
    	int inactiveDataId = meals.get(0).get(0).getData().getId();
    	new Merge().doFinalize(inactiveDataId, true);
    	
    	meals = (List<List<Meal>>) new InactiveMealData().getInactiveMealDatas().getEntity();    	
    	assertEquals(0, meals.size());
    	
    	session.close();
    	session = sessionFactory.openSession();
    	
    	session.beginTransaction();
    	MealData data = session.get(MealData.class, inactiveDataId);
    	Set<Meal> dbMeals = data.getMeals();
    	session.getTransaction().commit();
    	
    	for (Meal meal : dbMeals) {
    		assertEquals(meal.getData().getId(), data.getId());
    	}
    }
    /**
     * Test the admin side of merging if proposal is refused.
     */
    @Test
    public void adminTestRefuse() {
    	ApiMergePoster mergeMeals = new ApiMergePoster();
    	mergeMeals.doMerge(userid, mealid1, mealid2);
    	
    	session.beginTransaction();
    	int dataid1 = session.get(Meal.class, mealid1).getData().getId();
    	int dataid2 = session.get(Meal.class, mealid2).getData().getId();
    	session.getTransaction().commit();
    	
    	// get meals that will be displayed in adminpanel
    	List<List<Meal>> meals = (List<List<Meal>>) new InactiveMealData().getInactiveMealDatas().getEntity();
    	
    	assertEquals(1, meals.size());
    	
    	// Check if rejected merges properly remove the meal proposals with no changes:
    	int inactiveDataId = meals.get(0).get(0).getData().getId();
    	new Merge().doFinalize(inactiveDataId, false);
    	
    	meals = (List<List<Meal>>) new InactiveMealData().getInactiveMealDatas().getEntity();    	
    	assertEquals(0, meals.size());
    	
    	session.close();
    	session = sessionFactory.openSession();
    	

    	session.beginTransaction();
    	int dataid1AfterReject = session.get(Meal.class, mealid1).getData().getId();
    	int dataid2AfterReject = session.get(Meal.class, mealid2).getData().getId();
    	session.getTransaction().commit();
    	
    	assertEquals(dataid1AfterReject, dataid1);
    	assertEquals(dataid2AfterReject, dataid2);
    }
    
    

	@After
    public void after() {
		session.beginTransaction();
		Criteria inactiveMealData = session.createCriteria(MealData.class);
		inactiveMealData.add(Restrictions.eq("active", Boolean.FALSE));
		List<MealData> leftoverProposals = inactiveMealData.list();
		Iterator<MealData> it = leftoverProposals.iterator();
		while (it.hasNext()) {
			session.delete(it.next());
		}
		session.getTransaction().commit();

		// If merge was unsuccessful, delete meals explicitly:
		session.beginTransaction();		
		Meal meal = session.get(Meal.class, mealid1);
		if (meal != null) {
			session.delete(meal);
		}
		session.getTransaction().commit();
		
		session.beginTransaction();
		Meal meal2 = session.get(Meal.class, mealid2);
		if (meal2 != null) {
			session.delete(meal2);
		}
		
		session.getTransaction().commit();
		session.close();
    }
}
