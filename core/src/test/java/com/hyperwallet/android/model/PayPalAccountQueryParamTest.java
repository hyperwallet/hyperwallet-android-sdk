package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.QueryParam.Sortable.ASCENDANT_CREATE_ON;
import static com.hyperwallet.android.model.QueryParam.Sortable.ASCENDANT_STATUS;
import static com.hyperwallet.android.model.QueryParam.Sortable.DESCENDANT_CREATE_ON;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.PAYPAL_ACCOUNT;

import com.hyperwallet.android.model.transfermethod.PayPalAccountQueryParam;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Map;

public class PayPalAccountQueryParamTest {
    private final static String OFFSET = "offset";
    private final static String LIMIT = "limit";
    private final static String CREATE_BEFORE = "createdBefore";
    private final static String CREATE_AFTER = "createdAfter";
    private final static String CREATE_ON = "createdOn";
    private final static String TRANSFER_METHOD_TYPE = "type";
    private final static String STATUS = "status";
    private final static String SORT_BY = "sortBy";


    @Test
    public void testHyperwalletPayPalAccountQueryParam_withUrlQueryMap() {
        Calendar createdBefore = Calendar.getInstance();
        createdBefore.set(2017, 0, 1, 10, 12, 22);
        Calendar createdAfter = Calendar.getInstance();
        createdAfter.set(2017, 0, 1, 0, 0, 0);
        Calendar createdOn = Calendar.getInstance();
        createdOn.set(2017, 0, 1, 10, 10, 0);

        PayPalAccountQueryParam pagination = new PayPalAccountQueryParam.Builder()
                .offset(100)
                .limit(200)
                .createdBefore(createdBefore.getTime())
                .createdAfter(createdAfter.getTime())
                .createdOn(createdOn.getTime())
                .status(ACTIVATED)
                .sortByStatusAsc()
                .build();

        assertThat(pagination.getLimit(), is(200));
        assertThat(pagination.getOffset(), is(100));
        assertThat(pagination.getType(), is(PAYPAL_ACCOUNT));
        assertThat(pagination.getStatus(), is(ACTIVATED));
        assertThat(pagination.getSortBy(), is(ASCENDANT_STATUS));

        createdBefore = Calendar.getInstance();
        createdBefore.setTime(pagination.getCreatedBefore());
        assertThat(createdBefore.get(Calendar.YEAR), is(2017));
        assertThat(createdBefore.get(Calendar.MONTH), is(Calendar.JANUARY));
        assertThat(createdBefore.get(Calendar.DAY_OF_MONTH), is(1));
        assertThat(createdBefore.get(Calendar.HOUR), is(10));
        assertThat(createdBefore.get(Calendar.MINUTE), is(12));
        assertThat(createdBefore.get(Calendar.SECOND), is(22));

        createdAfter = Calendar.getInstance();
        createdAfter.setTime(pagination.getCreatedAfter());
        assertThat(createdAfter.get(Calendar.YEAR), is(2017));
        assertThat(createdAfter.get(Calendar.MONTH), is(Calendar.JANUARY));
        assertThat(createdAfter.get(Calendar.DAY_OF_MONTH), is(1));
        assertThat(createdAfter.get(Calendar.HOUR), is(0));
        assertThat(createdAfter.get(Calendar.MINUTE), is(0));
        assertThat(createdAfter.get(Calendar.SECOND), is(0));

        createdOn = Calendar.getInstance();
        createdOn.setTime(pagination.getCreatedOn());
        assertThat(createdOn.get(Calendar.YEAR), is(2017));
        assertThat(createdOn.get(Calendar.MONTH), is(Calendar.JANUARY));
        assertThat(createdOn.get(Calendar.DAY_OF_MONTH), is(1));
        assertThat(createdOn.get(Calendar.HOUR), is(10));
        assertThat(createdOn.get(Calendar.MINUTE), is(10));
        assertThat(createdOn.get(Calendar.SECOND), is(0));
    }

    @Test
    public void testHyperwalletPayPalAccountQueryParam_verifyDefaultValues() {
        PayPalAccountQueryParam pagination = new PayPalAccountQueryParam.Builder().build();
        assertThat(pagination.getLimit(), is(10));
        assertThat(pagination.getOffset(), is(0));
        assertThat(pagination.getType(), is(PAYPAL_ACCOUNT));
        assertThat(pagination.getStatus(), is(nullValue()));
        assertThat(pagination.getSortBy(), is(nullValue()));
        assertThat(pagination.getCreatedBefore(), is(nullValue()));
        assertThat(pagination.getCreatedAfter(), is(nullValue()));
    }

