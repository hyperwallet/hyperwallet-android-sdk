package com.hyperwallet.android.model.meta;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.hyperwallet.android.model.meta.field.HyperwalletFieldSelectionOption;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletFieldSelectionOptionTest {

    @Rule
    public final HyperwalletExternalResourceManager mResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testHyperwalletFieldSelectionOption_convertJsonObject() throws JSONException {
        String data = mResourceManager.getResourceContent("field_selection_option_data.json");
        JSONObject jsonObject = new JSONObject(data);
        HyperwalletFieldSelectionOption option = new HyperwalletFieldSelectionOption(jsonObject);

        assertThat(option, is(notNullValue()));
        assertThat(option.getLabel(), is("The Label"));
        assertThat(option.getValue(), is("The Value"));
    }
}
