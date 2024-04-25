package com.bobby.email.emailvalidator;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmailTimeUtil {
    public static String getOldDate(int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dft.format(endDate);
    }


    public static boolean isTimeEarlier(String time1Str, String time2Str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date time1 = format.parse(time1Str);
            Date time2 = format.parse(time2Str);

            return time1.after(time2);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
