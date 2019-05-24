package com.hyperwallet.android.model.paging;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletPageLinkTest {

    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testHyperwalletPageLink_convertJsonObject() throws JSONException {
        String json = mExternalResourceManager.getResourceContent("page_link_response.json");
        JSONObject jsonObject = new JSONObject(json);
        HyperwalletPageLink hyperwalletPageLink = new HyperwalletPageLink(jsonObject);

        assertThat(hyperwalletPageLink, is(notNullValue()));
        assertThat(hyperwalletPageLink.getPageParameter(), is(notNullValue()));
        assertThat(hyperwalletPageLink.getPageParameter().getRel(), is("self"));
//        assertThat(hyperwalletPageLink.getPageHref(),
//                is("https://api.sandbox.hyperwallet.com/rest/v3/users/usr-2973b883-e694-4cda-8ba5-359ac23bddc5"));
    }
}