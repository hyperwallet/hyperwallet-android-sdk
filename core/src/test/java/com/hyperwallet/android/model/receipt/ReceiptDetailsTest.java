package com.hyperwallet.android.model.receipt;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_CITY;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.CARD_NUMBER;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.CLIENT_PAYMENT_ID;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.MEMO;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.NOTES;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_NAME;
import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ReceiptDetailsTest {
    @Rule
    public final HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testFromJsonString_receiptDetailsResponse() throws Exception {
        final ReceiptDetails receiptDetails = fromJsonString(
                mExternalResourceManager.getResourceContent("receipt_details_item.json"),
                new TypeReference<ReceiptDetails>() {
                });
        assertThat(receiptDetails, is(notNullValue()));
        assertThat(receiptDetails.getField(CLIENT_PAYMENT_ID), is("8OxXefx5"));
        assertThat(receiptDetails.getField(CARD_NUMBER), is("************7917"));
        assertThat(receiptDetails.getField(PAYEE_NAME), is(equalTo("A Person")));
        assertThat(receiptDetails.getField(NOTES), is(equalTo("Buying something")));
        assertThat(receiptDetails.getField(MEMO), is(equalTo("Wallet required")));
        assertThat(receiptDetails.getField(BRANCH_CITY), is(nullValue()));

        assertThat(receiptDetails.getBranchCity(), is(nullValue()));
        assertThat(receiptDetails.getClientPaymentId(), is("8OxXefx5"));
        assertThat(receiptDetails.getCardNumber(), is("************7917"));
        assertThat(receiptDetails.getPayeeName(), is(equalTo("A Person")));
        assertThat(receiptDetails.getMemo(), is(equalTo("Wallet required")));
        assertThat(receiptDetails.getNotes(), is(equalTo("Buying something")));

        assertThat(receiptDetails.getBankAccountId(), is(nullValue()));
        assertThat(receiptDetails.getBankAccountPurpose(), is(nullValue()));
        assertThat(receiptDetails.getBankId(), is(nullValue()));
        assertThat(receiptDetails.getBankName(), is(nullValue()));
        assertThat(receiptDetails.getBranchAddressLine1(), is(nullValue()));
        assertThat(receiptDetails.getBranchAddressLine2(), is(nullValue()));
        assertThat(receiptDetails.getBranchCity(), is(nullValue()));
        assertThat(receiptDetails.getBranchCountry(), is(nullValue()));
        assertThat(receiptDetails.getBranchId(), is(nullValue()));
        assertThat(receiptDetails.getBranchName(), is(nullValue()));
        assertThat(receiptDetails.getBranchPostalCode(), is(nullValue()));
        assertThat(receiptDetails.getBranchStateProvince(), is(nullValue()));
        assertThat(receiptDetails.getCardExpiryDate(), is(nullValue()));
        assertThat(receiptDetails.getCardHolderName(), is(nullValue()));
        assertThat(receiptDetails.getCharityName(), is(nullValue()));
        assertThat(receiptDetails.getCheckNumber(), is(nullValue()));
        assertThat(receiptDetails.getPayeeAddressLine1(), is(nullValue()));
        assertThat(receiptDetails.getPayeeAddressLine2(), is(nullValue()));
        assertThat(receiptDetails.getPayeeCity(), is(nullValue()));
        assertThat(receiptDetails.getPayeeCountry(), is(nullValue()));
        assertThat(receiptDetails.getPayeeEmail(), is(nullValue()));
        assertThat(receiptDetails.getPayeePostalCode(), is(nullValue()));
        assertThat(receiptDetails.getPayeeStateProvince(), is(nullValue()));
        assertThat(receiptDetails.getPayerName(), is(nullValue()));
        assertThat(receiptDetails.getPaymentExpiryDate(), is(nullValue()));
        assertThat(receiptDetails.getReturnOrRecallReason(), is(nullValue()));
        assertThat(receiptDetails.getSecurityQuestion(), is(nullValue()));
        assertThat(receiptDetails.getSecurityAnswer(), is(nullValue()));
        assertThat(receiptDetails.getWebsite(), is(nullValue()));
    }
}