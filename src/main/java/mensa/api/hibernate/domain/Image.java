package mensa.api.hibernate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public final class Image {
	// THESE FIELDS SHOULD BE IMMUTABLE
	private int id;
	private int userid;
	private String url;
	private final int hashCode;
	
	public Image(int userid, String url) {
		this.userid = userid;
		this.url = url;
		hashCode = userid * 37 ^ url.hashCode();
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

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	};
	
	public void setId(int id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}