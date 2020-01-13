package com.hyperwallet.android.model.balance;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.model.balance.BalanceQueryParam.BalanceSortable.ASCENDANT_AMOUNT;
import static com.hyperwallet.android.model.balance.BalanceQueryParam.BalanceSortable.ASCENDANT_CURRENCY;
import static com.hyperwallet.android.model.balance.BalanceQueryParam.BalanceSortable.DESCENDANT_CURRENCY;

import com.hyperwallet.android.model.QueryParam;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class BalanceQueryParamTest {
    private final static String OFFSET = "offset";
    private final static String LIMIT = "limit";
    private final static String SORT_BY = "sortBy";
    private final static String CURRENCY = "currency";


    @Test
    public void testBalanceQueryParam_withUrlQueryMap() {

        BalanceQueryParam queryParam = new BalanceQueryParam.Builder()
                .offset(100)
                .limit(200)
                .currency("USD")
                .sortByCurrencyAsc()
                .build();

        assertThat(queryParam.getLimit(), is(200));
        assertThat(queryParam.getOffset(), is(100));
        assertThat(queryParam.getCurrency(), is("USD"));
        assertThat(queryParam.getSortBy(), is(ASCENDANT_CURRENCY));

    }

    @Test
    public void testBalanceQueryParam_verifyDefaultValues() {
        BalanceQueryParam queryParam = new BalanceQueryParam.Builder().build();
        assertThat(queryParam.getLimit(), is(10));
        assertThat(queryParam.getOffset(), is(0));
    }

    @Test
    public void testBuildQuery_verifyDefaultValues() {
        QueryParam queryParam = new BalanceQueryParam.Builder().build();

        Map<String, String> query = queryParam.buildQuery();

        Assert.assertNotNull(query);
        assertThat(query.size(), is(2));
        assertThat(query.get(OFFSET), is("0"));
        assertThat(query.get(LIMIT), is("10"));
        assertThat(query.get(SORT_BY), is(nullValue()));
        assertThat(query.get(CURRENCY), is(nullValue()));
    }

    @Test
    public void testBuildQuery_returnsQueryParameters() {

        BalanceQueryParam queryParam = new BalanceQueryParam.Builder()
                .offset(100)
                .limit(200)
                .currency("USD")
                .sortByAmountAsc()
                .build();
        Map<String, String> resultQuery = queryParam.buildQuery();

        assertThat(resultQuery.containsKey(SORT_BY), is(true));
        assertThat(resultQuery.containsKey(OFFSET), is(true));
        assertThat(resultQuery.containsKey(LIMIT), is(true));
        assertThat(resultQuery.containsKey(CURRENCY), is(true));

        assertThat(resultQuery.get(LIMIT), is("200"));
        assertThat(resultQuery.get(OFFSET), is("100"));
        assertThat(resultQuery.get(SORT_BY), is(ASCENDANT_AMOUNT));
        assertThat(resultQuery.get(CURRENCY), is("USD"));
    }

    @Test
    public void testBuilder_verifyValues() {
        BalanceQueryParam queryParam = new BalanceQueryParam.Builder()
                .offset(100)
                .limit(20)
                .sortByCurrencyDesc()
                .currency("USD")
                .build();

        assertThat(queryParam.getOffset(), is(100));
        assertThat(queryParam.getLimit(), is(20));
        assertThat(queryParam.getSortBy(), is(DESCENDANT_CURRENCY));
        assertThat(queryParam.getCurrency(), is("USD"));
    }
}