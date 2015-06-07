package mensa.api;

public class Meal {
	private int id;
	private String Name;
	private int Line;
	
	public Meal(String name, int line){
		this.Name = name;
		this.Line = line;
	}
	
	public String getName(){
		return Name;
	}
	
	public int getLine(){
		return Line;
	}
	
	public void setName(String name){
		this.Name = name;
	}
	
	public void setLine(int line){
		this.Line = line;;
	}
	
	@Override
	public String toString() {
		return "Meal [name=" + Name + ", line=" + Line +"]";
	}

}
