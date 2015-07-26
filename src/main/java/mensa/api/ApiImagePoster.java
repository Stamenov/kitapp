package mensa.api;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mensa.api.OAuth.BadTokenException;
import mensa.api.OAuth.Checker;
import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.ImageProposal;
import mensa.api.hibernate.domain.User;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.Session;

/**
 * Responsible for handling merge proposals.
 * @author Petar Vutov
 */
@Path("/image/")
public class ApiImagePoster {
	private static final String DIR_TO_SAVE_IMAGES_TO = "/var/www/html/PSESoSe15Gruppe3-Daten/photos";
	private static final String DIR_TO_SAVE_TO_DB = "https://i43pc164.ipd.kit.edu/PSESoSe15Gruppe3-Daten/photos/";
	
	
	/**
	 * Saves image in the db.
	 * @param args The image, the mealid and a token identifying a user.
	 * @return Response indicating success or error.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postImage(Args args) {
		String userid;
		try { 
			userid = Checker.getUserid(args.getToken());	
		} catch (BadTokenException e) {
			return Response.status(400).entity("bad token").build();
		}

		if (!User.hasUsesLeft(userid)) {
			return Response.status(429).entity("merge/image limit exceeded").build();			
		}

		BufferedImage bufferedImage;
	    byte[] decodedByte = Base64.decodeBase64(args.getImage());
	    InputStream in = new ByteArrayInputStream(decodedByte);
		try {
		    bufferedImage = ImageIO.read(in);
		} catch (IOException e) {
			System.err.println("This shouldn't be possible: Failed to decode received image due to IOError.");
			e.printStackTrace();
			return Response.status(500).build();
		}
		
		// Generate random unused filename in specified dir; despite the method name, it is NOT a temp file.
		File file = null;
		try {
			file = File.createTempFile("imgPoster", ".bmp", new File(DIR_TO_SAVE_IMAGES_TO));
		} catch (IOException e1) {
			System.err.println("IOError. Is the image path in ApiImagePoster.java set correctly? "
					+ "Exiting without saving the image.");
			e1.printStackTrace();
			return Response.status(500).build();
		}
		
		String path = file.getAbsolutePath();

		
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			System.err.println("File.createTempFile failed to create a file? Exiting without saving the image.");
			e.printStackTrace();
			return Response.status(500).build();
		}
		
		// Save image:
		try {
			ImageIO.write(bufferedImage, "png", out);
		} catch (IOException e) {
			System.err.println("Couldn't write file to specified directory? Exiting without saving the image.");
			e.printStackTrace();
			return Response.status(500).build();
		}
		
		// Make sure there's a slash between dir and filename:
		String slash = "";
		if (!DIR_TO_SAVE_TO_DB.endsWith("/")) {
			slash = "/";
		}

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		ImageProposal imageProposal = new ImageProposal(userid, args.getMealId(), 
				DIR_TO_SAVE_TO_DB + slash + file.getName(), path, decodedByte);
		session.save(imageProposal);
		session.getTransaction().commit();
		session.close();
		
		User.reportSuccess(userid);
		
		return Response.ok("success").build();
	}
	
	private static class Args {
		@JsonProperty("token")
		private String token;
		@JsonProperty("mealid")
		private int mealid;
		@JsonProperty("image")
		private String image;
		
		public String getToken() {
			return token;
		}
		public int getMealId() {
			return mealid;
		}
		public String getImage() {
			return image;
		}
	}
}
