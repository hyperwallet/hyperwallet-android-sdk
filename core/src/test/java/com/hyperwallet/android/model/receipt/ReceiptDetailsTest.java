package com.hyperwallet.android.model.receipt;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_ADDRESS_LINE_1;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_ADDRESS_LINE_2;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_CITY;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_COUNTRY;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_POSTAL_CODE;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_STATE_PROVINCE;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.CARD_EXPIRY_DATE;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.CARD_HOLDER_NAME;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.CARD_NUMBER;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.CHARITY_NAME;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.CHECK_NUMBER;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.CLIENT_PAYMENT_ID;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.MEMO;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.NOTES;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_ADDRESS_LINE_1;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_ADDRESS_LINE_2;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_CITY;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_COUNTRY;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_EMAIL;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_NAME;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_POSTAL_CODE;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYEE_STATE_PROVINCE;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYMENT_EXPIRY_DATE;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.RETURN_OR_RECALL_REASON;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.SECURITY_ANSWER;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.SECURITY_QUESTION;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.WEBSITE;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BANK_ACCOUNT_ID;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BANK_ACCOUNT_PURPOSE;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BANK_ID;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BANK_NAME;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BRANCH_ID;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BRANCH_NAME;
import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONObject;
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
    }

    @Test
    public void testToJsonObject_receiptDetails() throws Exception {

        ReceiptDetails.Builder builder = new ReceiptDetails.Builder()
                .bankAccountId("8017110254")
                .bankAccountPurpose("SAVINGS")
                .bankId("211179539")
                .bankName("GREATER WATERBURY HEALTHCARE FCU")
                .branchAddressLine1("Address 1")
                .branchAddressLine2("Address 2")
                .branchCity("New York")
                .branchCountry("US")
                .branchId("211179539")
                .branchName("TEST BRANCH")
                .branchPostalCode("W123456")
                .branchStateProvince("PA")
                .cardNumber("123456")
                .cardExpiryDate("2021-12")
                .cardHolderName("CardHolder")
                .charityName("Mars")
                .checkNumber("321654")
                .clientPaymentId("1357")
                .memo("Some memo")
                .notes("Some notes")
                .payeeAddressLine1("Payee Address 1")
                .payeeAddressLine2("Payee Address 2")
                .payeeCity("San Fransisco")
                .payeeCountry("CA")
                .payeeEmail("payed@email.com")
                .payeeName("Payed Name")
                .payeePostalCode("A2468")
                .payeeStateProvince("CA")
                .paymentExpiryDate("2020-03-24")
                .returnOrRecallReason("Some reason")
                .securityAnswer("Yes")
                .securityQuestion("Security?")
                .website("www.some.site.com");

        final ReceiptDetails receiptDetails = builder.build();

        final JSONObject jsonObject = receiptDetails.toJsonObject();

        assertThat(jsonObject.getString(BANK_ACCOUNT_ID), is("8017110254"));
        assertThat(jsonObject.getString(BANK_ACCOUNT_PURPOSE), is("SAVINGS"));
        assertThat(jsonObject.getString(BANK_ID), is("211179539"));
        assertThat(jsonObject.getString(BANK_NAME), is("GREATER WATERBURY HEALTHCARE FCU"));
        assertThat(jsonObject.getString(BRANCH_ADDRESS_LINE_1), is("Address 1"));
        assertThat(jsonObject.getString(BRANCH_ADDRESS_LINE_2), is("Address 2"));
        assertThat(jsonObject.getString(BRANCH_CITY), is("New York"));
        assertThat(jsonObject.getString(BRANCH_COUNTRY), is("US"));
        assertThat(jsonObject.getString(BRANCH_ID), is("211179539"));
        assertThat(jsonObject.getString(BRANCH_NAME), is("TEST BRANCH"));
        assertThat(jsonObject.getString(BRANCH_POSTAL_CODE), is("W123456"));
        assertThat(jsonObject.getString(BRANCH_STATE_PROVINCE), is("PA"));
        assertThat(jsonObject.getString(CARD_NUMBER), is("123456"));
        assertThat(jsonObject.getString(CARD_EXPIRY_DATE), is("2021-12"));
        assertThat(jsonObject.getString(CARD_HOLDER_NAME), is("CardHolder"));
        assertThat(jsonObject.getString(CHARITY_NAME), is("Mars"));
        assertThat(jsonObject.getString(CHECK_NUMBER), is("321654"));
        assertThat(jsonObject.getString(CLIENT_PAYMENT_ID), is("1357"));
        assertThat(jsonObject.getString(MEMO), is("Some memo"));
        assertThat(jsonObject.getString(NOTES), is("Some notes"));
        assertThat(jsonObject.getString(PAYEE_ADDRESS_LINE_1), is("Payee Address 1"));
        assertThat(jsonObject.getString(PAYEE_ADDRESS_LINE_2), is("Payee Address 2"));
        assertThat(jsonObject.getString(PAYEE_CITY), is("San Fransisco"));
        assertThat(jsonObject.getString(PAYEE_COUNTRY), is("CA"));
        assertThat(jsonObject.getString(PAYEE_EMAIL), is("payed@email.com"));
        assertThat(jsonObject.getString(PAYEE_NAME), is("Payed Name"));
        assertThat(jsonObject.getString(PAYEE_POSTAL_CODE), is("A2468"));
        assertThat(jsonObject.getString(PAYEE_STATE_PROVINCE), is("CA"));
        assertThat(jsonObject.getString(PAYMENT_EXPIRY_DATE), is("2020-03-24"));
        assertThat(jsonObject.getString(RETURN_OR_RECALL_REASON), is("Some reason"));
        assertThat(jsonObject.getString(SECURITY_ANSWER), is("Yes"));
        assertThat(jsonObject.getString(SECURITY_QUESTION), is("Security?"));
        assertThat(jsonObject.getString(WEBSITE), is("www.some.site.com"));
    }
}