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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.05.20 at 05:17:49 PM CEST 
//


package it.cnr.jada.firma.jaxb;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "denominazioneOrToponimoOrCivicoOrCAPOrComuneOrProvinciaOrNazione"
})
@XmlRootElement(name = "IndirizzoPostale")
public class IndirizzoPostale {

    @XmlElements({
            @XmlElement(name = "Denominazione", required = true, type = Denominazione.class),
            @XmlElement(name = "Toponimo", required = true, type = Toponimo.class),
            @XmlElement(name = "Civico", required = true, type = Civico.class),
            @XmlElement(name = "CAP", required = true, type = CAP.class),
            @XmlElement(name = "Comune", required = true, type = Comune.class),
            @XmlElement(name = "Provincia", required = true, type = Provincia.class),
            @XmlElement(name = "Nazione", required = true, type = Nazione.class)
    })
    protected List<Object> denominazioneOrToponimoOrCivicoOrCAPOrComuneOrProvinciaOrNazione;

    /**
     * Gets the value of the denominazioneOrToponimoOrCivicoOrCAPOrComuneOrProvinciaOrNazione property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the denominazioneOrToponimoOrCivicoOrCAPOrComuneOrProvinciaOrNazione property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDenominazioneOrToponimoOrCivicoOrCAPOrComuneOrProvinciaOrNazione().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Denominazione }
     * {@link Toponimo }
     * {@link Civico }
     * {@link CAP }
     * {@link Comune }
     * {@link Provincia }
     * {@link Nazione }
     */
    public List<Object> getDenominazioneOrToponimoOrCivicoOrCAPOrComuneOrProvinciaOrNazione() {
        if (denominazioneOrToponimoOrCivicoOrCAPOrComuneOrProvinciaOrNazione == null) {
            denominazioneOrToponimoOrCivicoOrCAPOrComuneOrProvinciaOrNazione = new ArrayList<Object>();
        }
        return this.denominazioneOrToponimoOrCivicoOrCAPOrComuneOrProvinciaOrNazione;
    }

}
