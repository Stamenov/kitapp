package mensa.api.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.ImageProposal;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;

import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 * Responsible for fetching all images awaiting admin review.
 * @author Martin Stamenov
 */
@Path("/admin/inactiveImages/")
public class InactiveImages {
	/**
	 * Fetches all inactive images.
	 * @return An http response containing a list of images and the names of the meals they belong to.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInactiveImages() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Criteria inactiveImage = session.createCriteria(ImageProposal.class);

		List<ImageProposal> imageList = inactiveImage.list();
		session.getTransaction().commit();
		session.close();
		
		HashSet<ImageProposal> set = new HashSet<ImageProposal>(imageList);
		
		Iterator<ImageProposal> it = set.iterator();
		
		ArrayList<ImageWithMealName> imgsWithNames = new ArrayList<ImageWithMealName>();
		Meal meal;
		ImageWithMealName currImgWithName;
		ImageProposal currImgProposal;
		while (it.hasNext()) {
			currImgProposal = it.next();
			
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			meal = (Meal) session.get(Meal.class, currImgProposal.getMealid());
			

			session.getTransaction().commit();
			
			currImgWithName = new ImageWithMealName(currImgProposal.getUrl(), currImgProposal.getId(), meal.getName());
			imgsWithNames.add(currImgWithName);
		}
		
		return Response.ok(imgsWithNames).build();
	}
	
	private class ImageWithMealName {
		private String url;
		private int imageid;
		private String mealName;
		
		public ImageWithMealName(String url, int imageid, String mealName) {
			this.url = url;
			this.imageid = imageid;
			this.mealName = mealName;
		}
		
		public String getUrl() {
			return url;
		}
		public int getImageid() {
			return imageid;
		}
		public String getMealName() {
			return mealName;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public void setImageid(int imageid) {
			this.imageid = imageid;
		}
		public void setMealName(String mealName) {
			this.mealName = mealName;
		}
		
	}
}
