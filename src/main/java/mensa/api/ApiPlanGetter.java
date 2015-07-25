package mensa.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Offer;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Path("/plan/")
public class ApiPlanGetter {
	
	/**
	 * Provides list of offers for the specified timespan
	 * @param timestampFrom
	 * @param timestampTo
	 * @return list of offers
	 */
	@GET
	@Path("/{timestampFrom:[0-9]*}/{timestampTo:[0-9]*}/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlanByTimestamps(@PathParam("timestampFrom") int timestampFrom, @PathParam("timestampTo") int timestampTo) {
		List<Offer> result = new ArrayList<Offer>();

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Criteria offerCriteria = session.createCriteria(Offer.class);
		offerCriteria.add(Restrictions.between("timestamp", timestampFrom, timestampTo));

		List<Offer> resultList = offerCriteria.list();

		Iterator<Offer> it = resultList.iterator();
        
		while(it.hasNext()) {
			Offer next = it.next();
			result.add(next);
		}

		return Response.ok().entity(result).build();
	}
}
