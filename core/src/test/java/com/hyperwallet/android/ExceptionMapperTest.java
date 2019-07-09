package com.hyperwallet.android;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import static com.hyperwallet.android.ExceptionMapper.toHyperwalletException;

import android.content.res.Resources;

import com.hyperwallet.android.exception.AuthenticationTokenProviderException;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletGqlException;
import com.hyperwallet.android.exception.HyperwalletJsonParseException;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.HyperwalletErrors;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.graphql.error.GqlErrors;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;
import com.hyperwallet.android.sdk.R;
import com.hyperwallet.android.util.JsonUtils;

import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class ExceptionMapperTest {

    @Rule
    public final HyperwalletExternalResourceManager mResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule();
    @Mock
    private Resources mResources;

    @Test
    public void testToHyperwalletException_convertHyperwalletException() throws Exception {

        HyperwalletErrors errors = JsonUtils.fromJsonString(mResourceManager.getResourceContentError(
                "forbidden_error_response.json"),
                new TypeReference<HyperwalletErrors>() {
                });
        HyperwalletException hyperwalletException = new HyperwalletException(errors);
        HyperwalletException hyperwalletExceptionResult = toHyperwalletException(hyperwalletException);
        assertNotNull(hyperwalletExceptionResult);

        final HyperwalletErrors hyperwalletErrors = hyperwalletExceptionResult.getHyperwalletErrors();
        assertNotNull(hyperwalletErrors);
        final List<HyperwalletError> list = hyperwalletErrors.getErrors();
        assertThat(list, hasSize(1));

        HyperwalletError error = list.get(0);
        assertThat(error.getCode(), is(equalTo("FORBIDDEN")));
        assertThat(error.getMessage(), is(equalTo("The caller does not have access to the requested resource")));
    }

    @Test
    public void testToHyperwalletException_convertIOException() {
        when(mResources.getString(R.string.io_exception)).thenReturn(
                "An error that is preventing access to the required data and/or resources has occurred");
        HyperwalletException hyperwalletException = toHyperwalletException(new IOException());
        assertNotNull(hyperwalletException);

        final HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertNotNull(hyperwalletErrors);
        final List<HyperwalletError> list = hyperwalletErrors.getErrors();
        assertThat(list, hasSize(1));

        HyperwalletError error = list.get(0);
        assertThat(error.getCode(), is(equalTo("EC_IO_EXCEPTION")));
        assertThat(error.getMessageFromResourceWhenAvailable(mResources),
                is(equalTo("An error that is preventing access to the required data and/or resources has occurred")));
    }

    @Test
    public void testToHyperwalletException_convertJSONException() {
        when(mResources.getString(R.string.json_exception)).thenReturn(
                "Processing the JSON document resulted in an error");
        HyperwalletException hyperwalletException = toHyperwalletException(new JSONException("exception"));
        assertNotNull(hyperwalletException);

        final HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertNotNull(hyperwalletErrors);
        final List<HyperwalletError> list = hyperwalletErrors.getErrors();
        assertThat(list, hasSize(1));

        HyperwalletError error = list.get(0);
        assertThat(error.getCode(), is(equalTo("EC_JSON_EXCEPTION")));
        assertThat(error.getMessageFromResourceWhenAvailable(mResources),
                is(equalTo("Processing the JSON document resulted in an error")));
    }

    @Test
    public void testToHyperwalletException_convertHyperwalletJsonParseException() {
        when(mResources.getString(R.string.json_parse_exception)).thenReturn(
                "A document (JSON) parsing error occurred");
        HyperwalletException hyperwalletException = toHyperwalletException(
                new HyperwalletJsonParseException("exception"));
        assertNotNull(hyperwalletException);

        final HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertNotNull(hyperwalletErrors);
        final List<HyperwalletError> list = hyperwalletErrors.getErrors();
        assertThat(list, hasSize(1));

        HyperwalletError error = list.get(0);
        assertThat(error.getCode(), is(equalTo("EC_JSON_PARSE_EXCEPTION")));
        assertThat(error.getMessageFromResourceWhenAvailable(mResources),
                is(equalTo("A document (JSON) parsing error occurred")));
    }

    @Test
    public void testToHyperwalletException_convertAuthenticationTokenProviderException() {
        when(mResources.getString(R.string.authentication_token_provider_exception)).thenReturn(
                "Authentication token retrieval attempt resulted in an error");
        HyperwalletException hyperwalletException = toHyperwalletException(
                new AuthenticationTokenProviderException("exception"));
        assertNotNull(hyperwalletException);

        final HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertNotNull(hyperwalletErrors);
        final List<HyperwalletError> list = hyperwalletErrors.getErrors();
        assertThat(list, hasSize(1));

        HyperwalletError error = list.get(0);
        assertThat(error.getCode(), is(equalTo("EC_AUTHENTICATION_TOKEN_PROVIDER_EXCEPTION")));
        assertThat(error.getMessageFromResourceWhenAvailable(mResources),
                is(equalTo("Authentication token retrieval attempt resulted in an error")));
    }

    @Test
    public void testToHyperwalletException_convertUnmappedException() {
        when(mResources.getString(R.string.unexpected_exception)).thenReturn(
                "An unexpected error has occurred, please try again");
        HyperwalletException hyperwalletException = toHyperwalletException(new IllegalAccessException());
        assertNotNull(hyperwalletException);

        final HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertNotNull(hyperwalletErrors);
        final List<HyperwalletError> list = hyperwalletErrors.getErrors();
        assertThat(list, hasSize(1));

        HyperwalletError error = list.get(0);
        assertThat(error.getCode(), is(equalTo("EC_UNEXPECTED_EXCEPTION")));
        assertThat(error.getMessageFromResourceWhenAvailable(mResources),
                is(equalTo("An unexpected error has occurred, please try again")));
    }

    @Test
    public void testToHyperwalletException_convertGqlException() throws Exception {
        GqlErrors gqlErrors = JsonUtils.fromJsonString(mResourceManager.getResourceContentError(
                "gql_error_response.json"), new TypeReference<GqlErrors>() {
        });

        HyperwalletException hyperwalletException = toHyperwalletException(new HyperwalletGqlException(gqlErrors));
        assertNotNull(hyperwalletException);

        final HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertNotNull(hyperwalletErrors);
        final List<HyperwalletError> list = hyperwalletErrors.getErrors();
        assertThat(list, hasSize(1));

        HyperwalletError error = list.get(0);
        assertThat(error.getCode(), is(equalTo("DataFetchingException")));
        assertThat(error.getMessage(), is(equalTo("Could not find any currency.")));
    }
}