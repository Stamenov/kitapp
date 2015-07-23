package mensa.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.Path;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.Offer;
import mensa.api.studentenwerk.Data;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.gson.Gson;

@Path("/populate/")
@WebListener
public class Populate extends TimerTask implements ServletContextListener{
	
	final static String API_URL = "https://www.studentenwerk-karlsruhe.de/json_interface/canteen/";
	final static String CHARSET = java.nio.charset.StandardCharsets.UTF_8.name();
	final static String USER = "jsonapi";
	final static String PASSWORD = "AhVai6OoCh3Quoo6ji";
	final static int POLL_FREQUENCY_IN_HOURS = 24;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// do nuffin
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// Poll mensa API daily; not necessary to do it so often but its cheap and could catch menu changes so why not?		
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(this, 0, POLL_FREQUENCY_IN_HOURS, TimeUnit.HOURS);
	}
	
	public void run(){
		System.out.println("populate triggered!");
		
		// Fetch data from SW API:
		String json = httpGet(USER, PASSWORD, API_URL, true);

		if (json == null) {
			System.out.println("Data could not be fetched from Studentenwerk."
					+ "The updating of the database has been cancelled and no changes have been made.");
			return;
		}
		
		// Parse data:
		Gson gson = new Gson();
		Data d;
		
		/**
		 *  There is a risk that the official SW API may change in format.
		 *  Instead of trying to predict all the different ways someone may change the API,
		 *  we just give up and ask for programmer attention if an oddity happens.
		 */
		try {
			d = gson.fromJson(json, Data.class);
		} catch (Exception e) {
			System.out.println("The parser for dynamic data from Studentenwerk "
					+ "has failed. The mensa API may have changed."
					+ "The updating of the database has been cancelled and no changes have been made.");
			e.printStackTrace();
			return;
		}
		
		List<Offer> offers = d.getOffers();
		if (offers == null){
			System.out.println("The data retrieved is not parsable."
					+ "The updating of the database has been cancelled and no changes have been made.");
			return;
		}

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		for (Offer offer: (List<Offer>) session.createCriteria(Offer.class).list()){
			offer.setMeal(null);
			session.delete(offer);
		}
		
		for (Offer offer: offers) {
			session.beginTransaction();
			List<Meal> mealsInDB = session.createCriteria(Meal.class)
			.add( Restrictions.like("name", offer.getMeal().getName())).list();
			session.getTransaction().commit();
			
			if (!mealsInDB.isEmpty()) {
				offer.setMeal(mealsInDB.get(0));
			}

			session.beginTransaction();
			
			session.save(offer);
			
			session.getTransaction().commit();
		}
		
		System.out.println("done");
	}
	
	public static String httpGet(String user, String pwd, String urlWithParameters, boolean returnResponse) {
	    URL url = null;
	    try {
	        url = new URL(urlWithParameters);
	    } catch (MalformedURLException e) {
	        System.out.println("MalformedUrlException: " + e.getMessage());
	        e.printStackTrace();
	        return null;
	    }

	    URLConnection uc = null;
	    try {
	        uc = url.openConnection();
	    } catch (IOException e) {
	        System.out.println("IOException: " + e.getMessage());
	        e.printStackTrace();
	        return null;
	    }


	    String userpass = user + ":" + pwd;
	    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());

	    uc.setRequestProperty("Authorization", basicAuth);
	    InputStream is = null;
	    try {
	        is = uc.getInputStream();
	    } catch (IOException e) {
	        System.out.println("IOException: " + e.getMessage());
	        e.printStackTrace();
	        return null;
	    }
	    if (returnResponse) {
	        BufferedReader buffReader = new BufferedReader(new InputStreamReader(is));
	        StringBuffer response = new StringBuffer();

	        String line = null;
	        try {
	            line = buffReader.readLine();
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "-1";
	        }
	        while (line != null) {
	            response.append(line);
	            response.append('\n');
	            try {
	                line = buffReader.readLine();
	            } catch (IOException e) {
	                System.out.println(" IOException: " + e.getMessage());
	                e.printStackTrace();
	                return null;
	            }
	        }
	        try {
	            buffReader.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	        return response.toString();
	    }
	    return "0";
	}
}
