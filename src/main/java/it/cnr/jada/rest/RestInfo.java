package it.cnr.jada.rest;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.util.action.ConsultazioniBP;

import java.util.Dictionary;
import java.util.Enumeration;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class RestInfo implements Forward {
	private final Dictionary<String, FieldProperty> columns;
	
	public RestInfo(Dictionary<String, FieldProperty> columns) {
		super();
		this.columns = columns;
	}

	public void perform(ActionContext actioncontext) {
		HttpActionContext httpactioncontext = (HttpActionContext)actioncontext;
		ConsultazioniBP bp = (ConsultazioniBP)actioncontext.getBusinessProcess();	
		HttpServletResponse response = httpactioncontext.getResponse();
		response.setContentType("application/json");
		try {
			JsonFactory jfactory = new JsonFactory();
		    JsonGenerator jGenerator = jfactory.createGenerator(response.getWriter());
		    jGenerator.writeStartObject();
		    jGenerator.writeStringField("title", bp.getBulkInfo().getShortDescription());	    
		    jGenerator.writeArrayFieldStart("fields");
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			Enumeration<FieldProperty> fields = columns.elements();
			while(fields.hasMoreElements()) {
				jGenerator.writeRawValue(mapper.writeValueAsString(fields.nextElement()));				
			}
			jGenerator.writeEndArray();
			jGenerator.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}