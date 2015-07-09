package mensa.api;

import mensa.api.hibernate.domain.*;

import java.util.HashSet;
import java.util.List;

import mensa.api.hibernate.HibernateUtil;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/meal/")
public class ApiMealGetter {
	
	@GET
	@Path("/{mealID:[0-9]*}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Meal getMealById(@PathParam("mealID") String mealId){
	
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Meal meal = (Meal) session.get(Meal.class, Integer.parseInt(mealId));

		return meal;
	}	
}
