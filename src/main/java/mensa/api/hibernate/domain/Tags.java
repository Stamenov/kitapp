package mensa.api.hibernate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Martin Stamenov
 */
@Entity
public class Tags implements Cloneable {
	private int id;
	private boolean bio;
	private boolean fish;
	private boolean pork;
	private boolean cow;
	private boolean cow_aw;
	private boolean vegan;
	private boolean veg;
	private String add;	
	
	/**
	 * Default constructor required by Hibernate.
	 */
	public Tags() {
		
	}

	@Id @GeneratedValue
	private int getId() {
		return id;
	};	
	public void setId(int id) {
		this.id = id;
	}		
	public boolean isBio() {
		return bio;
	}
	public void setBio(boolean bio) {
		this.bio = bio;
	}
	public boolean isFish() {
		return fish;
	}
	public void setFish(boolean fish) {
		this.fish = fish;
	}
	public boolean isPork() {
		return pork;
	}
	public void setPork(boolean pork) {
		this.pork = pork;
	}
	public boolean isCow() {
		return cow;
	}
	public void setCow(boolean cow) {
		this.cow = cow;
	}
	public boolean isCow_aw() {
		return cow_aw;
	}
	public void setCow_aw(boolean cow_aw) {
		this.cow_aw = cow_aw;
	}
	public boolean isVegan() {
		return vegan;
	}
	public void setVegan(boolean vegan) {
		this.vegan = vegan;
	}
	public boolean isVeg() {
		return veg;
	}
	public void setVeg(boolean veg) {
		this.veg = veg;
	}
	public String getAdd() {
		return add;
	}
	public void setAdd(String add) {
		this.add = add;
	}

	/**
	 * Compare two Tags objects without overriding the non-static equals method.
	 * @param tags1 
	 * @param tags2 
	 * @return <code> true </code> if they are equal, <code> false </code> otherwise.
	 */
	public static boolean equals(Tags tags1, Tags tags2) {
		return tags1.bio == tags2.bio && tags1.fish == tags2.fish && tags1.pork == tags2.pork 
				&& tags1.cow == tags2.cow && tags1.cow_aw == tags2.cow_aw &&  tags1.vegan == tags2.vegan 
					&&  tags1.veg == tags2.veg &&  tags1.add.equals(tags2.add);
	}
	
	/**
	 * Perform shallow copy.
	 * @return A shallow copy of the current Tags object.
	 */
	public Tags clone() {
		Tags result = new Tags();
		
		result.setAdd(this.add);
		result.setBio(this.bio);
		result.setCow(this.cow);
		result.setCow_aw(this.cow_aw);
		result.setFish(this.fish);
		result.setPork(this.pork);
		result.setVeg(this.veg);
		result.setVegan(this.vegan);
		
		return result;
	}
}
