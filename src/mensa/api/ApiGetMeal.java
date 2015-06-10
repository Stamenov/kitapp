package mensa.api;

import java.util.Date;

import mensa.hibernate.HibernateUtil;
import mensa.hibernate.dto.Event;

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
		
		testHibernate();
		
		System.out.println("yo");
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
}
