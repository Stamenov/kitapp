package mensa.api;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import mensa.api.OAuth.BadTokenException;
import mensa.api.OAuth.Checker;
import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Image;
import mensa.api.hibernate.domain.Meal;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Session;

@Path("/meal/")
public class ApiMealPoster {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Meal getMealById(Args args){
		int userid;
		try { 
			userid = Checker.getUserid(args.getToken());	
		} catch (BadTokenException e) {
			return null;
		}
	
		Session session = HibernateUtil.getSessionFactory().openSession();
		Meal meal = (Meal) session.get(Meal.class, args.getMealid());
		
		meal.setCurrentUser(userid);
		
		//temporarily exchange the img-set for a img-set with active imgs only
		//dont save in db, just for api-purposes
		Set<Image> currImages = meal.getData().getImages();
		Set<Image> newImagesSet = new HashSet<Image>();
		for(Image img: currImages){
			if(img.getActive()){
				newImagesSet.add(img);
			}
		}
		meal.getData().setImages(newImagesSet);
		return meal;
	}	
	

	private static class Args{
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
