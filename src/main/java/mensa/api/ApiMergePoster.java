package mensa.api;

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
import org.hibernate.Session;

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
		
		if (!User.hasUsesLeft(userid)) {
			return Response.status(429).entity("merge/image limit exceeded").build();			
		}
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Meal meal1 = (Meal) session.get(Meal.class, args.getMealid1());
		Meal meal2 = (Meal) session.get(Meal.class, args.getMealid2());
		
		if (meal1.getData() == meal2.getData()) {
			System.out.println("Received merge request of meals that are already merged or equal, exiting.");
			return Response.status(400).entity("These meals are already merged.").build();
		}
		
		//TODO CHECK if meals are involved in pending merges
		
		MealData mergedMealData = MealData.merge(meal1.getData(), meal2.getData());
		session.save(mergedMealData);
		session.getTransaction().commit();	
		
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
