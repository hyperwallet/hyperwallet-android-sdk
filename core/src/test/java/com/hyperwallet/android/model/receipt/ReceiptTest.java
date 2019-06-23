package com.hyperwallet.android.model.receipt;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import static com.hyperwallet.android.model.receipt.Receipt.Entries.CREDIT;
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
import static com.hyperwallet.android.model.receipt.Receipt.ReceiptTypes.PAYMENT;
import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import android.os.Parcel;

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
        assertThat(receipt.getField(TYPE), is(PAYMENT));
        assertThat(receipt.getField(CREATED_ON), is(equalTo("2017-11-01T17:08:58")));
        assertThat(receipt.getField(ENTRY), is(equalTo(CREDIT)));
        assertThat(receipt.getField(SOURCE_TOKEN), is(equalTo("act-123")));
        assertThat(receipt.getField(DESTINATION_TOKEN), is(equalTo("usr-123")));
        assertThat(receipt.getField(AMOUNT), is(equalTo("20.00")));
        assertThat(receipt.getField(FEE), is(equalTo("0.00")));
        assertThat(receipt.getField(CURRENCY), is(equalTo("USD")));
        assertThat(receipt.getField(FOREIGN_EXCHANGE_CURRENCY), is("CAD"));
        assertThat(receipt.getField(FOREIGN_EXCHANGE_RATE), is("15.20"));

        ReceiptDetails receiptDetails = receipt.getDetails();
        assertThat(receiptDetails, is(notNullValue()));
        assertThat(receiptDetails.getClientPaymentId(), is(equalTo("8OxXefx5")));
        assertThat(receiptDetails.getPayeeName(), is(equalTo("A Person")));

        assertThat(receipt.getJournalId(), is("3051579"));
        assertThat(receipt.getType(), is(PAYMENT));
        assertThat(receipt.getCreatedOn(), is(equalTo("2017-11-01T17:08:58")));
        assertThat(receipt.getEntry(), is(equalTo(CREDIT)));
        assertThat(receipt.getSourceToken(), is(equalTo("act-123")));
        assertThat(receipt.getDestinationToken(), is(equalTo("usr-123")));
        assertThat(receipt.getAmount(), is(equalTo("20.00")));
        assertThat(receipt.getFee(), is(equalTo("0.00")));
        assertThat(receipt.getCurrency(), is(equalTo("USD")));
        assertThat(receipt.getForeignExchangeCurrency(), is("CAD"));
        assertThat(receipt.getForeignExchangeRate(), is("15.20"));
    }

    @Test
    public void testFromJsonString_receiptResponseWithoutDetails() throws Exception {
        Receipt receipt = fromJsonString(
                mExternalResourceManager.getResourceContent("receipt_item_without_details.json"),
                new TypeReference<Receipt>() {
                });

        assertThat(receipt, is(notNullValue()));

        ReceiptDetails receiptDetails = receipt.getDetails();
        assertThat(receiptDetails, is(nullValue()));
        assertThat(receipt.getField(DETAILS), is(nullValue()));
    }

    @Test
    public void testReceipt_isParcelable() throws Exception {
        String json = mExternalResourceManager.getResourceContent("receipt_item.json");
        Receipt sourceReceipt = fromJsonString(json, new TypeReference<Receipt>() {
        });

        Parcel parcel = Parcel.obtain();
        sourceReceipt.writeToParcel(parcel, sourceReceipt.describeContents());
        parcel.setDataPosition(0);
        Receipt bundleReceipt = Receipt.CREATOR.createFromParcel(parcel);

        assertThat(bundleReceipt, is(notNullValue()));
        assertThat(bundleReceipt.getJournalId(), is("3051579"));
        assertThat(bundleReceipt.getType(), is(PAYMENT));
        assertThat(bundleReceipt.getCreatedOn(), is(equalTo("2017-11-01T17:08:58")));
        assertThat(bundleReceipt.getEntry(), is(equalTo(CREDIT)));
        assertThat(bundleReceipt.getSourceToken(), is(equalTo("act-123")));
        assertThat(bundleReceipt.getDestinationToken(), is(equalTo("usr-123")));
        assertThat(bundleReceipt.getAmount(), is(equalTo("20.00")));
        assertThat(bundleReceipt.getFee(), is(equalTo("0.00")));
        assertThat(bundleReceipt.getCurrency(), is(equalTo("USD")));
        assertThat(bundleReceipt.getForeignExchangeCurrency(), is("CAD"));
        assertThat(bundleReceipt.getForeignExchangeRate(), is("15.20"));

        ReceiptDetails receiptDetails = bundleReceipt.getDetails();
        assertThat(receiptDetails, is(notNullValue()));
        assertThat(receiptDetails.getClientPaymentId(), is(equalTo("8OxXefx5")));
        assertThat(receiptDetails.getPayeeName(), is(equalTo("A Person")));
    }

    @Test
    public void testFromJsonString_equals() throws Exception {
        Receipt sampleReceipt = new Receipt(new JSONObject("{\n"
                + "  \"entry\": \"CREDIT\",\n"
                + "  \"journalId\": \"3051579\",\n"
                + "  \"type\": \"PAYMENT\",\n"
                + "  \"createdOn\": \"2019-07-01T17:08:58\"\n"
                + "}"));

        Receipt debitReceipt = new Receipt(new JSONObject("{\n"
                + "  \"entry\": \"DEBIT\",\n"
                + "  \"journalId\": \"3051579\",\n"
                + "  \"type\": \"PAYMENT\",\n"
                + "  \"createdOn\": \"2019-07-01T17:08:58\"\n"
                + "}"));

        assertThat(sampleReceipt.equals(debitReceipt), is(false));
    }
}