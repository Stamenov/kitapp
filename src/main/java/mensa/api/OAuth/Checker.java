package mensa.api.OAuth;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

/**
 * @author http://android-developers.blogspot.de/2013/01/verifying-back-end-calls-from-android.html
 */
public class Checker {

    private static final String APP_ID =  "785844054287-moelp1283p15ofou728h6vjg7pvgn1s6.apps.googleusercontent.com";
    private static final String AUDIENCE = "785844054287-7hge652kf27md81acog9vg1u0nk9so83.apps.googleusercontent.com";
    private final GoogleIdTokenVerifier verifier;
    private final GsonFactory jsonFactory;
    private String mProblem = "Verification failed. (Time-out?)";

    /**
     * Create a new Checker.
     */
    public Checker() {
        NetHttpTransport transport = new NetHttpTransport();
        jsonFactory = new GsonFactory();
        verifier = new GoogleIdTokenVerifier(transport, jsonFactory);
    }

    /**
     * Check a token.
     * @param tokenString The token.
     * @return The payload of the token, if the token was valid and appropriately addressed.
     */
    public GoogleIdToken.Payload check(String tokenString) {
        GoogleIdToken.Payload payload = null;
        try {
            GoogleIdToken token = GoogleIdToken.parse(jsonFactory, tokenString);
            if (verifier.verify(token)) {
                GoogleIdToken.Payload tempPayload = token.getPayload();
                if (!tempPayload.getAudience().equals(AUDIENCE)) {
                    mProblem = "Audience mismatch";
                } else if (!APP_ID.equals(tempPayload.getAuthorizedParty())) {
                    mProblem = "Client ID mismatch";
                } else {
                    payload = tempPayload;
                }
            } else {
            	mProblem = "Token is not valid";
            }
        } catch (GeneralSecurityException e) {
            mProblem = "Security issue: " + e.getLocalizedMessage();
        } catch (IOException e) {
            mProblem = "Network problem: " + e.getLocalizedMessage();
        } catch (NullPointerException | IllegalArgumentException e) {
        	mProblem = "Token not parsable: " + e.getLocalizedMessage();
        }
        return payload;
    }

    /**
     * Get an error message, if an error occured.
     * @return The message.
     */
    public String getProblem() {
        return mProblem;
    }
    
    /**
     * A convenience method for extracting a userid from a token and printing any errors.
     * @param token The token.
     * @return The userid that was contained in the token.
     * @throws BadTokenException If the token was invalid, improperly addressed, 
     * or another error related to checking it occured.
     */
    public static String getUserid(String token) throws BadTokenException {
    	Checker checker = new Checker();
    	Payload payload = checker.check(token);
    	String userid;
    	
    	if (payload == null) {
    		System.err.println(checker.getProblem());
			throw new BadTokenException();
    	} else {
    		userid = payload.getSubject();    		
    	}
    	
    	return userid;
    }
}