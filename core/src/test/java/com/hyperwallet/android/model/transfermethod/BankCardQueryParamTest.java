package com.hyperwallet.android.model.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.VERIFIED;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.BANK_CARD;
import static com.hyperwallet.android.model.transfermethod.TransferMethodQueryParam.TransferMethodSortable.ASCENDANT_CREATE_ON;
import static com.hyperwallet.android.model.transfermethod.TransferMethodQueryParam.TransferMethodSortable.DESCENDANT_CREATE_ON;

import org.junit.Test;

import java.util.Calendar;
import java.util.Map;

public class BankCardQueryParamTest {
    private static final String ACCOUNT_TYPE = "type";
    private static final String CREATE_BEFORE = "createdBefore";
    private static final String CREATE_AFTER = "createdAfter";
    private static final String CREATE_ON = "createdOn";
    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";
    private static final String SORT_BY = "sortBy";
    private static final String STATUS = "status";

    @Test
    public void testHyperwalletBankCardQueryParam_withUrlQueryMap() {
        final int offset = 100;
        final int limit = 200;

        Calendar dateAfter = Calendar.getInstance();
        dateAfter.set(2017, 0, 1, 0, 0, 0);
        Calendar dateBefore = Calendar.getInstance();
        dateBefore.set(2017, 0, 1, 10, 12, 22);
        Calendar dateOn = Calendar.getInstance();
        dateOn.set(2017, 1, 1, 10, 0, 40);
        BankCardQueryParam queryParam = new BankCardQueryParam.Builder()
                .type(TransferMethod.TransferMethodTypes.WIRE_ACCOUNT)
                .status(VERIFIED)
                .createdAfter(dateAfter.getTime())
                .createdBefore(dateBefore.getTime())
                .createdOn(dateOn.getTime())
                .offset(offset)
                .limit(limit)
                .sortByCreatedOnAsc()
                .build();

        assertThat(queryParam.getLimit(), is(limit));
        assertThat(queryParam.getOffset(), is(offset));
        assertThat(queryParam.getType(), is(BANK_CARD));
        assertThat(queryParam.getStatus(), is(VERIFIED));
        assertThat(queryParam.getSortBy(), is(ASCENDANT_CREATE_ON));

        Calendar createdBefore = Calendar.getInstance();
        createdBefore.setTime(queryParam.getCreatedBefore());
        assertThat(createdBefore.get(Calendar.YEAR), is(2017));
        assertThat(createdBefore.get(Calendar.MONTH), is(Calendar.JANUARY));
        assertThat(createdBefore.get(Calendar.DAY_OF_MONTH), is(1));
        assertThat(createdBefore.get(Calendar.HOUR), is(10));
        assertThat(createdBefore.get(Calendar.MINUTE), is(12));
        assertThat(createdBefore.get(Calendar.SECOND), is(22));

        Calendar createdAfter = Calendar.getInstance();
        createdAfter.setTime(queryParam.getCreatedAfter());
        assertThat(createdAfter.get(Calendar.YEAR), is(2017));
        assertThat(createdAfter.get(Calendar.MONTH), is(Calendar.JANUARY));
        assertThat(createdAfter.get(Calendar.DAY_OF_MONTH), is(1));
        assertThat(createdAfter.get(Calendar.HOUR), is(0));
        assertThat(createdAfter.get(Calendar.MINUTE), is(0));
        assertThat(createdAfter.get(Calendar.SECOND), is(0));
    }

    @Test
    public void testHyperwalletBankCardQueryParam_verifyDefaultValues() {
        BankCardQueryParam queryParam = new BankCardQueryParam.Builder().build();
        assertThat(queryParam.getLimit(), is(10));
        assertThat(queryParam.getOffset(), is(0));
        assertThat(queryParam.getType(), is(BANK_CARD));
        assertThat(queryParam.getStatus(), is(nullValue()));
        assertThat(queryParam.getSortBy(), is(nullValue()));
        assertThat(queryParam.getCreatedBefore(), is(nullValue()));
        assertThat(queryParam.getCreatedAfter(), is(nullValue()));
    }

    @Test
    public void testBuildQuery_returnsQueryParameters() {
        final int offset = 100;
        final int limit = 200;

        Calendar dateAfter = Calendar.getInstance();
        dateAfter.set(2017, 0, 1, 0, 0, 0);
        Calendar dateBefore = Calendar.getInstance();
        dateBefore.set(2017, 0, 1, 10, 12, 22);
        Calendar dateOn = Calendar.getInstance();
        dateOn.set(2017, 0, 1, 10, 0, 40);
        BankCardQueryParam queryParam = new BankCardQueryParam.Builder()
                .createdAfter(dateAfter.getTime())
                .status(VERIFIED)
                .createdBefore(dateBefore.getTime())
                .createdOn(dateOn.getTime())
                .offset(offset)
                .limit(limit)
                .sortByCreatedOnAsc()
                .build();

        Map<String, String> resultQuery = queryParam.buildQuery();

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
        assertThat(resultQuery.get(CREATE_ON), is("2017-01-01T10:00:40"));
        assertThat(resultQuery.get(ACCOUNT_TYPE), is(BANK_CARD));
    }

    @Test
    public void testBuildQuery_verifyDefaultValues() {
        BankCardQueryParam queryParam = new BankCardQueryParam.Builder().build();

        Map<String, String> resultQuery = queryParam.buildQuery();
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

    @Test
    public void testBuilder_verifyValues() {
        Calendar dateAfter = Calendar.getInstance();
        dateAfter.set(2019, 6, 21, 12, 45);
        Calendar dateBefore = Calendar.getInstance();
        dateBefore.set(2019, 6, 20, 9, 10);
        Calendar dateOn = Calendar.getInstance();
        dateOn.set(2019, 6, 20, 10, 21);
        BankCardQueryParam queryParam = new BankCardQueryParam.Builder()
                .createdAfter(dateAfter.getTime())
                .status(ACTIVATED)
                .sortByCreatedOnDesc()
                .createdBefore(dateBefore.getTime())
                .offset(100)
                .createdOn(dateOn.getTime())
                .limit(20)
                .build();

        assertThat(queryParam.getOffset(), is(100));
        assertThat(queryParam.getLimit(), is(20));
        assertThat(queryParam.getSortBy(), is(DESCENDANT_CREATE_ON));
        assertThat(queryParam.getStatus(), is(ACTIVATED));
        assertThat(queryParam.getType(), is(BANK_CARD));
        assertThat(queryParam.getCreatedAfter().getTime(), is(dateAfter.getTimeInMillis()));
        assertThat(queryParam.getCreatedBefore().getTime(), is(dateBefore.getTimeInMillis()));
        assertThat(queryParam.getCreatedOn().getTime(), is(dateOn.getTimeInMillis()));
    }
}
