package mensa.api;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mensa.api.OAuth.BadTokenException;
import mensa.api.OAuth.Checker;
import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.Rating;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Session;

/**
 * Responsible for adding ratings.
 * @author Martin Stamenov
 */
@Path("/rating/")
public class ApiRatingPoster {
	
	/**
	 * Rate a meal.
	 * @param args Must contain a mealid, a value, a user token.
	 * @return Response indicating success or failure. In the case of success, the updates meal is also returned.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response saveRating(Args args) {
		String userid;
		try { 
			userid = Checker.getUserid(args.getToken());	
		} catch (BadTokenException e) {
			return Response.status(400).entity("bad token").build();
		}
		
		return doRate(userid, args.getMealid(), args.getValue());
	}

	/**
	 * The core of the rating method, split off so it fits in the test harness.
	 * @param userid 
	 * @param args 
	 * @return 
	 */
	public Response doRate(String userid, int mealid, int value) {		
		//  if !rating.between(1, 5), return:
		if (!(value >= 1 && value <= 5)) {
			return Response.status(400).build();
		}
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Meal meal = (Meal) session.get(Meal.class, mealid);
		Rating rating = new Rating();
		rating.setUserid(userid);
		rating.setValue(value);
		meal.rate(rating);
		
		session.merge(meal);
        session.getTransaction().commit();
        session.close();
        
		meal.setCurrentUser(userid);
        return Response.ok().entity(meal).build();
	}
	
	private static class Args {
		@JsonProperty("token")
		private String token;
		@JsonProperty("mealid")
		private int mealid;
		@JsonProperty("value")
		private int value;
		
		public String getToken() {
			return token;
		}		
		public int getMealid() {
			return mealid;
		}
		public int getValue() {
			return value;
		}
	}
}
