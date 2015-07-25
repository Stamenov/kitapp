package mensa.api.hibernate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
/**
 * Stores the image with the mealid while it is inactive (not approved).
 * Helps for diplaying the image with the meal name.
 * @author Martin Stamenov
 */
@Entity
public class ImageProposal {

	private int id;
	private String userid;
	private String url;
	private int mealid;

	/**
	 * Default constructor required by Hibernate.
	 */
	public ImageProposal(){
		
	}
	
	public ImageProposal(String userid, int mealid, String url){
		this.userid = userid;
		this.mealid = mealid;
		this.url = url;
	}
	@Id @GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getMealid() {
		return mealid;
	}
	public void setMealid(int mealid) {
		this.mealid = mealid;
	}
}
