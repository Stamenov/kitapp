package mensa.api;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Image;
import mensa.api.hibernate.domain.Meal;

import org.hibernate.Session;

@Path("/meal/")
public class ApiMealGetter {
	
	@GET
	@Path("/{mealID:[0-9]*}/{userID:[0-9]*}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Meal getMealById(@PathParam("mealID") String mealid, @PathParam("userID") String userid){
	
		Session session = HibernateUtil.getSessionFactory().openSession();
		Meal meal = (Meal) session.get(Meal.class, Integer.parseInt(mealid));
		
		meal.setCurrentUser( Integer.parseInt(userid) );
		
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
}
