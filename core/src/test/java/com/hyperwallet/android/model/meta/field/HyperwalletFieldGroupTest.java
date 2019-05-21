package com.hyperwallet.android.model.meta.field;

import static org.hamcrest.CoreMatchers.is;
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
public class HyperwalletFieldGroupTest {
    @Rule
    public HyperwalletExternalResourceManager mResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public final ExpectedException mThrown = ExpectedException.none();

    @Test
    public void testHyperwalletFieldGroup_convertJson() throws JSONException {
        String data = mResourceManager.getResourceContent("field_group_item.json");
        JSONObject jsonObject = new JSONObject(data);
        HyperwalletFieldGroup fieldGroup = new HyperwalletFieldGroup(jsonObject);
        assertThat(fieldGroup.getFields(), hasSize(1));
        assertThat(fieldGroup.getGroupName(), is(HyperwalletFieldGroup.GroupTypes.ADDRESS));
    }

    @Test
    public void testHyperwalletFieldGroup_convertJsonWithEmptyFields() throws JSONException {
        String data = mResourceManager.getResourceContent("field_group_empty_fields_item.json");
        JSONObject jsonObject = new JSONObject(data);
        HyperwalletFieldGroup fieldGroup = new HyperwalletFieldGroup(jsonObject);
        assertThat(fieldGroup.getFields(), is(nullValue()));
    }

    @Test
    public void testHyperwalletFieldGroup_convertIncorrectJsonObject() throws Exception {
        mThrown.expect(JSONException.class);

        String data = mResourceManager.getResourceContent("field_group_item.json");
        JSONObject jsonObject = new JSONObject(data);
        jsonObject.remove("group");

        new HyperwalletFieldGroup(jsonObject);

    }
}