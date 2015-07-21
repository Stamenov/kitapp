package mensa.api.hibernate.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class MealData {	
	private int id;
	private RatingCollection ratings;
	private Set<Image> images;
	private Tags tags;
	private Set<Meal> meals;
	
	private boolean active;
	
	public MealData() {
		
	}
	
	public MealData(Meal meal, Tags tags, boolean active){
		meals = new HashSet<Meal>();
		meals.add(meal);
		ratings = new RatingCollection();
		images = new HashSet<Image>();
		this.tags = tags;
		this.active = active;
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
		
		if (!Tags.equals(first.getTags(), second.getTags())) {
			throw new IllegalArgumentException("Tags of both meals should be equal!");
		} else {
			
			tags = first.getTags().clone();
		}
		
		images = new HashSet<Image>();
		images.addAll(first.images);	
		images.addAll(second.images);
		
		ratings = new RatingCollection();
		ratings.addAll(first.ratings);
		ratings.addAll(second.ratings);
		active = false;
	}	

	public static MealData merge(MealData first, MealData second) {
		MealData data = new MealData(first, second);
		
		return data;
	}
	
	
	@Id @GeneratedValue
	public int getId() {
		return id;
	};	
	
	public void setId(int id) {
		this.id = id;
	}
	
	@OneToMany(cascade = CascadeType.ALL)
	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	public RatingCollection getRatings() {
		return ratings;
	}

	public void setRatings(RatingCollection ratings) {
		this.ratings = ratings;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	public Tags getTags() {
		return tags;
	}

	public void setTags(Tags tags) {
		this.tags = tags;
	}

	@ManyToMany(cascade = CascadeType.ALL)
	@JsonIgnore
	public Set<Meal> getMeals() {
		return meals;
	}

	public void setMeals(Set<Meal> meals) {
		this.meals = meals;
	}
	
	@Column(name = "active")
	public boolean getActive(){
		return this.active;
	}
	
	public void setActive(boolean active){
		this.active = active; 
	}
}
