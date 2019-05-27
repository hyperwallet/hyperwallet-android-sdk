package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BANK_ACCOUNT_ID;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BANK_ACCOUNT_PURPOSE;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BANK_ACCOUNT_RELATIONSHIP;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BANK_ID;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BANK_NAME;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BRANCH_ID;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BRANCH_NAME;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.COUNTRY_OF_BIRTH;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.COUNTRY_OF_NATIONALITY;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.DATE_OF_BIRTH;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.FIRST_NAME;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.GENDER;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.GOVERNMENT_ID;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.LAST_NAME;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.MOBILE_NUMBER;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.PHONE_NUMBER;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import com.hyperwallet.android.model.transfermethod.HyperwalletBankAccount;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletBankAccountTest {

    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testFromJsonString_bankAccountResponse() throws Exception {
        HyperwalletBankAccount actualBankAccount = fromJsonString(
                mExternalResourceManager.getResourceContent("bank_account_response.json"),
                new TypeReference<HyperwalletBankAccount>() {
                });

        assertThat(actualBankAccount, is(notNullValue()));
        assertThat(actualBankAccount.getField(BANK_ACCOUNT_ID), is("8017110254"));
        assertThat(actualBankAccount.getField(BANK_ACCOUNT_PURPOSE), is("SAVINGS"));
        assertThat(actualBankAccount.getField(BANK_ACCOUNT_RELATIONSHIP), is("SELF"));
        assertThat(actualBankAccount.getField(BANK_ID), is("211179539"));
        assertThat(actualBankAccount.getField(BANK_NAME), is("GREATER WATERBURY HEALTHCARE FCU"));
        assertThat(actualBankAccount.getField(BRANCH_ID), is("211179539"));
        assertThat(actualBankAccount.getField(BRANCH_NAME), is("TEST BRANCH"));
        assertThat(actualBankAccount.getField(TOKEN), is("trm-854c4ec1-9161-49d6-92e2-b8d15aa4bf56"));
        assertThat(actualBankAccount.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(actualBankAccount.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(actualBankAccount.getField(TYPE), is(BANK_ACCOUNT));
        assertThat(actualBankAccount.getField(COUNTRY_OF_BIRTH), is("US"));
        assertThat(actualBankAccount.getField(COUNTRY_OF_NATIONALITY), is("CA"));
        assertThat(actualBankAccount.getField(DATE_OF_BIRTH), is("1980-01-01"));
        assertThat(actualBankAccount.getField(FIRST_NAME), is("Marsden"));
        assertThat(actualBankAccount.getField(GENDER), is("MALE"));
        assertThat(actualBankAccount.getField(GOVERNMENT_ID), is("987654321"));
        assertThat(actualBankAccount.getField(LAST_NAME), is("Griffin"));
        assertThat(actualBankAccount.getField(MOBILE_NUMBER), is("604 666 6666"));
        assertThat(actualBankAccount.getField(PHONE_NUMBER), is("+1 604 6666666"));
        assertThat(actualBankAccount.getField(CREATED_ON), is(equalTo("2019-01-07T22:03:13")));
        assertThat(actualBankAccount.getField(STATUS), is(equalTo("ACTIVATED")));

    }

    @Test
    public void testToJsonObject_bankAccount() throws Exception {

        final HyperwalletBankAccount expectedBankAccount = new HyperwalletBankAccount.Builder()
                .bankAccountId("8017110254")
                .bankAccountPurpose("SAVINGS")
                .bankAccountRelationship("SELF")
                .bankId("211179539")
                .bankName("GREATER WATERBURY HEALTHCARE FCU")
                .branchId("211179539")
                .branchName("TEST BRANCH")
                .token("trm-854c4ec1-9161-49d6-92e2-b8d15aa4bf56")
                .transferMethodCountry("US")
                .transferMethodCurrency("USD")
                .countryOfBirth("US")
                .countryOfNationality("CA")
                .dateOfBirth("1980-01-01")
                .firstName("Marsden")
                .gender("MALE")
                .governmentId("987654321")
                .lastName("Griffin")
                .mobileNumber("604 666 6666")
                .phoneNumber("+1 604 6666666")
                .build();

        JSONObject jsonObject = expectedBankAccount.toJsonObject();

        assertThat(jsonObject.getString(BANK_ACCOUNT_ID), is("8017110254"));
        assertThat(jsonObject.getString(BANK_ACCOUNT_PURPOSE), is("SAVINGS"));
        assertThat(jsonObject.getString(BANK_ACCOUNT_RELATIONSHIP), is("SELF"));
        assertThat(jsonObject.getString(BANK_ID), is("211179539"));
        assertThat(jsonObject.getString(BANK_NAME), is("GREATER WATERBURY HEALTHCARE FCU"));
        assertThat(jsonObject.getString(BRANCH_NAME), is("TEST BRANCH"));
        assertThat(jsonObject.getString(TOKEN), is("trm-854c4ec1-9161-49d6-92e2-b8d15aa4bf56"));
        assertThat(jsonObject.getString(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(jsonObject.getString(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(jsonObject.getString(TYPE), is(BANK_ACCOUNT));
        assertThat(jsonObject.getString(COUNTRY_OF_BIRTH), is("US"));
        assertThat(jsonObject.getString(COUNTRY_OF_NATIONALITY), is("CA"));
        assertThat(jsonObject.getString(DATE_OF_BIRTH), is("1980-01-01"));
        assertThat(jsonObject.getString(FIRST_NAME), is("Marsden"));
        assertThat(jsonObject.getString(GENDER), is("MALE"));
        assertThat(jsonObject.getString(GOVERNMENT_ID), is("987654321"));
        assertThat(jsonObject.getString(LAST_NAME), is("Griffin"));
        assertThat(jsonObject.getString(MOBILE_NUMBER), is("604 666 6666"));
        assertThat(jsonObject.getString(PHONE_NUMBER), is("+1 604 6666666"));
    }
}


