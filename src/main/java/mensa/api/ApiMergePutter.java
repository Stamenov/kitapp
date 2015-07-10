package mensa.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Session;

@Path("/merge/")
public class ApiMergePutter {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public MealData mergeByIds(MergingMeals meals){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Meal meal1 = (Meal) session.get(Meal.class, meals.getMeal1Id());
		Meal meal2 = (Meal) session.get(Meal.class, meals.getMeal2Id());

		System.out.println(meal1.getId() + "   ||||||||||||||||||  " + meal2.getId());
		
		MealData mergedMealData = MealData.merge(meal1.getData(), meal2.getData());
		session.save(mergedMealData);
		session.getTransaction().commit();
		
		return mergedMealData;
	}
	
	private static class MergingMeals{
		@JsonProperty("meal1Id")
		private int meal1Id;
		@JsonProperty("meal2Id")
		private int meal2Id;
		
		public int getMeal1Id(){
			return meal1Id;
		}
		
		public int getMeal2Id(){
			return meal2Id;
		}
	}
}
