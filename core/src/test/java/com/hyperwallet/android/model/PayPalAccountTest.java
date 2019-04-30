package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.EMAIL;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.PAYPAL_ACCOUNT;
import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PayPalAccountTest {
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testFromJsonString_payPalAccountResponse() throws Exception {
        PayPalAccount actualPayPalAccount = fromJsonString(
                mExternalResourceManager.getResourceContent("paypal_account_response.json"),
                new TypeReference<PayPalAccount>() {
                });

        assertThat(actualPayPalAccount, is(notNullValue()));
        assertThat(actualPayPalAccount.getField(TOKEN), is("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b"));
        assertThat(actualPayPalAccount.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(actualPayPalAccount.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(actualPayPalAccount.getField(TYPE), is(PAYPAL_ACCOUNT));
        assertThat(actualPayPalAccount.getField(CREATED_ON), is(equalTo("2019-01-09T22:50:14")));
        assertThat(actualPayPalAccount.getField(EMAIL), is(equalTo("jsmith@paypal.com")));
        assertThat(actualPayPalAccount.getField(STATUS), is(equalTo("ACTIVATED")));
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
