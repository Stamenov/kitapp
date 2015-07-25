package mensa.api;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mensa.api.OAuth.BadTokenException;
import mensa.api.OAuth.Checker;
import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Image;
import mensa.api.hibernate.domain.Meal;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Session;

/**
 * Responsible for delivering details about specific meals to the app.
 * @author Petar Vutov
 */
@Path("/meal/")
public class ApiMealGetter {

	/**
	 * Gets meal by id.
	 * @param args A json object containing the mealid and token.
	 * @return The meal.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMealById(Args args) {
		String userid;
		try {
			userid = Checker.getUserid(args.getToken());
		} catch (BadTokenException e) {
			return Response.status(400).entity("bad token").build();
		}
	
		Session session = HibernateUtil.getSessionFactory().openSession();
		Meal meal = (Meal) session.get(Meal.class, args.getMealid());
		
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
