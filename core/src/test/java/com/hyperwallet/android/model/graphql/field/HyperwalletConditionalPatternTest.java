package com.hyperwallet.android.model.graphql.field;

import static org.hamcrest.CoreMatchers.is;
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
public class HyperwalletConditionalPatternTest {

    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testHyperwalletConditionalPattern_convertJsonObject() throws JSONException {
        String jsonResponse = mExternalResourceManager.getResourceContent(
                "conditional_pattern_response.json");
        JSONObject jsonResponseObject = new JSONObject(jsonResponse);
        HyperwalletConditionalPattern hyperwalletConditionalPattern = new HyperwalletConditionalPattern(
                jsonResponseObject);

        assertThat(hyperwalletConditionalPattern, is(notNullValue()));
        assertThat(hyperwalletConditionalPattern.getPattern(), is("#### ### ############"));
        assertThat(hyperwalletConditionalPattern.getRegex(), is("^4[53]"));
    }
}
