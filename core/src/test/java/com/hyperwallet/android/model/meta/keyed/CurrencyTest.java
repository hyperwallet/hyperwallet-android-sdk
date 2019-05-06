package com.hyperwallet.android.model.meta.keyed;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.BANK_CARD;

import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class CurrencyTest {

    @Rule
    public HyperwalletExternalResourceManager mResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public final ExpectedException mThrown = ExpectedException.none();

    @Test
    public void testCurrency_convertJsonObjectWithCaD() throws JSONException, HyperwalletException {
        String data = mResourceManager.getResourceContent("currency_item.json");
        JSONObject jsonObject = new JSONObject(data);
        Currency currency = new Currency(jsonObject);
        assertThat(currency.getCode(), is("CAD"));
        assertThat(currency.getName(), is("Canadian Dollar"));
        assertThat(currency.getTransferMethodTypes(), hasSize(1));
        assertThat(currency.getTransferMethodType(BANK_ACCOUNT), is(notNullValue()));
    }

    @Test
    public void testCurrency_convertJsonObjectWithoutNodes() throws JSONException, HyperwalletException {
        String data = mResourceManager.getResourceContent("currency_without_nodes_item.json");
        JSONObject jsonObject = new JSONObject(data);
        Currency currency = new Currency(jsonObject);
        assertThat(currency.getCode(), is("CAD"));
        assertThat(currency.getName(), is("Canadian Dollar"));
        assertThat(currency.getTransferMethodTypes(), hasSize(0));
        assertThat(currency.getTransferMethodType(BANK_CARD), is(nullValue()));
    }


    @Test
    public void testCurrency_convertJsonObjectWithError()
            throws JSONException, HyperwalletException {
        mThrown.expect(HyperwalletException.class);

        String data = mResourceManager.getResourceContent("currency_item.json");
        JSONObject jsonObject = new JSONObject(data);
        jsonObject.remove("code");
        new Currency(jsonObject);
    }

    @Test
    public void testCurrency_equalsObjects() throws JSONException, HyperwalletException {
        String data = mResourceManager.getResourceContent("currency_item.json");
        JSONObject jsonObject = new JSONObject(data);
        Currency currency = new Currency(jsonObject);
        assertThat(currency.equals("another object"), is(false));

        Currency duplicateCurrency = currency;
        assertThat(currency.equals(duplicateCurrency), is(true));

        jsonObject.put("code", "USD");
        Currency anotherCodeCurrency = new Currency(jsonObject);
        assertThat(currency.equals(anotherCodeCurrency), is(false));

        jsonObject.put("code", "CAD");
        jsonObject.put("name", "US Dollar");
        Currency anotherNameCurrency = new Currency(jsonObject);
        assertThat(currency.equals(anotherNameCurrency), is(false));
    }
}