package mensa.api.hibernate.domain;

import java.util.Arrays;

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
	private int hashCode;

	/**
	 * Default constructor required by Hibernate.
	 */
	public ImageProposal() {
		
	}
	
	/**
	 * Full constructor
	 * @param userid The user who uploaded this image.
	 * @param mealid The id of the meal the image is meant for.
	 * @param url The url the image can be read at.
	 * @param image The byte[] representation of the image, to be used in calculating its hash.
	 */
	public ImageProposal(String userid, int mealid, String url, byte[] image) {
		this.userid = userid;
		this.mealid = mealid;
		this.url = url;
		hashCode = Arrays.hashCode(image);
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
	public int getHashCode() {
		return hashCode;
	}
	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}
}
