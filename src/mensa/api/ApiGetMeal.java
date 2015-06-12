package mensa.api;

import mensa.oop.*;

import java.util.Date;
import java.util.HashSet;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.dto.Event;

import org.hibernate.Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/meal/")
public class ApiGetMeal {
	
	@GET
	@Path("/{mealID:[0-9]*}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Meal getMealById(@PathParam("mealID") String mealID){
		
		//testHibernate();
		createMockMenu();
		
		return new Meal("asd", Integer.parseInt(mealID));
	}
	
	public void testHibernate() {

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Event emp = new Event("yesy", new Date());
		session.save(emp);

		session.getTransaction().commit();
		HibernateUtil.shutdown();
	}
	
	public void createMockMenu() {
		HashSet<Tag> tags1 = new HashSet<Tag>();
		tags1.add(Tag.MIT_URANIUM);		
		mensa.oop.Meal meal1 = new mensa.oop.Meal();
		meal1.setName("Spaghetti Con Carne");
		MealData data1 = new MealData(meal1, tags1);		
		meal1.setData(data1);
		

		HashSet<Tag> tags2 = new HashSet<Tag>();
		tags2.add(Tag.VEGETARISCH);		
		tags2.add(Tag.EXTRA_GESUND);		
		mensa.oop.Meal meal2 = new mensa.oop.Meal();
		meal2.setName("Salat Gruen");
		MealData data2 = new MealData(meal2, tags2);		
		meal2.setData(data2);
		
		ImageList images1 = new ImageList();
		ImageList images2 = new ImageList();
		
		RatingList ratings1 = new RatingList();
		RatingList ratings2 = new RatingList();
		
		data1.setImages(images1);
		data1.setRatings(ratings1);
		data2.setImages(images2);
		data2.setRatings(ratings2);
		
		images1.putImage(new Image(1000, "testurl"));
		
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		// iny mysql console:
		// use hibernatedb
		// show tables
		// show columns FROM tablename
		session.save(new Image(1000, "testurl"));
		//session.save(meal2);
		
//		session.save(meal1);
//		session.save(meal2);		

		session.getTransaction().commit();
		HibernateUtil.shutdown();
	}
}
