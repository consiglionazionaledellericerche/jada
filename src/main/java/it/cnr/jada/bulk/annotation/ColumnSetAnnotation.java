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

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Ragruppamento di columnFieldProperty da usare nelle form
 * HTML di ricerca
 *
 * @author mspasiano
 */
@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ColumnSetAnnotation {
    /**
     * Nome logico del ColumnSet, l'attributo e' obbligatorio, ed e' univoco all'interno del documento.
     */
    String name() default "default";

    /**
     * Array di ColumnFieldProperty che compongono il ColumnSet.
     */
    FieldPropertyAnnotation[] value();
}
