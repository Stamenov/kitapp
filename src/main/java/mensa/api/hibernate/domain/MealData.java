package mensa.api.hibernate.domain;

import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MealData {	
	private int id;
	private RatingList ratings;
	private ImageList images;
	private HashSet<Tag> tags;
	private HashSet<Meal> meals;
	
	public MealData(Meal meal, HashSet<Tag> tags){
		meals = new HashSet<Meal>();
		meals.add(meal);
		ratings = new RatingList();
		images = new ImageList();
		this.tags = tags;
	}
	
	/**
	 * For meging; Given two MealData objects, creates a third object that has their merged fields.
	 * @param first
	 * @param second
	 */
	private MealData(MealData first, MealData second) {
		meals = new HashSet<Meal>();
		meals.addAll(first.meals);
		meals.addAll(second.meals);
		
		tags = new HashSet<Tag>();
		if (!first.tags.containsAll(second.tags) || !second.tags.containsAll(first.tags)) {
			throw new IllegalArgumentException("Tags of both meals should be equal!");
		} else {
			tags.addAll(first.tags);
		}
		
		images = ImageList.merge(first.images, second.images);
		ratings = RatingList.merge(first.ratings, second.ratings);
		
		
	}	

	public static void merge(MealData first, MealData second) {
		MealData data = new MealData(first, second);
		// make data contain merged ratings etc
		
		for (Meal meal : data.meals) {
			meal.setData(data);
		}
	}
	

	@Id @GeneratedValue
	private int getId() {
		return id;
	};	
	
	public RatingList getRatings() {
		return ratings;
	}

	public void setRatings(RatingList ratings) {
		this.ratings = ratings;
	}

	public ImageList getImages() {
		return images;
	}

	public void setImages(ImageList images) {
		this.images = images;
	}
	
}
