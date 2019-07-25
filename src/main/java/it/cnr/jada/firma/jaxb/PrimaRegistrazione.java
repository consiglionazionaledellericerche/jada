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


/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "identificatore"
})
@XmlRootElement(name = "PrimaRegistrazione")
public class PrimaRegistrazione {

    @XmlElement(name = "Identificatore", required = true)
    protected Identificatore identificatore;

    /**
     * Gets the value of the identificatore property.
     *
     * @return possible object is
     * {@link Identificatore }
     */
    public Identificatore getIdentificatore() {
        return identificatore;
    }

    /**
     * Sets the value of the identificatore property.
     *
     * @param value allowed object is
     *              {@link Identificatore }
     */
    public void setIdentificatore(Identificatore value) {
        this.identificatore = value;
    }

}
