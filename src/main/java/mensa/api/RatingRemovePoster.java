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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Remover for the ratings
 * @author Peter Vutov
 *
 */
@Path("/ratingRemove/")
public class RatingRemovePoster {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeRating(Args args){
		String userid;
		try { 
			userid = Checker.getUserid(args.getToken());	
		} catch (BadTokenException e) {
			return Response.status(400).build();
		}
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Criteria cr = session.createCriteria(Meal.class);
		cr.add(Restrictions.idEq(args.getMealid()));

		Meal meal = (Meal) cr.list().get(0);
		meal.getData().getRatings().remove(userid); // TODO: REFACTOR
		
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
