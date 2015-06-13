package mensa.api.hibernate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Meal {

	private int id;
	private String name;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private MealData data;
	
	@Id @GeneratedValue
	public int getId() {
		return id;
	};

	public void setId(int id) {
		this.id = id;
	}

	public MealData getData() {
		return data;
	}
	
	public void setData(MealData data) {
		this.data = data;
	}
}
