package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HyperwalletPaginationTest {

    @Test
    public void testHyperwalletPagination_verifyDefaultValues() {
        HyperwalletPagination pagination = new HyperwalletPagination();
        assertThat(pagination.getLimit(), is(10));
        assertThat(pagination.getOffset(), is(0));
    }

    @Test
    public void testHyperwalletPagination_verifyQueryValues() {
        Map<String, String> urlQueryMap = new HashMap<>();
        urlQueryMap.put("limit", "23");
        urlQueryMap.put("offset", "11");

        HyperwalletPagination pagination = new HyperwalletPagination(urlQueryMap);
        assertThat(pagination.getLimit(), is(23));
        assertThat(pagination.getOffset(), is(11));
    }

    @Test
    public void testBuildQuery_returnsQueryParameters() {

        HyperwalletPagination pagination = new HyperwalletPagination();
        Map<String, String> resultDefaultQueryMap = pagination.buildQuery();
        assertThat(resultDefaultQueryMap.get("limit"), is("10"));
        assertThat(resultDefaultQueryMap.get("offset"), is("0"));

        pagination.setLimit(9);
        pagination.setOffset(13);
        Map<String, String> resultGetQueryMap = pagination.buildQuery();
        assertThat(resultGetQueryMap.get("limit"), is("9"));
        assertThat(resultGetQueryMap.get("offset"), is("13"));

        Map<String, String> urlQueryMap = new HashMap<>();
        urlQueryMap.put("limit", "3");
        urlQueryMap.put("offset", "7");

        HyperwalletPagination mapPagination = new HyperwalletPagination(urlQueryMap);
        Map<String, String> resultQueryMap = mapPagination.buildQuery();
        assertThat(resultQueryMap.get("limit"), is("3"));
        assertThat(resultQueryMap.get("offset"), is("7"));
    }
}