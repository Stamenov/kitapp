package mensa.api;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/json/")
public class Hello {
 
		@GET
		@Path("/track")
		@Produces(MediaType.APPLICATION_JSON)
		public Track getTrackInJSON() {
	 
			Track track = new Track();
			track.setTitle("Enter Sandman");
			track.setSinger("Metallica");
	 
			return track;
	 
		}
	 
		@GET
		@Path("/meal/{mealID}/")
		@Produces(MediaType.TEXT_PLAIN)
		public String getMealById(@PathParam("mealID") String mealID){
			return mealID;
		}
}
