package mensa.api.hibernate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
	
	public Tags() {
		
	}
	
	public Tags(boolean bio, boolean fish, boolean pork, boolean cow,
			boolean cow_aw, boolean vegan, boolean veg, String add) {
		super();
		this.bio = bio;
		this.fish = fish;
		this.pork = pork;
		this.cow = cow;
		this.cow_aw = cow_aw;
		this.vegan = vegan;
		this.veg = veg;
		this.add = add;
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

	public static boolean equals(Tags tags1, Tags tags2){
		if (tags1.bio == tags2.bio && tags1.fish == tags2.fish && tags1.pork == tags2.pork 
				&& tags1.cow == tags2.cow && tags1.cow_aw == tags2.cow_aw &&  tags1.vegan == tags2.vegan 
					&&  tags1.veg == tags2.veg &&  tags1.add.equals(tags2.add)) {
			return true;
		} else {
			return false;			
		}
	}
	
	/**
	 * Perform shallow copy.
	 */
	public Tags clone() {
		// having a clone method is bad coding style? or not?
		Tags result = null;
		
		try {
			result = (Tags) super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Internally to Tags, a CloneNotSupportedException "
					+ "was thrown when trying to clone Tags. Did you inherit from Tags?");
			e.printStackTrace();
		}
		
		return result;
	}
}
