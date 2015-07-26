package mensa.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Session;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;

/**
 * Responsible for delivering the names of all known meals.
 * @author Petar Vutov
 */
@Path("/names/")
public class ApiNamesGetter {

	/**
	 * Provides tuple of name and id 
	 * @return Http response containing list of tuples representing all meals in db
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNames() {
		List<StringMealidPair> result = new ArrayList<StringMealidPair>();
		
		Session session = HibernateUtil.getSessionFactory().openSession();		
		Iterator<Meal> it = session.createCriteria(Meal.class).list().iterator();

		while (it.hasNext()) {
			Meal next = it.next();
			next.getMealid();
			result.add(new StringMealidPair(next.getName(), next.getMealid()));
		}
		
		return Response.ok().entity(result).build();
	}
	
	private static final class StringMealidPair {
		@JsonProperty("name")
		private String name;
		@JsonProperty("mealid")
		private int mealid;
		
		private StringMealidPair(String name, int mealid) {
			super();
			this.name = name;
			this.mealid = mealid;
		}
		
		public String getName() {
			return name;
		}
		public int getMealid() {
			return mealid;
		}
	}
}
