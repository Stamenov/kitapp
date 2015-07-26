package mensa.api.studentenwerk;

import java.util.List;

/**
 * Represents a meal in Studentenwerk's API. 
 * Used by com.google.gson.Gson to provide expected structure when parsing json.
 * @author Petar Vutov
 */
public class MealSW {
	private String meal;
	private String dish;
	private List<String> add;
	private boolean bio;
	private boolean fish;
	private boolean pork;
	private boolean cow;
	private boolean cow_aw;
	private boolean vegan;
	private boolean veg; 
	private double price_1;
	private double price_2;
	private double price_3;
	private double price_4;
	private boolean nodata;
	private String closing_start;
	private String closing_end;
	
	/*
	 * not used but in SW API:
	 * private int price_flag; 
	 * private String info;
	 */
	
	public String getMeal() {
		return meal;
	}
	public String getDish() {
		return dish;
	}
	public List<String> getAdd() {
		return add;
	}
	public boolean isBio() {
		return bio;
	}
	public boolean isFish() {
		return fish;
	}
	public boolean isPork() {
		return pork;
	}
	public boolean isCow() {
		return cow;
	}
	public boolean isCow_aw() {
		return cow_aw;
	}
	public boolean isVegan() {
		return vegan;
	}
	public boolean isVeg() {
		return veg;
	}
	public double getPrice_1() {
		return price_1;
	}
	public double getPrice_2() {
		return price_2;
	}
	public double getPrice_3() {
		return price_3;
	}
	public double getPrice_4() {
		return price_4;
	}
	public boolean isNodata() {
		return nodata;
	}
	public String getClosing_start() {
		return closing_start;
	}
	public String getClosing_end() {
		return closing_end;
	}
	
}
