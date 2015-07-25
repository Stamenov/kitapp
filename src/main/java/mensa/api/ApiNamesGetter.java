package mensa.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;

@Path("/names/")
public class ApiNamesGetter {

	/**
	 * Provides tuple of name and id 
	 * @return list of tuples representing all meals in db
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNames(){
		List<StringIdPair> result = new ArrayList<StringIdPair>();
		
		Session session = HibernateUtil.getSessionFactory().openSession();		
		Iterator<Meal> it = session.createCriteria(Meal.class).list().iterator();

		while(it.hasNext()) {
			Meal next = it.next();
			next.getId();
			result.add(new StringIdPair(next.getName(), next.getId()));
		}
		
		return Response.ok().entity(result).build();
	}
	
	private class StringIdPair {
		public String name;
		public int id;
		public StringIdPair(String name, int id) {
			super();
			this.name = name;
			this.id = id;
		}
	}
}
