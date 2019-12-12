package com.hyperwallet.android.model.graphql;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.hyperwallet.android.rule.ExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PageInfoTest {
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();

    @Test
    public void testPageInfo_convertJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject(mExternalResourceManager.getResourceContent(
                "page_info.json"));

        PageInfo pageInfo = new PageInfo(jsonObject);
        assertThat(pageInfo.getLimit(), is(20));
        assertThat(pageInfo.getOffset(), is(30));
    }
}
