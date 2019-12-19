package com.hyperwallet.android.model.graphql.error;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.hyperwallet.android.rule.ExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class LocationTest {

    @Rule
    public ExternalResourceManager externalResourceManager = new ExternalResourceManager();

    @Test
    public void testLocation_convertJsonObject() throws JSONException {
        String pageParameter = externalResourceManager.getResourceContentError("location_error_response.json");
        JSONObject pageParameterJSON = new JSONObject(pageParameter);
        Location location = new Location(pageParameterJSON);

        assertThat(location.getColumn(), is(7));
        assertThat(location.getLine(), is(2));
    }
}
