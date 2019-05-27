package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethodPagination.TransferMethodSortable.ASCENDANT_CREATE_ON;

import com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethodPagination;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HyperwalletTransferMethodPaginationTest {
    private final static String OFFSET = "offset";
    private final static String LIMIT = "limit";
    private final static String CREATE_BEFORE = "createdBefore";
    private final static String CREATE_AFTER = "createdAfter";
    private final static String TRANSFER_METHOD_TYPE = "type";
    private final static String STATUS = "status";
    private final static String SORT_BY = "sortBy";


    @Test
    public void testHyperwalletTransferMethodPagination_withUrlQueryMap() {
        Map<String, String> query = new HashMap<>();
        query.put(OFFSET, "100");
        query.put(LIMIT, "200");
        query.put(CREATE_BEFORE, "2017-01-01T10:12:22");
        query.put(CREATE_AFTER, "2017-01-01T00:00:000");
        query.put(TRANSFER_METHOD_TYPE, BANK_ACCOUNT);
        query.put(STATUS, ACTIVATED);
        query.put(SORT_BY, ASCENDANT_CREATE_ON);

        HyperwalletTransferMethodPagination pagination = new HyperwalletTransferMethodPagination(query);

        assertThat(pagination.getLimit(), is(200));
        assertThat(pagination.getOffset(), is(100));
        assertThat(pagination.getType(), is(BANK_ACCOUNT));
        assertThat(pagination.getStatus(), is(ACTIVATED));
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
    public void testHyperwalletTransferMethodPagination_verifyDefaultValues() {

        HyperwalletTransferMethodPagination pagination = new HyperwalletTransferMethodPagination();
        assertThat(pagination.getLimit(), is(10));
        assertThat(pagination.getOffset(), is(0));
        assertThat(pagination.getType(), is(nullValue()));
        assertThat(pagination.getStatus(), is(nullValue()));
        assertThat(pagination.getSortBy(), is(nullValue()));
        assertThat(pagination.getCreatedBefore(), is(nullValue()));
        assertThat(pagination.getCreatedAfter(), is(nullValue()));

    }


    @Test
    public void testBuildQuery_verifyDefaultValues() {
        HyperwalletPagination pagination = new HyperwalletPagination();

        Map<String, String> query = pagination.buildQuery();

        Assert.assertNotNull(query);
        assertThat(query.size(), is(2));
        assertThat(query.get(OFFSET), is("0"));
        assertThat(query.get(LIMIT), is("10"));
        assertThat(query.get(TRANSFER_METHOD_TYPE), is(nullValue()));
        assertThat(query.get(STATUS), is(nullValue()));
        assertThat(query.get(SORT_BY), is(nullValue()));
        assertThat(query.get(CREATE_BEFORE), is(nullValue()));
        assertThat(query.get(CREATE_AFTER), is(nullValue()));
        assertThat(query.get(TRANSFER_METHOD_TYPE), is(nullValue()));

    }


    @Test
    public void testBuildQuery_returnsQueryParameters() {

        Map<String, String> query = new HashMap<>();
        query.put(OFFSET, "100");
        query.put(LIMIT, "200");
        query.put(CREATE_BEFORE, "2017-01-01T10:12:22");
        query.put(CREATE_AFTER, "2017-01-01T00:00:000");
        query.put(TRANSFER_METHOD_TYPE, BANK_ACCOUNT);
        query.put(STATUS, ACTIVATED);
        query.put(SORT_BY, ASCENDANT_CREATE_ON);

        HyperwalletTransferMethodPagination pagination = new HyperwalletTransferMethodPagination(query);
        Map<String, String> resultQuery = pagination.buildQuery();

        assertThat(resultQuery.containsKey(STATUS), is(true));
        assertThat(resultQuery.containsKey(SORT_BY), is(true));
        assertThat(resultQuery.containsKey(OFFSET), is(true));
        assertThat(resultQuery.containsKey(LIMIT), is(true));
        assertThat(resultQuery.containsKey(CREATE_BEFORE), is(true));
        assertThat(resultQuery.containsKey(CREATE_AFTER), is(true));
        assertThat(resultQuery.containsKey(TRANSFER_METHOD_TYPE), is(true));

        assertThat(resultQuery.get(LIMIT), is(String.valueOf("200")));
        assertThat(resultQuery.get(OFFSET), is(String.valueOf("100")));
        assertThat(resultQuery.get(STATUS), is(ACTIVATED));
        assertThat(resultQuery.get(SORT_BY), is(ASCENDANT_CREATE_ON));
        assertThat(resultQuery.get(CREATE_BEFORE), is("2017-01-01T10:12:22"));
        assertThat(resultQuery.get(CREATE_AFTER), is("2017-01-01T00:00:00"));
        assertThat(resultQuery.get(TRANSFER_METHOD_TYPE), is(BANK_ACCOUNT));

    }


}
