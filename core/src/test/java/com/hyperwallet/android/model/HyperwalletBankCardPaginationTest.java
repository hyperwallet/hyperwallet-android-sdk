package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition.VERIFIED;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.BANK_CARD;
import static com.hyperwallet.android.model.HyperwalletTransferMethodPagination.TransferMethodSortable.ASCENDANT_CREATE_ON;

import org.junit.Test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HyperwalletBankCardPaginationTest {
    private static final String ACCOUNT_TYPE = "type";
    private static final String CREATE_BEFORE = "createdBefore";
    private static final String CREATE_AFTER = "createdAfter";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";
    private static final String SORT_BY = "sortBy";
    private static final String STATUS = "status";

    @Test
    public void testHyperwalletBankCardPagination_withUrlQueryMap() {

        final int offset = 100;
        final int limit = 200;

        Map<String, String> query = new HashMap<>();
        query.put(STATUS, VERIFIED);
        query.put(SORT_BY, ASCENDANT_CREATE_ON);
        query.put(OFFSET, String.valueOf(offset));
        query.put(LIMIT, String.valueOf(limit));
        query.put(CREATE_BEFORE, "2017-01-01T10:12:22");
        query.put(CREATE_AFTER, "2017-01-01T00:00:00");

        HyperwalletBankCardPagination pagination = new HyperwalletBankCardPagination(query);
        assertThat(pagination.getLimit(), is(limit));
        assertThat(pagination.getOffset(), is(offset));
        assertThat(pagination.getType(), is(BANK_CARD));
        assertThat(pagination.getStatus(), is(VERIFIED));
        assertThat(pagination.getSortBy(), is(ASCENDANT_CREATE_ON));

        Calendar createdBefore = Calendar.getInstance();
        createdBefore.setTime(pagination.getCreatedBefore());
        assertThat(createdBefore.get(Calendar.YEAR), is(2017));
        assertThat(createdBefore.get(Calendar.MONTH), is(Calendar.JANUARY));
        assertThat(createdBefore.get(Calendar.DAY_OF_MONTH), is(1));
        assertThat(createdBefore.get(Calendar.HOUR), is(10));
        assertThat(createdBefore.get(Calendar.MINUTE), is(12));
        assertThat(createdBefore.get(Calendar.SECOND), is(22));

        Calendar createdAfter = Calendar.getInstance();
        createdAfter.setTime(pagination.getCreatedAfter());
        assertThat(createdAfter.get(Calendar.YEAR), is(2017));
        assertThat(createdAfter.get(Calendar.MONTH), is(Calendar.JANUARY));
        assertThat(createdAfter.get(Calendar.DAY_OF_MONTH), is(1));
        assertThat(createdAfter.get(Calendar.HOUR), is(0));
        assertThat(createdAfter.get(Calendar.MINUTE), is(0));
        assertThat(createdAfter.get(Calendar.SECOND), is(0));

    }


    @Test
    public void testHyperwalletBankCardPagination_verifyDefaultValues() {

        HyperwalletBankCardPagination pagination = new HyperwalletBankCardPagination();
        assertThat(pagination.getLimit(), is(10));
        assertThat(pagination.getOffset(), is(0));
        assertThat(pagination.getType(), is(BANK_CARD));
        assertThat(pagination.getStatus(), is(nullValue()));
        assertThat(pagination.getSortBy(), is(nullValue()));
        assertThat(pagination.getCreatedBefore(), is(nullValue()));
        assertThat(pagination.getCreatedAfter(), is(nullValue()));

    }


    @Test
    public void testBuildQuery_returnsQueryParameters() {
        final int offset = 100;
        final int limit = 200;

        Map<String, String> query = new HashMap<>();
        query.put(STATUS, VERIFIED);
        query.put(SORT_BY, ASCENDANT_CREATE_ON);
        query.put(OFFSET, String.valueOf(offset));
        query.put(LIMIT, String.valueOf(limit));
        query.put(CREATE_BEFORE, "2017-01-01T10:12:22");
        query.put(CREATE_AFTER, "2017-01-01T00:00:00");

        HyperwalletBankCardPagination pagination = new HyperwalletBankCardPagination(query);
        Map<String, String> resultQuery = pagination.buildQuery();

        assertThat(resultQuery.containsKey(STATUS), is(true));
        assertThat(resultQuery.containsKey(SORT_BY), is(true));
        assertThat(resultQuery.containsKey(OFFSET), is(true));
        assertThat(resultQuery.containsKey(LIMIT), is(true));
        assertThat(resultQuery.containsKey(CREATE_BEFORE), is(true));
        assertThat(resultQuery.containsKey(CREATE_AFTER), is(true));
        assertThat(resultQuery.containsKey(ACCOUNT_TYPE), is(true));

        assertThat(resultQuery.get(LIMIT), is(String.valueOf(limit)));
        assertThat(resultQuery.get(OFFSET), is(String.valueOf(offset)));
        assertThat(resultQuery.get(STATUS), is(VERIFIED));
        assertThat(resultQuery.get(SORT_BY), is(ASCENDANT_CREATE_ON));
        assertThat(resultQuery.get(CREATE_BEFORE), is("2017-01-01T10:12:22"));
        assertThat(resultQuery.get(CREATE_AFTER), is("2017-01-01T00:00:00"));
        assertThat(resultQuery.get(ACCOUNT_TYPE), is(BANK_CARD));
    }

    @Test
    public void testBuildQuery_verifyDefaultValues() {

        HyperwalletBankCardPagination pagination = new HyperwalletBankCardPagination();

        Map<String, String> resultQuery = pagination.buildQuery();
        assertThat(resultQuery.size(), is(3));
        assertThat(resultQuery.containsKey(OFFSET), is(true));
        assertThat(resultQuery.containsKey(LIMIT), is(true));
        assertThat(resultQuery.containsKey(ACCOUNT_TYPE), is(true));
        assertThat(resultQuery.get(LIMIT), is("10"));
        assertThat(resultQuery.get(OFFSET), is("0"));
        assertThat(resultQuery.get(ACCOUNT_TYPE), is(BANK_CARD));
        assertThat(resultQuery.get(STATUS), is(nullValue()));
        assertThat(resultQuery.get(SORT_BY), is(nullValue()));
        assertThat(resultQuery.get(CREATE_BEFORE), is(nullValue()));
        assertThat(resultQuery.get(CREATE_AFTER), is(nullValue()));
    }
}
