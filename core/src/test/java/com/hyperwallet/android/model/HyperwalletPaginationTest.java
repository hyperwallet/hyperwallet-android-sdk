package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HyperwalletPaginationTest {

    @Test
    public void testHyperwalletPagination_verifyDefaultValues() {
        QueryParam pagination = new QueryParam();
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

        QueryParam pagination = new QueryParam.Builder<>().limit(10).offset(0).build();
        Map<String, String> resultDefaultQueryMap = pagination.buildQuery();
        assertThat(resultDefaultQueryMap.get("limit"), is("10"));
        assertThat(resultDefaultQueryMap.get("offset"), is("0"));

        pagination = new QueryParam.Builder<>().limit(9).offset(13).build();
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
}