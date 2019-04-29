package com.hyperwallet.android.model.meta.error;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ExtensionTest {

    @Rule
    public final HyperwalletExternalResourceManager mResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testExtension_convertJsonObject() throws JSONException {
        String data = mResourceManager.getResourceContent("extension_item.json");
        JSONObject jsonObject = new JSONObject(data);

        Extension extension = new Extension(jsonObject);
        assertThat(extension.getCode(), is("DataFetchingException"));
        assertThat(extension.getTimestamp(), is("Fri Feb 9 14:33:09 UTC 2018"));
    }
}
