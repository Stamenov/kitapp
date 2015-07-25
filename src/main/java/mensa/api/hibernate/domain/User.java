package mensa.api.hibernate.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import mensa.api.hibernate.HibernateUtil;

import org.hibernate.Session;

/**
 * Class for throttling usage of image posting/meal merging
 * @author Petar Vutov
 */
@Entity
public class User {
	@Transient
	private static final int MAX_USES_PER_DAY = 10;
	@Transient
	private static final long DAY_IN_MILLIS = 80000000;
	private String userid;
	private int count;
	private long accesstime;
	
	/**
	 * Constructor for Hibernate.
	 */
	public User() {
		
	}

	@Id
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public long getAccesstime() {
		return accesstime;
	}
	public void setAccesstime(long accesstime) {
		this.accesstime = accesstime;
	}
		
	private boolean hasUsesLeft() {
		resetCountIfADayHasPassed();
		return count < MAX_USES_PER_DAY;
	}
	
	private void resetCountIfADayHasPassed() {
		if (accesstime < (System.currentTimeMillis() - DAY_IN_MILLIS)) {
			count = 0;
		}
	}
	
	/**
	 * Check if the given user is still below their daily limit for actions that require admin approval.
	 * @param userid The user to check.
	 * @return <code>true</code> if the user is below their limit, <code>false</code> if they have exceeded it.
	 */
	public static boolean hasUsesLeft(String userid) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		User user = (User) session.get(User.class, userid);
		
		if (user == null) {
			System.out.println("safe");
			user = new User();
			user.setUserid(userid);
			user.setAccesstime(System.currentTimeMillis());
			user.setCount(0);
			
			session.beginTransaction();			
			session.save(user);			
			session.getTransaction().commit();
		}
		
		return user.hasUsesLeft();
	}
	
	/**
	 * After successfully completing an operation which is limited on a per-user basis, use this method to count it.
	 * @param userid The user who requested the operation.
	 */
	public static void reportSuccess(String userid) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		User user = (User) session.get(User.class, userid);
		user.count++;
		
		session.beginTransaction();
		session.merge(user);
		session.getTransaction().commit();
	}
	
}
