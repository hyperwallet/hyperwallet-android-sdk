package com.hyperwallet.android.model.transfer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.model.QueryParam.CREATED_AFTER;
import static com.hyperwallet.android.model.QueryParam.CREATED_BEFORE;
import static com.hyperwallet.android.model.QueryParam.PAGINATION_LIMIT;
import static com.hyperwallet.android.model.QueryParam.PAGINATION_OFFSET;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.CLIENT_TRANSFER_ID;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.DESTINATION_TOKEN;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.SOURCE_TOKEN;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
public class TransferQueryParamTest {

    @Test
    public void testTransferQueryParam_verifyDefaultValues() {

        final TransferQueryParam.Builder builder = new TransferQueryParam.Builder();
        TransferQueryParam transferQueryParam = builder.build();
        assertThat(transferQueryParam.getLimit(), is(10));
        assertThat(transferQueryParam.getOffset(), is(0));

        Map<String, String> resultMap = transferQueryParam.buildQuery();
        assertThat(resultMap.get(PAGINATION_LIMIT), is("10"));
        assertThat(resultMap.get(PAGINATION_OFFSET), is("0"));
        assertThat(resultMap.get(CLIENT_TRANSFER_ID), is(nullValue()));
        assertThat(resultMap.get(SOURCE_TOKEN), is(nullValue()));
        assertThat(resultMap.get(DESTINATION_TOKEN), is(nullValue()));
        assertThat(resultMap.get(CREATED_AFTER), is(nullValue()));
        assertThat(resultMap.get(CREATED_BEFORE), is(nullValue()));
    }

    @Test
    public void testTransferQueryParamBuilder_verifyQueryParameters() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, 6, 20, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date createdBefore = calendar.getTime();
        calendar.set(2019, 1, 10);
        Date createdAfter = calendar.getTime();

        final TransferQueryParam.Builder builder = new TransferQueryParam.Builder()
                .createdAfter(createdAfter)
                .createdBefore(createdBefore)
                .limit(40)
                .offset(120)
                .clientTransferId("123000")
                .sourceToken("test-user-token")
                .destinationToken("trm-fake-token");
        TransferQueryParam transferQueryParam = builder.build();

        assertThat(transferQueryParam.getCreatedAfter(), is(createdAfter));
        assertThat(transferQueryParam.getCreatedBefore(), is(createdBefore));
        assertThat(transferQueryParam.getLimit(), is(40));
        assertThat(transferQueryParam.getOffset(), is(120));
        assertThat(transferQueryParam.getClientTransferId(), is("123000"));
        assertThat(transferQueryParam.getSourceToken(), is("test-user-token"));
        assertThat(transferQueryParam.getDestinationToken(), is("trm-fake-token"));

        Map<String, String> resultDefaultQueryMap = transferQueryParam.buildQuery();

        assertThat(resultDefaultQueryMap.get(PAGINATION_LIMIT), is("40"));
        assertThat(resultDefaultQueryMap.get(PAGINATION_OFFSET), is("120"));
        assertThat(resultDefaultQueryMap.get(CREATED_AFTER), is("2019-02-10T00:00:00"));
        assertThat(resultDefaultQueryMap.get(CREATED_BEFORE), is("2019-07-20T00:00:00"));
        assertThat(resultDefaultQueryMap.get(CLIENT_TRANSFER_ID), is("123000"));
        assertThat(resultDefaultQueryMap.get(SOURCE_TOKEN), is("test-user-token"));
        assertThat(resultDefaultQueryMap.get(DESTINATION_TOKEN), is("trm-fake-token"));
    }
}