package com.dr.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Date-->String
 * */
public class DateUtils {
    private static final String STANDARD_FORMAT="yyyy-MM-dd HH:mm:ss";

    public static String dataToString(Date date,String formate){
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formate);
    }
    public static String dataToString(Date date){
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
/**
 * StringDate-->Date
 * */
    public static Date stringToDate(String str){
        DateTimeFormatter dateTimeFormatter =DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }
    public static Date stringToDate(String str,String formate){
        DateTimeFormatter dateTimeFormatter =DateTimeFormat.forPattern(formate);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }

}
