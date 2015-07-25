package mensa.api.hibernate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public final class Image {
	private boolean active;
	private int id;
	private String userid;
	private String url;
	private int hashCode;
	
	public Image() {
		
	}
	
	public Image(String userid, String url) {
		this.userid = userid;
		this.url = url;
		this.active = false;
		//hashCode = userid * 37 ^ url.hashCode();
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
	public boolean getActive(){
		return active;
	}
	public void setActive(boolean active){
		this.active = active;
	}
}