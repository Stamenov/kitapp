package mensa.api.hibernate.domain;

import java.util.HashSet;

public class ImageList {
	private HashSet<Image> images = new HashSet<Image>();
	
	public static ImageList merge(ImageList first, ImageList second) {
		ImageList result = new ImageList();
		
		result.images.addAll(first.images);
		result.images.addAll(second.images);
		
		return result;
	}
	
	public void putImage(Image image) {
		images.add(image);
	}
}
