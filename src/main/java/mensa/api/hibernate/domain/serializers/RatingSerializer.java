package mensa.api.hibernate.domain.serializers;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import mensa.api.hibernate.domain.Rating;

/**
 * A class responsible for adapting the domain model to what the app actually needs.
 * @author Petar Vutov
 */
public class RatingSerializer extends JsonSerializer<Rating> {
	
    /**
     * Serializes only the value field.
     * Thus, instead of generating a JSON object with many fields, we replace it with a single integer.
     */
    @Override
    public void serialize(final Rating rating, final JsonGenerator generator, final SerializerProvider provider) {
    	
    	try {    		
	    	if (rating == null) {
	    		generator.writeObject(null);
	    	} else {    	
	    		generator.writeObject(rating.getValue());
	        }
	    	
    	} catch (JsonProcessingException e) {
    		System.err.println("Exception thrown when trying to serialize Rating to json. "
    				+ "Did the structure of the Rating class change?");
    		e.printStackTrace();
    	} catch (IOException e) {
    		System.err.println("IOException thrown when trying to serialize a class into json:");
    		e.printStackTrace();
    	}
    }
}
