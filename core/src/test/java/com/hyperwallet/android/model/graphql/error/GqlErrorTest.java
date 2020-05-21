package com.hyperwallet.android.model.graphql.error;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.hyperwallet.android.rule.ExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class GqlErrorTest {

    @Rule
    public final ExternalResourceManager mResourceManager = new ExternalResourceManager();

    @Test
    public void testGqlErrors_convertJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject(mResourceManager.getResourceContentError(
                "graphql_error_response.json"));
        GqlError graphQlError = new GqlError(jsonObject);


        assertThat(graphQlError.getMessage(), is("Could not find any currency."));

        Extension extension = graphQlError.getExtensions();
        assertThat(extension, is(notNullValue()));
        assertThat(extension.getCode(), is("DataFetchingException"));
        assertThat(extension.getTimestamp(), is("Fri Feb 9 14:33:09 UTC 2018"));

        List<Location> locations = graphQlError.getLocations();
        assertThat(locations.size(), is(1));
        Location location = locations.get(0);
        assertThat(location, is(notNullValue()));
        assertThat(location.getColumn(), is(7));
        assertThat(location.getLine(), is(6));

        List<String> paths = graphQlError.getPaths();
        assertThat(paths.size(), is(4));

        String countries = paths.get(0);
        assertThat(countries, is("countries"));
        String nodes = paths.get(1);
        assertThat(nodes, is("nodes"));
        String errorIndex = paths.get(2);
        assertThat(errorIndex, is("1"));
        String missingField = paths.get(3);
        assertThat(missingField, is("currencies"));
    }

}