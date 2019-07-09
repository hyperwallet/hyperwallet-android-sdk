package com.hyperwallet.android.model.graphql.keyed;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletTransferMethodConfigurationKeyResultTest {

    @Rule
    public final HyperwalletExternalResourceManager mResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public final ExpectedException mThrown = ExpectedException.none();

    @Test
    public void testTransferMethodConfigurationFieldResult_convertJsonObject()
            throws JSONException, ReflectiveOperationException {
        String data = mResourceManager.getResourceContent("tmc_get_keys_response.json");
        JSONObject jsonObject = new JSONObject(data);
        HyperwalletTransferMethodConfigurationKeyResult fieldResult =
                new HyperwalletTransferMethodConfigurationKeyResult(jsonObject);
        assertThat(fieldResult.getCountries(), hasSize(2));
        assertThat(fieldResult.getCountry("US"), is(notNullValue()));
    }

    @Test
    public void testTransferMethodConfigurationFieldResult_returnsNullWhenCountryInvalid()
            throws JSONException, ReflectiveOperationException {
        String data = mResourceManager.getResourceContent("tmc_get_keys_response.json");
        JSONObject jsonObject = new JSONObject(data);
        HyperwalletTransferMethodConfigurationKeyResult fieldResult =
                new HyperwalletTransferMethodConfigurationKeyResult(jsonObject);
        assertThat(fieldResult.getTransferMethodType("ZZ", "USD"), is(nullValue()));
        assertThat(fieldResult.getTransferMethodType("CA", "USS"), is(nullValue()));
        assertThat(fieldResult.getCurrencies("UU"), is(nullValue()));
    }
}