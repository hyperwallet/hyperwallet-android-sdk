package com.hyperwallet.android.model.paging;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletPageParameterTest {

    @Rule
    public HyperwalletExternalResourceManager externalResourceManager = new HyperwalletExternalResourceManager();


    @Test
    public void testHyperwalletPageParameter_convertJsonObject() throws JSONException {
        String pageParameter = externalResourceManager.getResourceContent("page_parameter.json");
        JSONObject pageParameterJSON = new JSONObject(pageParameter);

        HyperwalletPageParameter params = new HyperwalletPageParameter(pageParameterJSON);
        assertThat(params.getRel(), is("self"));
    }

}
