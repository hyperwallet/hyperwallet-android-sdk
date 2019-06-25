package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QueryParamTest {

    @Test
    public void testHyperwalletQueryParam_verifyDefaultValues() {
        QueryParam queryParam = new QueryParam.Builder().build();
        assertThat(queryParam.getLimit(), is(10));
        assertThat(queryParam.getOffset(), is(0));
    }

    @Test
    public void testHyperwalletQueryParam_verifyQueryValues() {
        QueryParam queryParam = new QueryParam.Builder().limit(23).offset(11).build();
        assertThat(queryParam.getLimit(), is(23));
        assertThat(queryParam.getOffset(), is(11));
    }

    @Test
    public void testBuildQuery_returnsQueryParameters() {

        QueryParam queryParam = new QueryParam.Builder().limit(10).offset(0).build();
        Map<String, String> resultDefaultQueryMap = queryParam.buildQuery();
        assertThat(resultDefaultQueryMap.get("limit"), is("10"));
        assertThat(resultDefaultQueryMap.get("offset"), is("0"));

        queryParam = new QueryParam.Builder().limit(9).offset(13).build();
        Map<String, String> resultGetQueryMap = queryParam.buildQuery();
        assertThat(resultGetQueryMap.get("limit"), is("9"));
        assertThat(resultGetQueryMap.get("offset"), is("13"));
    }

    @Test
    public void testBuildQuery_getDateFromMap() {

        QueryParam queryParam = new QueryParam.Builder().build();
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("someDate", "2019-06-15T10:20:30");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, 5, 15, 10, 20, 30);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = queryParam.getDateValue(queryMap, "someDate");
        assertThat(date, is(notNullValue()));
        assertThat(date.getTime(), is(calendar.getTimeInMillis()));
    }

    @Test
    public void testBuildQuery_getIntFromMap() {

        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("someInt", "21");
        QueryParam queryParam = new QueryParam.Builder().build();
        int someInt = queryParam.getIntegerValue(queryMap, "someInt", 0);
        int defaultInt = queryParam.getIntegerValue(queryMap, "incorrectKeyInt", 0);
        assertThat(someInt, is(21));
        assertThat(defaultInt, is(0));
    }
}