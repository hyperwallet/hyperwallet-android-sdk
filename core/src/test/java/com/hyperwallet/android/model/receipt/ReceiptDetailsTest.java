package com.hyperwallet.android.model.receipt;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BANK_ACCOUNT_ID;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BANK_ACCOUNT_PURPOSE;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BANK_ID;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BANK_NAME;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_ADDRESS_LINE_1;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_ADDRESS_LINE_2;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_CITY;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_COUNTRY;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_ID;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.BRANCH_NAME;
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
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYER_NAME;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.PAYMENT_EXPIRY_DATE;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.RETURN_OR_RECALL_REASON;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.SECURITY_ANSWER;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.SECURITY_QUESTION;
import static com.hyperwallet.android.model.receipt.ReceiptDetails.ReceiptDetailsFields.WEBSITE;
import static com.hyperwallet.android.model.transfermethod.BankAccount.Purpose.CHECKING;
import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import android.os.Parcel;

import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.rule.ExternalResourceManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ReceiptDetailsTest {
    @Rule
    public final ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();

    @Test
    public void testFromJsonString_receiptDetailsResponse() throws Exception {
        final ReceiptDetails receiptDetails = fromJsonString(
                mExternalResourceManager.getResourceContent("receipt_details_item.json"),
                new TypeReference<ReceiptDetails>() {
                });
        assertThat(receiptDetails, is(notNullValue()));
        assertThat(receiptDetails.getField(BANK_ACCOUNT_ID), is("123456789"));
        assertThat(receiptDetails.getField(BANK_ACCOUNT_PURPOSE), is(CHECKING));
        assertThat(receiptDetails.getField(BANK_ID), is("654321"));
        assertThat(receiptDetails.getField(BANK_NAME), is("Some bank name"));
        assertThat(receiptDetails.getField(BRANCH_ADDRESS_LINE_1), is("Branch Address 1"));
        assertThat(receiptDetails.getField(BRANCH_ADDRESS_LINE_2), is("Branch Address 2"));
        assertThat(receiptDetails.getField(BRANCH_CITY), is("New York"));
        assertThat(receiptDetails.getField(BRANCH_COUNTRY), is("USA"));
        assertThat(receiptDetails.getField(BRANCH_ID), is("1357"));
        assertThat(receiptDetails.getField(BRANCH_NAME), is("Name of branch"));
        assertThat(receiptDetails.getField(BRANCH_POSTAL_CODE), is("10123"));
        assertThat(receiptDetails.getField(BRANCH_STATE_PROVINCE), is("PA"));
        assertThat(receiptDetails.getField(CARD_EXPIRY_DATE), is("2020-11"));
        assertThat(receiptDetails.getField(CARD_HOLDER_NAME), is("Holder"));
        assertThat(receiptDetails.getField(CARD_NUMBER), is("************7917"));
        assertThat(receiptDetails.getField(CHARITY_NAME), is("Charity"));
        assertThat(receiptDetails.getField(CHECK_NUMBER), is("246"));
        assertThat(receiptDetails.getField(CLIENT_PAYMENT_ID), is("8OxXefx5"));
        assertThat(receiptDetails.getField(MEMO), is(equalTo("Wallet required")));
        assertThat(receiptDetails.getField(NOTES), is(equalTo("Buying something")));
        assertThat(receiptDetails.getField(PAYEE_ADDRESS_LINE_1), is(equalTo("Payee address 1")));
        assertThat(receiptDetails.getField(PAYEE_ADDRESS_LINE_2), is(equalTo("Payee address 2")));
        assertThat(receiptDetails.getField(PAYEE_CITY), is(equalTo("San Fransisco")));
        assertThat(receiptDetails.getField(PAYEE_COUNTRY), is(equalTo("USA")));
        assertThat(receiptDetails.getField(PAYEE_EMAIL), is(equalTo("payee@email.com")));
        assertThat(receiptDetails.getField(PAYEE_NAME), is(equalTo("A Person")));
        assertThat(receiptDetails.getField(PAYEE_POSTAL_CODE), is(equalTo("2424")));
        assertThat(receiptDetails.getField(PAYEE_STATE_PROVINCE), is(equalTo("CA")));
        assertThat(receiptDetails.getField(PAYER_NAME), is(equalTo("Best Store")));
        assertThat(receiptDetails.getField(PAYMENT_EXPIRY_DATE), is(equalTo("2019-06-08")));
        assertThat(receiptDetails.getField(RETURN_OR_RECALL_REASON), is(equalTo("Product is not valid")));
        assertThat(receiptDetails.getField(SECURITY_ANSWER), is(equalTo("Some answer")));
        assertThat(receiptDetails.getField(SECURITY_QUESTION), is(equalTo("Some question?")));
        assertThat(receiptDetails.getField(WEBSITE), is(nullValue()));

        assertThat(receiptDetails.getBankAccountId(), is("123456789"));
        assertThat(receiptDetails.getBankAccountPurpose(), is(CHECKING));
        assertThat(receiptDetails.getBankId(), is("654321"));
        assertThat(receiptDetails.getBankName(), is("Some bank name"));
        assertThat(receiptDetails.getBranchAddressLine1(), is("Branch Address 1"));
        assertThat(receiptDetails.getBranchAddressLine2(), is("Branch Address 2"));
        assertThat(receiptDetails.getBranchCity(), is("New York"));
        assertThat(receiptDetails.getBranchCountry(), is("USA"));
        assertThat(receiptDetails.getBranchId(), is("1357"));
        assertThat(receiptDetails.getBranchName(), is("Name of branch"));
        assertThat(receiptDetails.getBranchPostalCode(), is("10123"));
        assertThat(receiptDetails.getBranchStateProvince(), is("PA"));
        assertThat(receiptDetails.getCardExpiryDate(), is("2020-11"));
        assertThat(receiptDetails.getCardHolderName(), is("Holder"));
        assertThat(receiptDetails.getCardNumber(), is("************7917"));
        assertThat(receiptDetails.getCharityName(), is("Charity"));
        assertThat(receiptDetails.getCheckNumber(), is("246"));
        assertThat(receiptDetails.getClientPaymentId(), is("8OxXefx5"));
        assertThat(receiptDetails.getMemo(), is(equalTo("Wallet required")));
        assertThat(receiptDetails.getNotes(), is(equalTo("Buying something")));
        assertThat(receiptDetails.getPayeeAddressLine1(), is("Payee address 1"));
        assertThat(receiptDetails.getPayeeAddressLine2(), is("Payee address 2"));
        assertThat(receiptDetails.getPayeeCity(), is("San Fransisco"));
        assertThat(receiptDetails.getPayeeCountry(), is("USA"));
        assertThat(receiptDetails.getPayeeEmail(), is("payee@email.com"));
        assertThat(receiptDetails.getPayeeName(), is(equalTo("A Person")));
        assertThat(receiptDetails.getPayeePostalCode(), is("2424"));
        assertThat(receiptDetails.getPayeeStateProvince(), is("CA"));
        assertThat(receiptDetails.getPayerName(), is("Best Store"));
        assertThat(receiptDetails.getPaymentExpiryDate(), is("2019-06-08"));
        assertThat(receiptDetails.getReturnOrRecallReason(), is("Product is not valid"));
        assertThat(receiptDetails.getSecurityAnswer(), is("Some answer"));
        assertThat(receiptDetails.getSecurityQuestion(), is("Some question?"));
        assertThat(receiptDetails.getWebsite(), is(nullValue()));
    }

    @Test
    public void testReceiptDetail_isParcelable() throws Exception {
        String json = mExternalResourceManager.getResourceContent("receipt_details_item.json");
        ReceiptDetails sourceReceiptDetails = fromJsonString(json, new TypeReference<ReceiptDetails>() {
        });

        Parcel parcel = Parcel.obtain();
        sourceReceiptDetails.writeToParcel(parcel, sourceReceiptDetails.describeContents());
        parcel.setDataPosition(0);
        ReceiptDetails receiptDetails = ReceiptDetails.CREATOR.createFromParcel(parcel);

        assertThat(receiptDetails.getBankAccountId(), is("123456789"));
        assertThat(receiptDetails.getBankAccountPurpose(), is(CHECKING));
        assertThat(receiptDetails.getBankId(), is("654321"));
        assertThat(receiptDetails.getBankName(), is("Some bank name"));
        assertThat(receiptDetails.getBranchAddressLine1(), is("Branch Address 1"));
        assertThat(receiptDetails.getBranchAddressLine2(), is("Branch Address 2"));
        assertThat(receiptDetails.getBranchCity(), is("New York"));
        assertThat(receiptDetails.getBranchCountry(), is("USA"));
        assertThat(receiptDetails.getBranchId(), is("1357"));
        assertThat(receiptDetails.getBranchName(), is("Name of branch"));
        assertThat(receiptDetails.getBranchPostalCode(), is("10123"));
        assertThat(receiptDetails.getBranchStateProvince(), is("PA"));
        assertThat(receiptDetails.getCardExpiryDate(), is("2020-11"));
        assertThat(receiptDetails.getCardHolderName(), is("Holder"));
        assertThat(receiptDetails.getCardNumber(), is("************7917"));
        assertThat(receiptDetails.getCharityName(), is("Charity"));
        assertThat(receiptDetails.getCheckNumber(), is("246"));
        assertThat(receiptDetails.getClientPaymentId(), is("8OxXefx5"));
        assertThat(receiptDetails.getMemo(), is(equalTo("Wallet required")));
        assertThat(receiptDetails.getNotes(), is(equalTo("Buying something")));
        assertThat(receiptDetails.getPayeeAddressLine1(), is("Payee address 1"));
        assertThat(receiptDetails.getPayeeAddressLine2(), is("Payee address 2"));
        assertThat(receiptDetails.getPayeeCity(), is("San Fransisco"));
        assertThat(receiptDetails.getPayeeCountry(), is("USA"));
        assertThat(receiptDetails.getPayeeEmail(), is("payee@email.com"));
        assertThat(receiptDetails.getPayeeName(), is(equalTo("A Person")));
        assertThat(receiptDetails.getPayeePostalCode(), is("2424"));
        assertThat(receiptDetails.getPayeeStateProvince(), is("CA"));
        assertThat(receiptDetails.getPayerName(), is("Best Store"));
        assertThat(receiptDetails.getPaymentExpiryDate(), is("2019-06-08"));
        assertThat(receiptDetails.getReturnOrRecallReason(), is("Product is not valid"));
        assertThat(receiptDetails.getSecurityAnswer(), is("Some answer"));
        assertThat(receiptDetails.getSecurityQuestion(), is("Some question?"));
        assertThat(receiptDetails.getWebsite(), is(nullValue()));
    }
}