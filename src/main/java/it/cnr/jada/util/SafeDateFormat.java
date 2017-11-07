package it.cnr.jada.util;

import it.cnr.jada.bulk.ValidationParseException;

import java.text.*;
import java.util.*;

public class SafeDateFormat extends Format {

	private DateFormat dateFormat;
	private String format;
	private static final Date MAX_DATE;
	private static final Date MAXIMUM_DATE;
	private static final Date MINIMUM_DATE;
	public static final int FULL = 0;
	public static final int LONG = 1;
	public static final int MEDIUM = 2;
	public static final int SHORT = 3;
	public static final int DEFAULT = 2;

	static 
	{
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
	
    public SafeDateFormat()
    {

        this(((DateFormat) (new SimpleDateFormat())));
    }

    public SafeDateFormat(String s)
    {
        this(((DateFormat) (new SimpleDateFormat(s))));
        this.format = s;
    }

    public SafeDateFormat(String s, DateFormatSymbols dateformatsymbols)
    {
        this(((DateFormat) (new SimpleDateFormat(s, dateformatsymbols))));
        this.format = s;
    }

    public SafeDateFormat(String s, Locale locale)
    {

        this(((DateFormat) (new SimpleDateFormat(s, locale))));
        this.format = s;
    }

    public SafeDateFormat(DateFormat dateformat)
    {

        dateFormat = dateformat;
    }

    public StringBuffer format(Object obj, StringBuffer stringbuffer, FieldPosition fieldposition)
    {
        return dateFormat.format(obj, stringbuffer, fieldposition);
    }

    public static final SafeDateFormat getDateInstance()
    {
        return new SafeDateFormat(DateFormat.getDateInstance());
    }

    public static final SafeDateFormat getDateInstance(int i)
    {
        return new SafeDateFormat(DateFormat.getDateInstance(i));
    }

    public static final SafeDateFormat getDateInstance(int i, Locale locale)
    {
        return new SafeDateFormat(DateFormat.getDateInstance(i, locale));
    }

    public static final SafeDateFormat getDateTimeInstance()
    {
        return new SafeDateFormat(DateFormat.getDateTimeInstance());
    }

    public static final SafeDateFormat getDateTimeInstance(int i, int j)
    {
        return new SafeDateFormat(DateFormat.getDateTimeInstance(i, j));
    }

    public static final SafeDateFormat getDateTimeInstance(int i, int j, Locale locale)
    {
        return new SafeDateFormat(DateFormat.getDateTimeInstance(i, j, locale));
    }

    public static final SafeDateFormat getTimeInstance()
    {
        return new SafeDateFormat(DateFormat.getTimeInstance());
    }

    public static final SafeDateFormat getTimeInstance(int i)
    {
        return new SafeDateFormat(DateFormat.getTimeInstance(i));
    }

    public static final SafeDateFormat getTimeInstance(int i, Locale locale)
    {
        return new SafeDateFormat(DateFormat.getTimeInstance(i, locale));
    }

    public boolean isLenient()
    {
        return dateFormat.isLenient();
    }

    public Date parse(String s)
        throws ParseException
    {
        return validateDate(dateFormat.parse(s));
    }

    public String getFormat() {
        return format;
    }

    public Date parse(String s, ParsePosition parseposition)
    {
        return dateFormat.parse(s, parseposition);
    }

    public synchronized Object parseObject(String s)
        throws ParseException
    {
		return validateDate((Date)dateFormat.parseObject(s));
    }

    public synchronized Object parseObject(String s, ParsePosition parseposition)
    {
        return dateFormat.parseObject(s, parseposition);
    }

    public void setLenient(boolean flag)
    {
        dateFormat.setLenient(flag);
    }

    private Date validateDate(Date date)
        throws ParseException
    {
		validateMaxMinDate(date);
        if(date.after(MAX_DATE))
            throw new ValidationParseException("Data/ora non valida.", 0);
        else
            return date;
    }

    private Date validateDate(Date date, ParsePosition parseposition)
        throws ParseException
    {
		validateMaxMinDate(date);
        if(date.after(MAX_DATE))
            throw new ValidationParseException("Data/ora non valida.", parseposition.getIndex());
        else
            return date;
    }
	private void validateMaxMinDate(Date date) throws ParseException{
		if(date.after(MAXIMUM_DATE) || date.before(MINIMUM_DATE))
		  throw new ValidationParseException("Data/ora non compresa tra il 01/01/1850 e il 31/12/3000.", 0);
	}
}