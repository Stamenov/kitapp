package mensa.api.hibernate.domain;

import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ImageList {
	private int id;
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

	@Id @GeneratedValue
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
