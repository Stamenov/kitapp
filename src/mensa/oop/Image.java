package mensa.oop;

public class Image {
	// THESE FIELDS SOULD BE IMMUTABLE
	private int userid;
	private String url;
	
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
}