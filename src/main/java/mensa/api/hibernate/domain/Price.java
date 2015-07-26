package mensa.api.hibernate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Petar Vutov
 */
@Entity
public class Price {
	private int id;
	private double studentPrice;
	private double visitorPrice;
	private double workerPrice;
	private double childPrice;
	
	/**
	 * Default constructor required by Hibernate.
	 */
	public Price() {
		
	}

	@Id @GeneratedValue
	private int getId() {
		return id;
	};	
	public void setId(int id) {
		this.id = id;
	}
	public double getStudentPrice() {
		return studentPrice;
	}
	public void setStudentPrice(double studentPrice) {
		this.studentPrice = studentPrice;
	}
	public double getWorkerPrice() {
		return workerPrice;
	}
	public void setWorkerPrice(double workerPrice) {
		this.workerPrice = workerPrice;
	}
	public double getChildPrice() {
		return childPrice;
	}
	public void setChildPrice(double childPrice) {
		this.childPrice = childPrice;
	}
	public double getVisitorPrice() {
		return visitorPrice;
	}
	public void setVisitorPrice(double visitorPrice) {
		this.visitorPrice = visitorPrice;
	}
}
