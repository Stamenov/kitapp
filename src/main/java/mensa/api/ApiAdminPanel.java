package mensa.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Path("/admin/")
public class ApiAdminPanel {
	
	@GET
	@Path("/mergingMeals/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInactiveMealDatas(){
		
		ArrayList<ArrayList<Meal>> result = new ArrayList<ArrayList<Meal>>();

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Criteria inactiveMealData = session.createCriteria(MealData.class);
		inactiveMealData.add(Restrictions.eq("active", Boolean.FALSE));

		List resultList = inactiveMealData.list();
		Iterator<MealData> it = resultList.iterator();
        session.getTransaction().commit();

        
        ArrayList<Meal> currMeals;
		while(it.hasNext()) {
			currMeals = new ArrayList<Meal>();
			MealData next = it.next();
			currMeals.addAll(next.getMeals());
			for(Meal currMeal: currMeals){
				currMeal.setData(next);
			}
			result.add(currMeals);
		}
		return Response.ok(result).header("Access-Control-Allow-Origin", "*").build();
	}
	
	 @OPTIONS
	 @Path("/finalizeMerge/")
	 public Response getOptions() {
		 return Response.ok()
		  .header("Access-Control-Allow-Origin", "*")
	      .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
	      .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With").build();
	  }
	
	@POST
	@Path("/finalizeMerge/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response finalizeMerge(MergingResponse mealDataResponse){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		MealData mealData = (MealData) session.get(MealData.class, mealDataResponse.getMealDataId());

		if(mealDataResponse.getApproved()){
			mealData.setActive(true);
			session.update(mealData);
			session.getTransaction().commit();
			
			ArrayList<MealData> oldMealDatas = new ArrayList<MealData>();

			session.beginTransaction();
			for(Meal meal: mealData.getMeals()){
				if(!oldMealDatas.contains(meal.getData())) {
					oldMealDatas.add(meal.getData());
				}
				meal.setData(mealData);
				session.update(meal);
			}
			session.getTransaction().commit();			

			session.beginTransaction();

			for(MealData data: oldMealDatas){
				Set<Meal> emptySet = new HashSet<Meal>();
				data.setMeals(emptySet);
				session.update(data);
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
		
		return Response.ok("success").build();

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
