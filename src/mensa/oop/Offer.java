package mensa.oop;

import javax.persistence.Id;

public class Offer {
	private int id;
	private Meal meal;
	private Price price;
	
	@Id
	private int getId() {
		return id;
	};
}
