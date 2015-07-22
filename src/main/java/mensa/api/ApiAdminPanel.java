package mensa.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.FileSystems;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;
import mensa.api.hibernate.domain.Image;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Path("/admin/")
public class ApiAdminPanel {
	
	@GET
	@Path("/mergingMeals/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInactiveMealDatas(){
		
		ArrayList<ArrayList<Meal>> result = new ArrayList<ArrayList<Meal>>();

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Criteria inactiveMealData = session.createCriteria(MealData.class);
		inactiveMealData.add(Restrictions.eq("active", Boolean.FALSE));

		List resultList = inactiveMealData.list();
		Iterator<MealData> it = resultList.iterator();
        
        ArrayList<Meal> currMeals;
		while(it.hasNext()) {
			currMeals = new ArrayList<Meal>();
			MealData next = it.next();
			currMeals.addAll(next.getMeals());
			for(Meal currMeal: currMeals){
				currMeal.setData(next);
			}
			result.add(currMeals);
		}
		return Response.ok(result).build();
	}
		
	@POST
	@Path("/finalizeMerge/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response finalizeMerge(MergingResponse mealDataResponse){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		MealData mealData = (MealData) session.get(MealData.class, mealDataResponse.getMealDataId());

		if(mealDataResponse.getApproved()){
			mealData.setActive(true);
			session.update(mealData);
			session.getTransaction().commit();
			
			ArrayList<MealData> oldMealDatas = new ArrayList<MealData>();

			session.beginTransaction();
			for(Meal meal: mealData.getMeals()){
				if(!oldMealDatas.contains(meal.getData())) {
					oldMealDatas.add(meal.getData());
				}
				meal.setData(mealData);
				session.update(meal);
			}
			session.getTransaction().commit();			

			session.beginTransaction();

			for(MealData data: oldMealDatas){
				Set<Meal> emptySet = new HashSet<Meal>();
				data.setMeals(emptySet);
				session.update(data);
				session.delete(data);	
			}
			session.getTransaction().commit();
			
				
		} else {
			Set<Meal> emptySet = new HashSet<Meal>();
			mealData.setMeals(emptySet);
			session.update(mealData);
			session.getTransaction().commit();
		
			session.beginTransaction();
		    session.delete(mealData);
			session.getTransaction().commit();
		}
		
		return Response.ok("success").build();

	}
	
	private static class MergingResponse{
		@JsonProperty("mealDataId")
		private int mealDataId;
		@JsonProperty("approved")
		private boolean approved;
		
		public int getMealDataId(){
			return mealDataId;
		}
		public boolean getApproved(){
			return approved;
		}
	}
	
	@GET
	@Path("/inactiveImages/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInactiveImages(){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Criteria inactiveImage = session.createCriteria(Image.class);
		inactiveImage.add(Restrictions.eq("active", Boolean.FALSE));

		List imageList = inactiveImage.list();
		Iterator<Image> it = imageList.iterator();
		ArrayList<Image> imageArr = new ArrayList<Image>();
		while(it.hasNext()) {
			imageArr.add(it.next());
		}
		return Response.ok(imageArr).build();
	}
	@POST
	@Path("/finalizeImagePost/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response finalizeImagePost(ImageResponse imageResponse) throws IOException{
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Image image = (Image) session.get(Image.class, imageResponse.getImageId());

		if(imageResponse.getApproved()){
			image.setActive(true);
			session.update(image);
			session.getTransaction().commit();
		} else {
			String imgUrl = image.getUrl();
			String imgName = imgUrl.substring(imgUrl.lastIndexOf('/')+1, imgUrl.length());
			
			//no import because of collision of imports 'path'
			java.nio.file.Path path = FileSystems.getDefault().getPath("/var/www/html/PSESoSe15Gruppe3-Daten/photos", imgName);
	        //delete if exists
	        try {
	            boolean success = Files.deleteIfExists(path);
	        } catch (IOException | SecurityException e) {
	            throw e;
	        }
			session.delete(image);
		}
		return Response.ok().build();
	}
	
	private static class ImageResponse{
		@JsonProperty("imageId")
		private int imageId;
		@JsonProperty("approved")
		private boolean approved;
		
		public int getImageId(){
			return imageId;
		}
		public boolean getApproved(){
			return approved;
		}
	}


}
