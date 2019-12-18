package com.hyperwallet.android.model.paging;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.hyperwallet.android.rule.ExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PageLinkTest {

    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();

    @Test
    public void testPageLink_convertJsonObject() throws JSONException {
        String json = mExternalResourceManager.getResourceContent("page_link_response.json");
        JSONObject jsonObject = new JSONObject(json);
        PageLink pageLink = new PageLink(jsonObject);

        assertThat(pageLink, is(notNullValue()));
        assertThat(pageLink.getPageParameter(), is(notNullValue()));
        assertThat(pageLink.getPageParameter().getRel(), is("self"));
        assertThat(pageLink.getPageHref(),
                is("https://api.sandbox.hyperwallet.com/rest/v3/users/usr-2973b883-e694-4cda-8ba5-359ac23bddc5"));
    }
}