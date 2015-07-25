package mensa.api.hibernate.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class Offer {
	private int id;
	private int timestamp;
	private Line line;
	private Meal meal;
	private Price price;
	
	@Id @GeneratedValue
	@JsonIgnore
	private int getId() {
		return id;
	};	

	public void setId(int id) {
		this.id = id;
	}
	
	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	@ManyToOne(cascade = {CascadeType.ALL})
	public Meal getMeal() {
		return meal;
	}

	public void setMeal(Meal meal) {
		this.meal = meal;
	}

	@OneToOne(cascade = {CascadeType.ALL})
	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}
}
