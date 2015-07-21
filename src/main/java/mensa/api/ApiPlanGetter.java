package mensa.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import mensa.api.hibernate.HibernateUtil;
import mensa.api.hibernate.domain.Offer;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@Path("/plan/")
public class ApiPlanGetter {
	@GET
	@Path("/{timestampFrom:[0-9]*}/{timestampTo:[0-9]*}/") //TODO Make it take two timestamps
	@Produces(MediaType.APPLICATION_JSON)
	public List<Offer> getPlanByTimestamps(@PathParam("timestampFrom") int timestampFrom, @PathParam("timestampTo") int timestampTo) {
		List<Offer> result = new ArrayList<Offer>();

		Session session = HibernateUtil.getSessionFactory().openSession();
		
		Criteria offerCriteria = session.createCriteria(Offer.class);
		offerCriteria.add(Restrictions.between("timestamp", timestampFrom, timestampTo));

		List resultList = offerCriteria.list();

		Iterator<Offer> it = resultList.iterator();
        
		while(it.hasNext()) {
			Offer next = it.next();
			result.add(next);
		}

		return result;
	}
}
