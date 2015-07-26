package mensa.api.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Responsible for fetching all merge proposals.
 * @author Martin Stamenov
 */
@Path("/admin/mergingMeals/")
public class InactiveMealData {
	/**
	 * Deliver pending meal merges to the admin pannel as json objects.
	 * @return arrayList of arrayLists grouped by the current meal, all within a response object.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInactiveMealDatas() {
		
		ArrayList<ArrayList<Meal>> result = new ArrayList<ArrayList<Meal>>();

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		Criteria inactiveMealData = session.createCriteria(MealData.class);
		inactiveMealData.add(Restrictions.eq("active", Boolean.FALSE));

		List<MealData> resultList = inactiveMealData.list();
		Iterator<MealData> it = resultList.iterator();
        
        ArrayList<Meal> currMeals;
		while (it.hasNext()) {
			currMeals = new ArrayList<Meal>();
			MealData next = it.next();
			currMeals.addAll(next.getMeals());
			for (Meal currMeal: currMeals) {
				currMeal.setData(next);
			}
			result.add(currMeals);
		}
		return Response.ok(result).build();
	}
}
