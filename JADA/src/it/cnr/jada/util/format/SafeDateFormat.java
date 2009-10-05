/*
* Copyright 2008-2009 Italian National Research Council
* 	All rights reserved
*/
package it.cnr.jada.util.format;

import java.text.*;
import java.util.*;
/**
 * 
 * @author mspasiano
 * @version 1.0
 * @since October 2009
 */
public class SafeDateFormat extends Format{

	private static final long serialVersionUID = 1L;
	private DateFormat dateFormat;
	private static final Date MAX_DATE;
	private static final Date MAXIMUM_DATE;
	private static final Date MINIMUM_DATE;
	public static final int FULL = 0;
	public static final int LONG = 1;
	public static final int MEDIUM = 2;
	public static final int SHORT = 3;
	public static final int DEFAULT = 2;

	static{
		Calendar calendar = Calendar.getInstance();
		calendar.set(1, 9999);
		calendar.set(2, 11);
		calendar.set(5, 31);
		calendar.set(11, 23);
		calendar.set(12, 59);
		calendar.set(13, 59);
		calendar.set(14, 999);
		calendar.getTime();
		MAX_DATE = calendar.getTime();
        
		Calendar Maxcalendar = Calendar.getInstance();
		Maxcalendar.set(Calendar.YEAR,3001);
		Maxcalendar.set(Calendar.MONTH,Calendar.JANUARY);
		Maxcalendar.set(Calendar.DAY_OF_MONTH,1);
		Maxcalendar.set(Calendar.HOUR_OF_DAY,0);
		Maxcalendar.set(Calendar.MINUTE,0);
		Maxcalendar.set(Calendar.SECOND,0);		
		MAXIMUM_DATE = Maxcalendar.getTime();
		
		Calendar Mincalendar = Calendar.getInstance();
		Mincalendar.set(Calendar.YEAR,1849);
		Mincalendar.set(Calendar.MONTH,Calendar.DECEMBER);
		Mincalendar.set(Calendar.DAY_OF_MONTH,31);
		Mincalendar.set(Calendar.HOUR_OF_DAY,23);
		Mincalendar.set(Calendar.MINUTE,59);
		Mincalendar.set(Calendar.SECOND,59);			
		MINIMUM_DATE = Mincalendar.getTime();	
	}
	
    public SafeDateFormat(){
        this(((DateFormat) (new SimpleDateFormat())));
    }

    public SafeDateFormat(String pattern){
        this(((DateFormat) (new SimpleDateFormat(pattern))));
    }

    public SafeDateFormat(String pattern, DateFormatSymbols formatData){
        this(((DateFormat) (new SimpleDateFormat(pattern, formatData))));
    }

    public SafeDateFormat(String pattern, Locale locale){
        this(((DateFormat) (new SimpleDateFormat(pattern, locale))));
    }

    public SafeDateFormat(DateFormat formatData){
        dateFormat = formatData;
    }

    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos){
        return dateFormat.format(obj, toAppendTo, pos);
    }

    public static final SafeDateFormat getDateInstance(){
        return new SafeDateFormat(DateFormat.getDateInstance());
    }

    public static final SafeDateFormat getDateInstance(int style){
        return new SafeDateFormat(DateFormat.getDateInstance(style));
    }

    public static final SafeDateFormat getDateInstance(int style, Locale locale){
        return new SafeDateFormat(DateFormat.getDateInstance(style, locale));
    }

    public static final SafeDateFormat getDateTimeInstance(){
        return new SafeDateFormat(DateFormat.getDateTimeInstance());
    }

    public static final SafeDateFormat getDateTimeInstance(int dateStyle, int timeStyle){
        return new SafeDateFormat(DateFormat.getDateTimeInstance(dateStyle, timeStyle));
    }

    public static final SafeDateFormat getDateTimeInstance(int dateStyle, int timeStyle, Locale locale){
        return new SafeDateFormat(DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale));
    }

    public static final SafeDateFormat getTimeInstance(){
        return new SafeDateFormat(DateFormat.getTimeInstance());
    }

    public static final SafeDateFormat getTimeInstance(int style){
        return new SafeDateFormat(DateFormat.getTimeInstance(style));
    }

    public static final SafeDateFormat getTimeInstance(int style, Locale locale){
        return new SafeDateFormat(DateFormat.getTimeInstance(style, locale));
    }
	/**
	 * Tell whether date/time parsing is to be lenient. 
	 */
    public boolean isLenient(){
        return dateFormat.isLenient();
    }

    public Date parse(String text) throws ParseException{
        return validateDate(dateFormat.parse(text));
    }

    public Date parse(String text, ParsePosition pos){
        return dateFormat.parse(text, pos);
    }

    public synchronized Object parseObject(String text) throws ParseException{
		return validateDate((Date)dateFormat.parseObject(text));
    }

    public synchronized Object parseObject(String text, ParsePosition pos){
        return dateFormat.parseObject(text, pos);
    }

    /**
     * Specify whether or not date/time parsing is to be lenient. 
     * With lenient parsing, the parser may use heuristics to interpret inputs that do not precisely match this 
     * object's format. With strict parsing, inputs must match this object's format. 
     */
    public void setLenient(boolean flag){
        dateFormat.setLenient(flag);
    }

    private Date validateDate(Date date) throws ParseException{
		validateMaxMinDate(date);
        if(date.after(MAX_DATE))
            throw new it.cnr.jada.bulk.ValidationParseException("Data/ora non valida.", 0);
        else
            return date;
    }

    @SuppressWarnings("unused")
	private Date validateDate(Date date, ParsePosition parseposition) throws ParseException{
		validateMaxMinDate(date);
        if(date.after(MAX_DATE))
            throw new it.cnr.jada.bulk.ValidationParseException("Data/ora non valida.", parseposition.getIndex());
        else
            return date;
    }
	private void validateMaxMinDate(Date date) throws ParseException{
		if(date.after(MAXIMUM_DATE) || date.before(MINIMUM_DATE))
		  throw new it.cnr.jada.bulk.ValidationParseException("Data/ora non compresa tra il 01/01/1850 e il 31/12/3000.", 0);
	}
}