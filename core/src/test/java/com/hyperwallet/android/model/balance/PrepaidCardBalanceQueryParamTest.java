package com.hyperwallet.android.model.balance;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.model.balance.BalanceQueryParam.BalanceSortable.ASCENDANT_AMOUNT;
import static com.hyperwallet.android.model.balance.BalanceQueryParam.BalanceSortable.ASCENDANT_CURRENCY;
import static com.hyperwallet.android.model.balance.BalanceQueryParam.BalanceSortable.DESCENDANT_CURRENCY;

import org.junit.Test;

import java.util.Map;

public class PrepaidCardBalanceQueryParamTest {
    private static final String OFFSET = "offset";
    private static final String LIMIT = "limit";
    private static final String SORT_BY = "sortBy";

    @Test
    public void prepaidCardBalanceQueryParam_sortByCurrencyAsc() {
        PrepaidCardBalanceQueryParam queryParam = new PrepaidCardBalanceQueryParam.Builder()
                .offset(0)
                .limit(20)
                .sortByCurrencyAsc()
                .build();

        assertThat(queryParam.getOffset(), is(0));
        assertThat(queryParam.getLimit(), is(20));
        assertThat(queryParam.getSortBy(), is(ASCENDANT_CURRENCY));
    }

    @Test
    public void prepaidCardBalanceQueryParam_sortByCurrencyDesc() {
        PrepaidCardBalanceQueryParam queryParam = new PrepaidCardBalanceQueryParam.Builder()
                .offset(0)
                .limit(20)
                .sortByCurrencyDesc()
                .build();

        assertThat(queryParam.getOffset(), is(0));
        assertThat(queryParam.getLimit(), is(20));
        assertThat(queryParam.getSortBy(), is(DESCENDANT_CURRENCY));
    }

    @Test
    public void prepaidCardBalanceQueryParam_verifyDefaultValues() {
        PrepaidCardBalanceQueryParam queryParam = new PrepaidCardBalanceQueryParam.Builder().build();

        assertThat(queryParam.getOffset(), is(0));
        assertThat(queryParam.getLimit(), is(10));
    }

    @Test
    public void buildQuery_verifyDefaultValues() {
        PrepaidCardBalanceQueryParam queryParam = new PrepaidCardBalanceQueryParam.Builder().build();

        Map<String, String> query = queryParam.buildQuery();

        assertThat(query, is(notNullValue()));
        assertThat(query.size(), is(2));
        assertThat(query.get(OFFSET), is("0"));
        assertThat(query.get(LIMIT), is("10"));
        assertThat(query.get(SORT_BY), is(nullValue()));
    }

    @Test
    public void buildQuery_returnsQueryParameters() {
        PrepaidCardBalanceQueryParam queryParam = new PrepaidCardBalanceQueryParam.Builder()
                .offset(0)
                .limit(20)
                .sortByAmountAsc()
                .build();
        Map<String, String> query = queryParam.buildQuery();

        assertThat(query, is(notNullValue()));
        assertThat(query.size(), is(3));
        assertThat(query.get(OFFSET), is("0"));
        assertThat(query.get(LIMIT), is("20"));
        assertThat(query.get(SORT_BY), is(ASCENDANT_AMOUNT));
    }

}