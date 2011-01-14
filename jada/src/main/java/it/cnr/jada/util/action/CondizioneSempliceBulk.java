package it.cnr.jada.util.action;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SimpleFindClause;
import it.cnr.jada.util.NodoAlbero;
import it.cnr.jada.util.OrderedHashtable;

import java.beans.IntrospectionException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Dictionary;
import java.util.Enumeration;

// Referenced classes of package it.cnr.jada.util.action:
//            CondizioneRicercaBulk

public class CondizioneSempliceBulk extends CondizioneRicercaBulk
    implements Serializable, NodoAlbero
{

    public CondizioneSempliceBulk(OggettoBulk oggettobulk)
    {
        this(oggettobulk, null);
    }

    public CondizioneSempliceBulk(OggettoBulk oggettobulk, String s)
    {
        setPrototype(oggettobulk);
        setFreeSearchSet(s);
    }

    public FindClause creaFindClause()
    {
        SimpleFindClause simplefindclause = new SimpleFindClause();
        simplefindclause.setOperator(operator.intValue());
        simplefindclause.setPropertyName(findFieldProperty.getProperty());
        simplefindclause.setLogicalOperator(getLogicalOperator());
        simplefindclause.setCaseSensitive(findFieldProperty.isCaseSensitiveSearch());
        if(operator.intValue() == 8201 || operator.intValue() == 8202)
            simplefindclause.setValue(findFieldProperty.getPropertyType());
        else
            simplefindclause.setValue(value);
        return simplefindclause;
    }

    public String getDescrizioneNodo()
    {
        StringBuffer stringbuffer = new StringBuffer();
        if(getLogicalOperator() != null)
        {
            stringbuffer.append(getLogicalOperatorOptions().get(getLogicalOperator()));
            stringbuffer.append(' ');
        }
        if(findFieldProperty != null)
        {
            stringbuffer.append("<B>");
            stringbuffer.append(findFieldProperty.getLabel());
            stringbuffer.append("</B>");
            stringbuffer.append(' ');
        }
        if(operator != null)
        {
            try
            {
                stringbuffer.append(getOperatorsDescriptions().get(operator));
            }
            catch(Exception _ex)
            {
                stringbuffer.append("ERRORE");
            }
            stringbuffer.append(' ');
        }
        if(value != null)
        {
            stringbuffer.append("<B>");
            try
            {
                stringbuffer.append(findFieldProperty.getStringValueFrom(prototype, value));
            }
            catch(Exception _ex)
            {
                stringbuffer.append("ERRORE");
            }
            stringbuffer.append("</B>");
            stringbuffer.append(' ');
        }
        return stringbuffer.toString();
    }

    public Enumeration getFigliNodo()
    {
        return null;
    }

    public Enumeration getFindFieldProperties()
    {
        return bulkInfo.getFreeSearchProperties(freeSearchSet);
    }

    public FieldProperty getFindFieldProperty()
    {
        return findFieldProperty;
    }

    public String getFreeSearchSet()
    {
        return freeSearchSet;
    }

    public Object getObject()
    {
        return this;
    }

    public Integer getOperator()
    {
        return operator;
    }

    public Dictionary getOperatorsDescriptions()
        throws IntrospectionException, InvocationTargetException
    {
        if(findFieldProperty == null)
            return null;
        if(findFieldProperty.getKeysProperty() != null || findFieldProperty.getOptionsProperty() != null)
            return findFieldProperty.isNullable() ? optionsOperatorsDescriptions : notNullOptionsOperatorsDescriptions;
        Class class1 = findFieldProperty.getPropertyType(prototype.getClass());
        if(java.lang.Number.class.isAssignableFrom(class1) || (Integer.TYPE == class1 || Float.TYPE == class1 || Double.TYPE == class1 || Long.TYPE == class1 || Short.TYPE == class1 || Byte.TYPE == class1) || java.util.Date.class.isAssignableFrom(class1))
            return findFieldProperty.isNullable() ? numericOperatorsDescriptions : notNullNumericOperatorsDescriptions;
        if(class1 == java.lang.String.class)
            return findFieldProperty.isNullable() ? stringOperatorsDescriptions : notNullStringOperatorsDescriptions;
        else
            return findFieldProperty.isNullable() ? optionsOperatorsDescriptions : notNullOptionsOperatorsDescriptions;
    }

    public OggettoBulk getPrototype()
    {
        return prototype;
    }

    public Object getValue()
    {
        return value;
    }

    public void setFindFieldProperty(FieldProperty fieldproperty)
    {
        if(findFieldProperty != fieldproperty)
        {
            value = null;
            operator = null;
        }
        findFieldProperty = fieldproperty;
    }

    public void setFreeSearchSet(String s)
    {
        freeSearchSet = s;
    }

    public void setOperator(Integer integer)
    {
        if(findFieldProperty != null)
        {
            operator = integer;
            if(operator == null || operator.intValue() == 8201 || operator.intValue() == 8202)
                setValue(null);
        }
    }

    public void setPrototype(OggettoBulk oggettobulk)
    {
        prototype = oggettobulk;
        bulkInfo = prototype.getBulkInfo();
    }

    public void setValue(Object obj)
    {
        value = obj;
    }

    public void validate()
        throws ValidationException
    {
        super.validate();
        if(operator == null)
            throw new ValidationException("E' necessario specificare un operatore di confronto");
        if(operator.intValue() != 8202 && operator.intValue() != 8201 && (value == null || "".equals(value)))
            throw new ValidationException("E' necessario specificare un valore di confronto");
        else
            return;
    }

    private FieldProperty findFieldProperty;
    private OggettoBulk prototype;
    private BulkInfo bulkInfo;
    private Integer operator;
    private static final Dictionary stringOperatorsDescriptions;
    private static final Dictionary numericOperatorsDescriptions;
    private static final Dictionary optionsOperatorsDescriptions;
    private static final Dictionary notNullStringOperatorsDescriptions;
    private static final Dictionary notNullNumericOperatorsDescriptions;
    private static final Dictionary notNullOptionsOperatorsDescriptions;
    private Object value;
    private String freeSearchSet;
//    static Class class$1; /* synthetic field */
//    static Class class$2; /* synthetic field */
//    static Class class$3; /* synthetic field */
//    static Class class$4; /* synthetic field */
//    static Class class$5; /* synthetic field */
//    static Class class$6; /* synthetic field */

    static 
    {
        stringOperatorsDescriptions = new OrderedHashtable();
        numericOperatorsDescriptions = new OrderedHashtable();
        optionsOperatorsDescriptions = new OrderedHashtable();
        notNullStringOperatorsDescriptions = new OrderedHashtable();
        notNullNumericOperatorsDescriptions = new OrderedHashtable();
        notNullOptionsOperatorsDescriptions = new OrderedHashtable();
        notNullStringOperatorsDescriptions.put(new Integer(8192), "=");
        notNullStringOperatorsDescriptions.put(new Integer(8193), "diverso");
        notNullStringOperatorsDescriptions.put(new Integer(16386), "<");
        notNullStringOperatorsDescriptions.put(new Integer(16388), ">");
        notNullStringOperatorsDescriptions.put(new Integer(40967), "contiene");
        notNullStringOperatorsDescriptions.put(new Integer(40968), "inizia con");
        stringOperatorsDescriptions.put(new Integer(8192), "=");
        stringOperatorsDescriptions.put(new Integer(8193), "diverso");
        stringOperatorsDescriptions.put(new Integer(16386), "<");
        stringOperatorsDescriptions.put(new Integer(16388), ">");
        stringOperatorsDescriptions.put(new Integer(40967), "contiene");
        stringOperatorsDescriptions.put(new Integer(40968), "inizia con");
        stringOperatorsDescriptions.put(new Integer(8201), "\350 nullo");
        stringOperatorsDescriptions.put(new Integer(8202), "non \350 nullo");
        notNullNumericOperatorsDescriptions.put(new Integer(8192), "="); 
        notNullNumericOperatorsDescriptions.put(new Integer(8193), "diverso");
        notNullNumericOperatorsDescriptions.put(new Integer(8194), "contiene");
        notNullNumericOperatorsDescriptions.put(new Integer(16386), "<");
        notNullNumericOperatorsDescriptions.put(new Integer(16387), "<=");
        notNullNumericOperatorsDescriptions.put(new Integer(16388), ">");
        notNullNumericOperatorsDescriptions.put(new Integer(16389), ">=");
        numericOperatorsDescriptions.put(new Integer(8192), "=");
        numericOperatorsDescriptions.put(new Integer(8193), "diverso");
        numericOperatorsDescriptions.put(new Integer(8194), "contiene");
        numericOperatorsDescriptions.put(new Integer(16386), "<");
        numericOperatorsDescriptions.put(new Integer(16387), "<=");
        numericOperatorsDescriptions.put(new Integer(16388), ">");
        numericOperatorsDescriptions.put(new Integer(16389), ">=");
        numericOperatorsDescriptions.put(new Integer(8201), "\350 nullo");
        numericOperatorsDescriptions.put(new Integer(8202), "non \350 nullo");
        notNullOptionsOperatorsDescriptions.put(new Integer(8192), "=");
        notNullOptionsOperatorsDescriptions.put(new Integer(8193), "diverso");
        optionsOperatorsDescriptions.put(new Integer(8192), "=");
        optionsOperatorsDescriptions.put(new Integer(8193), "diverso");
        optionsOperatorsDescriptions.put(new Integer(8201), "\350 nullo");
        optionsOperatorsDescriptions.put(new Integer(8202), "non \350 nullo");
    }
}