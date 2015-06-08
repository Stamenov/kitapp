package mensa.oop;

public class Image {
	// THESE FIELDS SHOULD BE IMMUTABLE
	private int userid;
	private String url;
	private int hashCode;
	
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
}