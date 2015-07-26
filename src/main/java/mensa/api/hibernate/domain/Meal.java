package mensa.api.hibernate.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author Petar Vutov
 */
@Entity
public class Meal {

	private int mealid;
	private String name;
	private MealData data;	

	/**
	 * Default constructor required by Hibernate.
	 */
	public Meal() {
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	@Id @GeneratedValue
	public int getMealid() {
		return mealid;
	};
	public void setMealid(int mealid) {
		this.mealid = mealid;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	public MealData getData() {
		return data;
	}		
	public void setData(MealData data) {
		this.data = data;
	}
	
	/**
	 * Before exposing a meal to the app, transform batch data to user-specific fragments of it where appropriate.
	 * @param userid The user the data is for.
	 */
	public void setCurrentUser(String userid) {
		data.setCurrentUser(userid);
	}
	
	/**
	 * Add a rating to this meal.
	 * @param rating The rating to add.
	 */
	public void rate(Rating rating) {
		data.rate(rating);
	}
}

