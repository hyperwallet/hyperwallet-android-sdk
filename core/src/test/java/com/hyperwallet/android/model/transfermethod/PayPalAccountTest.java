package com.hyperwallet.android.model.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.EMAIL;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.PAYPAL_ACCOUNT;
import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.rule.ExternalResourceManager;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PayPalAccountTest {
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();

    @Test
    public void testFromJsonString_payPalAccountResponse() throws Exception {
        PayPalAccount actualPayPalAccount = fromJsonString(
                mExternalResourceManager.getResourceContent("paypal_account_response.json"),
                new TypeReference<PayPalAccount>() {
                });

        assertThat(actualPayPalAccount, is(notNullValue()));
        assertThat(actualPayPalAccount.getField(TOKEN), is("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b"));
        assertThat(actualPayPalAccount.getCountry(), is("US"));
        assertThat(actualPayPalAccount.getCurrency(), is("USD"));
        assertThat(actualPayPalAccount.getField(TYPE), is(PAYPAL_ACCOUNT));
        assertThat(actualPayPalAccount.getField(CREATED_ON), is("2019-01-09T22:50:14"));
        assertThat(actualPayPalAccount.getEmail(), is("jsmith@paypal.com"));
        assertThat(actualPayPalAccount.getField(STATUS), is("ACTIVATED"));
    }

    @Test
    public void testToJsonObject_payPalAccount() throws Exception {

        final PayPalAccount payPalAccount = new PayPalAccount.Builder()
                .transferMethodCountry("US")
                .transferMethodCurrency("USD")
                .email("jsmith@paypal.com")
                .token("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b")
                .build();

        JSONObject jsonObject = payPalAccount.toJsonObject();

        assertThat(jsonObject.getString(TOKEN), is("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b"));
        assertThat(jsonObject.getString(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(jsonObject.getString(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(jsonObject.getString(TYPE), is(PAYPAL_ACCOUNT));
        assertThat(jsonObject.getString(EMAIL), is("jsmith@paypal.com"));
    }
}
