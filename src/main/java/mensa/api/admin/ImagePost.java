package mensa.api.admin;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Image;

import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Session;

@Path("admin/finalizeImagePost/")
public class ImagePost {
	/**
	 * Handle admin decision to publish or delete the img
	 * @param args json obj
	 * @return response object
	 * @throws IOException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response finalizeImagePost(Args args) throws IOException{
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Image image = (Image) session.get(Image.class, args.getImageId());

		if(args.getApproved()){
			image.setActive(true);
			session.update(image);
			session.getTransaction().commit();
		} else {
			String imgUrl = image.getUrl();
			String imgName = imgUrl.substring(imgUrl.lastIndexOf('/')+1, imgUrl.length());
			
			String s = "ftp://s_stamen:3Pg7JTj7@i43pc164.ipd.kit.edu/var/www/html/PSESoSe15Gruppe3-Daten/photos/" + imgName;
			URL u = new URL(s);
			URLConnection uc = u.openConnection();
			PrintStream ps = new PrintStream((uc.getOutputStream()));
			ps.close();

			session.delete(image);
		}
		return Response.ok().build();
	}
	
	private static class Args{
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
