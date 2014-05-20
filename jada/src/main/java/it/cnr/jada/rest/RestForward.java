package it.cnr.jada.rest;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.util.Enumeration;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

public class RestForward implements Forward {

	@SuppressWarnings("unchecked")
	public void perform(ActionContext actioncontext) {
		HttpActionContext httpactioncontext = (HttpActionContext)actioncontext;
		ConsultazioniBP bp = (ConsultazioniBP)actioncontext.getBusinessProcess();
		HttpServletResponse response = httpactioncontext.getResponse();
		response.setContentType("application/json");
		try {
			JsonFactory jfactory = new JsonFactory();
		    JsonGenerator jGenerator = jfactory.createJsonGenerator(response.getWriter());
		    jGenerator.writeStartObject();
		    jGenerator.writeNumberField("totalNumItems", bp.getElementsCount());
		    jGenerator.writeNumberField("maxItemsPerPage", bp.getPageSize());
		    jGenerator.writeNumberField("activePage", bp.getCurrentPage());
		    jGenerator.writeArrayFieldStart("elements");
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
			Enumeration<OggettoBulk> elements = bp.fetchPage(httpactioncontext);
			while(elements.hasMoreElements()) {
				jGenerator.writeRawValue(mapper.writeValueAsString(elements.nextElement()));				
			}
			jGenerator.writeEndArray();
			jGenerator.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				EJBCommonServices.closeRemoteIterator(httpactioncontext, bp.detachIterator());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}