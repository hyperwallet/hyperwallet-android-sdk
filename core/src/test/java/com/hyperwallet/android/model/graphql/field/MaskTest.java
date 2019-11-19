package com.hyperwallet.android.model.graphql.field;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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

        assertThat(mask.getDefaultPattern(), is("#### #### #### ####"));
        assertThat(mask.getScrubRegex(), is("\\s"));
        assertFalse(mask.containsConditionalPattern());
        assertNull(mask.getConditionalPatterns());
    }

    @Test
    public void testHyperwalletFieldMask_convertJsonObjectWithConditionalPattern() throws JSONException {
        String jsonResponse = mExternalResourceManager.getResourceContent(
                "mask_with_conditional_formatting_response.json");
        JSONObject jsonResponseObject = new JSONObject(jsonResponse);
        HyperwalletField hyperwalletField = new HyperwalletField(jsonResponseObject);
        Mask mask = hyperwalletField.getMask();

        assertThat(mask.getDefaultPattern(), is("#### #### #### ####"));
        assertThat(mask.getScrubRegex(), is("\\s"));
        assertTrue(mask.containsConditionalPattern());
        assertThat(mask.getConditionalPatterns(), is(notNullValue()));
        assertThat(mask.getConditionalPatterns().size(), is(2));
        assertThat(mask.getConditionalPattern("653"), is("######## ####### ####"));
    }
}
