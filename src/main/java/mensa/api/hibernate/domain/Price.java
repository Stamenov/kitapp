package mensa.api.hibernate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Price {
	private int id;
	private int studentPrice;
	private int professorPrice;
	private int childPrice;
	private int visitorPrice;
	
	@Id @GeneratedValue
	private int getId() {
		return id;
	};	

	public void setId(int id) {
		this.id = id;
	}

	public int getStudentPrice() {
		return studentPrice;
	}

	public void setStudentPrice(int studentPrice) {
		this.studentPrice = studentPrice;
	}

	public int getProfessorPrice() {
		return professorPrice;
	}

	public void setProfessorPrice(int professorPrice) {
		this.professorPrice = professorPrice;
	}

	public int getChildPrice() {
		return childPrice;
	}

	public void setChildPrice(int childPrice) {
		this.childPrice = childPrice;
	}

	public int getVisitorPrice() {
		return visitorPrice;
	}

	public void setVisitorPrice(int visitorPrice) {
		this.visitorPrice = visitorPrice;
	}
}
