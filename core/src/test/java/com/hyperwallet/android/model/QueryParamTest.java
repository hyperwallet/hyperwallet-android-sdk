package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.model.QueryParam.Sortable.DESCENDANT_CREATE_ON;

import org.junit.Test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class QueryParamTest {

    @Test
    public void testHyperwalletPagination_verifyDefaultValues() {
        QueryParam pagination = QueryParam.builder().build();
        assertThat(pagination.getLimit(), is(10));
        assertThat(pagination.getOffset(), is(0));
    }

    @Test
    public void testHyperwalletPagination_verifyQueryValues() {
        Map<String, String> urlQueryMap = new HashMap<>();
        urlQueryMap.put("limit", "23");
        urlQueryMap.put("offset", "11");

        QueryParam pagination = new QueryParam(urlQueryMap);
        assertThat(pagination.getLimit(), is(23));
        assertThat(pagination.getOffset(), is(11));
    }

    @Test
    public void testBuildQuery_returnsQueryParameters() {

        QueryParam pagination = QueryParam.builder().limit(10).offset(0).build();
        Map<String, String> resultDefaultQueryMap = pagination.buildQuery();
        assertThat(resultDefaultQueryMap.get("limit"), is("10"));
        assertThat(resultDefaultQueryMap.get("offset"), is("0"));

        pagination = QueryParam.builder().limit(9).offset(13).build();
        Map<String, String> resultGetQueryMap = pagination.buildQuery();
        assertThat(resultGetQueryMap.get("limit"), is("9"));
        assertThat(resultGetQueryMap.get("offset"), is("13"));

        Map<String, String> urlQueryMap = new HashMap<>();
        urlQueryMap.put("limit", "3");
        urlQueryMap.put("offset", "7");

        QueryParam mapPagination = new QueryParam(urlQueryMap);
        Map<String, String> resultQueryMap = mapPagination.buildQuery();
        assertThat(resultQueryMap.get("limit"), is("3"));
        assertThat(resultQueryMap.get("offset"), is("7"));
    }

    @Test
    public void testBuilder_verifyValues() {
        Calendar dateAfter = Calendar.getInstance();
        dateAfter.set(2019, 6, 21, 12, 45);
        Calendar dateBefore = Calendar.getInstance();
        dateBefore.set(2019, 6, 20, 9, 10);
        QueryParam pagination = QueryParam.builder()
                .offset(100)
                .limit(20)
                .sortBy(DESCENDANT_CREATE_ON)
                .createdAfter(dateAfter.getTime())
                .createdBefore(dateBefore.getTime())
                .build();

        assertThat(pagination.getOffset(), is(100));
        assertThat(pagination.getLimit(), is(20));
        assertThat(pagination.getSortBy(), is(DESCENDANT_CREATE_ON));
        assertThat(pagination.getCreatedAfter().getTime(), is(dateAfter.getTimeInMillis()));
        assertThat(pagination.getCreatedBefore().getTime(), is(dateBefore.getTimeInMillis()));
    }
}