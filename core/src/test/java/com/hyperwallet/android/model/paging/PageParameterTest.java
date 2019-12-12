package com.hyperwallet.android.model.paging;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.hyperwallet.android.rule.ExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PageParameterTest {

    @Rule
    public ExternalResourceManager externalResourceManager = new ExternalResourceManager();


    @Test
    public void testHyperwalletPageParameter_convertJsonObject() throws JSONException {
        String pageParameter = externalResourceManager.getResourceContent("page_parameter.json");
        JSONObject pageParameterJSON = new JSONObject(pageParameter);

        PageParameter params = new PageParameter(pageParameterJSON);
        assertThat(params.getRel(), is("self"));
    }

}
