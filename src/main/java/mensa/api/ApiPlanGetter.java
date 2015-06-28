package mensa.api;

import java.util.HashSet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Image;
import mensa.api.hibernate.domain.Line;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;
import mensa.api.hibernate.domain.Offer;
import mensa.api.hibernate.domain.Price;
import mensa.api.hibernate.domain.Rating;
import mensa.api.hibernate.domain.Tags;

@Path("/plan/")
public class ApiPlanGetter {
	@GET
	@Path("/{timestamp1:[0-9]*}/") //TODO Make it take two timestamps
	@Produces(MediaType.APPLICATION_JSON)
	public Offer[] getPlanByTimestamps(@PathParam("timestamp1") int timestamp1) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Offer[] result = new Offer[3];
		result[0] = new Offer();
		result[0].setLine(Line.abend);
		result[0].setPrice(new Price());
		result[0].setTimestamp(timestamp1);
		

		Tags tags1 = new Tags();	
		Meal meal1 = new Meal();
		meal1.setName("Spaghetti Con Carne");
		MealData data1 = new MealData(meal1, tags1);		
		meal1.setData(data1);
		

		Tags tags2 = new Tags();	
		Meal meal2 = new Meal();
		meal2.setName("Salat Gruen");
		MealData data2 = new MealData(meal2, tags2);		
		meal2.setData(data2);
		
		data1.setImages(new HashSet<Image>());
		data1.setRatings(new HashSet<Rating>());
		data2.setImages(new HashSet<Image>());
		data2.setRatings(new HashSet<Rating>());
		
		data1.getImages().add(new Image(1000, "testurl"));
		
		
		result[0].setMeal(meal1);
		result[1] = new Offer();
		result[1].setMeal(meal2);

		session.save(result[0]);

		session.getTransaction().commit();
		HibernateUtil.shutdown();
		
		return result;
	}
}
