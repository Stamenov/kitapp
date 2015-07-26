
import mensa.api.hibernate.HibernateUtil;

import org.hibernate.Session;
import org.junit.Test;

public class DBTest {
	
	@Test
	public void testDatabaseConnection() {

		HibernateUtil.getSessionFactory().openSession();
	}
}
