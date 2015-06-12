package mensa.oop;

import java.util.LinkedList;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class Line {
	private int id;
	private String lineName;
	private LinkedList<Offer> offers;
	
	@Id
	private int getId() {
		return id;
	};
}
