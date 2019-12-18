package com.hyperwallet.android.model.graphql.field;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import com.hyperwallet.android.rule.ExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class FieldGroupTest {
    @Rule
    public final ExpectedException mThrown = ExpectedException.none();
    @Rule
    public ExternalResourceManager mResourceManager = new ExternalResourceManager();

    @Test
    public void testFieldGroup_convertJson() throws JSONException {
        String data = mResourceManager.getResourceContent("field_group_item.json");
        JSONObject jsonObject = new JSONObject(data);
        FieldGroup fieldGroup = new FieldGroup(jsonObject);
        assertThat(fieldGroup.getFields(), hasSize(1));
        assertThat(fieldGroup.getGroupName(), is(FieldGroup.GroupTypes.ADDRESS));
    }

    @Test
    public void testFieldGroup_convertJsonWithEmptyFields() throws JSONException {
        String data = mResourceManager.getResourceContent("field_group_empty_fields_item.json");
        JSONObject jsonObject = new JSONObject(data);
        FieldGroup fieldGroup = new FieldGroup(jsonObject);
        assertThat(fieldGroup.getFields(), is(nullValue()));
    }

    @Test
    public void testFieldGroup_convertIncorrectJsonObject() throws Exception {
        mThrown.expect(JSONException.class);

        String data = mResourceManager.getResourceContent("field_group_item.json");
        JSONObject jsonObject = new JSONObject(data);
        jsonObject.remove("group");

        new FieldGroup(jsonObject);

    }
}