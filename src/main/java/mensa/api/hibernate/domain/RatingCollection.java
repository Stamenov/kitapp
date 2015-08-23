package mensa.api.hibernate.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Petar Vutov
 */
@Entity
public class RatingCollection {
	private int id;
	private Map<String, Rating> ratings = new HashMap<String, Rating>();;
	private int sum;
	private double average;	
	private Rating currentUserRating;

	/**
	 * Default constructor required by Hibernate.
	 */
	public RatingCollection() { 
		
	}

	@Id @GeneratedValue
	@JsonIgnore
	public int getId() {
		return id;
	};
	public void setId(int id) {
		this.id = id;
	}
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	public Map<String, Rating> getRatings() {
		return ratings;
	}
	public void setRatings(Map<String, Rating> ratings) {
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
	@Transient
	public Rating getCurrentUserRating() {
		return currentUserRating;
	}	
	public void setCurrentUserRating(String currentUser) {
		this.currentUserRating = ratings.get(currentUser);
	}

	/**
	 * Add all ratings from the provided collection without modifying it.	
	 * @param ratings A collection of ratings.
	 */
	public void addAll(RatingCollection ratings) {
		this.ratings.putAll(ratings.getRatings());
	}

	/**
	 * Add a specified rating.
	 * @param rating 
	 */
	public void add(Rating rating) {
		Rating old = this.ratings.put(rating.getUserid(), rating);
		
		if (old != null) {
			sum -= old.getValue();
		}

		sum += rating.getValue();
		updateAverage();
	}
	
	private void updateAverage() {
		average = ((double) sum) / ratings.size();
	}
}
