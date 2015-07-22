package mensa.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Path("/ratingRemove/")
public class RatingRemovePoster {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Meal removeRating(mealUserPair ratingKeys){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Criteria cr = session.createCriteria(Meal.class);
		cr.add(Restrictions.idEq(ratingKeys.mealid));

		Meal meal = (Meal) cr.list().get(0);
		meal.getData().getRatings().remove(ratingKeys.userid); // TODO: REFACTOR
		
		session.merge(meal);
        session.getTransaction().commit();
        return meal;
		
	}
	
	private static class mealUserPair {
		@JsonProperty("mealid")
		private int mealid;
		@JsonProperty("userid")
		private int userid;
		
		public int getMealid() {
			return mealid;
		}
		public int getUserid() {
			return userid;
		}
	}

}
