/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.jada.rest;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Enumeration;


public class RestForward implements Forward {

    @SuppressWarnings("unchecked")
    public void perform(ActionContext actioncontext) {
        HttpActionContext httpactioncontext = (HttpActionContext) actioncontext;
        ConsultazioniBP bp = (ConsultazioniBP) actioncontext.getBusinessProcess();
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
            while (elements.hasMoreElements()) {
                jGenerator.writeStartObject();
                OggettoBulk oggettoBulk = elements.nextElement();
                for (Enumeration<ColumnFieldProperty> enumeration2 = bp.getColumns().elements(); enumeration2.hasMoreElements(); ) {
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