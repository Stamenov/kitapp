package mensa.api;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Line;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;
import mensa.api.hibernate.domain.Offer;
import mensa.api.hibernate.domain.Price;
import mensa.api.hibernate.domain.Tags;

import org.hibernate.Session;

@Path("/populate/")
@WebListener
public class Populate extends TimerTask implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// do nuffin
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(this, 0, 168, TimeUnit.HOURS);
		
		//Timer time = new Timer(); // Instantiate Timer Object
	    //long dayInMilliseconds = 1000 * 60 * 60 * 24;
		//time.schedule(this, 0, dayInMilliseconds);
	}
	
	public void run(){
		System.out.println("populate triggered!");
		makeMockData();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Offer[][] makeMockData(){
		
		// Mon-Fri for two weeks
		int[] timestamps = new int[10];
		timestamps[0] = 1435528800;
		timestamps[1] = 1435615200;
		timestamps[2] = 1435701600;
		timestamps[3] = 1435788000;
		timestamps[4] = 1435874400;
		timestamps[5] = 1436133600;
		timestamps[6] = 1436220000;
		timestamps[7] = 1436306400;
		timestamps[8] = 1436392800;
		timestamps[9] = 1436479200;
		
		// First index is line
		String[][] names = new String[4][10];
		Tags[][] tags = new Tags[4][10];
		Price[][] prices = new Price[4][10];
		Offer[][] offers = new Offer[4][10];
		
		// ADD DATA -----------------------------------------------------------
		names[0][0] = "Käseknacker mit Ketchup und Nudelsalat";
		tags[0][0] = new Tags(false, false, true, false, false, false, false, "");
		prices[0][0] = new Price(2.6, 3.85, 3.15, 2.95);		

		names[0][1] = "Blumenkohl in Käse Sahnesoße mit Salzkartoffeln";
		tags[0][1] = new Tags(false,false,false,false,false,false,true, "");
		prices[0][1] = new Price(2.6, 3.85, 3.15, 2.95);
		
		names[0][2] = "Dessert: Sahnefruchtjoghurt";
		tags[0][2] = new Tags(false, false, false, false, false, false, true,"");
		prices[0][2] = new Price(0,0,0,0);
		
		names[0][3] = "oder auf Wunsch Obst";
		tags[0][3] = new Tags(false,false,false,false,false,true,false, "");
		prices[0][3] = new Price(0,0,0,0);
		

		names[1][0] = "Schweinerückensteak mit Café de Paris-Soße";
		tags[1][0] = new Tags(false,false,true,false,false,false,false, "");
		prices[1][0] = new Price(2.2, 4.85, 3.25, 2.55);
		
		names[1][1] = "Pommes";
		tags[1][1] = new Tags(false, false,false,false,false,true,false, "");
		prices[1][1] = new Price(0.95, 1.2, 0.95, 0.95);
		
		names[1][2] = "Kaisergemüse";
		tags[1][2] = new Tags(false,false,false,false,false,true,false, "");
		prices[1][2] = new Price(0.8,1,0.8,0.8);
		
		names[1][3] = "Blattsalat Karottensalat Top-Fit-Salat";
		tags[1][3] = new Tags(false,false,false,false,false,true,false, "");
		prices[1][3] = new Price(0.8,1,0.8,0.8);
		
		names[1][4] = "Stracciatella-Creme";
		tags[1][4] = new Tags(false,false,false,false,false,false,true, "");
		prices[1][4] = new Price(0.75, 0.95, 0.75, 0.75);
		
		names[1][5] = "Apfeltasche";
		tags[1][5] = new Tags(false,false,false,false,false,true,false, "");
		prices[1][5] = new Price(0.75,0.75,0.75,0.75);
		
		names[1][6] = "Teigwaren";
		tags[1][6] = new Tags(false,false,false,false,false,true,false, "");
		prices[1][6] = new Price(0.75, 0.95, 0.75, 0.75);
		
		
		names[2][0] = "Käseknacker mit Ketchup und Nudelsalat";
		tags[2][0] = new Tags(false, false, true, false, false, false, false, "");
		prices[2][0] = new Price(2.6, 3.85, 3.15, 2.95);
		
		names[2][1] = "Gemüseschnitzel mit Curryreis mit Kräutersoße";
		tags[2][1] = new Tags(false,false,false,false,false,false,true, "");
		prices[2][1] = new Price(2.55, 5.6, 3.75, 2.95);
		
		names[2][2] = "Blattsalat Gärtnerinsalat Kretasalat";
		tags[2][2] = new Tags(false,false,false,false,false,true,false, "");
		prices[2][2] = new Price(0.8, 1, 0.8, 0.8);
		
		names[2][3] = "Himbeerquark";
		tags[2][3] = new Tags(false,false,false,false,false,false,true, "");
		prices[2][3] = new Price(0.75, 0.95, 0.75, 0.75);
		
		names[2][4] = "Apfeltasche";
		tags[2][4] = new Tags(false,false,false,false,false,true,false, "");
		prices[2][4] = new Price(0.75,0.75,0.75,0.75);
		
		
		names[3][0] = "Käseknacker mit Ketchup und Nudelsalat";
		tags[3][0] = new Tags(false, false, true, false, false, false, false, "");
		prices[3][0] = new Price(2.6, 3.85, 3.15, 2.95);
		
		names[3][1] = "Spinatspätzle mit frischen Champignons";
		tags[3][1] = new Tags(false,false,false,false,false,false,true, "");
		prices[3][1] = new Price(2.25, 4.95, 3.3, 2.6);
		
		names[3][2] = "Hähnchen Cordon bleu mit Putenschinken und Bratensoße";
		tags[3][2] = new Tags(false,false,false,false,false,false,false, "");
		prices[3][2] = new Price(2.2,4.85, 3.25, 2.55);
		
		names[3][3] = "Pommes";
		tags[3][3] = new Tags(false, false,false,false,false,true,false, "");
		prices[3][3] = new Price(0.95, 1.2, 0.95, 0.95);
		
		names[3][4] = "Leipziger Allerlei-Gemüse";
		tags[3][4] = new Tags(false,false,false,false,false,true,false, "");
		prices[3][4] = new Price(0.8, 1.0, 0.8, 0.8);
		
		names[3][5] = "Blattsalat Gärtnerinsalat Kretasalat";
		tags[3][5] = new Tags(false, false,false,false,false,true,false, "");
		prices[3][5] = new Price(0.8, 1.0, 0.8, 0.8);
		
		names[3][6] = "Himbeerquark";
		tags[3][6] = new Tags(false,false,false,false,false,false,true, "");
		prices[3][6] = new Price(0.75, 0.95, 0.75, 0.75);
		
		names[3][7] = "Dampfkartoffeln";
		tags[3][7] = new Tags(false,false,false,false,false,true,false, "");
		prices[3][7] = new Price(0.75, 0.95, 0.75, 0.75);
		
		names[3][8] = "Apfeltasche";
		tags[3][8] = new Tags(false,false,false,false,false,true,false, "");
		prices[3][8] = new Price(0.75, 0.75, 0.75, 0.75);
		
		names[3][8] = "Eierflockensuppe";
		tags[3][8] = new Tags(false,false,false,false,false,false,true, "");
		prices[3][8] = new Price(0.45, 0.45, 0.45, 0.45);
		
		// END DATA -----------------------------------------------------------

		// CREATE OBJECTS AND SAVE IN DB
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; ; j++) {	
				if (names[i][j] == null)
					break;
				
				Offer offer = new Offer();
				offer.setLine(Line.values()[i]);
				offer.setPrice(prices[i][j]);
				offer.setTimestamp(timestamps[0]);
				
				Meal meal = new Meal();
				meal.setName(names[i][j]);
				
				MealData data = new MealData(meal, tags[i][j]);				
				data.setActive(true);
				
				meal.setData(data);
				offer.setMeal(meal);
					
				offers[i][j] = offer;		
				session.save(offers[i][j]);
			}
		}
		

		session.getTransaction().commit();
		//HibernateUtil.shutdown();
		
		
		/////////////////////////////////////////////////////////////
		return offers;
	}
}
