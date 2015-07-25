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

    private final String APP_ID =  "785844054287-moelp1283p15ofou728h6vjg7pvgn1s6.apps.googleusercontent.com";
    private final String AUDIENCE = "785844054287-7hge652kf27md81acog9vg1u0nk9so83.apps.googleusercontent.com";
    private final GoogleIdTokenVerifier VERIFIER;
    private final GsonFactory JSON_FACTORY;
    private String mProblem = "Verification failed. (Time-out?)";

    public Checker() {
        NetHttpTransport transport = new NetHttpTransport();
        JSON_FACTORY = new GsonFactory();
        VERIFIER = new GoogleIdTokenVerifier(transport, JSON_FACTORY);
    }

    public GoogleIdToken.Payload check(String tokenString) {
        GoogleIdToken.Payload payload = null;
        try {
            GoogleIdToken token = GoogleIdToken.parse(JSON_FACTORY, tokenString);
            if (VERIFIER.verify(token)) {
                GoogleIdToken.Payload tempPayload = token.getPayload();
                if (!tempPayload.getAudience().equals(AUDIENCE))
                    mProblem = "Audience mismatch";
                else if (!APP_ID.equals(tempPayload.getAuthorizedParty()))
                    mProblem = "Client ID mismatch";
                else
                    payload = tempPayload;
            } else {
            	mProblem = "Token is not valid";
            }
        } catch (GeneralSecurityException e) {
            mProblem = "Security issue: " + e.getLocalizedMessage();
        } catch (IOException e) {
            mProblem = "Network problem: " + e.getLocalizedMessage();
        }
        return payload;
    }

    public String getProblem() {
        return mProblem;
    }
    
    public static String getUserid(String token) throws BadTokenException {
    	Checker checker = new Checker();
    	Payload payload = checker.check(token);
    	String userid;
    	
    	if (payload == null) {
    		System.out.println(checker.getProblem());
			throw new BadTokenException();
    	} else {
    		userid = payload.getSubject();    		
    	}
    	
    	return userid;
    }
}