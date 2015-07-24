package mensa.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import mensa.api.OAuth.BadTokenException;
import mensa.api.OAuth.Checker;
import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Image;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Session;

@Path("/image/")
public class ApiImagePoster {
	
	/**
	 * Saves image in the db
	 * @param args the img
	 */
	@Path("/post/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void postImage(Args args){
		String userid;
		try { 
			userid = Checker.getUserid(args.getToken());	
		} catch (BadTokenException e) {
			return;
		}
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Image image = new Image(userid, args.getImagePath());
		session.save(image);
		session.getTransaction().commit();
		
		session.beginTransaction();
		
		Meal meal = (Meal) session.get(Meal.class, args.getMealId());
		MealData data = meal.getData();
		data.addImage(image);
		
		session.update(data);
		session.getTransaction().commit();
	}
	
	private static class Args{
		@JsonProperty("token")
		private String token;
		@JsonProperty("mealId")
		private int mealId;
		@JsonProperty("imagePath")
		private String imagePath;
		
		public String getToken(){
			return token;
		}
		public int getMealId(){
			return mealId;
		}
		public String getImagePath(){
			return imagePath;
		}
	}
}
