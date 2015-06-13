package mensa.api.hibernate.domain;


import java.util.LinkedList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Line {
	private int id;
	private String lineName;
	private LinkedList<Offer> offers;

	@Id @GeneratedValue
	private int getId() {
		return id;
	};	

	public void setId(int id) {
		this.id = id;
	}
}
