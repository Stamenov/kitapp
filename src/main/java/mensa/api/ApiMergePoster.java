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

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Session;

@Path("/merge/")
public class ApiMergePoster {
	/**
	 * Submitting merge suggestion
	 * @param args mealid1, mealid2, token
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response mergeByIds(Args args){
		String userid;
		try { 
			userid = Checker.getUserid(args.getToken());	
		} catch (BadTokenException e) {
			return Response.status(400).entity("bad token").build();
		}
		
		// Mealids can't be equal:
		if (args.getMealid1() == args.getMealid2()) {
			System.out.println("Received merge request with equal mealids, exiting.");
			return Response.status(400).build();
		}
		
		// TODO: Check if same request has been submitted already?
		
		// TODO: Request throttling per user?
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Meal meal1 = (Meal) session.get(Meal.class, args.getMealid1());
		Meal meal2 = (Meal) session.get(Meal.class, args.getMealid2());
		
		MealData mergedMealData = MealData.merge(meal1.getData(), meal2.getData());
		session.save(mergedMealData);
		session.getTransaction().commit();		

		return Response.ok().build();
	}
	
	private static class Args{
		@JsonProperty("token")
		private String token;
		@JsonProperty("mealid1")
		private int mealid1;
		@JsonProperty("mealid2")
		private int mealid2;
		
		public String getToken(){
			return token;
		}
		
		public int getMealid1(){
			return mealid1;
		}
		
		public int getMealid2(){
			return mealid2;
		}
	}
}
