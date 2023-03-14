package th.co.pentasol.pcs.master.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("ALL")
@Slf4j
public class DateTimeUtil {
    private static final DateFormat yyFormat = new SimpleDateFormat("yy", Locale.ENGLISH);
    private static final DateFormat yyyyFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    private static final DateFormat yyyyMMddFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private static final DateFormat ddMMMyyFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
    private static final DateFormat ddMMyyyyHHmmssFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
    private static final DateFormat ddMMyyyyCommaHHmmssFormat = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss", Locale.ENGLISH);
    private static final DateFormat yyyyMMddHHmmssFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private static final DateFormat HHmmssFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
    private static final DateFormat ddMonyyFormat = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);

    public static String getYear2Digits(Date date){
        if(date != null){
            StringBuilder year = new StringBuilder(yyFormat.format(((Date) date).getTime()));
            return year.toString();
        }else return "";
    }

    public static Integer getYear(Date date){
        if(date != null){
            StringBuilder year = new StringBuilder(yyyyFormat.format(((Date) date).getTime()));
            return NumberUtil.convertToInteger(year);
        }else return null;
    }

    public static Date convertStringToDate(String datestr){
        Date date = null;
        try{
                Date dt = yyyyMMddFormat.parse(datestr);
                date = new Date(dt.getTime());
        }catch(Exception e){
                log.error("Can't parse to date : " + datestr);
        }
        return date;
    }

    public static Date convertNumbericToDate(Integer dateInt){
        Date date = null;
        try{
            Date dt = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(Integer.toString(dateInt));
            date = new Date(dt.getTime());
        }catch(Exception e){
            log.error("Can't parse to date : " + dateInt);
        }
        return date;
    }

    public static Date convertStringToDdMmYyyy(String datestr){
        Date date = null;
        try{
            Date dt = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(datestr);
            date = new Date(dt.getTime());
        }catch(Exception e){
            log.error("Can't parse to date : " + datestr);
        }
        return date;
    }

    public static Date convertStringToDateTimeUTC(String datestr){
        Date date = null;
        try{
            Date dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).parse(datestr);
            date = new Date(dt.getTime());
        }catch(Exception e){
            log.error("Can't parse to date " + datestr + " to format yyyy-MM-dd'T'HH:mm:ss'Z'" );
        }
        return date;
    }

    public static String convertDateToDateTimeUTC(Date dateInput){
        String dateOutput = null;
        try{
            dateOutput = new StringBuilder(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).format(((Date) dateInput).getTime())).toString();
        }catch(Exception e){
            log.error("Can't parse to date " + dateInput + " to format yyyy-MM-dd'T'HH:mm:ss'Z'" );
        }
        return dateOutput;
    }

    public static String convertDateToEndDateTimeUTC(Date dateStart){
        String dateOutput = null;
        try{
            Calendar calendar = dateToCalendar(dateStart);
            calendar.set(Calendar.HOUR, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            dateOutput = new StringBuilder(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).format(((Date) calendar.getTime()).getTime())).toString();
        }catch(Exception e){
            log.error("Can't parse to date " + dateStart + " to format yyyy-MM-dd'T'HH:mm:ss'Z'" );
        }
        return dateOutput;
    }

    public static Date convertDDMMYYYYToDate(String datestr){
        Date date = null;
        try{
            Date dt = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(datestr);
            date = new Date(dt.getTime());
        }catch(Exception e){
            System.out.println("Can't parse to calendar date : " + datestr);
        }
        return date;
    }

    public static String convertDateToyyyyMMdd(Object date) {
        if(date != null){
            StringBuilder datestr = new StringBuilder(yyyyMMddFormat.format(((Date) date).getTime()));
            return datestr.toString();
        }else return "";
    }

    public static String convertDateToyyyyMM(Object date) {
        if(date != null){
            StringBuilder datestr = new StringBuilder(new SimpleDateFormat("yyyyMM", Locale.ENGLISH).format(((Date) date).getTime()));
            return datestr.toString();
        }else return "";
    }

    public static String convertDateToddMMMyy(Object date) {
        if(date != null){
            StringBuilder datestr = new StringBuilder(ddMMMyyFormat.format(((Date) date).getTime()));
            return datestr.toString();
        }else return "";
    }

    public static String convertDateToddMMyyyyHHmmss(Object date) {
        if(date != null){
            StringBuilder datestr = new StringBuilder(ddMMyyyyHHmmssFormat.format(((Date) date).getTime()));
            return datestr.toString();
        }else return "";
    }

    public static String convertDateToddMMyyyyHHmmss(Object date, Locale local) {
        if(date != null){
            if(local.getLanguage().equalsIgnoreCase("TH")) {
                StringBuilder datestr = new StringBuilder(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("th", "TH")).format(((Date) date).getTime()));
                return datestr.toString();
            }else {
                StringBuilder datestr = new StringBuilder(ddMMyyyyHHmmssFormat.format(((Date) date).getTime()));
                return datestr.toString();
            }

        }else return "";
    }

    public static String convertDateToddMMyyyyCommaHHmmss(Object date, Locale local) {
        if(date != null){
            if(local.getLanguage().equalsIgnoreCase("TH")) {
                StringBuilder datestr = new StringBuilder(new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss", new Locale("th", "TH")).format(((Date) date).getTime()));
                return datestr.toString();
            }else {
                StringBuilder datestr = new StringBuilder(ddMMyyyyCommaHHmmssFormat.format(((Date) date).getTime()));
                return datestr.toString();
            }
        }else return "";
    }

    public static String convertDateToddMMyyyy(Object date, Locale local) {
        if(date != null){
            if(local.getLanguage().equalsIgnoreCase("TH")) {
                StringBuilder datestr = new StringBuilder(new SimpleDateFormat("dd/MM/yyyy", new Locale("th", "TH")).format(((Date) date).getTime()));
                return datestr.toString();
            }else {
                StringBuilder datestr = new StringBuilder(new SimpleDateFormat("dd/MM/yyyy").format(((Date) date).getTime()));
                return datestr.toString();
            }
        }else return "";
    }

    public static Date convertddMMyyyyHHmmssToDate(String datestr) {
        Date date = null;
        try{
                Date dt = ddMMyyyyHHmmssFormat.parse(datestr);
                date = new Date(dt.getTime());
        }catch(Exception e){
            System.out.println("Can't parse to calendar date : " + datestr);
        }
        return date;
    }

    public static String convertDateToyyyyMMddHHmmss(Object date) {
        if(date != null){
            StringBuilder datestr = new StringBuilder(yyyyMMddHHmmssFormat.format(((Date) date).getTime()));
            return datestr.toString();
        }else return "";
    }

    public static String convertDateToHHmmss(Object date) {
        if(date != null){
            StringBuilder datestr = new StringBuilder(HHmmssFormat.format(((Date) date).getTime()));
            return datestr.toString();
        }else return "";
    }

    public static Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }

    public static Date getLastDateOfMonth(Date dateFrom){
        Calendar cal = dateToCalendar(dateFrom);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        return cal.getTime();
    }


    public static Date getFirstDateOfMonth(Date dateFrom){
        Calendar cal = dateToCalendar(dateFrom);
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
        return cal.getTime();
    }

    public static Date findDate(Date curdate, int leadTime){
        if(curdate != null && curdate.toString().trim().length() > 0){
            Calendar calendarDate = dateToCalendar(curdate);
            calendarDate.add(Calendar.DAY_OF_MONTH, leadTime);
            return calendarDate.getTime();
        }
        return null;
    }

    public static Date findDateByCondition(int type, Date curdate, int leadTime){
        if(curdate != null && curdate.toString().trim().length() > 0){
            Calendar calendarDate = dateToCalendar(curdate);
            calendarDate.add(type, leadTime);
            return calendarDate.getTime();
        }
        return null;
    }

    public static Date findDateOfSunday(Date date){
        Calendar calendar = dateToCalendar(date);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        int days = Calendar.SUNDAY - weekday;
        if (days < 0)
        {
            days += 7;
        }
        calendar.add(Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    public static Date firstDateOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date setDateByCondition(Integer condition, Integer conditionValue, Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(condition, conditionValue);
        return calendar.getTime();
    }

    public static Integer convertDateToNumeric(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        if(!Objects.isNull(date) && date.toString().length() > 0){
            StringBuilder datestr = new StringBuilder(formatter.format(((Date) date).getTime()));
            return Integer.parseInt(datestr.toString());
        }else return 0;
    }

    public static Date convertToDateTime(Date date, String time) {
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        String hour = time.substring(0, time.indexOf(":"));
        String min  = time.substring(time.indexOf(":")+1, time.length());
        if(min.contains(":")){
            min = min.substring(0, min.indexOf(":"));
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, NumberUtil.convertToInteger(hour));
        calendar.set(Calendar.MINUTE, NumberUtil.convertToInteger(min));
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }


    public static String convertDateToddMonyy(Object date) {
        if(date != null){
            StringBuilder datestr = new StringBuilder(ddMonyyFormat.format(((Date) date).getTime()));
            return datestr.toString();
        }else return "";
    }

    public static Calendar convertIntToCalendar(Integer dateInt) {
        try {
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
            Date dt = dateformat.parse(dateInt.toString());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(dt.getTime()));
            return calendar;
        }catch(Exception e){
            log.error("Can't parse to date " + dateInt + " to format yyyyMMdd" );
        }
        return null;
    }

    public static int convertTimeToNumeric(String timestr, Integer size){
        int timeTonumeric = 0;
        if(timestr != null && timestr.trim().length() > 0){
            String time = timestr.trim().replaceAll(":", "");
            if(time.length() == size) return NumberUtil.convertToInteger(time);
            else if(time.length() > size) {
                time = timestr.trim().substring(0, 4).replaceAll(":", "");
                return  NumberUtil.convertToInteger(time);
            } if(time.length() < size){
                time = StringUtils.rightPad(time, size, '0');
                return NumberUtil.convertToInteger(time);
            }
        }
        return timeTonumeric;
    }

    public static Date convertStringToYyMmDd(String datestr){
        Date date = null;
        try{
            Date dt = new SimpleDateFormat("yyMMdd", Locale.ENGLISH).parse(datestr);
            date = new Date(dt.getTime());
        }catch(Exception e){
            log.error("Can't parse to date : " + datestr);
        }
        return date;
    }
}
