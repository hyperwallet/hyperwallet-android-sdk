package com.hyperwallet.android.util;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtilTest {

    private static final String MS_DATE_STRING = "2019-02-27T11:37:31.026";
    private static final long MS_DATE = 1551296251026L;

    @Test
    public void testToDateFormat_formattedDateStringInYearMonthDay() {
        Date date = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String formattedTime = DateUtil.toDateFormat(date);

        assertThat(formattedTime, is(notNullValue()));
        assertThat(formattedTime, is(dateFormatter.format(date)));
    }

    @Test
    public void testToDateFormat_formattedDateStringInDateTime() {
        Date date = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        String formattedTime = DateUtil.toDateTimeFormat(date);

        assertThat(formattedTime, is(notNullValue()));
        assertThat(formattedTime, is(dateFormatter.format(date)));
    }

    @Test
    public void testToDateFormat_formattedDateStringInDateTimeMillis() {
        Date date = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        String formattedTime = DateUtil.toDateTimeMillisFormat(date);

        assertThat(formattedTime, is(notNullValue()));
        assertThat(formattedTime, is(dateFormatter.format(date)));
    }

    @Test
    public void testToDateFormat_fromStringToDateObject() {
        Date expectedDate = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        String formattedTime = DateUtil.toDateTimeFormat(expectedDate);

        assertThat(expectedDate.toString(), is(DateUtil.fromDateTimeString(formattedTime).toString()));
    }
}
