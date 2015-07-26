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

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Session;

/**
 * Responsible for removing ratings.
 * @author Peter Vutov
 */
@Path("/ratingRemove/")
public class RatingRemovePoster {
	/**
	 * Remove the Rating submitted by a given user.
	 * @param args A user token and a mealid.
	 * @return Response indicating success or failure. In case of success, the updated Meal is also returned.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeRating(Args args) {
		String userid;
		try { 
			userid = Checker.getUserid(args.getToken());	
		} catch (BadTokenException e) {
			return Response.status(400).entity("bad token").build();
		}
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		Meal meal = (Meal) session.get(Meal.class, args.getMealid());
		meal.unrate(userid);
		
		session.merge(meal);
        session.getTransaction().commit();
        
		meal.setCurrentUser(userid);
        return Response.ok().entity(meal).build();
	}
	
	private static class Args {
		@JsonProperty("token")
		private String token;
		@JsonProperty("mealid")
		private int mealid;

		public String getToken() {
			return token;
		}		
		public int getMealid() {
			return mealid;
		}
	}

}
