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
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Entity
public class MealData {	
	private int id;
	private Set<Rating> ratings;
	private Set<Image> images;
	private Tags tags;
	private Set<Meal> meals;
	
	public MealData() {
		
	}
	
	public MealData(Meal meal, Tags tags){
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
		
		if (!first.tags.equals(second.tags)) {
			throw new IllegalArgumentException("Tags of both meals should be equal!");
		} else {
			tags = first.tags.clone();
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
	
	@OneToOne(cascade = CascadeType.ALL)
	public Tags getTags() {
		return tags;
	}

	public void setTags(Tags tags) {
		this.tags = tags;
	}

	@OneToMany(mappedBy = "data", cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<Meal> getMeals() {
		return meals;
	}

	public void setMeals(Set<Meal> meals) {
		this.meals = meals;
	}	
}
