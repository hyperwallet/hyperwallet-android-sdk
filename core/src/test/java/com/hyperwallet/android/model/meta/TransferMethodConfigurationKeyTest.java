package com.hyperwallet.android.model.meta;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

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
public class TransferMethodConfigurationKeyTest {

    @Rule
    public final HyperwalletExternalResourceManager mResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public final ExpectedException mThrown = ExpectedException.none();

    @Test
    public void testTransferMethodConfigurationKey_convertJsonObject()
            throws JSONException, HyperwalletException {
        String data = mResourceManager.getResourceContent("tmc_key_item.json");
        JSONObject jsonObject = new JSONObject(data);
        TransferMethodConfigurationKey configurationKey =
                new TransferMethodConfigurationKey(jsonObject);
        assertThat(configurationKey.getCountries(), hasSize(1));
        assertThat(configurationKey.getCountry("CA"), is(notNullValue()));
        assertThat(configurationKey.getCountry("GU"), is(nullValue()));
    }

    @Test
    public void testTransferMethodConfigurationKey_convertIncorrectJsonObject()
            throws JSONException, HyperwalletException {
        mThrown.expect(HyperwalletException.class);

        String data = mResourceManager.getResourceContent("tmc_key_item.json");
        JSONObject jsonObject = new JSONObject(data);
        jsonObject.remove("countries");

        new TransferMethodConfigurationKey(jsonObject);

    }

    @Test
    public void testTransferMethodConfigurationKey_convertEmptyCountriesJsonObject()
            throws JSONException, HyperwalletException {
        String data = mResourceManager.getResourceContent("tmc_key_empty_nodes_item.json");
        JSONObject jsonObject = new JSONObject(data);
        TransferMethodConfigurationKey configurationKeyResult =
                new TransferMethodConfigurationKey(jsonObject);
        assertThat(configurationKeyResult.getCountries(), is(empty()));
        assertThat(configurationKeyResult.getCountry("CA"), is(nullValue()));
    }
}