    @Test
    public void testBuildQuery_verifyDefaultValues() {
        QueryParam pagination = new PayPalAccountQueryParam.Builder().build();

        Map<String, String> query = pagination.buildQuery();

        Assert.assertNotNull(query);
        assertThat(query.size(), is(3));
        assertThat(query.get(OFFSET), is("0"));
        assertThat(query.get(LIMIT), is("10"));
        assertThat(query.get(TRANSFER_METHOD_TYPE), is(PAYPAL_ACCOUNT));
        assertThat(query.get(STATUS), is(nullValue()));
        assertThat(query.get(SORT_BY), is(nullValue()));
        assertThat(query.get(CREATE_BEFORE), is(nullValue()));
        assertThat(query.get(CREATE_AFTER), is(nullValue()));
        assertThat(query.get(CREATE_ON), is(nullValue()));
    }

    @Test
    public void testBuildQuery_returnsQueryParameters() {
        Calendar createdBefore = Calendar.getInstance();
        createdBefore.set(2017, 0, 1, 10, 12, 22);
        Calendar createdAfter = Calendar.getInstance();
        createdAfter.set(2017, 0, 1, 0, 0, 0);
        Calendar createdOn = Calendar.getInstance();
        createdOn.set(2017, 0, 1, 10, 10, 0);

        PayPalAccountQueryParam pagination = new PayPalAccountQueryParam.Builder()
                .offset(100)
                .limit(200)
                .createdBefore(createdBefore.getTime())
                .createdAfter(createdAfter.getTime())
                .createdOn(createdOn.getTime())
                .status(ACTIVATED)
                .sortByCreatedOnAsc()
                .build();
        Map<String, String> resultQuery = pagination.buildQuery();

        assertThat(resultQuery.containsKey(STATUS), is(true));
        assertThat(resultQuery.containsKey(SORT_BY), is(true));
        assertThat(resultQuery.containsKey(OFFSET), is(true));
        assertThat(resultQuery.containsKey(LIMIT), is(true));
        assertThat(resultQuery.containsKey(CREATE_BEFORE), is(true));
        assertThat(resultQuery.containsKey(CREATE_AFTER), is(true));
        assertThat(resultQuery.containsKey(TRANSFER_METHOD_TYPE), is(true));

        assertThat(resultQuery.get(LIMIT), is("200"));
        assertThat(resultQuery.get(OFFSET), is("100"));
        assertThat(resultQuery.get(STATUS), is(ACTIVATED));
        assertThat(resultQuery.get(SORT_BY), is(ASCENDANT_CREATE_ON));
        assertThat(resultQuery.get(CREATE_BEFORE), is("2017-01-01T10:12:22"));
        assertThat(resultQuery.get(CREATE_AFTER), is("2017-01-01T00:00:00"));
        assertThat(resultQuery.get(CREATE_ON), is("2017-01-01T10:10:00"));
        assertThat(resultQuery.get(TRANSFER_METHOD_TYPE), is(PAYPAL_ACCOUNT));
    }

    @Test
    public void testBuilder_verifyValues() {
        Calendar dateAfter = Calendar.getInstance();
        dateAfter.set(2019, 6, 21, 12, 45);
        Calendar dateBefore = Calendar.getInstance();
        dateBefore.set(2019, 6, 20, 9, 10);
        Calendar dateOn = Calendar.getInstance();
        dateOn.set(2019, 6, 20, 10, 21);
        PayPalAccountQueryParam pagination = new PayPalAccountQueryParam.Builder()
                .createdAfter(dateAfter.getTime())
                .createdBefore(dateBefore.getTime())
                .createdOn(dateOn.getTime())
                .offset(100)
                .limit(20)
                .sortByCreatedOnDesc()
                .status(ACTIVATED)
                .build();

        assertThat(pagination.getOffset(), is(100));
        assertThat(pagination.getLimit(), is(20));
        assertThat(pagination.getSortBy(), is(DESCENDANT_CREATE_ON));
        assertThat(pagination.getStatus(), is(ACTIVATED));
        assertThat(pagination.getType(), is(PAYPAL_ACCOUNT));
        assertThat(pagination.getCreatedAfter().getTime(), is(dateAfter.getTimeInMillis()));
        assertThat(pagination.getCreatedBefore().getTime(), is(dateBefore.getTimeInMillis()));
        assertThat(pagination.getCreatedOn().getTime(), is(dateOn.getTimeInMillis()));
    }
}
