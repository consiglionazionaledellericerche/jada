/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.bp;

import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.FieldValidationMap;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public interface FormController{

    public static final int UNDEFINED = -1;
    public static final int SEARCH = 0;
    public static final int INSERT = 1;
    public static final int EDIT = 2;
    public static final int FREESEARCH = 4;
    public static final int VIEW = 5;

    public abstract void addChildController(FormController controller);

    /**
     * Restituisce il BulkInfo che definisce le propriet� visuali del modello
     */
    public abstract BulkInfo getBulkInfo();

    public abstract FormController getChildController(java.lang.String name);

    public abstract java.util.Enumeration<FormController> getChildrenController();

    public abstract java.lang.String getControllerName();

    public abstract FieldValidationMap getFieldValidationMap();

    /**
     * Restituisce la FormField con il nome specificato; una FormField combina una FieldProperty con il 
     * FormController in cui � visualizzata.
     */
    public abstract FormField getFormField(java.lang.String name);

    /**
     * Restituisce la stringa da usare come prefisso nel nome degli INPUT HTML e nei corrispondenti parametri 
     * HTTP durante la lettura/scrittura della FORM HTML gestita dal ricevente
     */
    public abstract java.lang.String getInputPrefix();

    public abstract FormController getParentController();

}