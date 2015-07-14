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

public class Day {
	private List<MealSW> l1;
	private List<MealSW> l2;
	private List<MealSW> l3;
	private List<MealSW> l45;
	private List<MealSW> schnitzelbar;
	private List<MealSW> update;	

	public List<Offer> getOffers(int timestamp) {
		List<Offer> result = new ArrayList<Offer>();
		
		List<MealSW> allMeals = new ArrayList<MealSW>();
		allMeals.addAll(l1);
		allMeals.addAll(l2);
		allMeals.addAll(l3);
		allMeals.addAll(l45);
		allMeals.addAll(schnitzelbar);
		allMeals.addAll(update);
		
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
		
		return result;
	}
	
	private void iterateAndExtractOffers(Iterator<MealSW> it, List<Offer> result, Line line, int timestamp) {
		while (it.hasNext()) {
			MealSW next = it.next();
			
			if (next.isNodata()) {
				return;
			}
			
			if (next.getClosing_end() != null || next.getClosing_start() != null) {
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
			price.setWorkerPrice (next.getPrice_3());
			price.setChildPrice  (next.getPrice_4());

			Offer offer = new Offer();
			offer.setMeal(meal);
			offer.setTimestamp(timestamp);
			offer.setLine(line);
			offer.setPrice(price);
			
			result.add(offer);
		} 
	}
}
