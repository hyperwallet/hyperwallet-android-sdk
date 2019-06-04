package com.hyperwallet.android.model.receipt;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.model.QueryParam.CREATED_AFTER;
import static com.hyperwallet.android.model.QueryParam.CREATED_BEFORE;
import static com.hyperwallet.android.model.QueryParam.PAGINATION_LIMIT;
import static com.hyperwallet.android.model.QueryParam.PAGINATION_OFFSET;
import static com.hyperwallet.android.model.QueryParam.SORT_BY;
import static com.hyperwallet.android.model.QueryParam.Sortable.ASCENDANT_AMOUNT;
import static com.hyperwallet.android.model.QueryParam.Sortable.ASCENDANT_CREATE_ON;
import static com.hyperwallet.android.model.QueryParam.Sortable.ASCENDANT_CURRENCY;
import static com.hyperwallet.android.model.QueryParam.Sortable.ASCENDANT_TYPE;
import static com.hyperwallet.android.model.QueryParam.Sortable.DESCENDANT_AMOUNT;
import static com.hyperwallet.android.model.QueryParam.Sortable.DESCENDANT_CREATE_ON;
import static com.hyperwallet.android.model.QueryParam.Sortable.DESCENDANT_CURRENCY;
import static com.hyperwallet.android.model.QueryParam.Sortable.DESCENDANT_TYPE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.CARD_ACTIVATION_FEE;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.AMOUNT;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.CREATED_ON;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.CURRENCY;
import static com.hyperwallet.android.model.receipt.ReceiptQueryParam.ReceiptQueryParamFields.TYPE;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class ReceiptQueryParamTest {

    @Test
    public void testReceiptQueryParam_verifyDefaultValues() {

        final ReceiptQueryParam.Builder<?> builder = ReceiptQueryParam.builder();
        ReceiptQueryParam receiptQueryParam = builder.build();
        assertThat(receiptQueryParam.getLimit(), is(10));
        assertThat(receiptQueryParam.getOffset(), is(0));

        Map<String, String> resultMap = receiptQueryParam.buildQuery();
        assertThat(resultMap.get(PAGINATION_LIMIT), is("10"));
        assertThat(resultMap.get(PAGINATION_OFFSET), is("0"));

        assertThat(resultMap.get(CURRENCY), is(nullValue()));
        assertThat(resultMap.get(AMOUNT), is(nullValue()));
        assertThat(resultMap.get(CREATED_ON), is(nullValue()));
        assertThat(resultMap.get(TYPE), is(nullValue()));
        assertThat(resultMap.get(SORT_BY), is(nullValue()));
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

        final ReceiptQueryParam.Builder builder = ReceiptQueryParam.builder()
                .amount("20.00")
                .createdAfter(createdAfter)
                .createdBefore(createdBefore)
                .createdOn(createdOn)
                .currency("USD")
                .limit(40)
                .offset(120)
                .sortByAmountAsc()
                .type(CARD_ACTIVATION_FEE);
        ReceiptQueryParam receiptQueryParam = builder.build();

        assertThat(receiptQueryParam.getCreatedOn(), is(createdOn));
        assertThat(receiptQueryParam.getCreatedAfter(), is(createdAfter));
        assertThat(receiptQueryParam.getCreatedBefore(), is(createdBefore));
        assertThat(receiptQueryParam.getAmount(), is("20.00"));
        assertThat(receiptQueryParam.getCurrency(), is("USD"));
        assertThat(receiptQueryParam.getLimit(), is(40));
        assertThat(receiptQueryParam.getOffset(), is(120));
        assertThat(receiptQueryParam.getType(), is(CARD_ACTIVATION_FEE));

        Map<String, String> resultDefaultQueryMap = receiptQueryParam.buildQuery();

        assertThat(resultDefaultQueryMap.get(PAGINATION_LIMIT), is("40"));
        assertThat(resultDefaultQueryMap.get(PAGINATION_OFFSET), is("120"));
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
                {ReceiptQueryParam.builder().sortByAmountAsc(), ASCENDANT_AMOUNT},
                {ReceiptQueryParam.builder().sortByAmountDesc(), DESCENDANT_AMOUNT},
                {ReceiptQueryParam.builder().sortByCreatedOnAsc(), ASCENDANT_CREATE_ON},
                {ReceiptQueryParam.builder().sortByCreatedOnDesc(), DESCENDANT_CREATE_ON},
                {ReceiptQueryParam.builder().sortByCurrencyAsc(), ASCENDANT_CURRENCY},
                {ReceiptQueryParam.builder().sortByCurrencyDesc(), DESCENDANT_CURRENCY},
                {ReceiptQueryParam.builder().sortByTypeAsc(), ASCENDANT_TYPE},
                {ReceiptQueryParam.builder().sortByTypeDesc(), DESCENDANT_TYPE}
        });
    }
}