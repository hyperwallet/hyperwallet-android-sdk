package com.hyperwallet.android.model.graphql.field;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.hyperwallet.android.rule.ExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class FieldSelectionOptionTest {

    @Rule
    public final ExternalResourceManager mResourceManager = new ExternalResourceManager();

    @Test
    public void testHyperwalletFieldSelectionOption_convertJsonObject() throws JSONException {
        String data = mResourceManager.getResourceContent("field_selection_option_data.json");
        JSONObject jsonObject = new JSONObject(data);
        FieldSelectionOption option = new FieldSelectionOption(jsonObject);

        assertThat(option, is(notNullValue()));
        assertThat(option.getLabel(), is("The Label"));
        assertThat(option.getValue(), is("The Value"));
    }
}
