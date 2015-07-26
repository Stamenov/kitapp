package mensa.api.studentenwerk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mensa.api.hibernate.domain.Offer;

/**
 * Represents all data read from Studentenwerk in a given run.
 * Used by com.google.gson.Gson to provide expected structure when parsing json.
 * @author Petar Vutov
 */
public class Data {
	private Map<String, Day> adenauerring;
	
	/*
	 *  We could extract all data by defining all other mensas as fields here.
	 *  We do not need the rest of the data. Therefore, only adenauerring is defined.
	 */
	
	
	/**
	 * Convert the data into Offer classes that can be directly stored in the db.
	 * @return List of the offers that were parsed from Studentenwerk's API.
	 */
	public List<Offer> getOffers() {
		List<Offer> result = new ArrayList<Offer>();
		
		Set<String> timestamps = adenauerring.keySet();
		
		// For all days, create all offers contained in that particular day:
		for (String t : timestamps) {
			int timestamp = 0;
			try {
				timestamp = Integer.parseInt(t);
			} catch (NumberFormatException e) {
				System.err.println("The data contained a timestamp that was not a number. "
						+ "It is likely that the SW API has changed. Aborting.");
				return null;
			}
			result.addAll(adenauerring.get(t).getOffers(timestamp));
		}
		
		return result;
	}
}
