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
import mensa.api.hibernate.domain.MealData;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Session;

@Path("/merge/")
public class ApiMergePoster {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public MealData mergeByIds(Args args){
		int userid;
		try { 
			userid = Checker.getUserid(args.getToken());	
		} catch (BadTokenException e) {
			return null;
		}
		
		// TODO: Request throttling per user?
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Meal meal1 = (Meal) session.get(Meal.class, args.getMealid1());
		Meal meal2 = (Meal) session.get(Meal.class, args.getMealid2());
		
		MealData mergedMealData = MealData.merge(meal1.getData(), meal2.getData());
		session.save(mergedMealData);
		session.getTransaction().commit();
		
		return mergedMealData;
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
