package mensa.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.Rating;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Path("/rating/")
public class ApiRatingPutter {
	@GET // TODO: CHANGE TO PUT http://www.codereye.com/2010/12/configure-tomcat-to-accept-http-put.html
	@Path("/{mealid:[0-9]*}/{value:[0-5]}/{userid:[0-9]*}/")
	public String saveRating(@PathParam("mealid") String mealid, 
						   @PathParam("value") String value,
						   @PathParam("userid") String userid){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Criteria cr = session.createCriteria(Meal.class);
		cr.add(Restrictions.idEq(Integer.parseInt(mealid)));

		Meal meal = (Meal) cr.list().get(0);
		Rating rating = new Rating();
		rating.setUserid(Integer.parseInt(userid));
		rating.setValue(Integer.parseInt(value));
		meal.getData().getRatings().add(rating);
		session.merge(meal);
		

        session.getTransaction().commit();
        return "done";
		
	}
}
