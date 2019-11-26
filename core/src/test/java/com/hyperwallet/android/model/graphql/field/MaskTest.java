package com.hyperwallet.android.model.graphql.field;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class MaskTest {

    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testHyperwalletFieldMask_convertJsonObjectWithoutConditionalPattern() throws JSONException {
        String jsonResponse = mExternalResourceManager.getResourceContent(
                "mask_without_conditional_formatting_response.json");
        JSONObject jsonResponseObject = new JSONObject(jsonResponse);
        HyperwalletField hyperwalletField = new HyperwalletField(jsonResponseObject);
        Mask mask = hyperwalletField.getMask();

        assertThat(mask, is(notNullValue()));
        assertThat(mask.getScrubRegex(), is("\\s"));
        assertThat(mask.getConditionalPatterns(), is(nullValue()));
    }

    @Test
    public void testHyperwalletFieldMask_convertJsonObjectWithConditionalPattern() throws JSONException {
        String jsonResponse = mExternalResourceManager.getResourceContent(
                "mask_with_conditional_formatting_response.json");
        JSONObject jsonResponseObject = new JSONObject(jsonResponse);
        HyperwalletField hyperwalletField = new HyperwalletField(jsonResponseObject);
        Mask mask = hyperwalletField.getMask();

        assertThat(mask, is(notNullValue()));
        assertThat(mask.getScrubRegex(), is("\\s"));
        assertThat(mask.getConditionalPatterns(), is(notNullValue()));
        assertThat(mask.getConditionalPatterns().size(), is(2));
    }

    @Test
    public void testGetConditionalPattern_returnsDefaultPattern() throws JSONException {
        String jsonResponse = mExternalResourceManager.getResourceContent(
                "mask_with_conditional_formatting_response.json");
        JSONObject jsonResponseObject = new JSONObject(jsonResponse);
        HyperwalletField hyperwalletField = new HyperwalletField(jsonResponseObject);
        Mask mask = hyperwalletField.getMask();

        assertThat(mask, is(notNullValue()));
        assertThat(mask.getPattern("613"), is("#### #### #### ####"));
    }

    @Test
    public void testGetConditionalPattern_returnsPattern() throws JSONException {
        String jsonResponse = mExternalResourceManager.getResourceContent(
                "mask_with_conditional_formatting_response.json");
        JSONObject jsonResponseObject = new JSONObject(jsonResponse);
        HyperwalletField hyperwalletField = new HyperwalletField(jsonResponseObject);
        Mask mask = hyperwalletField.getMask();

        assertThat(mask, is(notNullValue()));
        assertThat(mask.getPattern("653"), is("######## ####### ####"));
    }
}
