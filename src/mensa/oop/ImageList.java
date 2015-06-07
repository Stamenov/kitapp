package mensa.oop;

import java.util.HashSet;

public class ImageList {
	private HashSet<Image> images;
	
	public static ImageList merge(ImageList first, ImageList second) {
		ImageList result = new ImageList();
		
		result.images.addAll(first.images);
		result.images.addAll(second.images);
		
		return result;
	}
}
