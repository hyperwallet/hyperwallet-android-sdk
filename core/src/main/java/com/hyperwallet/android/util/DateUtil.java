package com.hyperwallet.android.util;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Util class to convert date to string and string to date
 */
public class DateUtil {

    /**
     * HW Rest API date formats
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DATE_TIME_FORMAT_MILLISECONDS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT, Locale.US);
    private static final SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
    private static final SimpleDateFormat DATE_TIME_FORMATTER_MILLISECONDS = new SimpleDateFormat(
            DATE_TIME_FORMAT_MILLISECONDS, Locale.US);
    private static final String TAG = DateUtil.class.getName();

    /**
     * Format date in <code>yyyy-MM-dd</code> not time specified
     *
     * @return String representation of date in <code>yyyy-MM-dd</code> format
     */
    public static String toDateFormat(@NonNull Date date) {
        return DATE_FORMATTER.format(date);
    }

    /**
     * Format date in <code>yyyy-MM-dd'T'HH:mm:ss</code> not time specified
     *
     * @return String representation of date in <code>yyyy-MM-dd'T'HH:mm:ss</code> format
     */
    public static String toDateTimeFormat(@NonNull Date date) {
        return DATE_TIME_FORMATTER.format(date);
    }

    /**
     * Format date in <code>yyyy-MM-dd'T'HH:mm:ss.SSS</code> not time specified
     *
     * @return String representation of date in <code>yyyy-MM-dd'T'HH:mm:ss.SSS</code> format
     */
    public static String toDateTimeMillisFormat(@NonNull Date date) {
        return DATE_TIME_FORMATTER_MILLISECONDS.format(date);
    }

    /**
     * Returns the java.util.Date object from the string value formatted at ISO860-1
     *
     * @param dateString string formatted yyyy-MM-dd'T'HH:mm:ss
     * @return date
     */
    public static Date fromDateTimeString(@NonNull String dateString) {
        try {
            return DATE_TIME_FORMATTER.parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("An exception occurred when attempting to parse " +
                    "the date " + dateString, e);
        }
    }
}
