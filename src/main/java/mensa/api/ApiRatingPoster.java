package mensa.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.Rating;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Path("/rating/")
public class ApiRatingPoster {
	@POST // TODO: CHANGE TO PUT http://www.codereye.com/2010/12/configure-tomcat-to-accept-http-put.html
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Meal saveRating(RatingMeals ratingReceived){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Criteria cr = session.createCriteria(Meal.class);
		cr.add(Restrictions.idEq(ratingReceived.mealid));

		Meal meal = (Meal) cr.list().get(0);
		Rating rating = new Rating();
		rating.setUserid(ratingReceived.userid);
		rating.setValue(ratingReceived.value);
		meal.getData().getRatings().add(rating);
		
		session.merge(meal);
        session.getTransaction().commit();
        return meal;
		
	}
	
	private static class RatingMeals {
		@JsonProperty("mealid")
		private int mealid;
		@JsonProperty("value")
		private int value;
		@JsonProperty("userid")
		private int userid;
		
		public int getMealid() {
			return mealid;
		}
		public void setMealid(int mealid) {
			this.mealid = mealid;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		public int getUserid() {
			return userid;
		}
		public void setUserid(int userid) {
			this.userid = userid;
		}
	}
}
