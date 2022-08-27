package com.hyperwallet.android;

import android.content.res.Resources;

import com.hyperwallet.android.exception.HyperwalletAuthenticationTokenProviderException;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletGqlException;
import com.hyperwallet.android.exception.HyperwalletJsonParseException;
import com.hyperwallet.android.exception.HyperwalletRestException;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.graphql.error.GqlErrors;
import com.hyperwallet.android.rule.ExternalResourceManager;
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
import java.net.HttpURLConnection;
import java.util.List;

import static com.hyperwallet.android.ExceptionMapper.toHyperwalletException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ExceptionMapperTest {

    @Rule
    public final ExternalResourceManager mResourceManager = new ExternalResourceManager();
    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule();
    @Mock
    private Resources mResources;

    @Test
    public void testToHyperwalletException_convertHyperwalletException() throws Exception {

        Errors errorsFromJson = JsonUtils.fromJsonString(mResourceManager.getResourceContentError(
                "forbidden_error_response.json"),
                new TypeReference<Errors>() {
                });
        HyperwalletException hyperwalletException = new HyperwalletException(errorsFromJson);
        HyperwalletException hyperwalletExceptionResult = toHyperwalletException(hyperwalletException);
        assertNotNull(hyperwalletExceptionResult);

        final Errors errors = hyperwalletExceptionResult.getErrors();
        assertNotNull(errors);
        final List<Error> list = errors.getErrors();
        assertThat(list, hasSize(1));

        Error error = list.get(0);
        assertThat(error.getCode(), is(equalTo("FORBIDDEN")));
        assertThat(error.getMessage(), is(equalTo("The caller does not have access to the requested resource")));
    }

    @Test
    public void testToHyperwalletException_convertRestUnauthenticatedHyperwalletException() throws Exception {
        when(mResources.getString(R.string.authentication_token_provider_exception)).thenReturn(
                "Authentication token retrieval attempt resulted in an error");
        Errors errorsFromJson = JsonUtils.fromJsonString(mResourceManager.getResourceContentError(
                        "jwt_token_expired.json"),
                new TypeReference<Errors>() {
                });
        HyperwalletRestException hyperwalletException = new HyperwalletRestException(HttpURLConnection.HTTP_UNAUTHORIZED, errorsFromJson);
        HyperwalletException hyperwalletExceptionResult = toHyperwalletException(hyperwalletException);
        assertNotNull(hyperwalletExceptionResult);

        final Errors errors = hyperwalletExceptionResult.getErrors();
        assertNotNull(errors);
        final List<Error> list = errors.getErrors();
        assertThat(list, hasSize(1));

        Error error = list.get(0);
        assertThat(error.getCode(), is(equalTo("EC_AUTHENTICATION_TOKEN_PROVIDER_EXCEPTION")));
        assertThat(error.getMessageFromResourceWhenAvailable(mResources), is(equalTo("Authentication token retrieval attempt resulted in an error")));
    }

    @Test
    public void testToHyperwalletException_convertGqlUnauthenticatedHyperwalletException() throws Exception {
        when(mResources.getString(R.string.authentication_token_provider_exception)).thenReturn(
                "Authentication token retrieval attempt resulted in an error");
        GqlErrors gqlErrors = JsonUtils.fromJsonString(mResourceManager.getResourceContentError(
                "jwt_token_expired.json"), new TypeReference<GqlErrors>() {
        });

        HyperwalletGqlException hyperwalletException = new HyperwalletGqlException(HttpURLConnection.HTTP_UNAUTHORIZED, gqlErrors);
        HyperwalletException hyperwalletExceptionResult = toHyperwalletException(hyperwalletException);
        assertNotNull(hyperwalletExceptionResult);

        final Errors errors = hyperwalletExceptionResult.getErrors();
        assertNotNull(errors);
        final List<Error> list = errors.getErrors();
        assertThat(list, hasSize(1));

        Error error = list.get(0);
        assertThat(error.getCode(), is(equalTo("EC_AUTHENTICATION_TOKEN_PROVIDER_EXCEPTION")));
        assertThat(error.getMessageFromResourceWhenAvailable(mResources), is(equalTo("Authentication token retrieval attempt resulted in an error")));
    }

    @Test
    public void testToHyperwalletException_convertIOException() {
        when(mResources.getString(R.string.io_exception)).thenReturn(
                "An error that is preventing access to the required data and/or resources has occurred");
        HyperwalletException hyperwalletException = toHyperwalletException(new IOException());
        assertNotNull(hyperwalletException);

        final Errors errors = hyperwalletException.getErrors();
        assertNotNull(errors);
        final List<Error> list = errors.getErrors();
        assertThat(list, hasSize(1));

        Error error = list.get(0);
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

        final Errors errors = hyperwalletException.getErrors();
        assertNotNull(errors);
        final List<Error> list = errors.getErrors();
        assertThat(list, hasSize(1));

        Error error = list.get(0);
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

        final Errors errors = hyperwalletException.getErrors();
        assertNotNull(errors);
        final List<Error> list = errors.getErrors();
        assertThat(list, hasSize(1));

        Error error = list.get(0);
        assertThat(error.getCode(), is(equalTo("EC_JSON_PARSE_EXCEPTION")));
        assertThat(error.getMessageFromResourceWhenAvailable(mResources),
                is(equalTo("A document (JSON) parsing error occurred")));
    }

    @Test
    public void testToHyperwalletException_convertHyperwalletAuthenticationTokenProviderException() {
        when(mResources.getString(R.string.authentication_token_provider_exception)).thenReturn(
                "Authentication token retrieval attempt resulted in an error");
        HyperwalletException hyperwalletException = toHyperwalletException(
                new HyperwalletAuthenticationTokenProviderException("exception"));
        assertNotNull(hyperwalletException);

        final Errors errors = hyperwalletException.getErrors();
        assertNotNull(errors);
        final List<Error> list = errors.getErrors();
        assertThat(list, hasSize(1));

        Error error = list.get(0);
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

        final Errors errors = hyperwalletException.getErrors();
        assertNotNull(errors);
        final List<Error> list = errors.getErrors();
        assertThat(list, hasSize(1));

        Error error = list.get(0);
        assertThat(error.getCode(), is(equalTo("EC_UNEXPECTED_EXCEPTION")));
        assertThat(error.getMessageFromResourceWhenAvailable(mResources),
                is(equalTo("An unexpected error has occurred, please try again")));
    }

    @Test
    public void testToHyperwalletException_convertHyperwalletGqlException() throws Exception {
        GqlErrors gqlErrors = JsonUtils.fromJsonString(mResourceManager.getResourceContentError(
                "gql_error_response.json"), new TypeReference<GqlErrors>() {
        });

        HyperwalletException hyperwalletException = toHyperwalletException(
                new HyperwalletGqlException(HttpURLConnection.HTTP_BAD_REQUEST, gqlErrors));
        assertNotNull(hyperwalletException);

        final Errors errors = hyperwalletException.getErrors();
        assertNotNull(errors);
        final List<Error> list = errors.getErrors();
        assertThat(list, hasSize(1));

        Error error = list.get(0);
        assertThat(error.getCode(), is(equalTo("DataFetchingException")));
        assertThat(error.getMessage(), is(equalTo("Could not find any currency.")));
    }

    @Test
    public void testToHyperwalletRestException_convertHyperwalletRestException() throws Exception {

        Errors errorsFromJson = JsonUtils.fromJsonString(mResourceManager.getResourceContentError(
                "system_error_response.json"),
                new TypeReference<Errors>() {
                });
        HyperwalletRestException hyperwalletRestException = new HyperwalletRestException(HttpURLConnection.HTTP_BAD_REQUEST, errorsFromJson);
        HyperwalletException hyperwalletExceptionResult = toHyperwalletException(hyperwalletRestException);
        assertNotNull(hyperwalletExceptionResult);
        assertThat(hyperwalletExceptionResult, instanceOf(HyperwalletRestException.class));
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, ((HyperwalletRestException)hyperwalletExceptionResult).getHttpCode());

        final Errors errors = hyperwalletExceptionResult.getErrors();
        assertNotNull(errors);
        final List<Error> list = errors.getErrors();
        assertThat(list, hasSize(1));

        Error error = list.get(0);
        assertThat(error.getCode(), is(equalTo("SYSTEM_ERROR")));
        assertThat(error.getMessage(), is(equalTo("A system error has occurred. Please try again. If you continue to receive this error, please contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b).")));
    }

    @Test
    public void testToHyperwalletRestException_convertUnmappedExceptionForbidden() throws Exception {
        when(mResources.getString(R.string.unexpected_exception)).thenReturn(
                "An unexpected error has occurred, please try again");
        HyperwalletRestException hyperwalletRestException = new HyperwalletRestException(HttpURLConnection.HTTP_FORBIDDEN, null);
        HyperwalletException hyperwalletException = toHyperwalletException(hyperwalletRestException);
        assertNotNull(hyperwalletException);
        assertThat(hyperwalletException, instanceOf(HyperwalletException.class));

        final Errors errors = hyperwalletException.getErrors();
        assertNotNull(errors);
        final List<Error> list = errors.getErrors();
        assertThat(list, hasSize(1));

        Error error = list.get(0);
        assertThat(error.getCode(), is(equalTo("EC_UNEXPECTED_EXCEPTION")));
        assertThat(error.getMessageFromResourceWhenAvailable(mResources),
                is(equalTo("An unexpected error has occurred, please try again")));
    }
}