package mensa.api.hibernate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Offer {
	private int id;
	private Meal meal;
	private Price price;
	
	@Id @GeneratedValue
	private int getId() {
		return id;
	};	

	public void setId(int id) {
		this.id = id;
	}
}
