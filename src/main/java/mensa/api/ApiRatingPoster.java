package mensa.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import mensa.api.OAuth.BadTokenException;
import mensa.api.OAuth.Checker;
import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.Rating;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Path("/rating/")
public class ApiRatingPoster {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Meal saveRating(Args args){
		int userid;
		try { 
			userid = Checker.getUserid(args.getToken());	
		} catch (BadTokenException e) {
			return null;
		}
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Criteria cr = session.createCriteria(Meal.class);
		cr.add(Restrictions.idEq(args.getMealid()));

		Meal meal = (Meal) cr.list().get(0);
		Rating rating = new Rating();
		rating.setUserid(userid);
		rating.setValue(args.value);
		meal.getData().addRating(rating);
		
		session.merge(meal);
        session.getTransaction().commit();
        
		meal.setCurrentUser(userid);
        return meal;
		
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
