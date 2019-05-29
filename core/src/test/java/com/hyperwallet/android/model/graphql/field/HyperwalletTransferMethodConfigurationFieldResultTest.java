package com.hyperwallet.android.model.graphql.field;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletTransferMethodConfigurationFieldResultTest {

    @Rule
    public HyperwalletExternalResourceManager mResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public final ExpectedException mThrown = ExpectedException.none();

    @Test
    public void testTransferMethodConfigurationFieldResult_convertJson()
            throws JSONException, ReflectiveOperationException {
        String data = mResourceManager.getResourceContent("tmc_field_response.json");
        JSONObject jsonObject = new JSONObject(data);
        HyperwalletTransferMethodConfigurationFieldResult fieldResult =
                new HyperwalletTransferMethodConfigurationFieldResult(jsonObject);
        assertThat(fieldResult.getFees(), hasSize(1));
        assertThat(fieldResult.getFields(), is(notNullValue()));
    }

    @Test
    public void testTransferMethodConfigurationFieldResult_convertJsonWithEmptyFields()
            throws JSONException, ReflectiveOperationException {
        String data = mResourceManager.getResourceContent("tmc_field_empty_node_response.json");
        JSONObject jsonObject = new JSONObject(data);
        jsonObject.remove("nodes");
        HyperwalletTransferMethodConfigurationFieldResult fieldResult =
                new HyperwalletTransferMethodConfigurationFieldResult(jsonObject);
        assertThat(fieldResult.getFields(), is(nullValue()));
    }

    @Test
    public void testTransferMethodConfigurationFieldResult_convertIncorrectJsonObject()
            throws JSONException, ReflectiveOperationException {
        mThrown.expect(JSONException.class);

        String data = mResourceManager.getResourceContent("field_group_item.json");
        JSONObject jsonObject = new JSONObject(data);
        new HyperwalletTransferMethodConfigurationFieldResult(jsonObject);

    }
}