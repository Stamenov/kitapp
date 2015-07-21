package mensa.api;

import mensa.api.hibernate.domain.*;

import mensa.api.hibernate.HibernateUtil;

import org.hibernate.Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/meal/")
public class ApiMealGetter {
	
	@GET
	@Path("/{mealID:[0-9]*}/{userID:[0-9]*}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Meal getMealById(@PathParam("mealID") String mealid, @PathParam("userID") String userid){
	
		Session session = HibernateUtil.getSessionFactory().openSession();
		Meal meal = (Meal) session.get(Meal.class, Integer.parseInt(mealid));
		
		meal.setCurrentUser( Integer.parseInt(userid) );

		return meal;
	}	
}
