/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
/**
 * Classe di utilit� che permette la lettura/scrittura di propriet� mediante il paradigma dei JavaBeans. 
 * Per propriet� si intende: 
 * Il nome di un field publico di una classe; 
 * Il nome di una propriet� di un JavaBean, ovvero una coppia di metodi getter/setter che permettono di 
 * 	leggerne e scriverne il valore; Un nome di propriet� composto da pi� nomi di propriet� semplici concatenati 
 * 	tra loro tramite il carattere '.'
 */
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public final class Introspector implements Serializable{

	private static final long serialVersionUID = 1L;
	private static Hashtable properties = new Hashtable();
	private static Hashtable rwproperties = new Hashtable();
	private static Map methodCache = new HashMap();
	private static Map constructorCache = new HashMap();
	private static Object arr1[] = new Object[1];
	private static Object arr2[] = new Object[2];
	private static Object arr3[] = new Object[3];
	private static Object arr4[] = new Object[4];
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
	private static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	public Introspector(){
	}
    /**
     * Aggiunge un elemento a una ListProperty di un oggetto.
     */
	public static int addToListProperty(Object bean, String name, Object value) throws IntrospectionException, InvocationTargetException{
		try{
			for(StringTokenizer stringtokenizer = new StringTokenizer(name, "."); stringtokenizer.hasMoreElements();){
				String s1 = stringtokenizer.nextToken();
				if(bean == null)
					return -1;
				PropertyDescriptor propertydescriptor = getProperyDescriptor(bean.getClass(), s1);
				if(propertydescriptor == null)
					throw new IntrospectionException(bean.getClass() + " non possiede la propriet\340 " + s1);
				if(stringtokenizer.hasMoreTokens())
					bean = propertydescriptor.getReadMethod().invoke(bean, null);
				else
				if(propertydescriptor instanceof ListPropertyDescriptor){
					Object obj2 = ((ListPropertyDescriptor)propertydescriptor).getAddMetod().invoke(bean, new Object[] {
						value
					});
					if(obj2 instanceof Integer)
						return ((Integer)obj2).intValue();
					else
						return -1;
				}else{
					throw new IntrospectionException("Property is not a list property");
				}
			}
			throw new IntrospectionException("Non dovrebbe mai arrivare qui!!");
		}catch(IllegalAccessException illegalaccessexception){
			throw new IllegalAccessError(illegalaccessexception.getMessage());
		}
	}
    /**
     * Costruisce al nome di un metodo un prefisso.
     */
	public static String buildMetodName(String prefix, String methodName){
		methodName = methodName.replace('.', '_');
		StringBuffer stringbuffer = new StringBuffer(prefix.length() + methodName.length());
		stringbuffer.append(prefix);
		stringbuffer.append(Character.toUpperCase(methodName.charAt(0)));
		stringbuffer.append(methodName.substring(1));
		return stringbuffer.toString();
	}

	public static String capitalize(String s){
		StringBuffer stringbuffer = new StringBuffer(s.length());
		stringbuffer.append(Character.toUpperCase(s.charAt(0)));
		stringbuffer.append(s.substring(1));
		return stringbuffer.toString();
	}

	public static Collection<?> emptyCollection(Class<?> class1) throws IntrospectionException{
		try{
			return (Collection<?>)class1.newInstance();
		}catch(InstantiationException _ex){
		}catch(IllegalAccessException _ex){
			throw new IntrospectionException("Can't instantiate the class " + class1 + " beacause the default constructor is not public");
		}
		if(java.util.Set.class == class1)
			return Collections.EMPTY_SET;
		if(java.util.Collection.class.isAssignableFrom(class1))
			return Collections.EMPTY_LIST;
		else
			throw new IntrospectionException("Can't instantiate the abstract class " + class1);
	}
    /**
     * Estrae il valore di una propriet� boolean da un oggetto.
     */
	public static boolean getBoolean(Object bean, String name, boolean defaultValue) throws IntrospectionError{
		try{
			if(name == null)
				return defaultValue;
			if(bean == null)
				return defaultValue;
			Object obj1 = getPropertyValue(bean, name);
			if(obj1 instanceof Boolean)
				return ((Boolean)obj1).booleanValue();
			if(obj1 == null)
				return defaultValue;
			else
				throw new ClassCastException();
		}catch(Exception exception){
			throw new IntrospectionError(exception);
		}
	}
    /**
     * Cerca il costruttore di una classe compatibile con un eleneco di parametri
     */
	public static Constructor<?> getConstructor(Class<?> clazz, Class<?> parameterTypes[]){
		Constructor<?> aconstructor[] = getConstructors(clazz);
		byte byte0 = -1;
		Constructor<?> constructor = null;
		allConstructor:
		for(int i = 0; i < aconstructor.length; i++){
			Constructor constructor1 = aconstructor[i];
			int j = 0;
			Class aclass1[] = constructor1.getParameterTypes();
			if(aclass1.length != parameterTypes.length)
				continue;
			for(int k = 0; k < aclass1.length; k++){
				if(aclass1[k] == parameterTypes[k]){
					j++;
					continue;
				}
				if(!aclass1[k].isAssignableFrom(parameterTypes[k]))
					continue allConstructor;
			}

			if(j > byte0)
				constructor = constructor1;
		}
		return constructor;
	}
    /**
     * Cerca il costruttore di una classe compatibile con un eleneco di parametri
     */
	public static Constructor getConstructor(Class clazz, Object parameters[]){
		Constructor aconstructor[] = getConstructors(clazz);
		byte byte0 = -1;
		Constructor constructor = null;
		allConstructor:
		for(int i = 0; i < aconstructor.length; i++){
			Constructor constructor1 = aconstructor[i];
			int j = 0;
			Class aclass[] = constructor1.getParameterTypes();
			if(aclass.length != parameters.length)
				continue;
			for(int k = 0; k < aclass.length; k++){
				if(parameters[k] == null){
					if(!aclass[i].isPrimitive())
						continue;
					continue allConstructor;
				}
				if(aclass[k] == parameters[k].getClass()){
					j++;
					continue;
				}
				if(!aclass[k].isInstance(parameters[k]))
					continue allConstructor;
			}
			if(j > byte0)
				constructor = constructor1;
		}
		return constructor;
	}
    /**
     * Cerca il costruttore di una classe con un numero di parametri specificato
     */
	public static Constructor getConstructor(Class clazz, int numParams){
		Constructor aconstructor[] = getConstructors(clazz);
		for(int j = 0; j < aconstructor.length; j++)
			if(aconstructor[j].getParameterTypes().length == numParams)
				return aconstructor[j];
		return null;
	}
    /**
     * Restituisce l'elenco dei costruttori di una classe
     */
	public static Constructor[] getConstructors(Class clazz){
		Constructor aconstructor[] = (Constructor[])constructorCache.get(clazz);
		if(aconstructor == null)
			synchronized(constructorCache){
				aconstructor = (Constructor[])constructorCache.get(clazz);
				if(aconstructor == null)
					constructorCache.put(clazz, aconstructor = clazz.getConstructors());
			}
		return aconstructor;
	}

	public static Enumeration getEnumeration(Object bean) throws IntrospectionException{
		if(bean == null)
			return EmptyEnumeration.getInstance();
		if(bean instanceof Enumeration)
			return (Enumeration)bean;
		if(bean instanceof Vector)
			return ((Vector)bean).elements();
		if(bean.getClass().isArray())
			return new ArrayEnumeration((Object[])bean);
		if(bean instanceof Dictionary)
			return ((Dictionary)bean).elements();
		if(bean instanceof Collection)
			return Collections.enumeration((Collection)bean);
		else
			return null;
	}

	public static Enumeration getEnumeration(Object bean, String property) throws IntrospectionException, InvocationTargetException{
		if(bean == null)
			return EmptyEnumeration.getInstance();
		else
			return getEnumeration(getPropertyValue(bean, property));
	}
    /**
     * Cerca un meotodo di una classe compatibile con un elenco di parametri
     */
	public static Method getMethod(Class clazz, String name, Class parameterTypes[]){
		Method amethod[] = getMethods(clazz, name);
		byte byte0 = -1;
		Method method = null;
		allMethods:
		for(int i = 0; i < amethod.length; i++){
			Method method1 = amethod[i];
			int j = 0;
			Class aclass1[] = method1.getParameterTypes();
			if(aclass1.length != parameterTypes.length)
				continue;
			for(int k = 0; k < aclass1.length; k++){
				if(aclass1[k] == parameterTypes[k]){
					j++;
					continue;
				}
				if(!aclass1[k].isAssignableFrom(parameterTypes[k]))
					continue allMethods;
			}
			if(j > byte0)
				method = method1;
		}
		return method;
	}
    /**
     * Cerca un meotodo di una classe compatibile con un elenco di parametri
     */
	public static Method getMethod(Class clazz, String name, Object parameters[]){
		Method amethod[] = getMethods(clazz, name);
		byte byte0 = -1;
		Method method = null;
		allMethods:
		for(int i = 0; i < amethod.length; i++){
			Method method1 = amethod[i];
			int j = 0;
			Class aclass[] = method1.getParameterTypes();
			if(aclass.length != parameters.length)
				continue;
			for(int k = 0; k < aclass.length; k++){
				if(parameters[k] == null){
					if(!aclass[k].isPrimitive()){
						j++;
						continue;
					}
					continue allMethods;
				}
				if(aclass[k] == parameters[k].getClass()){
					j++;
					continue;
				}
				if(aclass[k].isPrimitive() ? aclass[k] != Integer.TYPE ? aclass[k] != Long.TYPE ? aclass[k] != Float.TYPE ? aclass[k] != Double.TYPE ? aclass[k] != Boolean.TYPE ? aclass[k] != Character.TYPE ? aclass[k] != Short.TYPE ? aclass[k] == Byte.TYPE && !(parameters[k] instanceof Byte) : !(parameters[k] instanceof Short) : !(parameters[k] instanceof Character) : !(parameters[k] instanceof Boolean) : !(parameters[k] instanceof Double) : !(parameters[k] instanceof Float) : !(parameters[k] instanceof Long) : !(parameters[k] instanceof Integer) : !aclass[k].isInstance(parameters[k]))
					continue allMethods;
			}
			if(j > byte0)
				method = method1;
			if(j == parameters.length)
			  return method;	
		}
		return method;
	}
    /**
     * Cerca un metodo di una classe con un numero di parametri specificato
     */
	public static Method getMethod(Class clazz, String name, int numParams){
		Method amethod[] = getMethods(clazz, name);
		for(int j = 0; j < amethod.length; j++)
			if(amethod[j].getParameterTypes().length == numParams)
				return amethod[j];
		return null;
	}
    /**
     * Cerca un metodo di una classe
     */
	@SuppressWarnings("unchecked")
	public static Method[] getMethods(Class clazz, String name){
		Object obj = (Map)methodCache.get(clazz);
		if(obj == null)
			synchronized(methodCache){
				obj = (Map)methodCache.get(clazz);
				if(obj == null)
					methodCache.put(clazz, obj = new HashMap());
			}
		Method amethod[] = (Method[])((Map) (obj)).get(name);
		if(amethod == null)
			synchronized(obj){
				if(amethod == null){
					amethod = (Method[])((Map<?, ?>) (obj)).get(name);
					Method amethod1[] = clazz.getMethods();
					ArrayList<Method> arraylist = new ArrayList<Method>(amethod1.length);
					for(int i = 0; i < amethod1.length; i++)
						if(amethod1[i].getName().equals(name))
							arraylist.add(amethod1[i]);
					amethod = new Method[arraylist.size()];
					((Map) (obj)).put(name, ((Object) (arraylist.toArray(amethod))));
				}
			}
		return amethod;
	}
    /**
     * Restituisce il tipo di una propriet�
     */
	public static Class getPropertyType(Class clazz, String name) throws IntrospectionException{
		if(name == null)
			return clazz;
		for(StringTokenizer stringtokenizer = new StringTokenizer(name, "."); stringtokenizer.hasMoreElements();){
			String s1 = stringtokenizer.nextToken();
			PropertyDescriptor propertydescriptor = getProperyDescriptor(clazz, s1);
			if(propertydescriptor == null)
				throw new IntrospectionException(clazz + " non possiede la propriet\340 " + s1);
			clazz = propertydescriptor.getPropertyType();
		}
		return clazz;
	}
    /**
     * Legge il valore di una propriet� in un oggetto. 
     * Se la propriet� � una propriet� composta restituisce null se uno qualsiasi dei valori parziali della 
     * propriet� vale null.
     */
	public static Object getPropertyValue(Object bean, String name) throws IntrospectionException, InvocationTargetException{
		if(name == null)
			return bean;
		try{
			for(StringTokenizer stringtokenizer = new StringTokenizer(name, "."); stringtokenizer.hasMoreElements();){
				String s1 = stringtokenizer.nextToken();
				if(bean == null)
					return null;
				PropertyDescriptor propertydescriptor = getProperyDescriptor(bean.getClass(), s1);
				if(propertydescriptor == null)
					try{
						Field field = bean.getClass().getField(name);
						bean = field.get(bean);
					}catch(NoSuchFieldException _ex){
						if(bean instanceof SelfIntrospector)
							return ((SelfIntrospector)bean).getPropertyValue(name);
						else
							throw new IntrospectionException(bean.getClass() + " non possiede la propriet\340 " + s1);
					}
				else
					bean = propertydescriptor.getReadMethod().invoke(bean, null);
			}
		}catch(IllegalAccessException illegalaccessexception){
			throw new IllegalAccessError(illegalaccessexception.getMessage());
		}
		return bean;
	}
    /**
     * Legge il valore di una propriet� in un oggetto applicando un formato. 
     * Se il valore della propriet� � null restituisce null. 
     * Se il formato specficato � nullo restituisce il risultato di toString applicato al valore della propriet�.
     */
	public static String getPropertyValue(Object bean, String name, Format format) throws IntrospectionException, InvocationTargetException{
		Object obj1 = getPropertyValue(bean, name);
		if(obj1 == null)
			return null;
		if(format == null)
			return obj1.toString();
		else
			return format.format(obj1);
	}

	private static PropertyDescriptor getProperyDescriptor(Class clazz, String name) throws IntrospectionException{
		return (PropertyDescriptor)getProperyDescriptors(clazz).get(name);
	}

	private static Hashtable getProperyDescriptors(Class clazz) throws IntrospectionException{
		Hashtable hashtable = (Hashtable)properties.get(clazz);
		if(hashtable == null)
			synchronized(properties){
				hashtable = (Hashtable)properties.get(clazz);
				if(hashtable == null){
					PropertyDescriptor apropertydescriptor[] = java.beans.Introspector.getBeanInfo(clazz).getPropertyDescriptors();
					properties.put(clazz, hashtable = new Hashtable());
					for(int i = 0; i < apropertydescriptor.length; i++)
						try{
							hashtable.put(apropertydescriptor[i].getName(), new ListPropertyDescriptor(apropertydescriptor[i], clazz));
						}catch(IntrospectionException _ex){
							hashtable.put(apropertydescriptor[i].getName(), apropertydescriptor[i]);
						}
					properties.put(clazz, hashtable);
				}
			}
		return hashtable;
	}
    /**
     * Restituisce un array contenente l'elenco dei nomi di tutte le propriet� di una classe che sono contemporaneamente leggibili e scrivibili. L'elenco comprende i nomi di tutti i field pubblici e di tutte le propriet� per cui esiste sia un getter che un setter.
     */
	public static String[] getReadWriteProperties(Class clazz) throws IntrospectionException{
		String as[] = (String[])rwproperties.get(clazz);
		if(as == null)
			synchronized(rwproperties){
				as = (String[])rwproperties.get(clazz);
				if(as == null){
					Vector vector = new Vector();
					Hashtable hashtable1 = getProperyDescriptors(clazz);
					for(Enumeration enumeration = hashtable1.elements(); enumeration.hasMoreElements();){
						PropertyDescriptor propertydescriptor = (PropertyDescriptor)enumeration.nextElement();
						if(propertydescriptor.getReadMethod() != null && propertydescriptor.getWriteMethod() != null)
							vector.addElement(propertydescriptor.getName());
					}
					Field afield[] = clazz.getFields();
					for(int i = 0; i < afield.length; i++)
						if(Modifier.isPublic(afield[i].getModifiers()) && !Modifier.isStatic(afield[i].getModifiers()))
							vector.addElement(afield[i].getName());
					as = new String[vector.size()];
					vector.copyInto(as);
					rwproperties.put(clazz, as);
				}
			}
		return as;
	}

	public static boolean hasProperty(Class clazz, String name){
		if(name == null)
			return false;
		try{
			for(StringTokenizer stringtokenizer = new StringTokenizer(name, "."); stringtokenizer.hasMoreElements();){
				String s1 = stringtokenizer.nextToken();
				PropertyDescriptor propertydescriptor = getProperyDescriptor(clazz, s1);
				if(propertydescriptor == null)
					return false;
				clazz = propertydescriptor.getPropertyType();
			}
			return true;
		}catch(IntrospectionException _ex){
			return false;
		}
	}
    /**
     * Invoca un metodo su un'oggetto. Il metodo da invocare viene selezionato in base al nome e al tipo 
     * effettivo dei parametri passati.
     */
	public static Object invoke(Object receiver, String methodName, Object args[]) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		Method method = getMethod(receiver.getClass(), methodName, args);
		if(method == null)
			throw new NoSuchMethodException("No method " + methodName + " in class " + receiver.getClass() + " with parameters specified.");
		else
			return method.invoke(receiver, args);
	}
    /**
     * Invoca un metodo su un'oggetto. Il metodo da invocare viene selezionato in base al nome e al tipo 
     * effettivo dei parametri passati.
     */
	public static synchronized Object invoke(Object receiver, String methodName, Object args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		arr1[0] = args;
		Method method = getMethod(receiver.getClass(), methodName, arr1);
		if(method == null)
			throw new NoSuchMethodException("No method " + methodName + " in class " + receiver.getClass() + " with parameters specified.");
		else
			return method.invoke(receiver, arr1);
	}
    /**
     * Invoca un metodo su un'oggetto. 
     * Il metodo da invocare viene selezionato in base al nome e al tipo effettivo dei parametri passati.
     */
	public static synchronized Object invoke(Object receiver, String methodName, Object arg1, Object arg2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		arr2[0] = arg1;
		arr2[1] = arg2;
		Method method = getMethod(receiver.getClass(), methodName, arr2);
		if(method == null)
			throw new NoSuchMethodException("No method " + methodName + " in class " + receiver.getClass() + " with parameters specified.");
		else
			return method.invoke(receiver, arr2);
	}
    /**
     * Invoca un metodo su un'oggetto. 
     * Il metodo da invocare viene selezionato in base al nome e al tipo effettivo dei parametri passati.
     */
	public static synchronized Object invoke(Object receiver, String methodName, Object arg1, Object arg2, Object arg3) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		arr3[0] = arg1;
		arr3[1] = arg2;
		arr3[2] = arg3;
		Method method = getMethod(receiver.getClass(), methodName, arr3);
		if(method == null)
			throw new NoSuchMethodException("No method " + methodName + " in class " + receiver.getClass() + " with parameters specified.");
		else
			return method.invoke(receiver, arr3);
	}
    /**
     * Invoca un metodo su un'oggetto. 
     * Il metodo da invocare viene selezionato in base al nome e al tipo effettivo dei parametri passati.
     */
	public static synchronized Object invoke(Object receiver, String methodName, Object arg1, Object arg2, Object arg3, Object arg4) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		arr4[0] = arg1;
		arr4[1] = arg2;
		arr4[2] = arg3;
		arr4[3] = arg4;
		Method method = getMethod(receiver.getClass(), methodName, arr4);
		if(method == null)
			throw new NoSuchMethodException("No method " + methodName + " in class " + receiver.getClass() + " with parameters specified.");
		else
			return method.invoke(receiver, arr4);
	}
    /**
     * Invoca un metodo su un'oggetto. 
     * Il metodo da invocare viene selezionato in base al nome e al tipo effettivo dei parametri passati.
     */
	public static Object invoke(Object receiver, String prefix, String methodName, Object args[]) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		return invoke(receiver, buildMetodName(prefix, methodName), args);
	}
    /**
     * Invoca un metodo su un'oggetto. 
     * Il metodo da invocare viene selezionato in base al nome e al tipo effettivo dei parametri passati.
     */
	public static synchronized Object invoke(Object receiver, String prefix, String methodName, Object arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		return invoke(receiver, buildMetodName(prefix, methodName), arg);
	}
    /**
     * Invoca un metodo su un'oggetto. 
     * Il metodo da invocare viene selezionato in base al nome e al tipo effettivo dei parametri passati.
     */
	public static synchronized Object invoke(Object receiver, String prefix, String methodName, Object arg1, Object arg2) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		return invoke(receiver, buildMetodName(prefix, methodName), arg1, arg2);
	}
    /**
     * Invoca un metodo su un'oggetto. 
     * Il metodo da invocare viene selezionato in base al nome e al tipo effettivo dei parametri passati.
     */
	public static synchronized Object invoke(Object receiver, String prefix, String methodName, Object arg1, Object arg2, Object arg3) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		return invoke(receiver, buildMetodName(prefix, methodName), arg1, arg2, arg3);
	}
    /**
     * Invoca un metodo su un'oggetto. 
     * Il metodo da invocare viene selezionato in base al nome e al tipo effettivo dei parametri passati.
     */
	public static synchronized Object invoke(Object receiver, String prefix, String methodName, Object arg1, Object arg2, Object arg3, Object arg4) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		return invoke(receiver, buildMetodName(prefix, methodName), arg1, arg2, arg3, arg4);
	}
    /**
     * Restituisce se una propriet� di un oggetto � scrivibile
     */
	public static boolean isPropertyWriteable(Object bean, String name) throws IntrospectionException, InvocationTargetException{
		if(name == null)
			return false;
		try{
			for(StringTokenizer stringtokenizer = new StringTokenizer(name, "."); stringtokenizer.hasMoreElements();){
				String s1 = stringtokenizer.nextToken();
				if(bean == null)
					return false;
				PropertyDescriptor propertydescriptor = getProperyDescriptor(bean.getClass(), s1);
				if(propertydescriptor == null)
					try{
						Field field = bean.getClass().getField(name);
						bean = field.get(bean);
					}catch(NoSuchFieldException _ex){
						if(bean instanceof SelfIntrospector)
							bean = ((SelfIntrospector)bean).getPropertyValue(name);
						throw new IntrospectionException(bean.getClass() + " non possiede la propriet\340 " + s1);
					}
				else
					if(stringtokenizer.hasMoreElements())
						bean = propertydescriptor.getReadMethod().invoke(bean, null);
					else
						return propertydescriptor.getWriteMethod() != null;
			}

		}catch(IllegalAccessException illegalaccessexception){
			throw new IllegalAccessError(illegalaccessexception.getMessage());
		}
		return true;
	}
    /**
     * Restituisce se una propriet� di un oggetto � un lista ordinabile
     */
	public static boolean isSortableProperty(Class clazz, String name) throws IntrospectionException{
		if(name == null)
			return false;
		PropertyDescriptor propertydescriptor = null;
		for(StringTokenizer stringtokenizer = new StringTokenizer(name, "."); stringtokenizer.hasMoreElements();){
			String s1 = stringtokenizer.nextToken();
			propertydescriptor = getProperyDescriptor(clazz, s1);
			if(propertydescriptor == null)
				return false;
			clazz = propertydescriptor.getPropertyType();
		}
		return (propertydescriptor instanceof ListPropertyDescriptor) && ((ListPropertyDescriptor)propertydescriptor).getSortMethod() != null;
	}

	public static Collection newCollection(Class clazz) throws IntrospectionException{
		try{
			return (Collection)clazz.newInstance();
		}
		catch(InstantiationException _ex){
		}catch(IllegalAccessException _ex){
			throw new IntrospectionException("Can't instantiate the class " + clazz + " beacause the default constructor is not public");
		}
		if(java.util.Set.class == clazz)
			return new HashSet();
		if(java.util.Collection.class.isAssignableFrom(clazz))
			return new LinkedList();
		else
			throw new IntrospectionException("Can't instantiate the abstract class " + clazz);
	}
    /**
     * Costruisce una nuova istanza di una classe.
     */
	public static Object newInstance(Class clazz, Object args[]) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException{
		Constructor constructor = getConstructor(clazz, args);
		if(constructor == null)
			throw new NoSuchMethodException("No constructor in class " + clazz + " with parameters specified.");
		else
			return constructor.newInstance(args);
	}
    /**
     * Costruisce una nuova istanza di una classe.
     */
	public static synchronized Object newInstance(Class clazz, Object arg) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException{
		arr1[0] = arg;
		Constructor constructor = getConstructor(clazz, arr1);
		if(constructor == null)
			throw new NoSuchMethodException("No constructor in class " + clazz + " with parameters specified.");
		else
			return constructor.newInstance(arr1);
	}
    /**
     * Costruisce una nuova istanza di una classe.
     */
	public static synchronized Object newInstance(Class clazz, Object arg1, Object arg2) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException{
		arr2[0] = arg1;
		arr2[1] = arg2;
		Constructor constructor = getConstructor(clazz, arr2);
		if(constructor == null)
			throw new NoSuchMethodException("No constructor in class " + clazz + " with parameters specified.");
		else
			return constructor.newInstance(arr2);
	}
    /**
     * Costruisce una nuova istanza di una classe.
     */
	public static synchronized Object newInstance(Class clazz, Object arg1, Object arg2, Object arg3) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException{
		arr3[0] = arg1;
		arr3[1] = arg2;
		arr3[2] = arg3;
		Constructor constructor = getConstructor(clazz, arr3);
		if(constructor == null)
			throw new NoSuchMethodException("No constructor in class " + clazz + " with parameters specified.");
		else
			return constructor.newInstance(arr3);
	}
    /**
     * Costruisce una nuova istanza di una classe.
     */
	public static synchronized Object newInstance(Class clazz, Object arg1, Object arg2, Object arg3, Object arg4) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException{
		arr4[0] = arg1;
		arr4[1] = arg2;
		arr4[2] = arg3;
		arr4[3] = arg4;
		Constructor constructor = getConstructor(clazz, arr4);
		if(constructor == null)
			throw new NoSuchMethodException("No constructor in class " + clazz + " with parameters specified.");
		else
			return constructor.newInstance(arr4);
	}
    /**
     * Rimuove un elemento da una ListProperty di un oggetto.
     */
	public static Object removeFromListProperty(Object bean, String name, int index) throws IntrospectionException, InvocationTargetException{
		try{
			for(StringTokenizer stringtokenizer = new StringTokenizer(name, "."); stringtokenizer.hasMoreElements();){
				String s1 = stringtokenizer.nextToken();
				if(bean == null)
					return null;
				PropertyDescriptor propertydescriptor = getProperyDescriptor(bean.getClass(), s1);
				if(propertydescriptor == null)
					throw new IntrospectionException(bean.getClass() + " non possiede la propriet\340 " + s1);
				if(stringtokenizer.hasMoreTokens())
					bean = propertydescriptor.getReadMethod().invoke(bean, null);
				else
				if(propertydescriptor instanceof ListPropertyDescriptor)
					return ((ListPropertyDescriptor)propertydescriptor).getRemoveMetod().invoke(bean, new Object[] {
						new Integer(index)
					});
				else
					throw new IntrospectionException("Property is not a list property");
			}
			throw new IntrospectionException("Non dovrebbe mai arrivare qui!!");
		}catch(IllegalAccessException illegalaccessexception){
			throw new IllegalAccessError(illegalaccessexception.getMessage());
		}
	}
    /**
     * Pulisce la cache dei metodi dell'Introspector
     */
	public static void resetPropertiesCache(){
		synchronized(properties){
			properties.clear();
		}
		synchronized(rwproperties){
			rwproperties.clear();
		}
		synchronized(methodCache){
			methodCache.clear();
		}
		synchronized(constructorCache){
			constructorCache.clear();
		}
	}
    /**
     * Pulisce la cache dei metodi dell'Introspector
     */
	public static void resetPropertiesCache(Class clazz){
		synchronized(properties){
			properties.remove(clazz);
		}
		synchronized(rwproperties){
			rwproperties.remove(clazz);
		}
		synchronized(methodCache){
			methodCache.remove(clazz);
		}
		synchronized(constructorCache){
			constructorCache.remove(clazz);
		}
	}
    /**
     * Scrive il valore di una propriet� in un oggetto. 
     * Il valore da scrivere nella propriet� viene ottenuto mediante 
     * Se la propriet� � composta estrae il valore parziale della propriet� fino alla penultima propriet� semplice, 
     * quindi vi scrive il valore nell'ultima propriet�; 
     * ad esempio se la propriet� � "anagrafica.indirizzo.via" estrae il valore di "anagrafica.indirizzo" e, 
     * se quest'ultimo � non nullo, vi scrive la propriet� "via".
     */
	public static void setPropertyValue(Object bean, String name, Object value) throws IntrospectionException, InvocationTargetException{
		try{
			for(StringTokenizer stringtokenizer = new StringTokenizer(name, "."); stringtokenizer.hasMoreElements();){
				String s1 = stringtokenizer.nextToken();
				if(bean == null)
					return;
				PropertyDescriptor propertydescriptor = getProperyDescriptor(bean.getClass(), s1);
				if(propertydescriptor == null)
					try{
						Field field = bean.getClass().getField(name);
						if(stringtokenizer.hasMoreTokens())
							bean = field.get(bean);
						else
							field.set(bean, value);
					}catch(NoSuchFieldException _ex){
						throw new IntrospectionException(bean.getClass() + " non possiede la propriet\340 " + s1);
					}
				else
					if(stringtokenizer.hasMoreTokens())
						bean = propertydescriptor.getReadMethod().invoke(bean, null);
					else{
						if (propertydescriptor.getWriteMethod() != null)
							propertydescriptor.getWriteMethod().invoke(bean, new Object[] {value});
						else{
							getMethod(bean.getClass(), "set"+capitalize(propertydescriptor.getName()), 1).invoke(bean, new Object[] {value});
						}
						
					}
			}

		}catch(IllegalAccessException illegalaccessexception){
			throw new IllegalAccessError(illegalaccessexception.getMessage());
		}
	}
    /**
     * Scrive il valore di una propriet� in un oggetto. 
     * Il valore da scrivere nella propriet� viene ottenuto mediante 
     * Se la propriet� � composta estrae il valore parziale della propriet� fino alla penultima propriet� semplice, 
     * quindi vi scrive il valore nell'ultima propriet�; ad esempio se la propriet� � "anagrafica.indirizzo.via" 
     * estrae il valore di "anagrafica.indirizzo" e, se quest'ultimo � non nullo, vi scrive la propriet� "via".
     */
	/*
	public static void setPropertyValue(Object bean, String name, String value) throws IntrospectionException, InvocationTargetException, ParseException{
		Class class1 = getPropertyType(bean.getClass(), name);
		if(value == null && class1.isPrimitive()){
			return;
		}else{
			setPropertyValue(bean, name, standardParse(value, class1));
			return;
		}
	}
	*/
    /**
     * Scrive il valore di una propriet� in un oggetto. 
     * Il valore da scrivere nella propriet� viene ottenuto mediante Se la propriet� � composta estrae il 
     * valore parziale della propriet� fino alla penultima propriet� semplice, 
     * quindi vi scrive il valore nell'ultima propriet�; ad esempio se la propriet� � "anagrafica.indirizzo.via" 
     * estrae il valore di "anagrafica.indirizzo" e, se quest'ultimo � non nullo, vi scrive la propriet� "via".
     */
	public static void setPropertyValue(Object bean, String name, String value, Format format) throws IntrospectionException, InvocationTargetException, ParseException{
		try{
			for(StringTokenizer stringtokenizer = new StringTokenizer(name, "."); stringtokenizer.hasMoreElements();){
				String s2 = stringtokenizer.nextToken();
				if(bean == null)
					return;
				PropertyDescriptor propertydescriptor = getProperyDescriptor(bean.getClass(), s2);
				if(propertydescriptor == null)
					throw new IntrospectionException(bean.getClass() + " non possiede la propriet\340 " + s2);
				if(stringtokenizer.hasMoreTokens()){
					bean = propertydescriptor.getReadMethod().invoke(bean, null);
				}else{
					Object obj1 = value;
					if(obj1 != null)
						obj1 = format.parseObject(value);
					propertydescriptor.getWriteMethod().invoke(bean, new Object[] {
						obj1
					});
				}
			}
		}catch(IllegalAccessException illegalaccessexception){
			throw new IllegalAccessError(illegalaccessexception.getMessage());
		}
	}

	public static List sort(Object bean, String name, Comparator comparator) throws IntrospectionException, InvocationTargetException{
		try{
			for(StringTokenizer stringtokenizer = new StringTokenizer(name, "."); stringtokenizer.hasMoreElements();){
				String s1 = stringtokenizer.nextToken();
				if(bean == null)
					return null;
				PropertyDescriptor propertydescriptor = getProperyDescriptor(bean.getClass(), s1);
				if(propertydescriptor == null)
					throw new IntrospectionException(bean.getClass() + " non possiede la propriet\340 " + s1);
				if(stringtokenizer.hasMoreTokens()){
					bean = propertydescriptor.getReadMethod().invoke(bean, null);
				}else{
					if((propertydescriptor instanceof ListPropertyDescriptor) && ((ListPropertyDescriptor)propertydescriptor).getSortMethod() != null){
						Method method = ((ListPropertyDescriptor)propertydescriptor).getSortMethod();
						Object obj1 = method.invoke(bean, new Object[] {
							comparator
						});
						if(obj1 instanceof List)
							return (List)obj1;
						else
							return null;
					}
					bean = propertydescriptor.getReadMethod().invoke(bean, null);
					if(bean instanceof Sortable){
						((Sortable)bean).sort(comparator);
						return (List)bean;
					}
					if(bean instanceof List){
						Collections.sort((List)bean, comparator);
						return (List)bean;
					}
				}
				throw new IntrospectionException("Property is not a list property");
			}
			throw new IntrospectionException("Non dovrebbe mai arrivare qui!!");
		}catch(IllegalAccessException illegalaccessexception){
			throw new IllegalAccessError(illegalaccessexception.getMessage());
		}
	}
    /**
     * Effettua una conversione standard (se possibile) di un oggetto in un altro tipo. 
     * La conversione viene effettuata: da qualsiasi oggetto a una string (tramite toString()) 
     * da java.util.Date a java.sql.Date, java.sql.Time, java.sql.Timestamp da un istanza di Number a Float, 
     * Integer,Double,Short, Long,Byte,BigDecimal
     */
	public static Object standardConvert(Object value, Class type)
	{
		if(value == null)
			return null;
		if(type == java.lang.String.class)
			return value.toString();
		if(type == java.sql.Timestamp.class && (value instanceof Date))
			return new Timestamp(((Date)value).getTime());
		if(type == java.sql.Date.class && (value instanceof Date))
			return new java.sql.Date(((Date)value).getTime());
		if(type == java.sql.Time.class && (value instanceof Date))
			return new Time(((Date)value).getTime());
		if(type == java.lang.Integer.class && (value instanceof Number))
			return new Integer(((Number)value).intValue());
		if(type == java.lang.Float.class && (value instanceof Number))
			return new Float(((Number)value).floatValue());
		if(type == java.lang.Double.class && (value instanceof Number))
			return new Double(((Number)value).doubleValue());
		if(type == java.lang.Short.class && (value instanceof Number))
			return new Short(((Number)value).shortValue());
		if(type == java.lang.Long.class && (value instanceof Number))
			return new Long(((Number)value).longValue());
		if(type == java.lang.Byte.class && (value instanceof Number))
			return new Byte(((Number)value).byteValue());
		if(type == java.math.BigDecimal.class && (value instanceof Number)){
			return new BigDecimal(value.toString());
		}
		else
			return value;
	}

	public static String standardFormat(Object value){
		if(value == null)
			return "";
		if(value instanceof Boolean)
			return Boolean.TRUE.equals(value) ? "Si" : "No";
		if(value instanceof Date)
			return TIMESTAMP_FORMAT.format(value);
		if(value instanceof Time)
			return TIME_FORMAT.format(value);
		if(value instanceof Timestamp)
			return TIMESTAMP_FORMAT.format(value);
		if(value instanceof java.sql.Date)
			return DATE_FORMAT.format(value);
		else
			return value.toString();
	}
    /**
     * Effettua una conversione standard (se possibile) di una stringa in un altro tipo.
     */
	public static Object standardParse(String s, Class class1) throws ParseException{
		Object obj = null;
		if(class1 == java.lang.String.class)
			obj = s;
		else
		if(s == null){
			if(class1.isPrimitive())
				throw new ParseException("Can't convert null String to a primitive type", 0);
			obj = null;
		} else
			if(class1 == java.lang.Boolean.class || class1 == Boolean.TYPE){
				if(s == null || s.length() == 0){
					obj = Boolean.FALSE;
				}else{
					char c = Character.toUpperCase(s.charAt(0));
					obj = new Boolean(c == 'Y' || c == 'T' || c == 'S' || c == 'V' || c == '1');
				}
			}else
				if(class1 == java.lang.Character.class || class1 == Character.TYPE)
					obj = new Character(s.length() != 0 ? s.charAt(0) : '\0');
				else
					try{
						if(class1 == java.lang.Integer.class || class1 == Integer.TYPE)
							obj = Integer.valueOf(s);
						else
							if(class1 == java.lang.Float.class || class1 == Float.TYPE)
								obj = Float.valueOf(s);
							else
								if(class1 == java.lang.Double.class || class1 == Double.TYPE)
									obj = Double.valueOf(s);
								else
									if(class1 == java.lang.Long.class || class1 == Long.TYPE)
										obj = Long.valueOf(s);
									else
										if(class1 == java.lang.Short.class || class1 == Short.TYPE)
											obj = Short.valueOf(s);
										else
											if(class1 == java.math.BigDecimal.class)
												obj = new BigDecimal(s);
											if(class1 == java.util.Date.class)
												obj = TIMESTAMP_FORMAT.parse(s);
											if(class1 == java.sql.Time.class)
												obj = new Time(TIME_FORMAT.parse(s).getTime());
											if(class1 == java.sql.Timestamp.class)
												obj = new Timestamp(TIMESTAMP_FORMAT.parse(s).getTime());
											if(class1 == java.sql.Date.class)
												obj = new java.sql.Date(DATE_FORMAT.parse(s).getTime());
					} catch(NumberFormatException _ex){
						if(class1.isPrimitive() || s.trim().length() > 0)
							throw new ParseException("The conversion of \"" + s + "\" is a null Object, but the property is primitive", 0);
					}
		if(class1.isPrimitive() && obj == null)
			throw new ParseException("The conversion of \"" + s + "\" is a null Object, but the property is primitive", 0);
		else
			return obj;
	}
}