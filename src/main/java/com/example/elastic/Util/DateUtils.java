package com.example.elastic.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

/**
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  ------------------
 * 2018/10/9    刘节                 Created
 */
public class DateUtils {


    public static final String DATE_TIME_FORMAT= "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT= "yyyy-MM-dd";

    public static final String DAYTIME_START = "00:00:00";

    public static final String DAYTIME_END = "23:59:59";


    private static final String[] FORMATS = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "HH:mm",
            "HH:mm:ss", "yyyy-MM", "yyyy-MM-dd HH:mm:ss.S","yyyy-MM-dd HH:mm:ss" ,"yyyyMMddHHmmss"};

    public static Date convert(String str) {
        if (str != null && str.length() > 0) {
            if (str.length() > 10 && str.charAt(10) == 'T') {
                str = str.replace('T', ' '); // 去掉json-lib加的T字母
            }
            for (String format : FORMATS) {
                if (str.length() == format.length()) {
                    try {
                        Date date = new SimpleDateFormat(format).parse(str);
                        return date;
                    } catch (ParseException e) {
//                        if (logger.isWarnEnabled()) {
//                            logger.warn(e.getMessage(),e);
//                        }
                    }
                }
            }
        }
        return null;
    }

    public static String convert(Date date,String dateFormat){
        if(date == null){
            return null;
        }

        if(null == dateFormat){
            dateFormat = DATE_TIME_FORMAT ;
        }

        return new SimpleDateFormat(dateFormat).format(date);
    }

    /**
     * 增加/加少 天数
     * @param date
     * @param day
     * @return
     */
    public static Date  addDayToDate(Date date,long day){
        return new Date(date.getTime()+1000*24*60*60*day);
    }

    /**
     * 返回该天从00:00:00开始的日期
     *
     * @param date
     * @return
     */
    public static Date getStartDatetime(Date date) {
        String thisdate = convert(date, DATE_FORMAT);
        return convert(thisdate + " " + DAYTIME_START);

    }

    /**
     * 返回n天后从00:00:00开始的日期
     *
     * @param date
     * @return
     */
    public static Date getStartDatetime(Date date, Integer diffDays) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        String thisdate = df.format(date.getTime() + 1000l * 24 * 60 * 60 * diffDays);
        return convert(thisdate + " " + DAYTIME_START);
    }

    /**
     * 返回该天到23:59:59结束的日期
     *
     * @param date
     * @return
     */
    public static Date getEndDatetime(Date date) {
        String thisdate = convert(date, DATE_FORMAT);
        return convert(thisdate + " " + DAYTIME_END);

    }

    /**
     * 返回n天到23:59:59结束的日期
     *
     * @param date
     * @return
     */
    public static Date getEndDatetime(Date date, Integer diffDays) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        String thisdate = df.format(date.getTime() + 1000l * 24 * 60 * 60 * diffDays);
        return convert(thisdate + " " + DAYTIME_END);

    }
}
