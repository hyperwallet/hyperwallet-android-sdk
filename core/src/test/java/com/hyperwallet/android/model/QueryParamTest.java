package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

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
}