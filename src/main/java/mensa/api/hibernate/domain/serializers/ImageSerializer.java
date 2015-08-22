package mensa.api.hibernate.domain.serializers;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import mensa.api.hibernate.domain.Image;

/**
 * A class responsible for adapting the domain model to what the app actually needs.
 * @author Petar Vutov
 */
public class ImageSerializer extends JsonSerializer<Image> {
	
    /**
     * Serializes only the url field.
     * Thus, instead of generating a JSON object with many fields, we replace it with a single String.
     */
    @Override
    public void serialize(final Image image, final JsonGenerator generator, final SerializerProvider provider) {
    	
    	try {    		
	    	if (image == null) {
	    		generator.writeObject(null);
	    	} else {    	
	    		generator.writeObject(image.getUrl());
	        }
	    	
    	} catch (JsonProcessingException e) {
    		System.err.println("Exception thrown when trying to serialize Image to json. "
    				+ "Did the structure of the Image class change?");
    		e.printStackTrace();
    	} catch (IOException e) {
    		System.err.println("IOException thrown when trying to serialize a class into json:");
    		e.printStackTrace();
    	}
    }
}
