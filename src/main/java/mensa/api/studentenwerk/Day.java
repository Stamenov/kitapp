package mensa.api.studentenwerk;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mensa.api.hibernate.domain.Line;
import mensa.api.hibernate.domain.Meal;
import mensa.api.hibernate.domain.MealData;
import mensa.api.hibernate.domain.Offer;
import mensa.api.hibernate.domain.Price;
import mensa.api.hibernate.domain.Tags;

/**
 * Represents all meals under the same timestamp in Studentenwerk's API.
 * Used by com.google.gson.Gson to provide expected structure when parsing json.
 * @author Petar Vutov
 */
public class Day {
	private List<MealSW> l1;
	private List<MealSW> l2;
	private List<MealSW> l3;
	private List<MealSW> l45;
	private List<MealSW> schnitzelbar;
	private List<MealSW> update;
	private List<MealSW> abend;
	private List<MealSW> aktion;
	private List<MealSW> heisstheke;
	private List<MealSW> nmtisch;

	/**
	 * Convert the data into Offer classes that can be directly stored in the db.
	 * @param timestamp The timestamp to use for these offers.
	 * @return A list of Offers on this particular day.
	 */
	public List<Offer> getOffers(int timestamp) {
		List<Offer> result = new ArrayList<Offer>();
		
		Iterator<MealSW> it = l1.iterator();
		iterateAndExtractOffers(it, result, Line.l1, timestamp);
		
		it = l2.iterator();
		iterateAndExtractOffers(it, result, Line.l2, timestamp);
		
		it = l3.iterator();
		iterateAndExtractOffers(it, result, Line.l3, timestamp);
		
		it = l45.iterator();
		iterateAndExtractOffers(it, result, Line.l45, timestamp);
		
		it = schnitzelbar.iterator();
		iterateAndExtractOffers(it, result, Line.schnitzelbar, timestamp);
		
		it = update.iterator();
		iterateAndExtractOffers(it, result, Line.update, timestamp);		

		it = abend.iterator();
		iterateAndExtractOffers(it, result, Line.abend, timestamp);
		
		it = aktion.iterator();
		iterateAndExtractOffers(it, result, Line.aktion, timestamp);
		
		it = heisstheke.iterator();
		iterateAndExtractOffers(it, result, Line.heisstheke, timestamp);
		
		it = nmtisch.iterator();
		iterateAndExtractOffers(it, result, Line.nmtisch, timestamp);
		
		return result;
	}
	
	/**
	 * For a given line, iterate over all meals offered there and repack them into Offers.
	 * @param it Iterator over the line.
	 * @param result A list to append the created offers to.
	 * @param line The name of the line we are iterating, so it can be added to the Offer objects.
	 * @param timestamp The timestamp to be added to the Offer objects.
	 */
	private void iterateAndExtractOffers(Iterator<MealSW> it, List<Offer> result, Line line, int timestamp) {
		
		while (it.hasNext()) {
			MealSW next = it.next();
			
			/*
			 *  If there is no data or the data just tells us that the line is closed, 
			 *  end parsing because there is nothing to parse:
			 */
			if (next.isNodata() || next.getClosing_end() != null || next.getClosing_start() != null) {
				return;
			}
		
			Meal meal = new Meal();
			meal.setName(next.getMeal() + " " + next.getDish());
			
			Tags tags = new Tags();
			tags.setBio(next.isBio());
			tags.setFish(next.isFish());
			tags.setPork(next.isPork());
			tags.setCow(next.isCow());
			tags.setCow_aw(next.isCow_aw());
			tags.setVegan(next.isVegan());
			tags.setVeg(next.isVeg());
			tags.setAdd(next.getAdd().toString());

			MealData data = new MealData(meal, tags, true);	
			meal.setData(data);
			
			Price price = new Price();
			price.setStudentPrice(next.getPrice_1());
			price.setVisitorPrice(next.getPrice_2());
			price.setWorkerPrice(next.getPrice_3());
			price.setChildPrice(next.getPrice_4());

			Offer offer = new Offer();
			offer.setMeal(meal);
			offer.setTimestamp(timestamp);
			offer.setLine(line);
			offer.setPrice(price);
			
			result.add(offer);
		} 
	}
}
