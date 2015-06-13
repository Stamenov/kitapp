package mensa.api.hibernate.domain;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RatingList {
	private int id;
	private HashMap<Integer, Integer> RatingMap;
	
	public static RatingList merge(RatingList first, RatingList second) {
		RatingList result = new RatingList();
		// TODO
		
		return result;
	}
	

	@Id @GeneratedValue
	private int getId() {
		return id;
	};	

	public void setId(int id) {
		this.id = id;
	}
}
