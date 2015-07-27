package mensa.api;

import java.util.Iterator;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mensa.api.OAuth.BadTokenException;
import mensa.api.OAuth.Checker;
import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;
import mensa.api.hibernate.domain.User;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Responsible for handling user requests for merging.
 * @author Martin Stamenov
 */
@Path("/merge/")
public class ApiMergePoster {
	/**
	 * Submitting merge suggestion
	 * @param args mealid1, mealid2, token
	 * @return An http response indicating success or failure in creating the merge suggestion.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response mergeByIds(Args args) {

		String userid;
		try { 
			userid = Checker.getUserid(args.getToken());	
		} catch (BadTokenException e) {
			return Response.status(400).entity("bad token").build();
		}		
		
		return doMerge(userid, args.getMealid1(), args.getMealid2());
	}

	/**
	 * Split off for testing.
	 * @param userid
	 * @param mealid1
	 * @param mealid2
	 * @return
	 */
	public Response doMerge(String userid, int mealid1, int mealid2) {

		if (!User.hasUsesLeft(userid)) {
			return Response.status(429).entity("merge/image limit exceeded").build();			
		}
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Meal meal1 = (Meal) session.get(Meal.class, mealid1);
		Meal meal2 = (Meal) session.get(Meal.class, mealid2);
		
		if (meal1.getData() == meal2.getData()) {
			System.out.println("Received merge request of meals that are already merged or equal, exiting.");
			return Response.status(400).entity("These meals are already merged.").build();
		}
		
		// Check if meals are involved in pending merges:
		boolean mealsAreInvolvedInMergeRequest = false;
		Criteria cr = session.createCriteria(MealData.class);
		cr.add(Restrictions.eq("active", Boolean.FALSE));
		Iterator<MealData> pendingMergesIterator = cr.list().iterator();
		
		while (pendingMergesIterator.hasNext()) {
			MealData next = pendingMergesIterator.next();
			if (next.getMeals().contains(meal1) || next.getMeals().contains(meal2)) {
				mealsAreInvolvedInMergeRequest = true;
			}
		}
		
		// If either meal is involved in a merge request, quit without doing anything:
		if (mealsAreInvolvedInMergeRequest) {
			return Response.ok().build();			
		}
		
		
		MealData mergedMealData = MealData.merge(meal1.getData(), meal2.getData());
		session.save(mergedMealData);
		session.getTransaction().commit();	
		session.close();
		
		User.reportSuccess(userid);	

		return Response.ok().build();
	}
	
	private static class Args {
		@JsonProperty("token")
		private String token;
		@JsonProperty("mealid1")
		private int mealid1;
		@JsonProperty("mealid2")
		private int mealid2;
		
		public String getToken() {
			return token;
		}
		
		public int getMealid1() {
			return mealid1;
		}
		
		public int getMealid2() {
			return mealid2;
		}
	}
}
