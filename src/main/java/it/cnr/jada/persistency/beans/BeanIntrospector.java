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

package it.cnr.jada.persistency.beans;

import it.cnr.jada.persistency.*;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class BeanIntrospector extends AbstractIntrospector
        implements Serializable {

    private static final BeanIntrospector sqlInstance;

    static {
        try {
            sqlInstance = new BeanIntrospector(it.cnr.jada.persistency.sql.SQLPersistentInfo.class);
        } catch (IntrospectionException introspectionexception) {
            throw new IntrospectionError(introspectionexception);
        }
    }

    private Map propertyCaches;

    public BeanIntrospector(Class class1)
            throws IntrospectionException {
        super(class1);
        propertyCaches = new HashMap();
    }

    public static BeanIntrospector getSQLInstance() {
        return sqlInstance;
    }

    public String getOid(KeyedPersistent keyedpersistent)
            throws IntrospectionException {
        if (keyedpersistent == null) {
            return null;
        } else {
            StringBuffer stringbuffer = new StringBuffer(keyedpersistent.getClass().getName());
            stringbuffer.append('@');
            stringbuffer.append(getUntypedOid(keyedpersistent));
            return stringbuffer.toString();
        }
    }

    private Map getPropertyCache(Class class1)
            throws IntrospectionException {
        Object obj = propertyCaches.get(class1);
        if (obj == null)
            synchronized (propertyCaches) {
                obj = propertyCaches.get(class1);
                if (obj == null) {
                    obj = new HashMap();
                    try {
                        BeanInfo beaninfo = Introspector.getBeanInfo(class1);
                        PropertyDescriptor[] apropertydescriptor = beaninfo.getPropertyDescriptors();
                        for (int i = 0; i < apropertydescriptor.length; i++) {
                            PropertyDescriptor propertydescriptor = apropertydescriptor[i];
                            if (propertydescriptor.getReadMethod() != null && propertydescriptor.getReadMethod().getParameterTypes().length == 0 || propertydescriptor.getReadMethod() != null && propertydescriptor.getWriteMethod().getParameterTypes().length == 1)
                                ((Map) (obj)).put(propertydescriptor.getName(), propertydescriptor);
                        }

                        propertyCaches.put(class1, obj);
                    } catch (java.beans.IntrospectionException introspectionexception) {
                        throw new IntrospectionException(introspectionexception);
                    }
                }
            }
        return ((Map) (obj));
    }

    private PropertyDescriptor getPropertyDescriptor(Class class1, String s)
            throws IntrospectionException {
        PropertyDescriptor propertydescriptor = (PropertyDescriptor) getPropertyCache(class1).get(s);
        if (propertydescriptor == null)
            throw new IntrospectionException("Class " + class1.getName() + " does not have property named " + s);
        else
            return propertydescriptor;
    }

    public Class getPropertyType(Class class1, String s)
            throws IntrospectionException {
        for (StringTokenizer stringtokenizer = tokenize(s); stringtokenizer.hasMoreTokens(); )
            class1 = getPropertyDescriptor(class1, stringtokenizer.nextToken()).getPropertyType();

        return class1;
    }

    public Object getPropertyValue(Object obj, String s)
            throws IntrospectionException {
        for (StringTokenizer stringtokenizer = tokenize(s); stringtokenizer.hasMoreTokens(); ) {
            if (obj == null)
                return null;
            Method method = getPropertyDescriptor(obj.getClass(), stringtokenizer.nextToken()).getReadMethod();
            if (method == null)
                throw new IntrospectionException("Property " + s + " does not have a getter.");
            try {
                obj = method.invoke(obj, null);
            } catch (InvocationTargetException invocationtargetexception) {
                throw new IntrospectionException("Invocation target exception", invocationtargetexception.getTargetException());
            } catch (IllegalAccessException illegalaccessexception) {
                throw new IntrospectionException("Illegal access exception", illegalaccessexception);
            }
        }

        return obj;
    }

    public String getUntypedOid(KeyedPersistent keyedpersistent)
            throws IntrospectionException {
        if (keyedpersistent == null)
            return null;
        StringBuffer stringbuffer = new StringBuffer();
        PersistentInfo persistentinfo = getPersistentInfo(keyedpersistent.getClass());
        for (Iterator iterator = persistentinfo.getOidPersistentProperties().keySet().iterator(); iterator.hasNext(); ) {
            Object obj = getPropertyValue(keyedpersistent, (String) iterator.next());
            if (obj == null)
                throw new IntrospectionException("An OID persistent property can't have null value");
            if (obj instanceof KeyedPersistent)
                stringbuffer.append(getUntypedOid((KeyedPersistent) obj));
            else
                stringbuffer.append(obj);
            if (iterator.hasNext())
                stringbuffer.append('.');
        }

        return stringbuffer.toString();
    }

    public synchronized void resetPropertyCache(Class class1) {
        synchronized (propertyCaches) {
            propertyCaches.remove(class1);
        }
    }

    public synchronized void resetPropertyCaches() {
        synchronized (propertyCaches) {
            propertyCaches.clear();
        }
    }

    public void setPropertyValue(Object obj, String s, Object obj1)
            throws IntrospectionException {
        for (StringTokenizer stringtokenizer = tokenize(s); stringtokenizer.hasMoreTokens(); ) {
            if (obj == null)
                return;
            String s1 = stringtokenizer.nextToken();
            if (stringtokenizer.hasMoreTokens()) {
                Method method = getPropertyDescriptor(obj.getClass(), s1).getReadMethod();
                if (method == null)
                    throw new IntrospectionException("Property " + s + " does not have a getter.");
                try {
                    obj = method.invoke(obj, null);
                } catch (InvocationTargetException invocationtargetexception) {
                    throw new IntrospectionException("Invocation target exception", invocationtargetexception.getTargetException());
                } catch (IllegalAccessException illegalaccessexception) {
                    throw new IntrospectionException("Illegal access exception", illegalaccessexception);
                }
            } else {
                Method method1 = getPropertyDescriptor(obj.getClass(), s1).getWriteMethod();
                if (method1 == null)
                    throw new IntrospectionException("Property " + s + " does not have a setter.");
                try {
                    obj = method1.invoke(obj, obj1);
                } catch (InvocationTargetException invocationtargetexception1) {
                    throw new IntrospectionException("Invocation target exception", invocationtargetexception1.getTargetException());
                } catch (IllegalAccessException illegalaccessexception1) {
                    throw new IntrospectionException("Illegal access exception", illegalaccessexception1);
                }
            }
        }

    }

    private StringTokenizer tokenize(String s) {
        return new StringTokenizer(s, ".");
    }
}