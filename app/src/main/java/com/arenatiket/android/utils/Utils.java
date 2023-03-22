package com.arenatiket.android.utils;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.arenatiket.android.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;

/**
 * Created by kahfi on 09/04/16.
 */
public class Utils {
    private static String sMessage;

    public static void logd(String message) {
        Log.d("arenatiketlog", message);
    }


    public static void longLog(String data) {
        if (data.length() > 4000) {
            logd(data.substring(0, 4000));
            longLog(data.substring(4000));
        } else
            logd(data);
    }


    public static String getMonthShortName(int index) {
        String[] months = {"JAN", "FEB", "MARET", "APRIL", "MEI", "JUNI", "JULI", "AGUST", "SEP", "OKT", "NOV", "DES"};
        return months[index];
    }

    public static String getMonthName(int index) {
        String[] months = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November",
                "Desember"};
        return months[index];
    }

    public static Collection<CalendarDay> getDates(Date date1, Date date2) {
        Collection<CalendarDay> dates = new ArrayList<CalendarDay>();

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        cal2.add(Calendar.DATE, -1);

        while (!cal1.after(cal2) && !cal1.equals(cal2)) {
            cal1.add(Calendar.DATE, 1);
            CalendarDay calendarDay = CalendarDay.from(cal1);
            dates.add(calendarDay);
        }
        return dates;
    }

    public static String getMoneyFormat(Number number, boolean withCurrency) {
        if (number == null) {
            number = 0;
        }

        Utils.logd("number " + number.toString());

        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
        String hasil = formatter.format(number);
//        if (hasil.indexOf(",") == -1) {
//            hasil = hasil + ",-";
//        }
        if (withCurrency) {
            hasil = "Rp " + hasil;
        }
        return hasil;
    }

    public static String getFormattedNumber(Number number) {
        if (number == null) {
            number = 0;
        }

        Utils.logd("number " + number.toString());

        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator('.');
        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
        String hasil = formatter.format(number);
//        if (hasil.indexOf(",") == -1) {
//            hasil = hasil + ",-";
//        }
        return hasil;
    }

    public static int getDateDifference(Date startDate, Date endDate) {
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;

        return (int) elapsedDays;

    }

//    public static int getMillisDifference(Date startDate, Date endDate) {
//        long different = endDate.getTime() - startDate.getTime();
//
//        long secondsInMilli = 1000;
//        long minutesInMilli = secondsInMilli * 60;
//        long hoursInMilli = minutesInMilli * 60;
//        long daysInMilli = hoursInMilli * 24;
//
//        long elapsedDays = different / daysInMilli;
//
//        return (int) elapsedDays;
//
//    }

    private static int sTheme;
    public final static int THEME_DEFAULT = 0;
    public final static int THEME_TRANSPARENT = 1;

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme, String message) {
        sTheme = theme;
        sMessage = message;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    /**
     * Set the theme of the activity, according to the configuration.
     */
    public static String onActivityCreateSetTheme(Activity activity) {
        Utils.logd("stheme " + sTheme + ":" + sMessage);
        switch (sTheme) {
            case THEME_TRANSPARENT:
                activity.setTheme(R.style.Theme_Transparent);
                break;
            case THEME_DEFAULT:
        }

        return sTheme + ":" + sMessage;
    }

    public static String sha1(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
