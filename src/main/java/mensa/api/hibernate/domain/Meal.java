package mensa.api.hibernate.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Meal {

	private int mealid;
	private String name;
	private MealData data;	

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
	
	public void setCurrentUser(String userid) {
		data.setCurrentUser(userid);
	}
}

