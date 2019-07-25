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

package it.cnr.jada.bulk.annotation;

import java.util.Hashtable;

public enum FormatName {
    date_short,
    date_medium,
    date_long,
    timestamp,
    UppercaseStringFormat,
    LowercaseStringFormat,
    PrimitiveNumberFormat,
    EuroFormat,
    EuroPositivoFormat,
    CapFormat,
    Constrained6DescimalPercentFormat,
    ConstrainedPercentFormat,
    GenericImportoFormat,
    GenericPrezzoUnitarioFormat,
    Importo2CifreFormat,
    Importo2CifrePositivoFormat,
    Importo6CifreFormat,
    ImportoFormat,
    PercentFormat,
    PositiveDecimalFormat,
    PositivePercentFormat,
    PrezzoUnitarioPositivoFormat,
    StringFormat,
    NoFormat;

    public static Hashtable<String, String> formatNameForAnnotation = new Hashtable<String, String>();

    static {
        formatNameForAnnotation.put("date_short", "date_short");
        formatNameForAnnotation.put("date_medium", "date_medium");
        formatNameForAnnotation.put("date_long", "date_long");
        formatNameForAnnotation.put("timestamp", "timestamp");
        formatNameForAnnotation.put("UppercaseStringFormat", "it.cnr.jada.util.UppercaseStringFormat");
        formatNameForAnnotation.put("LowercaseStringFormat", "it.cnr.jada.util.LowercaseStringFormat");
        formatNameForAnnotation.put("PrimitiveNumberFormat", "it.cnr.jada.bulk.PrimitiveNumberFormat");
        formatNameForAnnotation.put("ConstrainedPercentFormat", "it.cnr.contab.util.ConstrainedPercentFormat");
        formatNameForAnnotation.put("EuroFormat", "it.cnr.contab.util.EuroFormat");
        formatNameForAnnotation.put("EuroPositivoFormat", "it.cnr.contab.util.EuroPositivoFormat");
        formatNameForAnnotation.put("CapFormat", "it.cnr.contab.util.CapFormat");
        formatNameForAnnotation.put("Constrained6DescimalPercentFormat", "it.cnr.contab.util.Constrained6DescimalPercentFormat");
        formatNameForAnnotation.put("GenericImportoFormat", "it.cnr.contab.util.GenericImportoFormat");
        formatNameForAnnotation.put("GenericPrezzoUnitarioFormat", "it.cnr.contab.util.GenericPrezzoUnitarioFormat");
        formatNameForAnnotation.put("Importo2CifreFormat", "it.cnr.contab.util.Importo2CifreFormat");
        formatNameForAnnotation.put("Importo2CifrePositivoFormat", "it.cnr.contab.util.Importo2CifrePositivoFormat");
        formatNameForAnnotation.put("Importo6CifreFormat", "it.cnr.contab.util.Importo6CifreFormat");
        formatNameForAnnotation.put("ImportoFormat", "it.cnr.contab.util.ImportoFormat");
        formatNameForAnnotation.put("PercentFormat", "it.cnr.contab.util.PercentFormat");
        formatNameForAnnotation.put("PositiveDecimalFormat", "it.cnr.contab.util.PositiveDecimalFormat");
        formatNameForAnnotation.put("PositivePercentFormat", "it.cnr.contab.util.PositivePercentFormat");
        formatNameForAnnotation.put("PrezzoUnitarioPositivoFormat", "it.cnr.contab.util.PrezzoUnitarioPositivoFormat");
        formatNameForAnnotation.put("StringFormat", "it.cnr.contab.util.StringFormat");
    }
}
