
import static org.junit.Assert.assertNull;

import java.math.BigInteger;

import mensa.api.OAuth.BadTokenException;
import mensa.api.OAuth.Checker;

import org.junit.Test;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;


public class CheckerTest {
	@Test
	public void testCheckerBasic() {
		Checker checker = new Checker();
		Payload payload = checker.check("5524346tafdghsdfh");
		assertNull(payload);
	}
	
	@Test(expected=BadTokenException.class)
	public void testChecker() throws BadTokenException {
		Checker checker = new Checker();
		checker.getUserid("");
	}
	
	@Test(expected=BadTokenException.class)
	public void testChecker2() throws BadTokenException {
		Checker checker = new Checker();
		checker.getUserid("gshfdhdfjhhjghm");
	}
	
	@Test(expected=BadTokenException.class)
	public void testChecker3() throws BadTokenException {
		Checker checker = new Checker();
		// legit expired token
		checker.getUserid("eyJhbGciOiJSUzI1NiIsImtpZCI6IjVhNmNkMGJjMmY1NTY1ZmFmMGU3MGU5MmQwNTcyYTJkZjJmNmM0OWMifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwic3ViIjoiMTAwMDM5MjQ0NzEwNjI1NDI2MDAyIiwiYXpwIjoiNzg1ODQ0MDU0Mjg3LW1vZWxwMTI4M3AxNW9mb3U3MjhoNnZqZzdwdmduMXM2LmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiZW1haWwiOiJlbWluLnNlbnR1ZXJrQGdvb2dsZW1haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF1ZCI6Ijc4NTg0NDA1NDI4Ny03aGdlNjUya2YyN21kODFhY29nOXZnMXUwbms5c284My5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsImlhdCI6MTQzNzk0MjcyNywiZXhwIjoxNDM3OTQ2MzI3LCJuYW1lIjoiRWtyZW0gU2VudMO8cmsiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDQuZ29vZ2xldXNlcmNvbnRlbnQuY29tLy1NRGZWajUwZjVmZy9BQUFBQUFBQUFBSS9BQUFBQUFBQUFDTS9VQ2pkeW5IMUxVQS9zOTYtYy9waG90by5qcGciLCJnaXZlbl9uYW1lIjoiRWtyZW0iLCJmYW1pbHlfbmFtZSI6IlNlbnTDvHJrIn0.M8zynGERmeoCb3x92E5nw-HDDKCkcF3LF9YUakk4g_IQclGZLypCaBaDbpn2ZoJe7VZtC8-FWB52JHYJ6Qd0TQ6MbGUcb1h9oOEIqc574TiP8c2pF2WeDD2qSVhHbyjam2WEVBwN97xKQxA8nKVp_INMvGDjYTSDbdmqDQ0EdU8jqsTPwqIrvVpokixHICVaU3Nir5flGRc5HYfJZsMA-uZId-uDjP4U5XdgTbWj2mJniBvz3-wQYfDcGX5EFYXwCYxvPoFyYLXgfPqWaQeZntUNK5P7LXu8QrRnjl8p4AevopicwwCkpcWJBs8vljaaap65M1y0XEUpUIk8XXBWOQ");
	}

}
