package mensa.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;

@Path("/names/")
public class ApiNamesGetter {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getNames(){
		List<String> result = new ArrayList<String>();

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Iterator<Meal> it = session.createCriteria(Meal.class).list().iterator();
        session.getTransaction().commit();

		System.out.println(it.hasNext());
		while(it.hasNext()) {
			result.add(it.next().getName());
		}
		
		return result;
	}
}
