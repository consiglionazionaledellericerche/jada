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

package it.cnr.jada.util.action;

import it.cnr.jada.UserTransaction;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.FieldValidationMap;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.ValidationException;
import jakarta.servlet.jsp.JspWriter;

import java.io.IOException;
import java.util.Enumeration;

// Referenced classes of package it.cnr.jada.util.action:
//            ModelController, FormField

public interface FormController
        extends ModelController {

    int UNDEFINED = -1;
    int SEARCH = 0;
    int INSERT = 1;
    int EDIT = 2;
    int FREESEARCH = 4;
    int VIEW = 5;

    void addChildController(FormController formcontroller);

    boolean fillModel(ActionContext actioncontext)
            throws FillException;

    BulkInfo getBulkInfo();

    FormController getChildController(String s);

    Enumeration getChildrenController();

    String getControllerName();

    FieldValidationMap getFieldValidationMap();

    FormField getFormField(String s);

    String getInputPrefix();

    FormController getParentController();

    UserTransaction getUserTransaction();

    void resync(ActionContext actioncontext)
            throws BusinessProcessException;

    void validate(ActionContext actioncontext)
            throws ValidationException;

    void writeForm(JspWriter jspwriter)
            throws IOException;

    void writeForm(JspWriter jspwriter, String s)
            throws IOException;

    void writeFormField(JspWriter jspwriter, String s)
            throws IOException;
    void writeFormField(JspWriter jspwriter, String s, Boolean isInsideTable)
            throws IOException;

    void writeFormField(JspWriter jspwriter, String s, String s1)
            throws IOException;

    void writeFormField(JspWriter jspwriter, String s, String s1, Boolean isInsideTable)
            throws IOException;

    void writeFormField(JspWriter jspwriter, String s, String s1, int i, int j)
            throws IOException;

    void writeFormField(JspWriter jspwriter, String s, String s1, int i, int j, Boolean isInsideTable)
            throws IOException;

    void writeFormInput(JspWriter jspwriter, String s)
            throws IOException;

    void writeFormInput(JspWriter jspwriter, String s, String s1)
            throws IOException;

    void writeFormInput(JspWriter jspwriter, String s, String s1, boolean flag, String s2, String s3)
            throws IOException;

    void writeFormInputByStatus(JspWriter jspwriter, String s)
            throws IOException;

    void writeFormLabel(JspWriter jspwriter, String s)
            throws IOException;

    void writeFormLabel(JspWriter jspwriter, String s, String s1)
            throws IOException;

    void writeFormLabel(JspWriter jspwriter, String s, String s1, String s2)
            throws IOException;
}