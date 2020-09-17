package com.hyperwallet.android.model.transfermethod;

import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.rule.ExternalResourceManager;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.VENMO_ACCOUNT_ID;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.VENMO_ACCOUNT;
import static com.hyperwallet.android.util.JsonUtils.fromJsonString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(RobolectricTestRunner.class)
public class VenmoAccountTest {
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();

    @Test
    public void testFromJsonString_venmoAccountResponse() throws Exception {
        VenmoAccount actualVenmoAccount = fromJsonString(
                mExternalResourceManager.getResourceContent("venmo_account_response.json"),
                new TypeReference<VenmoAccount>() {
                });

        assertThat(actualVenmoAccount, is(notNullValue()));
        assertThat(actualVenmoAccount.getField(TOKEN), is("trm-fake-token"));
        assertThat(actualVenmoAccount.getCountry(), is("US"));
        assertThat(actualVenmoAccount.getCurrency(), is("USD"));
        assertThat(actualVenmoAccount.getField(TYPE), is(VENMO_ACCOUNT));
        assertThat(actualVenmoAccount.getField(CREATED_ON), is("2019-01-09T22:50:14"));
        assertThat(actualVenmoAccount.getAccountId(), is("9876543210"));
        assertThat(actualVenmoAccount.getField(STATUS), is("ACTIVATED"));
    }

    @Test
    public void testToJsonObject_venmoAccount() throws Exception {

        final VenmoAccount venmoAccount = new VenmoAccount.Builder()
                .transferMethodCountry("US")
                .transferMethodCurrency("USD")
                .accountId("9876543210")
                .token("trm-fake-token")
                .build();

        JSONObject jsonObject = venmoAccount.toJsonObject();

        assertThat(jsonObject.getString(TOKEN), is("trm-fake-token"));
        assertThat(jsonObject.getString(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(jsonObject.getString(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(jsonObject.getString(TYPE), is(VENMO_ACCOUNT));
        assertThat(jsonObject.getString(VENMO_ACCOUNT_ID), is("9876543210"));
    }


}
