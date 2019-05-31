package com.hyperwallet.android.model.receipt;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.CARD_ACTIVATION_FEE;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.AMOUNT;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.CREATED_AFTER;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.CREATED_BEFORE;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.CREATED_ON;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.CURRENCY;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.LIMIT;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.OFFSET;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.SORT_BY;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.TYPE;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptSortables.ASCENDANT_AMOUNT;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptSortables.ASCENDANT_CREATE_ON;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptSortables.ASCENDANT_CURRENCY;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptSortables.ASCENDANT_TYPE;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptSortables.DESCENDANT_AMOUNT;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptSortables.DESCENDANT_CREATE_ON;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptSortables.DESCENDANT_CURRENCY;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptSortables.DESCENDANT_TYPE;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class ReceiptQueryParamTest {

    @Test
    public void testReceiptQueryParam_verifyDefaultValues() {

        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder();
        ReceiptQueryParam receiptQueryParam = builder.build();
        assertThat(receiptQueryParam.getLimit(), is(10));
        assertThat(receiptQueryParam.getOffset(), is(0));

        Map<String, String> resultDefaultQueryMap = receiptQueryParam.buildQuery();
        assertThat(resultDefaultQueryMap.get("limit"), is("10"));
        assertThat(resultDefaultQueryMap.get("offset"), is("0"));
    }

    @Test
    public void testReceiptQueryParam_verifyMapQueryValues() {
        Map<String, String> urlQueryMap = new HashMap<>();
        urlQueryMap.put("limit", "23");
        urlQueryMap.put("offset", "11");

        ReceiptQueryParam receiptQueryParam = new ReceiptQueryParam(urlQueryMap);
        assertThat(receiptQueryParam.getLimit(), is(23));
        assertThat(receiptQueryParam.getOffset(), is(11));
    }

    @Test
    public void testReceiptQueryParamBuilder_verifyQueryParameters() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, 5, 30, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date createdOn = calendar.getTime();
        calendar.set(2019, 4, 20);
        Date createdBefore = calendar.getTime();
        calendar.set(2018, 8, 10);
        Date createdAfter = calendar.getTime();

        final ReceiptQueryParam.Builder builder = new ReceiptQueryParam.Builder()
                .createdOn(createdOn)
                .createdBefore(createdBefore)
                .createdAfter(createdAfter)
                .sortByAmountAsc()
                .amount("20.00")
                .currency("USD")
                .type(CARD_ACTIVATION_FEE)
                .limit(40)
                .offset(120)
                .currency("USD");

        ReceiptQueryParam receiptQueryParam = builder.build();

        assertThat(receiptQueryParam.getCreatedOn().getTime(), is(createdOn.getTime()));
        assertThat(receiptQueryParam.getCreatedAfter(), is(createdAfter));
        assertThat(receiptQueryParam.getCreatedBefore(), is(createdBefore));
        assertThat(receiptQueryParam.getAmount(), is("20.00"));
        assertThat(receiptQueryParam.getCurrency(), is("USD"));
        assertThat(receiptQueryParam.getLimit(), is(40));
        assertThat(receiptQueryParam.getOffset(), is(120));
        assertThat(receiptQueryParam.getType(), is(CARD_ACTIVATION_FEE));

        Map<String, String> resultDefaultQueryMap = receiptQueryParam.buildQuery();

        assertThat(resultDefaultQueryMap.get(LIMIT), is("40"));
        assertThat(resultDefaultQueryMap.get(OFFSET), is("120"));
        assertThat(resultDefaultQueryMap.get(AMOUNT), is("20.00"));
        assertThat(resultDefaultQueryMap.get(CURRENCY), is("USD"));
        assertThat(resultDefaultQueryMap.get(TYPE), is(CARD_ACTIVATION_FEE));
        assertThat(resultDefaultQueryMap.get(CREATED_ON), is("2019-06-30T00:00:00"));
        assertThat(resultDefaultQueryMap.get(CREATED_AFTER), is("2018-09-10T00:00:00"));
        assertThat(resultDefaultQueryMap.get(CREATED_BEFORE), is("2019-05-20T00:00:00"));
    }

    @Test
    @Parameters(method = "testReceiptQueryParamBuilderSortBy")
    public void testReceiptQueryParamBuilder_verifySortOrder(final ReceiptQueryParam.Builder builder,
            final String sortBy) {
        ReceiptQueryParam receiptQueryParam = builder.build();
        assertThat(receiptQueryParam.buildQuery().get(SORT_BY), is(sortBy));
    }

    private Collection<Object[]> testReceiptQueryParamBuilderSortBy() {
        return Arrays.asList(new Object[][]{
                {new ReceiptQueryParam.Builder().sortByAmountAsc(), ASCENDANT_AMOUNT},
                {new ReceiptQueryParam.Builder().sortByAmountDesc(), DESCENDANT_AMOUNT},
                {new ReceiptQueryParam.Builder().sortByCreatedOnAsc(), ASCENDANT_CREATE_ON},
                {new ReceiptQueryParam.Builder().sortByCreatedOnDesc(), DESCENDANT_CREATE_ON},
                {new ReceiptQueryParam.Builder().sortByCurrencyAsc(), ASCENDANT_CURRENCY},
                {new ReceiptQueryParam.Builder().sortByCurrencyDesc(), DESCENDANT_CURRENCY},
                {new ReceiptQueryParam.Builder().sortByTypeAsc(), ASCENDANT_TYPE},
                {new ReceiptQueryParam.Builder().sortByTypeDesc(), DESCENDANT_TYPE}
        });
    }
}