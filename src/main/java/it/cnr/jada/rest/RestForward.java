package it.cnr.jada.rest;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.util.Enumeration;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class RestForward implements Forward {

	@SuppressWarnings("unchecked")
	public void perform(ActionContext actioncontext) {
		HttpActionContext httpactioncontext = (HttpActionContext)actioncontext;
		ConsultazioniBP bp = (ConsultazioniBP)actioncontext.getBusinessProcess();
		HttpServletResponse response = httpactioncontext.getResponse();
		response.setContentType("application/json");
		try {
			JsonFactory jfactory = new JsonFactory();
		    JsonGenerator jGenerator = jfactory.createGenerator(response.getWriter());
		    jGenerator.writeStartObject();
		    jGenerator.writeNumberField("totalNumItems", bp.getElementsCount());
		    jGenerator.writeNumberField("maxItemsPerPage", bp.getPageSize());
		    jGenerator.writeNumberField("activePage", bp.getCurrentPage());
		    jGenerator.writeArrayFieldStart("elements");
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			Enumeration<OggettoBulk> elements = bp.fetchPage(httpactioncontext);
			while(elements.hasMoreElements()) {
				jGenerator.writeStartObject();
				OggettoBulk oggettoBulk = elements.nextElement();
				for(Enumeration<ColumnFieldProperty> enumeration2 = bp.getColumns().elements(); enumeration2.hasMoreElements();){
					ColumnFieldProperty columnFieldProperty = enumeration2.nextElement();
					jGenerator.writeFieldName(columnFieldProperty.getName());
					jGenerator.writeRawValue(mapper.writeValueAsString(
							Introspector.getPropertyValue(oggettoBulk, columnFieldProperty.getProperty())));
				}
				jGenerator.writeEndObject();
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