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

package it.cnr.jada.util;

import it.cnr.jada.bulk.BulkList;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class Introspector
        implements Serializable {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
    private static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static Hashtable properties = new Hashtable();
    private static Hashtable rwproperties = new Hashtable();
    private static Map methodCache = new HashMap();
    private static Map constructorCache = new HashMap();
    private static Object[] arr1 = new Object[1];
    private static Object[] arr2 = new Object[2];
    private static Object[] arr3 = new Object[3];
    private static Object[] arr4 = new Object[4];
    private static Object[] arr5 = new Object[5];

    public Introspector() {
    }

    public static int addToListProperty(Object obj, String s, Object obj1)
            throws IntrospectionException, InvocationTargetException {
        try {
            for (StringTokenizer stringtokenizer = new StringTokenizer(s, "."); stringtokenizer.hasMoreElements(); ) {
                String s1 = stringtokenizer.nextToken();
                if (obj == null)
                    return -1;
                PropertyDescriptor propertydescriptor = getProperyDescriptor(obj.getClass(), s1);
                if (propertydescriptor == null)
                    throw new IntrospectionException(obj.getClass() + " non possiede la propriet\340 " + s1);
                if (stringtokenizer.hasMoreTokens())
                    obj = propertydescriptor.getReadMethod().invoke(obj, null);
                else if (propertydescriptor instanceof ListPropertyDescriptor) {
                    Object obj2 = ((ListPropertyDescriptor) propertydescriptor).getAddMetod().invoke(obj, obj1);
                    if (obj2 instanceof Integer)
                        return ((Integer) obj2).intValue();
                    else
                        return -1;
                } else {
                    throw new IntrospectionException("Property is not a list property");
                }
            }

            throw new IntrospectionException("Non dovrebbe mai arrivare qui!!");
        } catch (IllegalAccessException illegalaccessexception) {
            throw new IllegalAccessError(illegalaccessexception.getMessage());
        }
    }

    public static String buildMetodName(String s, String s1) {
        s1 = s1.replace('.', '_');
        StringBuffer stringbuffer = new StringBuffer(s.length() + s1.length());
        stringbuffer.append(s);
        stringbuffer.append(Character.toUpperCase(s1.charAt(0)));
        stringbuffer.append(s1.substring(1));
        return stringbuffer.toString();
    }

    public static String capitalize(String s) {
        StringBuffer stringbuffer = new StringBuffer(s.length());
        stringbuffer.append(Character.toUpperCase(s.charAt(0)));
        stringbuffer.append(s.substring(1));
        return stringbuffer.toString();
    }

    public static Collection emptyCollection(Class class1)
            throws IntrospectionException {
        try {
            return (Collection) class1.newInstance();
        } catch (InstantiationException _ex) {
        } catch (IllegalAccessException _ex) {
            throw new IntrospectionException("Can't instantiate the class " + class1 + " beacause the default constructor is not public");
        }
        if (java.util.Set.class == class1)
            return Collections.EMPTY_SET;
        if (it.cnr.jada.bulk.BulkCollection.class == class1)
            return new BulkList();
        if (java.util.Collection.class.isAssignableFrom(class1))
            return Collections.EMPTY_LIST;
        else
            throw new IntrospectionException("Can't instantiate the abstract class " + class1);
    }

    public static boolean getBoolean(Object obj, String s, boolean flag)
            throws IntrospectionError {
        try {
            if (s == null)
                return flag;
            if (obj == null)
                return flag;
            Object obj1 = getPropertyValue(obj, s);
            if (obj1 instanceof Boolean)
                return ((Boolean) obj1).booleanValue();
            if (obj1 == null)
                return flag;
            else
                throw new ClassCastException();
        } catch (Exception exception) {
            throw new IntrospectionError(exception);
        }
    }

    public static Constructor getConstructor(Class class1, Class aclass[]) {
        Constructor[] aconstructor = getConstructors(class1);
        byte byte0 = -1;
        Constructor constructor = null;
        label0:
        for (int i = 0; i < aconstructor.length; i++) {
            Constructor constructor1 = aconstructor[i];
            int j = 0;
            Class[] aclass1 = constructor1.getParameterTypes();
            if (aclass1.length != aclass.length)
                continue;
            for (int k = 0; k < aclass1.length; k++) {
                if (aclass1[k] == aclass[k]) {
                    j++;
                    continue;
                }
                if (!aclass1[k].isAssignableFrom(aclass[k]))
                    continue label0;
            }

            if (j > byte0)
                constructor = constructor1;
        }

        return constructor;
    }

    public static Constructor getConstructor(Class class1, Object aobj[]) {
        Constructor[] aconstructor = getConstructors(class1);
        byte byte0 = -1;
        Constructor constructor = null;
        label0:
        for (int i = 0; i < aconstructor.length; i++) {
            Constructor constructor1 = aconstructor[i];
            int j = 0;
            Class[] aclass = constructor1.getParameterTypes();
            if (aclass.length != aobj.length)
                continue;
            for (int k = 0; k < aclass.length; k++) {
                if (aobj[k] == null) {
                    if (!aclass[i].isPrimitive())
                        continue;
                    continue label0;
                }
                if (aclass[k] == aobj[k].getClass()) {
                    j++;
                    continue;
                }
                if (!aclass[k].isInstance(aobj[k]))
                    continue label0;
            }

            if (j > byte0)
                constructor = constructor1;
        }

        return constructor;
    }

    public static Constructor getConstructor(Class class1, int i) {
        Constructor[] aconstructor = getConstructors(class1);
        for (int j = 0; j < aconstructor.length; j++)
            if (aconstructor[j].getParameterTypes().length == i)
                return aconstructor[j];

        return null;
    }

    public static Constructor[] getConstructors(Class class1) {
        Constructor[] aconstructor = (Constructor[]) constructorCache.get(class1);
        if (aconstructor == null)
            synchronized (constructorCache) {
                aconstructor = (Constructor[]) constructorCache.get(class1);
                if (aconstructor == null)
                    constructorCache.put(class1, aconstructor = class1.getConstructors());
            }
        return aconstructor;
    }

    public static Enumeration getEnumeration(Object obj)
            throws IntrospectionException {
        if (obj == null)
            return EmptyEnumeration.getInstance();
        if (obj instanceof Enumeration)
            return (Enumeration) obj;
        if (obj instanceof Vector)
            return ((Vector) obj).elements();
        if (obj.getClass().isArray())
            return new ArrayEnumeration((Object[]) obj);
        if (obj instanceof Dictionary)
            return ((Dictionary) obj).elements();
        if (obj instanceof Collection)
            return Collections.enumeration((Collection) obj);
        else
            return null;
    }

    public static Enumeration getEnumeration(Object obj, String s)
            throws IntrospectionException, InvocationTargetException {
        if (obj == null)
            return EmptyEnumeration.getInstance();
        else
            return getEnumeration(getPropertyValue(obj, s));
    }

    public static Method getMethod(Class class1, String s, Class aclass[]) {
        Method[] amethod = getMethods(class1, s);
        byte byte0 = -1;
        Method method = null;
        label0:
        for (int i = 0; i < amethod.length; i++) {
            Method method1 = amethod[i];
            int j = 0;
            Class[] aclass1 = method1.getParameterTypes();
            if (aclass1.length != aclass.length)
                continue;
            for (int k = 0; k < aclass1.length; k++) {
                if (aclass1[k] == aclass[k]) {
                    j++;
                    continue;
                }
                if (!aclass1[k].isAssignableFrom(aclass[k]))
                    continue label0;
            }

            if (j > byte0)
                method = method1;
        }

        return method;
    }

    public static Method getMethod(Class class1, String s, Object aobj[]) {
        Method[] amethod = getMethods(class1, s);
        byte byte0 = -1;
        Method method = null;
        label0:
        for (int i = 0; i < amethod.length; i++) {
            Method method1 = amethod[i];
            int j = 0;
            Class[] aclass = method1.getParameterTypes();
            if (aclass.length != aobj.length)
                continue;
            for (int k = 0; k < aclass.length; k++) {
                if (aobj[k] == null) {
                    if (!aclass[k].isPrimitive()) {
                        j++;
                        continue;
                    }
                    continue label0;
                }
                if (aclass[k] == aobj[k].getClass()) {
                    j++;
                    continue;
                }
                if (aclass[k].isPrimitive() ? aclass[k] != Integer.TYPE ? aclass[k] != Long.TYPE ? aclass[k] != Float.TYPE ? aclass[k] != Double.TYPE ? aclass[k] != Boolean.TYPE ? aclass[k] != Character.TYPE ? aclass[k] != Short.TYPE ? aclass[k] == Byte.TYPE && !(aobj[k] instanceof Byte) : !(aobj[k] instanceof Short) : !(aobj[k] instanceof Character) : !(aobj[k] instanceof Boolean) : !(aobj[k] instanceof Double) : !(aobj[k] instanceof Float) : !(aobj[k] instanceof Long) : !(aobj[k] instanceof Integer) : !aclass[k].isInstance(aobj[k]))
                    continue label0;
            }

            if (j > byte0)
                method = method1;
            if (j == aobj.length)
                return method;
        }

        return method;
    }

    public static Method getMethod(Class class1, String s, int i) {
        Method[] amethod = getMethods(class1, s);
        for (int j = 0; j < amethod.length; j++)
            if (amethod[j].getParameterTypes().length == i)
                return amethod[j];

        return null;
    }

    public static Method[] getMethods(Class class1, String s) {
        Object obj = methodCache.get(class1);
        if (obj == null)
            synchronized (methodCache) {
                obj = methodCache.get(class1);
                if (obj == null)
                    methodCache.put(class1, obj = new HashMap());
            }
        Method[] amethod = (Method[]) ((Map) (obj)).get(s);
        if (amethod == null)
            synchronized (obj) {
                if (amethod == null) {
                    amethod = (Method[]) ((Map) (obj)).get(s);
                    Method[] amethod1 = class1.getMethods();
                    ArrayList arraylist = new ArrayList(amethod1.length);
                    for (int i = 0; i < amethod1.length; i++)
                        if (amethod1[i].getName().equals(s))
                            arraylist.add(amethod1[i]);

                    amethod = new Method[arraylist.size()];
                    ((Map) (obj)).put(s, arraylist.toArray(amethod));
                }
            }
        return amethod;
    }

    public static Class getPropertyType(Class class1, String s)
            throws IntrospectionException {
        if (s == null)
            return class1;
        for (StringTokenizer stringtokenizer = new StringTokenizer(s, "."); stringtokenizer.hasMoreElements(); ) {
            String s1 = stringtokenizer.nextToken();
            PropertyDescriptor propertydescriptor = getProperyDescriptor(class1, s1);
            if (propertydescriptor == null)
                throw new IntrospectionException(class1 + " non possiede la propriet\340 " + s1);
            class1 = propertydescriptor.getPropertyType();
        }

        return class1;
    }

    public static Object getPropertyValue(Object obj, String s)
            throws IntrospectionException, InvocationTargetException {
        if (s == null)
            return obj;
        try {
            for (StringTokenizer stringtokenizer = new StringTokenizer(s, "."); stringtokenizer.hasMoreElements(); ) {
                String s1 = stringtokenizer.nextToken();
                if (obj == null)
                    return null;
                PropertyDescriptor propertydescriptor = getProperyDescriptor(obj.getClass(), s1);
                if (propertydescriptor == null)
                    try {
                        Field field = obj.getClass().getField(s);
                        obj = field.get(obj);
                    } catch (NoSuchFieldException _ex) {
                        if (obj instanceof SelfIntrospector)
                            return ((SelfIntrospector) obj).getPropertyValue(s);
                        else
                            throw new IntrospectionException(obj.getClass() + " non possiede la propriet\340 " + s1);
                    }
                else
                    obj = propertydescriptor.getReadMethod().invoke(obj, null);
            }

        } catch (IllegalAccessException illegalaccessexception) {
            throw new IllegalAccessError(illegalaccessexception.getMessage());
        }
        return obj;
    }

    public static String getPropertyValue(Object obj, String s, Format format)
            throws IntrospectionException, InvocationTargetException {
        Object obj1 = getPropertyValue(obj, s);
        if (obj1 == null)
            return null;
        if (format == null)
            return obj1.toString();
        else
            return format.format(obj1);
    }

    private static PropertyDescriptor getProperyDescriptor(Class class1, String s)
            throws IntrospectionException {
        return (PropertyDescriptor) getProperyDescriptors(class1).get(s);
    }

    private static Hashtable getProperyDescriptors(Class class1)
            throws IntrospectionException {
        Hashtable hashtable = (Hashtable) properties.get(class1);
        if (hashtable == null)
            synchronized (properties) {
                hashtable = (Hashtable) properties.get(class1);
                if (hashtable == null) {
                    PropertyDescriptor[] apropertydescriptor = java.beans.Introspector.getBeanInfo(class1).getPropertyDescriptors();
                    properties.put(class1, hashtable = new Hashtable());
                    for (int i = 0; i < apropertydescriptor.length; i++)
                        try {
                            hashtable.put(apropertydescriptor[i].getName(), new ListPropertyDescriptor(apropertydescriptor[i], class1));
                        } catch (IntrospectionException _ex) {
                            hashtable.put(apropertydescriptor[i].getName(), apropertydescriptor[i]);
                        }

                    properties.put(class1, hashtable);
                }
            }
        return hashtable;
    }

    public static String[] getReadWriteProperties(Class class1)
            throws IntrospectionException {
        String[] as = (String[]) rwproperties.get(class1);
        if (as == null)
            synchronized (rwproperties) {
                as = (String[]) rwproperties.get(class1);
                if (as == null) {
                    Vector vector = new Vector();
                    Hashtable hashtable1 = getProperyDescriptors(class1);
                    for (Enumeration enumeration = hashtable1.elements(); enumeration.hasMoreElements(); ) {
                        PropertyDescriptor propertydescriptor = (PropertyDescriptor) enumeration.nextElement();
                        if (propertydescriptor.getReadMethod() != null && propertydescriptor.getWriteMethod() != null)
                            vector.addElement(propertydescriptor.getName());
                    }

                    Field[] afield = class1.getFields();
                    for (int i = 0; i < afield.length; i++)
                        if (Modifier.isPublic(afield[i].getModifiers()) && !Modifier.isStatic(afield[i].getModifiers()))
                            vector.addElement(afield[i].getName());

                    as = new String[vector.size()];
                    vector.copyInto(as);
                    rwproperties.put(class1, as);
                }
            }
        return as;
    }

    public static boolean hasProperty(Class class1, String s) {
        if (s == null)
            return false;
        try {
            for (StringTokenizer stringtokenizer = new StringTokenizer(s, "."); stringtokenizer.hasMoreElements(); ) {
                String s1 = stringtokenizer.nextToken();
                PropertyDescriptor propertydescriptor = getProperyDescriptor(class1, s1);
                if (propertydescriptor == null)
                    return false;
                class1 = propertydescriptor.getPropertyType();
            }

            return true;
        } catch (IntrospectionException _ex) {
            return false;
        }
    }

    public static Object invoke(Object obj, Method method, Object... aobj)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (method == null)
            throw new NoSuchMethodException("No method in class " + obj.getClass() + " with parameters specified.");
        else
            return method.invoke(obj, aobj);
    }

    public static Object invoke(Object obj, String s, Object aobj[])
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = getMethod(obj.getClass(), s, aobj);
        if (method == null)
            throw new NoSuchMethodException("No method " + s + " in class " + obj.getClass() + " with parameters specified.");
        else
            return method.invoke(obj, aobj);
    }

    public static Object invoke(Object obj, String s, Object obj1)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        synchronized (obj) {
            arr1[0] = obj1;
            Method method = getMethod(obj.getClass(), s, arr1);
            if (method == null)
                throw new NoSuchMethodException("No method " + s + " in class " + obj.getClass() + " with parameters specified.");
            else
                return method.invoke(obj, arr1);
        }
    }

    public static Object invoke(Object obj, String s, Object obj1, Object obj2)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        synchronized (obj) {
            arr2[0] = obj1;
            arr2[1] = obj2;
            Method method = getMethod(obj.getClass(), s, arr2);
            if (method == null)
                throw new NoSuchMethodException("No method " + s + " in class " + obj.getClass() + " with parameters specified.");
            else
                return method.invoke(obj, arr2);
        }
    }

    public static Object invoke(Object obj, String s, Object obj1, Object obj2, Object obj3)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        synchronized (obj) {
            arr3[0] = obj1;
            arr3[1] = obj2;
            arr3[2] = obj3;
            Method method = getMethod(obj.getClass(), s, arr3);
            if (method == null)
                throw new NoSuchMethodException("No method " + s + " in class " + obj.getClass() + " with parameters specified.");
            else
                return method.invoke(obj, arr3);
        }
    }

    public static Object invoke(Object obj, String s, Object obj1, Object obj2, Object obj3, Object obj4)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        synchronized (obj) {
            arr4[0] = obj1;
            arr4[1] = obj2;
            arr4[2] = obj3;
            arr4[3] = obj4;
            Method method = getMethod(obj.getClass(), s, arr4);
            if (method == null)
                throw new NoSuchMethodException("No method " + s + " in class " + obj.getClass() + " with parameters specified.");
            else
                return method.invoke(obj, arr4);
        }
    }

    public static Object invoke(Object obj, String s, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        synchronized (obj) {
            arr5[0] = obj1;
            arr5[1] = obj2;
            arr5[2] = obj3;
            arr5[3] = obj4;
            arr5[4] = obj5;

            Method method = getMethod(obj.getClass(), s, arr5);
            if (method == null)
                throw new NoSuchMethodException("No method " + s + " in class " + obj.getClass() + " with parameters specified.");
            else
                return method.invoke(obj, arr5);
        }
    }

    public static Object invoke(Object obj, String s, String s1, Object aobj[])
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invoke(obj, buildMetodName(s, s1), aobj);
    }

    public static Object invoke(Object obj, String s, String s1, Object obj1)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invoke(obj, buildMetodName(s, s1), obj1);
    }

    public static Object invoke(Object obj, String s, String s1, Object obj1, Object obj2)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invoke(obj, buildMetodName(s, s1), obj1, obj2);
    }

    public static Object invoke(Object obj, String s, String s1, Object obj1, Object obj2, Object obj3)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invoke(obj, buildMetodName(s, s1), obj1, obj2, obj3);
    }

    public static Object invoke(Object obj, String s, String s1, Object obj1, Object obj2, Object obj3, Object obj4)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invoke(obj, buildMetodName(s, s1), obj1, obj2, obj3, obj4);
    }

    public static Object invoke(Object obj, String s, String s1, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invoke(obj, buildMetodName(s, s1), obj1, obj2, obj3, obj4, obj5);
    }

    public static boolean isPropertyWriteable(Object obj, String s)
            throws IntrospectionException, InvocationTargetException {
        if (s == null)
            return false;
        try {
            for (StringTokenizer stringtokenizer = new StringTokenizer(s, "."); stringtokenizer.hasMoreElements(); ) {
                String s1 = stringtokenizer.nextToken();
                if (obj == null)
                    return false;
                PropertyDescriptor propertydescriptor = getProperyDescriptor(obj.getClass(), s1);
                if (propertydescriptor == null)
                    try {
                        Field field = obj.getClass().getField(s);
                        obj = field.get(obj);
                    } catch (NoSuchFieldException _ex) {
                        if (obj instanceof SelfIntrospector)
                            obj = ((SelfIntrospector) obj).getPropertyValue(s);
                        throw new IntrospectionException(obj.getClass() + " non possiede la propriet\340 " + s1);
                    }
                else if (stringtokenizer.hasMoreElements())
                    obj = propertydescriptor.getReadMethod().invoke(obj, null);
                else
                    return propertydescriptor.getWriteMethod() != null;
            }

        } catch (IllegalAccessException illegalaccessexception) {
            throw new IllegalAccessError(illegalaccessexception.getMessage());
        }
        return true;
    }

    public static boolean isSortableProperty(Class class1, String s)
            throws IntrospectionException {
        if (s == null)
            return false;
        PropertyDescriptor propertydescriptor = null;
        for (StringTokenizer stringtokenizer = new StringTokenizer(s, "."); stringtokenizer.hasMoreElements(); ) {
            String s1 = stringtokenizer.nextToken();
            propertydescriptor = getProperyDescriptor(class1, s1);
            if (propertydescriptor == null)
                return false;
            class1 = propertydescriptor.getPropertyType();
        }

        return (propertydescriptor instanceof ListPropertyDescriptor) && ((ListPropertyDescriptor) propertydescriptor).getSortMethod() != null;
    }

    public static Collection newCollection(Class class1)
            throws IntrospectionException {
        try {
            return (Collection) class1.newInstance();
        } catch (InstantiationException _ex) {
        } catch (IllegalAccessException _ex) {
            throw new IntrospectionException("Can't instantiate the class " + class1 + " beacause the default constructor is not public");
        }
        if (java.util.Set.class == class1)
            return new HashSet();
        if (it.cnr.jada.bulk.BulkCollection.class == class1)
            return new BulkList();
        if (java.util.Collection.class.isAssignableFrom(class1))
            return new LinkedList();
        else
            throw new IntrospectionException("Can't instantiate the abstract class " + class1);
    }

    public static Object newInstance(Class class1, Object aobj[])
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor constructor = getConstructor(class1, aobj);
        if (constructor == null)
            throw new NoSuchMethodException("No constructor in class " + class1 + " with parameters specified.");
        else
            return constructor.newInstance(aobj);
    }

    public static Object newInstance(Class class1, Object obj)
            throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        arr1[0] = obj;
        Constructor constructor = getConstructor(class1, arr1);
        if (constructor == null)
            throw new NoSuchMethodException("No constructor in class " + class1 + " with parameters specified.");
        else
            return constructor.newInstance(arr1);
    }

    public static Object newInstance(Class class1, Object obj, Object obj1)
            throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        arr2[0] = obj;
        arr2[1] = obj1;
        Constructor constructor = getConstructor(class1, arr2);
        if (constructor == null)
            throw new NoSuchMethodException("No constructor in class " + class1 + " with parameters specified.");
        else
            return constructor.newInstance(arr2);
    }

    public static Object newInstance(Class class1, Object obj, Object obj1, Object obj2)
            throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        arr3[0] = obj;
        arr3[1] = obj1;
        arr3[2] = obj2;
        Constructor constructor = getConstructor(class1, arr3);
        if (constructor == null)
            throw new NoSuchMethodException("No constructor in class " + class1 + " with parameters specified.");
        else
            return constructor.newInstance(arr3);
    }

    public static Object newInstance(Class class1, Object obj, Object obj1, Object obj2, Object obj3)
            throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        arr4[0] = obj;
        arr4[1] = obj1;
        arr4[2] = obj2;
        arr4[3] = obj3;
        Constructor constructor = getConstructor(class1, arr4);
        if (constructor == null)
            throw new NoSuchMethodException("No constructor in class " + class1 + " with parameters specified.");
        else
            return constructor.newInstance(arr4);
    }

    public static Object removeFromListProperty(Object obj, String s, int i)
            throws IntrospectionException, InvocationTargetException {
        try {
            for (StringTokenizer stringtokenizer = new StringTokenizer(s, "."); stringtokenizer.hasMoreElements(); ) {
                String s1 = stringtokenizer.nextToken();
                if (obj == null)
                    return null;
                PropertyDescriptor propertydescriptor = getProperyDescriptor(obj.getClass(), s1);
                if (propertydescriptor == null)
                    throw new IntrospectionException(obj.getClass() + " non possiede la propriet\340 " + s1);
                if (stringtokenizer.hasMoreTokens())
                    obj = propertydescriptor.getReadMethod().invoke(obj, null);
                else if (propertydescriptor instanceof ListPropertyDescriptor)
                    return ((ListPropertyDescriptor) propertydescriptor).getRemoveMetod().invoke(obj, new Integer(i));
                else
                    throw new IntrospectionException("Property is not a list property");
            }

            throw new IntrospectionException("Non dovrebbe mai arrivare qui!!");
        } catch (IllegalAccessException illegalaccessexception) {
            throw new IllegalAccessError(illegalaccessexception.getMessage());
        }
    }

    public static void resetPropertiesCache() {
        synchronized (properties) {
            properties.clear();
        }
        synchronized (rwproperties) {
            rwproperties.clear();
        }
        synchronized (methodCache) {
            methodCache.clear();
        }
        synchronized (constructorCache) {
            constructorCache.clear();
        }
    }

    public static void resetPropertiesCache(Class class1) {
        synchronized (properties) {
            properties.remove(class1);
        }
        synchronized (rwproperties) {
            rwproperties.remove(class1);
        }
        synchronized (methodCache) {
            methodCache.remove(class1);
        }
        synchronized (constructorCache) {
            constructorCache.remove(class1);
        }
    }

    public static void setPropertyValue(Object obj, String s, Object obj1)
            throws IntrospectionException, InvocationTargetException {
        try {
            for (StringTokenizer stringtokenizer = new StringTokenizer(s, "."); stringtokenizer.hasMoreElements(); ) {
                String s1 = stringtokenizer.nextToken();
                if (obj == null)
                    return;
                PropertyDescriptor propertydescriptor = getProperyDescriptor(obj.getClass(), s1);
                if (propertydescriptor == null)
                    try {
                        Field field = obj.getClass().getField(s);
                        if (stringtokenizer.hasMoreTokens())
                            obj = field.get(obj);
                        else
                            field.set(obj, obj1);
                    } catch (NoSuchFieldException _ex) {
                        throw new IntrospectionException(obj.getClass() + " non possiede la propriet\340 " + s1);
                    }
                else if (stringtokenizer.hasMoreTokens())
                    obj = propertydescriptor.getReadMethod().invoke(obj, null);
                else
                    propertydescriptor.getWriteMethod().invoke(obj, obj1);
            }

        } catch (IllegalAccessException illegalaccessexception) {
            throw new IllegalAccessError(illegalaccessexception.getMessage());
        }
    }

    public static void setPropertyValue(Object obj, String s, String s1)
            throws IntrospectionException, InvocationTargetException, ParseException {
        Class class1 = getPropertyType(obj.getClass(), s);
        if (s1 == null && class1.isPrimitive()) {
            return;
        } else {
            setPropertyValue(obj, s, standardParse(s1, class1));
            return;
        }
    }

    public static void setPropertyValue(Object obj, String s, String s1, Format format)
            throws IntrospectionException, InvocationTargetException, ParseException {
        try {
            for (StringTokenizer stringtokenizer = new StringTokenizer(s, "."); stringtokenizer.hasMoreElements(); ) {
                String s2 = stringtokenizer.nextToken();
                if (obj == null)
                    return;
                PropertyDescriptor propertydescriptor = getProperyDescriptor(obj.getClass(), s2);
                if (propertydescriptor == null)
                    throw new IntrospectionException(obj.getClass() + " non possiede la propriet\340 " + s2);
                if (stringtokenizer.hasMoreTokens()) {
                    obj = propertydescriptor.getReadMethod().invoke(obj, null);
                } else {
                    Object obj1 = s1;
                    if (obj1 != null)
                        obj1 = format.parseObject(s1);
                    propertydescriptor.getWriteMethod().invoke(obj, obj1);
                }
            }

        } catch (IllegalAccessException illegalaccessexception) {
            throw new IllegalAccessError(illegalaccessexception.getMessage());
        }
    }

    public static List sort(Object obj, String s, Comparator comparator)
            throws IntrospectionException, InvocationTargetException {
        try {
            for (StringTokenizer stringtokenizer = new StringTokenizer(s, "."); stringtokenizer.hasMoreElements(); ) {
                String s1 = stringtokenizer.nextToken();
                if (obj == null)
                    return null;
                PropertyDescriptor propertydescriptor = getProperyDescriptor(obj.getClass(), s1);
                if (propertydescriptor == null)
                    throw new IntrospectionException(obj.getClass() + " non possiede la propriet\340 " + s1);
                if (stringtokenizer.hasMoreTokens()) {
                    obj = propertydescriptor.getReadMethod().invoke(obj, null);
                } else {
                    if ((propertydescriptor instanceof ListPropertyDescriptor) && ((ListPropertyDescriptor) propertydescriptor).getSortMethod() != null) {
                        Method method = ((ListPropertyDescriptor) propertydescriptor).getSortMethod();
                        Object obj1 = method.invoke(obj, comparator);
                        if (obj1 instanceof List)
                            return (List) obj1;
                        else
                            return null;
                    }
                    obj = propertydescriptor.getReadMethod().invoke(obj, null);
                    if (obj instanceof Sortable) {
                        ((Sortable) obj).sort(comparator);
                        return (List) obj;
                    }
                    if (obj instanceof List) {
                        Collections.sort((List) obj, comparator);
                        return (List) obj;
                    }
                }
                throw new IntrospectionException("Property is not a list property");
            }

            throw new IntrospectionException("Non dovrebbe mai arrivare qui!!");
        } catch (IllegalAccessException illegalaccessexception) {
            throw new IllegalAccessError(illegalaccessexception.getMessage());
        }
    }

    public static Object standardConvert(Object obj, Class class1) {
        if (obj == null)
            return null;
        if (class1 == java.lang.String.class)
            return obj.toString();
        if (class1 == java.sql.Timestamp.class && (obj instanceof Date))
            return new Timestamp(((Date) obj).getTime());
        if (class1 == java.sql.Date.class && (obj instanceof Date))
            return new java.sql.Date(((Date) obj).getTime());
        if (class1 == java.sql.Time.class && (obj instanceof Date))
            return new Time(((Date) obj).getTime());
        if (class1 == java.lang.Integer.class && (obj instanceof Number))
            return new Integer(((Number) obj).intValue());
        if (class1 == java.lang.Float.class && (obj instanceof Number))
            return new Float(((Number) obj).floatValue());
        if (class1 == java.lang.Double.class && (obj instanceof Number))
            return new Double(((Number) obj).doubleValue());
        if (class1 == java.lang.Short.class && (obj instanceof Number))
            return new Short(((Number) obj).shortValue());
        if (class1 == java.lang.Long.class && (obj instanceof Number))
            return new Long(((Number) obj).longValue());
        if (class1 == java.lang.Byte.class && (obj instanceof Number))
            return new Byte(((Number) obj).byteValue());
        if (class1 == java.math.BigDecimal.class && (obj instanceof Number)) {
            /*
             * Modifica effttuata da Marco il 04/11/2004
             * per il problema sui BigDecimal
             */
            //return new BigDecimal(((Number)obj).doubleValue());
            return new BigDecimal(obj.toString());
        } else
            return obj;
    }

    public static String standardFormat(Object obj) {
        if (obj == null)
            return "";
        if (obj instanceof Boolean)
            return Boolean.TRUE.equals(obj) ? "Si" : "No";
        if (obj instanceof Date)
            return TIMESTAMP_FORMAT.format(obj);
        if (obj instanceof Time)
            return TIME_FORMAT.format(obj);
        if (obj instanceof Timestamp)
            return TIMESTAMP_FORMAT.format(obj);
        if (obj instanceof java.sql.Date)
            return DATE_FORMAT.format(obj);
        else
            return obj.toString();
    }

    public static Object standardParse(String s, Class class1)
            throws ParseException {
        Object obj = null;
        if (class1 == java.lang.String.class)
            obj = s;
        else if (s == null) {
            if (class1.isPrimitive())
                throw new ParseException("Can't convert null String to a primitive type", 0);
            obj = null;
        } else if (class1 == java.lang.Boolean.class || class1 == Boolean.TYPE) {
            if (s == null || s.length() == 0) {
                obj = Boolean.FALSE;
            } else {
                char c = Character.toUpperCase(s.charAt(0));
                obj = new Boolean(c == 'Y' || c == 'T' || c == 'S' || c == 'V' || c == '1');
            }
        } else if (class1 == java.lang.Character.class || class1 == Character.TYPE)
            obj = new Character(s.length() != 0 ? s.charAt(0) : '\0');
        else
            try {
                if (class1 == java.lang.Integer.class || class1 == Integer.TYPE)
                    obj = Integer.valueOf(s);
                else if (class1 == java.lang.Float.class || class1 == Float.TYPE)
                    obj = Float.valueOf(s);
                else if (class1 == java.lang.Double.class || class1 == Double.TYPE)
                    obj = Double.valueOf(s);
                else if (class1 == java.lang.Long.class || class1 == Long.TYPE)
                    obj = Long.valueOf(s);
                else if (class1 == java.lang.Short.class || class1 == Short.TYPE)
                    obj = Short.valueOf(s);
                else if (class1 == java.math.BigDecimal.class)
                    obj = new BigDecimal(s);
                if (class1 == java.util.Date.class)
                    obj = TIMESTAMP_FORMAT.parse(s);
                if (class1 == java.sql.Time.class)
                    obj = new Time(TIME_FORMAT.parse(s).getTime());
                if (class1 == java.sql.Timestamp.class)
                    obj = new Timestamp(TIMESTAMP_FORMAT.parse(s).getTime());
                if (class1 == java.sql.Date.class)
                    obj = new java.sql.Date(DATE_FORMAT.parse(s).getTime());
            } catch (NumberFormatException _ex) {
                if (class1.isPrimitive() || s.trim().length() > 0)
                    throw new ParseException("The conversion of \"" + s + "\" is a null Object, but the property is primitive", 0);
            }
        if (class1.isPrimitive() && obj == null)
            throw new ParseException("The conversion of \"" + s + "\" is a null Object, but the property is primitive", 0);
        else
            return obj;
    }
}