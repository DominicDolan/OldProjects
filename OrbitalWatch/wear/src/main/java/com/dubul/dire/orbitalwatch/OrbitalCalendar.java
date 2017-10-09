package com.dubul.dire.orbitalwatch;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by domin on 8 Aug 2016.
 */
public class OrbitalCalendar {
    private Calendar calendar;
    Date date;

    public OrbitalCalendar(){
        calendar = GregorianCalendar.getInstance();
    }

    public void update (){
        calendar = GregorianCalendar.getInstance();
    }

    public String getDayofWeek(){
        String day;
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case Calendar.MONDAY:
                day = "Monday";
                break;
            case Calendar.TUESDAY:
                day = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                day = "Wednesday";
                break;
            case Calendar.THURSDAY:
                day = "Thursday";
                break;
            case Calendar.FRIDAY:
                day = "Friday";
                break;
            case Calendar.SATURDAY:
                day = "Saturday";
                break;
            case Calendar.SUNDAY:
                day = "Sunday";
                break;
            default:
                day = "Invalid";
                break;
        }
        return day;
    }

    public String getMonth(){
        String month;
        switch (calendar.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                month = "January";
                break;
            case Calendar.FEBRUARY:
                month = "February";
                break;
            case Calendar.MARCH:
                month = "March";
                break;
            case Calendar.APRIL:
                month = "April";
                break;
            case Calendar.MAY:
                month = "May";
                break;
            case Calendar.JUNE:
                month = "June";
                break;
            case Calendar.JULY:
                month = "July";
                break;
            case Calendar.AUGUST:
                month = "August";
                break;
            case Calendar.SEPTEMBER:
                month = "September";
                break;
            case Calendar.OCTOBER:
                month = "October";
                break;
            case Calendar.NOVEMBER:
                month = "November";
                break;
            case Calendar.DECEMBER:
                month = "December";
                break;
            default:
                month = "Invalid month";
                break;
        }
        return month;
    }

    public int getDay(){
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getHour(){
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute(){
        return calendar.get(Calendar.MINUTE);
    }

    public int getSecond(){
        return calendar.get(Calendar.SECOND);
    }
}
