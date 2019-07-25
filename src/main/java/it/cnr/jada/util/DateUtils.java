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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils
        implements Serializable {
    static final long ONE_HOUR = 60 * 60 * 1000L;

    private DateUtils() {
    }

    public static Timestamp max(Timestamp timestamp, Timestamp timestamp1) {
        if (timestamp == null)
            return timestamp1;
        if (timestamp1 == null)
            return timestamp;
        else
            return timestamp.after(timestamp1) ? timestamp : timestamp1;
    }

    public static Date max(Date date, Date date1) {
        if (date == null)
            return date1;
        if (date1 == null)
            return date;
        else
            return date.after(date1) ? date : date1;
    }

    public static Timestamp min(Timestamp timestamp, Timestamp timestamp1) {
        if (timestamp == null)
            return timestamp1;
        if (timestamp1 == null)
            return timestamp;
        else
            return timestamp.before(timestamp1) ? timestamp : timestamp1;
    }

    public static Date min(Date date, Date date1) {
        if (date == null)
            return date1;
        if (date1 == null)
            return date;
        else
            return date.before(date1) ? date : date1;
    }

    public static Timestamp truncate(Timestamp timestamp) {
        return new Timestamp(truncate(((Date) (timestamp))).getTime());
    }

    public static Date truncate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    public static long daysBetweenDates(Date date1, Date date2) {
        Calendar earlierDate = new GregorianCalendar();
        Calendar laterDate = new GregorianCalendar();

        GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
        data_da.setTime(date1);
        GregorianCalendar data_a = (GregorianCalendar) GregorianCalendar.getInstance();
        data_a.setTime(date2);

        int day1 = data_da.get(java.util.GregorianCalendar.DAY_OF_MONTH);
        int month1 = data_da.get(java.util.GregorianCalendar.MONTH);
        int year1 = data_da.get(java.util.GregorianCalendar.YEAR);

        int day2 = data_a.get(java.util.GregorianCalendar.DAY_OF_MONTH);
        int month2 = data_a.get(java.util.GregorianCalendar.MONTH);
        int year2 = data_a.get(java.util.GregorianCalendar.YEAR);

        earlierDate.set(year1, month1, day1, 0, 0, 0);
        laterDate.set(year2, month2, day2, 0, 0, 0);

        long duration = laterDate.getTime().getTime() - earlierDate.getTime().getTime();

        long nDays = (duration + ONE_HOUR) / (24 * ONE_HOUR);// System.out.println("difference in days: " + nDays);
        return nDays;
    }

    /**
     * si presume che la data_from sia superiore alla data_to altrimenti viene restituito un valore negativo
     *
     * @param date_from date
     * @param date_to   date
     * @return intero
     */
    public static int monthsBetweenDates(Date date_from, Date date_to) {
        GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
        data_da.setTime(date_from);
        GregorianCalendar data_a = (GregorianCalendar) GregorianCalendar.getInstance();
        data_a.setTime(date_to);

        int month1 = data_da.get(java.util.GregorianCalendar.MONTH);
        int year1 = data_da.get(java.util.GregorianCalendar.YEAR);

        int month2 = data_a.get(java.util.GregorianCalendar.MONTH);
        int year2 = data_a.get(java.util.GregorianCalendar.YEAR);
        int dif_year = year2 - year1;

        return (dif_year * 12) + (month2 - month1) + 1;
    }

    public static Date dataContabile(Date date, int year) {
        GregorianCalendar dataInizio = (GregorianCalendar) GregorianCalendar.getInstance();
        dataInizio.setTime(new GregorianCalendar(year, Calendar.JANUARY, 1).getTime());

        GregorianCalendar dataFine = (GregorianCalendar) GregorianCalendar.getInstance();
        dataFine.setTime(new GregorianCalendar(year, Calendar.DECEMBER, 31).getTime());

        if (date.before(dataInizio.getTime()))
            return dataInizio.getTime();
        else if (date.after(dataFine.getTime()))
            return dataFine.getTime();
        else
            return date;
    }

    public static Timestamp dataContabile(Timestamp timestamp, int year) {
        GregorianCalendar dataInizio = (GregorianCalendar) GregorianCalendar.getInstance();
        dataInizio.setTime(new GregorianCalendar(year, Calendar.JANUARY, 1).getTime());

        GregorianCalendar dataFine = (GregorianCalendar) GregorianCalendar.getInstance();
        dataFine.setTime(new GregorianCalendar(year, Calendar.DECEMBER, 31).getTime());

        if (timestamp.before(dataInizio.getTime()))
            return new Timestamp(dataInizio.getTimeInMillis());
        else if (timestamp.after(dataFine.getTime()))
            return new Timestamp(dataFine.getTimeInMillis());
        else
            return timestamp;
    }

    public static Timestamp firstDateOfTheYear(int year) {
        GregorianCalendar dataInizio = (GregorianCalendar) GregorianCalendar.getInstance();
        dataInizio.setTime(new GregorianCalendar(year, Calendar.JANUARY, 1).getTime());
        return new Timestamp(dataInizio.getTimeInMillis());
    }

}