package com.hyperwallet.android.model.graphql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;

import static com.hyperwallet.android.ExceptionMapper.toHyperwalletException;

import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletGqlException;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.graphql.error.GqlErrors;
import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.util.JsonUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.HttpURLConnection;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class GqlErrorMappingTest {

    @Rule
    public final ExternalResourceManager mResourceManager = new ExternalResourceManager();

    @Test
    public void testGQLErrorDeserialization() throws Exception {
        GqlErrors gqlErrors = JsonUtils.fromJsonString(mResourceManager.getResourceContentError(
                "gql_error_response.json"), new TypeReference<GqlErrors>() {
        });

        assertThat(gqlErrors, is(notNullValue()));
        assertThat(gqlErrors.getGQLErrors(), is(not(empty())));
        assertThat(gqlErrors.getGQLErrors().size(), is(1));
        assertThat(gqlErrors.getGQLErrors().get(0).getMessage(),
                is("Could not find any currency."));
        assertThat(gqlErrors.getGQLErrors().get(0).getLocations(), is(not(empty())));
        assertThat(gqlErrors.getGQLErrors().get(0).getLocations().size(), is(1));
        assertThat(gqlErrors.getGQLErrors().get(0).getLocations().get(0).getLine(), is(6));
        assertThat(gqlErrors.getGQLErrors().get(0).getLocations().get(0).getColumn(), is(7));
        assertThat(gqlErrors.getGQLErrors().get(0).getPaths(), is(not(empty())));
        assertThat(gqlErrors.getGQLErrors().get(0).getPaths().size(), is(4));
        assertThat(gqlErrors.getGQLErrors().get(0).getPaths(), hasItems("countries", "nodes", "1", "currencies"));
        assertThat(gqlErrors.getGQLErrors().get(0).getExtensions(), is(notNullValue()));
        assertThat(gqlErrors.getGQLErrors().get(0).getExtensions().getCode(), is("DataFetchingException"));
        assertThat(gqlErrors.getGQLErrors().get(0).getExtensions().getTimestamp(), is("Fri Feb 9 14:33:09 UTC 2018"));
    }

    @Test
    public void testExtensionMapperForGQLErrorConversion() throws Exception {
        GqlErrors gqlErrors = JsonUtils.fromJsonString(mResourceManager.getResourceContentError(
                "gql_error_response.json"), new TypeReference<GqlErrors>() {
        });
        HyperwalletException hyperwalletException = toHyperwalletException(
                new HyperwalletGqlException(HttpURLConnection.HTTP_BAD_REQUEST, gqlErrors));

        assertThat(hyperwalletException, is(notNullValue()));
        final Errors errors = hyperwalletException.getErrors();
        assertNotNull(errors);
        final List<Error> errorsList = errors.getErrors();
        assertThat(errorsList, is(not(empty())));
        assertThat(errorsList.size(), is(1));
        assertThat(errorsList.get(0).getCode(), is("DataFetchingException"));
        assertThat(errorsList.get(0).getMessage(), is("Could not find any currency."));
    }
}