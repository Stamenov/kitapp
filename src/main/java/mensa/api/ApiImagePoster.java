package mensa.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Image;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Session;

@Path("/image/")
public class ApiImagePoster {
	@Path("/post/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Image postImage(ImagePost imagePost){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Image image = new Image(imagePost.getUserId(), imagePost.getImagePath());
		session.save(image);
		session.getTransaction().commit();
		
		session.beginTransaction();
		
		Meal meal = (Meal) session.get(Meal.class, imagePost.getMealId());
		MealData data = meal.getData();
		data.addImage(image);
		
		session.update(data);
		session.getTransaction().commit();
		
		return image;
	}
	
	private static class ImagePost{
		@JsonProperty("userId")
		private int userId;
		@JsonProperty("mealId")
		private int mealId;
		@JsonProperty("imagePath")
		private String imagePath;
		
		public int getUserId(){
			return userId;
		}
		public int getMealId(){
			return mealId;
		}
		public String getImagePath(){
			return imagePath;
		}
	}
}
