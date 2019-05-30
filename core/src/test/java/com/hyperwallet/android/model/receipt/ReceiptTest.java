package com.hyperwallet.android.model.receipt;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.AMOUNT;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.CREATED_ON;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.CURRENCY;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.DESTINATION_TOKEN;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.DETAILS;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.ENTRY;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.FEE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.FOREIGN_EXCHANGE_CURRENCY;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.FOREIGN_EXCHANGE_RATE;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.JOURNAL_ID;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.SOURCE_TOKEN;
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptFields.TYPE;
import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ReceiptTest {
    @Rule
    public final HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testFromJsonString_receiptResponse() throws Exception {
        Receipt receipt = fromJsonString(
                mExternalResourceManager.getResourceContent("receipt_item.json"),
                new TypeReference<Receipt>() {
                });

        assertThat(receipt, is(notNullValue()));
        assertThat(receipt.getField(JOURNAL_ID), is("3051579"));
        assertThat(receipt.getField(TYPE), is(Receipt.ReceiptTypes.PAYMENT));
        assertThat(receipt.getField(CREATED_ON), is(equalTo("2017-11-01T17:08:58")));
        assertThat(receipt.getField(ENTRY), is(equalTo(Receipt.Entries.CREDIT)));
        assertThat(receipt.getField(SOURCE_TOKEN), is(equalTo("act-123")));
        assertThat(receipt.getField(DESTINATION_TOKEN), is(equalTo("usr-123")));
        assertThat(receipt.getField(AMOUNT), is(equalTo("20.00")));
        assertThat(receipt.getField(FEE), is(equalTo("0.00")));
        assertThat(receipt.getField(CURRENCY), is(equalTo("USD")));
        ReceiptDetails receiptDetails = receipt.getDetails();
        assertThat(receiptDetails, is(notNullValue()));
        assertThat(receiptDetails.getClientPaymentId(), is(equalTo("8OxXefx5")));
        assertThat(receiptDetails.getPayeeName(), is(equalTo("A Person")));
    }

    @Test
    public void testToJsonObject_receiptDetails() throws Exception {

        ReceiptDetails.Builder detailsBuilder = new ReceiptDetails.Builder()
                .clientPaymentId("8OxXefx5")
                .payeeName("A Person");
        ReceiptDetails receiptDetails = detailsBuilder.build();

        Receipt.Builder builder = new Receipt.Builder()
                .journalId("8017110254")
                .type(Receipt.ReceiptTypes.TRANSFER_TO_PREPAID_CARD)
                .createdOn("2017-11-01T17:08:58")
                .currency("USD")
                .entry(Receipt.Entries.DEBIT)
                .sourceToken("act-123")
                .destinationToken("usr-123")
                .amount("12.03")
                .fee("0.02")
                .foreignExchangeRate("0.32")
                .foreignExchangeCurrency("CAD")
                .currency("USD")
                .details(receiptDetails);

        final Receipt receipt = builder.build();

        final JSONObject jsonObject = receipt.toJsonObject();

        assertThat(jsonObject.getString(JOURNAL_ID), is("8017110254"));
        assertThat(jsonObject.getString(TYPE), is(Receipt.ReceiptTypes.TRANSFER_TO_PREPAID_CARD));
        assertThat(jsonObject.getString(CREATED_ON), is("2017-11-01T17:08:58"));
        assertThat(jsonObject.getString(ENTRY), is(Receipt.Entries.DEBIT));
        assertThat(jsonObject.getString(SOURCE_TOKEN), is("act-123"));
        assertThat(jsonObject.getString(DESTINATION_TOKEN), is("usr-123"));
        assertThat(jsonObject.getString(AMOUNT), is("12.03"));
        assertThat(jsonObject.getString(FEE), is("0.02"));
        assertThat(jsonObject.getString(FOREIGN_EXCHANGE_RATE), is("0.32"));
        assertThat(jsonObject.getString(FOREIGN_EXCHANGE_CURRENCY), is("CAD"));
        assertThat(jsonObject.getString(CURRENCY), is("USD"));
        assertThat(jsonObject.getString(DETAILS), is("{\"payeeName\":\"A Person\",\"clientPaymentId\":\"8OxXefx5\"}"));

    }
}