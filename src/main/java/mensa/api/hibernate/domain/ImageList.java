package mensa.api.hibernate.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class ImageList {
	private int id;	
	private Set<Image> images = new HashSet<Image>();
	
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

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "imageListToImage")
	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}
	
	
}
