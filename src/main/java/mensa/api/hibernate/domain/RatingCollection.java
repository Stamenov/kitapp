package mensa.api.hibernate.domain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class RatingCollection {
	private int id;
	private Map<Integer, Rating> ratings;
	private int sum;
	private double average;	

	@Id @GeneratedValue
	public int getId() {
		return id;
	};

	public void setId(int id) {
		this.id = id;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JsonIgnore
	public Map<Integer, Rating> getRatings() {
		return ratings;
	}
	public void setRatings(Map<Integer, Rating> ratings) {
		this.ratings = ratings;
	}
	
	@JsonIgnore
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	
	public double getAverage() {
		return average;
	}
	public void setAverage(double average) {
		this.average = average;
	}
	
	public void addAll(RatingCollection ratings) {
		this.ratings.putAll(ratings.getRatings());
	}

	public void add(Rating rating) {
		Rating old = this.ratings.put(rating.getUserid(), rating);
		
		if(old != null) {
			sum -= old.getValue();
		}

		sum += rating.getValue();
		average = ((double) sum) / ratings.size();
	}
	
	public void remove(int userid) {

	}
}
