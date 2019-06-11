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
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.INTERMEDIARY_BANK_ACCOUNT_ID;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.INTERMEDIARY_BANK_ADDRESS_LINE_1;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.INTERMEDIARY_BANK_ADDRESS_LINE_2;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.INTERMEDIARY_BANK_CITY;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.INTERMEDIARY_BANK_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.INTERMEDIARY_BANK_ID;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.INTERMEDIARY_BANK_NAME;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.INTERMEDIARY_BANK_POSTAL_CODE;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.INTERMEDIARY_BANK_STATE_PROVINCE;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.LAST_NAME;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.MOBILE_NUMBER;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.PHONE_NUMBER;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.WIRE_INSTRUCTIONS;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.WIRE_ACCOUNT;
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

    @Test
    public void testFromJsonString_wireAccountResponse() throws Exception {
        HyperwalletBankAccount actualBankAccount = fromJsonString(
                mExternalResourceManager.getResourceContent("bank_account_wire_response.json"),
                new TypeReference<HyperwalletBankAccount>() {
                });

        assertThat(actualBankAccount, is(notNullValue()));
        assertThat(actualBankAccount.getField(BANK_ACCOUNT_ID), is("765432108"));
        assertThat(actualBankAccount.getField(BANK_ACCOUNT_PURPOSE), is("CHECKING"));
        assertThat(actualBankAccount.getField(BANK_ACCOUNT_RELATIONSHIP), is("SELF"));
        assertThat(actualBankAccount.getField(BRANCH_ID), is("012004567"));
        assertThat(actualBankAccount.getField(TOKEN), is("trm-12345"));
        assertThat(actualBankAccount.getField(TYPE), is(WIRE_ACCOUNT));
        assertThat(actualBankAccount.getField(DATE_OF_BIRTH), is("1991-01-01"));
        assertThat(actualBankAccount.getField(FIRST_NAME), is("Tommy"));
        assertThat(actualBankAccount.getField(LAST_NAME), is("Gray"));
        assertThat(actualBankAccount.getField(CREATED_ON), is(equalTo("2018-10-31T16:47:15")));
        assertThat(actualBankAccount.getField(STATUS), is(equalTo("ACTIVATED")));

        assertThat(actualBankAccount.getField(INTERMEDIARY_BANK_ACCOUNT_ID), is(equalTo("246810")));
        assertThat(actualBankAccount.getField(INTERMEDIARY_BANK_ADDRESS_LINE_1), is(equalTo("5 Market Street")));
        assertThat(actualBankAccount.getField(INTERMEDIARY_BANK_ADDRESS_LINE_2), is(equalTo("75 Market Street")));
        assertThat(actualBankAccount.getField(INTERMEDIARY_BANK_CITY), is(equalTo("New York")));
        assertThat(actualBankAccount.getField(INTERMEDIARY_BANK_COUNTRY), is(equalTo("US")));
        assertThat(actualBankAccount.getField(INTERMEDIARY_BANK_ID), is(equalTo("12345678901")));
        assertThat(actualBankAccount.getField(INTERMEDIARY_BANK_NAME), is(equalTo("Intermediary Big Bank")));
        assertThat(actualBankAccount.getField(INTERMEDIARY_BANK_POSTAL_CODE), is(equalTo("134679")));
        assertThat(actualBankAccount.getField(INTERMEDIARY_BANK_STATE_PROVINCE), is(equalTo("PA")));
        assertThat(actualBankAccount.getField(WIRE_INSTRUCTIONS), is(equalTo("This is instruction")));
    }

    @Test
    public void testToJsonObject_wireAccount() throws Exception {

        final HyperwalletBankAccount expectedBankAccount = new HyperwalletBankAccount.Builder()
                .bankAccountId("8017110254")
                .bankAccountPurpose("SAVINGS")
                .token("trm-123")
                .transferMethodCountry("US")
                .transferMethodCurrency("USD")
                .dateOfBirth("1980-01-01")
                .firstName("Marsden")
                .lastName("Griffin")
                .transferMethodType(WIRE_ACCOUNT)
                .intermediaryBankAccountId("012345678")
                .intermediaryBankAddressLine1("21 5th Avenue")
                .intermediaryBankAddressLine2("1 Penny Avenue")
                .intermediaryBankCity("New York")
                .intermediaryBankCountry("US")
                .intermediaryBankId("01234567891")
                .intermediaryBankName("Secondary Bank of the USA")
                .intermediaryBankPostalCode("974518")
                .intermediaryBankStateProvince("PA")
                .wireInstructions("Some instructions")
                .build();

        JSONObject jsonObject = expectedBankAccount.toJsonObject();

        assertThat(jsonObject.getString(BANK_ACCOUNT_ID), is("8017110254"));
        assertThat(jsonObject.getString(BANK_ACCOUNT_PURPOSE), is("SAVINGS"));
        assertThat(jsonObject.getString(TYPE), is(WIRE_ACCOUNT));
        assertThat(jsonObject.getString(TOKEN), is("trm-123"));
        assertThat(jsonObject.getString(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(jsonObject.getString(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(jsonObject.getString(DATE_OF_BIRTH), is("1980-01-01"));
        assertThat(jsonObject.getString(FIRST_NAME), is("Marsden"));
        assertThat(jsonObject.getString(LAST_NAME), is("Griffin"));

        assertThat(jsonObject.getString(INTERMEDIARY_BANK_ACCOUNT_ID), is(equalTo("012345678")));
        assertThat(jsonObject.getString(INTERMEDIARY_BANK_ADDRESS_LINE_1), is(equalTo("21 5th Avenue")));
        assertThat(jsonObject.getString(INTERMEDIARY_BANK_ADDRESS_LINE_2), is(equalTo("1 Penny Avenue")));
        assertThat(jsonObject.getString(INTERMEDIARY_BANK_CITY), is(equalTo("New York")));
        assertThat(jsonObject.getString(INTERMEDIARY_BANK_COUNTRY), is(equalTo("US")));
        assertThat(jsonObject.getString(INTERMEDIARY_BANK_ID), is(equalTo("01234567891")));
        assertThat(jsonObject.getString(INTERMEDIARY_BANK_NAME), is(equalTo("Secondary Bank of the USA")));
        assertThat(jsonObject.getString(INTERMEDIARY_BANK_POSTAL_CODE), is(equalTo("974518")));
        assertThat(jsonObject.getString(INTERMEDIARY_BANK_STATE_PROVINCE), is(equalTo("PA")));
        assertThat(jsonObject.getString(WIRE_INSTRUCTIONS), is(equalTo("Some instructions")));
    }
}


