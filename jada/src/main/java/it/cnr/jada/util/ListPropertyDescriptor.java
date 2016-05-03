package it.cnr.jada.util;

import java.beans.*;
import java.io.Serializable;
import java.lang.reflect.Method;

public class ListPropertyDescriptor extends PropertyDescriptor
    implements Serializable
{

    ListPropertyDescriptor(PropertyDescriptor propertydescriptor, Class class1)
        throws IntrospectionException
    {
        super(propertydescriptor.getName(), propertydescriptor.getReadMethod(), propertydescriptor.getWriteMethod());
        String s = _capitalize(propertydescriptor.getName());
        try
        {
            shrinker = class1.getMethod("removeFrom" + s, new Class[] {
                Integer.TYPE
            });
        }
        catch(NoSuchMethodException _ex) { }
        try
        {
            Class class2 = java.lang.Object.class;
            if(shrinker != null && shrinker.getReturnType() != Void.TYPE)
                class2 = shrinker.getReturnType();
            grower = class1.getMethod("addTo" + s, new Class[] {
                class2
            });
        }
        catch(NoSuchMethodException _ex) { }
        try
        {
            sorter = class1.getMethod("sort" + s, new Class[] {
                java.util.Comparator.class
            });
        }
        catch(NoSuchMethodException _ex) { }
        if(shrinker == null && grower == null && sorter == null)
            throw new IntrospectionException(propertydescriptor.getName() + " is not a ListProperty");
        else
            return;
    }

    ListPropertyDescriptor(String s, Class class1)
        throws IntrospectionException
    {
        this(s, class1, "get" + _capitalize(s), "set" + _capitalize(s), "addTo" + _capitalize(s), "removeFrom" + _capitalize(s));
    }

    ListPropertyDescriptor(String s, Class class1, String s1, String s2, String s3, String s4)
        throws IntrospectionException
    {
        super(s, class1, s1, s2);
        try
        {
            grower = class1.getMethod(s3, new Class[] {
                getPropertyType()
            });
            shrinker = class1.getMethod(s4, new Class[] {
                Integer.TYPE
            });
        }
        catch(NoSuchMethodException nosuchmethodexception)
        {
            throw new IntrospectionException(nosuchmethodexception.getMessage());
        }
    }

    ListPropertyDescriptor(String s, Method method, Method method1, Method method2, Method method3)
        throws IntrospectionException
    {
        super(s, method, method1);
        grower = method2;
        shrinker = method3;
    }

    static String _capitalize(String s)
    {
        if(s.length() == 0)
        {
            return s;
        } else
        {
            char ac[] = s.toCharArray();
            ac[0] = Character.toUpperCase(ac[0]);
            return new String(ac);
        }
    }

    public Method getAddMetod()
    {
        return grower;
    }

    public Method getRemoveMetod()
    {
        return shrinker;
    }

    public Method getSortMethod()
    {
        return sorter;
    }

    private Method grower;
    private Method shrinker;
    private Method sorter;
//    static Class class$0; /* synthetic field */
//    static Class class$2; /* synthetic field */
}