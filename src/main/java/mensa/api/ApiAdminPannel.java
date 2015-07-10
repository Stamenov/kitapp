package mensa.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Path("/admin/")
public class ApiAdminPannel {
	
	@GET
	@Path("/inactiveMealData/")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<MealData> getInactiveMealDatas(){
		
		ArrayList<MealData> result = new ArrayList<MealData>();

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Criteria inactiveMealData = session.createCriteria(MealData.class);
		inactiveMealData.add(Restrictions.eq("active", Boolean.FALSE));

		List resultList = inactiveMealData.list();

		Iterator<MealData> it = resultList.iterator();
		
        session.getTransaction().commit();
        
		while(it.hasNext()) {
			MealData next = it.next();
			result.add(next);
		}
		return result;
	}
	
	@POST
	@Path("/finalizeMerge/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public MealData finalizeMerge(MergingResponse mealDataResponse){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		MealData mealData = (MealData) session.get(MealData.class, mealDataResponse.getMealDataId());

		if(mealDataResponse.getApproved()){
			mealData.setActive(true);
			session.update(mealData);
			session.getTransaction().commit();
			
			ArrayList<MealData> oldMealDatas = new ArrayList<MealData>();
			System.out.println("=====================>"+mealData.getMeals().size()+"<====================");
			session.beginTransaction();
			for(Meal meal: mealData.getMeals()){
				oldMealDatas.add(meal.getData());
				meal.setData(mealData);
				session.update(meal);
			}
			session.getTransaction().commit();			

			session.beginTransaction();
			for(MealData data: oldMealDatas){
				session.delete(data);	
			}
			session.getTransaction().commit();
			
				
		} else {
			Set<Meal> emptySet = new HashSet<Meal>();
			mealData.setMeals(emptySet);
			session.update(mealData);
			session.getTransaction().commit();
		
			session.beginTransaction();
		    session.delete(mealData);
			session.getTransaction().commit();
		}
		
		return mealData;
	}
	
	private static class MergingResponse{
		@JsonProperty("mealDataId")
		private int mealDataId;
		@JsonProperty("approved")
		private boolean approved;
		
		public int getMealDataId(){
			return mealDataId;
		}
		public boolean getApproved(){
			return approved;
		}
	}
}
