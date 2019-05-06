package com.hyperwallet.android.model.meta.keyed;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

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
public class CountryTest {

    @Rule
    public HyperwalletExternalResourceManager mResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public final ExpectedException mThrown = ExpectedException.none();

    @Test
    public void testCountry_convertValidJsonObject() throws JSONException, HyperwalletException {
        String data = mResourceManager.getResourceContent("country_item.json");
        JSONObject jsonObject = new JSONObject(data);
        Country country = new Country(jsonObject);
        assertThat(country.getCode(), is("CA"));
        assertThat(country.getName(), is("CANADA"));
        assertThat(country.getCurrencies(), hasSize(1));
        assertThat(country.getCurrency("USD"), is(notNullValue()));
    }

    @Test
    public void testCountry_convertJsonObjectWithoutNodes() throws JSONException, HyperwalletException {
        String data = mResourceManager.getResourceContent("country_without_nodes_item.json");
        JSONObject jsonObject = new JSONObject(data);
        Country country = new Country(jsonObject);
        assertThat(country.getCode(), is("CA"));
        assertThat(country.getName(), is("CANADA"));
        assertThat(country.getCurrencies(), hasSize(0));
        assertThat(country.getCurrency("USD"), is(nullValue()));
    }

    @Test
    public void testCountry_convertJsonObjectWithError()
            throws JSONException, HyperwalletException {
        mThrown.expect(HyperwalletException.class);

        String data = mResourceManager.getResourceContent("country_item.json");
        JSONObject jsonObject = new JSONObject(data);
        jsonObject.remove("code");
        new Country(jsonObject);
    }

    @Test
    public void testCountry_equalsObject() throws JSONException, HyperwalletException {
        String data = mResourceManager.getResourceContent("country_item.json");
        JSONObject jsonObject = new JSONObject(data);
        Country country = new Country(jsonObject);
        assertThat(country.equals("some object"), is(false));

        Country duplicateCountry = country;
        assertThat(country.equals(duplicateCountry), is(true));

        jsonObject.put("code", "GI");
        Country anotherCodeCountry = new Country(jsonObject);
        assertThat(country.equals(anotherCodeCountry), is(false));

        jsonObject.put("code", "CA");
        jsonObject.put("name", "GUATEMALA");
        Country anotherNameCountry = new Country(jsonObject);
        assertThat(country.equals(anotherNameCountry), is(false));
    }
}