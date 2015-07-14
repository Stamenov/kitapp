package mensa.api.studentenwerk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mensa.api.hibernate.domain.Offer;

/**
 * Data for two weeks
 * @author root
 *
 */
public class Data {
	private Map<String, Day> adenauerring;
	
	/**
	 *  We could extract all data by defining all other mensas as fields here.
	 *  We do not need the rest of the data. Therefore, only adenauerring is defined.
	 */
	
	
	/**
	 * Delivers all offers contained in the data, in the hibernate domain format.
	 * @return A list of offers.
	 */
	public List<Offer> getOffers() {
		List<Offer> result = new ArrayList<Offer>();
		
		Set<String> timestamps = adenauerring.keySet();
		
		for (String t : timestamps) {
			int timestamp = 0;
			try {
				timestamp = Integer.parseInt(t);
			} catch (NumberFormatException e) {
				System.out.println("The data contained a timestamp that was not a number. "
						+ "It is likely that the SW API has changed.");
				return null;
			}
			result.addAll(adenauerring.get(t).getOffers(timestamp));
		}
		
		return result;
	}
}
