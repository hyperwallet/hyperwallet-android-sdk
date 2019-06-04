package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import java.util.Map;

public class QueryParamTest {

    @Test
    public void testHyperwalletPagination_verifyDefaultValues() {
        QueryParam.Builder<QueryParam.Builder> builder = new QueryParam.Builder<>();
        QueryParam pagination = builder.build();
        assertThat(pagination.getLimit(), is(10));
        assertThat(pagination.getOffset(), is(0));
    }

    @Test
    public void testHyperwalletPagination_verifyQueryValues() {
        QueryParam.Builder builder = new QueryParam.Builder<>().limit(23).offset(11);
        QueryParam pagination = builder.build();
        assertThat(pagination.getLimit(), is(23));
        assertThat(pagination.getOffset(), is(11));
    }

    @Test
    public void testBuildQuery_returnsQueryParameters() {

        QueryParam.Builder builder = new QueryParam.Builder<>().limit(10).offset(0);
        QueryParam pagination = builder.build();
        Map<String, String> resultDefaultQueryMap = pagination.buildQuery();
        assertThat(resultDefaultQueryMap.get("limit"), is("10"));
        assertThat(resultDefaultQueryMap.get("offset"), is("0"));

        pagination = new QueryParam.Builder<>().limit(9).offset(13).build();
        Map<String, String> resultGetQueryMap = pagination.buildQuery();
        assertThat(resultGetQueryMap.get("limit"), is("9"));
        assertThat(resultGetQueryMap.get("offset"), is("13"));
    }
}