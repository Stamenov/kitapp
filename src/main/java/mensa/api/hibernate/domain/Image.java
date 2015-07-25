package mensa.api.hibernate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Images are saved somewhere on a filesystem that is accessible from the internet.
 * This class helps store the internet urls in the database.
 * @author Petar Vutov
 */
@Entity
public class Image {
	private int id;
	private String userid;
	private String url;
	private int hashCode;
	
	/**	 
	 * Default constructor required by Hibernate.
	 */
	public Image() {
		
	}
	
	/**
	 * Shortcut constructor.
	 * @param userid id of the user who originally uploaded the image.
	 * @param url Address where the image is hosted.
	 * @param hashCode A hash representing the image.
	 */
	public Image(String userid, String url, int hashCode) {
		this.userid = userid;
		this.url = url;
		this.hashCode = hashCode;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other != null) {
			if (other.getClass() == Image.class) {
				Image casted = (Image) other;
				if (casted.userid == userid) {
					if (casted.url == url) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	@Override
	public  int hashCode() {
		return hashCode;
	}	

	@Id @GeneratedValue
	public int getId() {
		return id;
	};
	
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
}