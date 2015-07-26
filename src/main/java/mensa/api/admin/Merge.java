package mensa.api.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Session;

/**
 * Class responsible for finalizing a merge, aka the admin-side of a merge.
 * @author Martin Stamenov
 */
@Path("/admin/finalizeMerge/")
public class Merge {

	/**
	 * Accepts a json-object telling if a merge is approved or not.
	 * Updates the meal-to-mealdata relations or deletes the pending mealdata.
	 * @param args A class containing the mealdata signifying the merge and the decision.
	 * @return response An http response indicating if the operation succeeded or erred.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response finalizeMerge(Args args) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		MealData mealData = (MealData) session.get(MealData.class, args.getMealDataId());

		if (args.getApproved()) {
			mealData.setActive(true);
			session.update(mealData);
			session.getTransaction().commit();
			
			ArrayList<MealData> oldMealDatas = new ArrayList<MealData>();

			session.beginTransaction();
			for (Meal meal: mealData.getMeals()) {
				if (!oldMealDatas.contains(meal.getData())) {
					oldMealDatas.add(meal.getData());
				}
				meal.setData(mealData);
				session.update(meal);
			}
			session.getTransaction().commit();	

			session.beginTransaction();

			for (MealData data: oldMealDatas) {
				Set<Meal> emptySet = new HashSet<Meal>();
				data.setMeals(emptySet);
				session.update(data);
				session.delete(data);	
			}
			session.getTransaction().commit();
			session.close();			
				
		} else {
			Set<Meal> emptySet = new HashSet<Meal>();
			mealData.setMeals(emptySet);
			session.update(mealData);
			session.getTransaction().commit();
		
			session.beginTransaction();
		    session.delete(mealData);
			session.getTransaction().commit();
		}

		session.close();
		return Response.ok().build();

	}
	
	private static class Args {
		@JsonProperty("mealDataId")
		private int mealDataId;
		@JsonProperty("approved")
		private boolean approved;
		
		public int getMealDataId() {
			return mealDataId;
		}
		public boolean getApproved() {
			return approved;
		}
	}
}
