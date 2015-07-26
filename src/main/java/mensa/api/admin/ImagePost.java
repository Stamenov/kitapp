package mensa.api.admin;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Image;
import mensa.api.hibernate.domain.ImageProposal;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;

import java.nio.file.Files;
import java.nio.file.FileSystems;



import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Session;

/**
 * Responsible for the final, admin-involved process of publishing an image.
 * @author Martin Stamenov
 */
@Path("/admin/finalizeImagePost/")
public class ImagePost {
	/**
	 * Handle admin decision to publish or delete the image.
	 * @param args A class containing the decision and the id of the image the decision relates to.
	 * @return An http Response indicating failure or success.
	 * @throws IOException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response finalizeImagePost(Args args) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		
		ImageProposal imageProposal = (ImageProposal) session.get(ImageProposal.class, args.getImageId());
		
		if (args.getApproved()) {
	
			Image image = new Image(imageProposal.getUserid(), imageProposal.getUrl(), imageProposal.getHashCode());
			session.save(image);
			session.getTransaction().commit();
			
			session.beginTransaction();

			Meal meal = (Meal) session.get(Meal.class, imageProposal.getMealid());
			MealData data = meal.getData();
			data.addImage(image);
			
			session.update(data);
			session.delete(imageProposal);
			session.getTransaction().commit();

		} else {
			String imgUrl = imageProposal.getUrl();
			String imgName = imgUrl.substring(imgUrl.lastIndexOf('/') + 1, imgUrl.length());
			
			java.nio.file.Path path = FileSystems.getDefault().getPath(imageProposal.getPath());
			
			//delete if exists
	        try {
	            Files.deleteIfExists(path);
	        } catch (IOException | SecurityException e) {
	        	System.err.println("Image file is not there or no access permission.");
				e.printStackTrace();
				return Response.status(500).build();
	        }
			session.beginTransaction();
			session.delete(imageProposal);
			session.getTransaction().commit();
		}
		return Response.ok().build();
	}
	
	private static class Args {
		@JsonProperty("imageId")
		private int imageId;
		@JsonProperty("approved")
		private boolean approved;
		
		public int getImageId() {
			return imageId;
		}
		public boolean getApproved() {
			return approved;
		}
	}
}
