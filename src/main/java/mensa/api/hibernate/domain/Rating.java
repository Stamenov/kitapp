package mensa.api.hibernate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import mensa.api.hibernate.domain.serializers.RatingSerializer;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Petar Vutov
 */
@Entity
@JsonSerialize(using = RatingSerializer.class)
public class Rating {
	private int id;
	private int value;
	private String userid;
	
	/**
	 * Default constructor required by Hibernate.
	 */
	public Rating() {
		
	}

	@Id @GeneratedValue
	private int getId() {
		return id;
	};
	public void setId(int id) {
		this.id = id;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
}
