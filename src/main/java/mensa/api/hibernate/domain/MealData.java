package mensa.api.hibernate.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class MealData {	
	private int id;
	private Set<Rating> ratings;
	private Set<Image> images;
	private Set<Tag> tags;
	private Set<Meal> meals;
	
	public MealData(Meal meal, HashSet<Tag> tags){
		meals = new HashSet<Meal>();
		meals.add(meal);
		ratings = new HashSet<Rating>();
		images = new HashSet<Image>();
		this.tags = tags;
	}
	
	/**
	 * For merging; Given two MealData objects, creates a third object that has their merged fields.
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
		
		images = new HashSet<Image>();
		images.addAll(first.images);
		images.addAll(second.images);
		
		ratings = new HashSet<Rating>();
		ratings.addAll(first.ratings);
		ratings.addAll(second.ratings);
	}	

	public static void merge(MealData first, MealData second) {
		MealData data = new MealData(first, second);
		// make data contain merged ratings etc
		
		for (Meal meal : data.meals) {
			meal.setData(data);
		}
	}
	

	@Id @GeneratedValue
	public int getId() {
		return id;
	};	

	public void setId(int id) {
		this.id = id;
	}
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "imageListToImage")
	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "ratingListToRating")
	public Set<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(Set<Rating> ratings) {
		this.ratings = ratings;
	}
	
	@ElementCollection(fetch=FetchType.EAGER)
    @JoinTable(name="tags")
	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	@OneToMany(mappedBy = "data")
	public Set<Meal> getMeals() {
		return meals;
	}

	public void setMeals(Set<Meal> meals) {
		this.meals = meals;
	}	
}
