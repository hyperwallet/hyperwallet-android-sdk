package com.hyperwallet.android.model.graphql.field;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import com.hyperwallet.android.rule.ExternalResourceManager;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class TransferMethodUpdateConfigurationFieldResultTest {

    @Rule
    public final ExpectedException mThrown = ExpectedException.none();
    @Rule
    public ExternalResourceManager mResourceManager = new ExternalResourceManager();

    @Test
    public void testTransferMethodUpdateConfigurationFieldResult_convertJson()
            throws JSONException, ReflectiveOperationException {
        String data = mResourceManager.getResourceContent("tmc_update_field_bankaccount_response.json");
        JSONObject jsonObject = new JSONObject(data);
        TransferMethodUpdateConfigurationFieldResult fieldResult =
                new TransferMethodUpdateConfigurationFieldResult(jsonObject);
        assertThat(fieldResult.getFields(), is(notNullValue()));
    }


    @Test
    public void testTransferMethodConfigurationFieldResult_convertJsonWithEmptyFields()
            throws JSONException, ReflectiveOperationException {
        String data = mResourceManager.getResourceContent("tmc_update_field_empty_response.json");
        JSONObject jsonObject = new JSONObject(data);
        TransferMethodUpdateConfigurationFieldResult fieldResult =
                new TransferMethodUpdateConfigurationFieldResult(jsonObject);
        assertThat(fieldResult.getFields(), is(nullValue()));
    }

    @Test
    public void testTransferMethodConfigurationFieldResult_convertIncorrectJsonObject()
            throws JSONException, ReflectiveOperationException {
        mThrown.expect(JSONException.class);

        String data = mResourceManager.getResourceContent("field_group_item.json");
        JSONObject jsonObject = new JSONObject(data);
        TransferMethodUpdateConfigurationFieldResult fieldResult = new TransferMethodUpdateConfigurationFieldResult(jsonObject);
        assertThat(fieldResult.getFields(), is(nullValue()));
    }

}
