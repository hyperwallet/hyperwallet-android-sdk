package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import android.os.Parcel;

import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.sdk.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class ErrorsTest {

    private static final Error FORBIDDEN_HYPERWALLET_ERROR = new Error(
            "The caller does not have access to the requested resource", "FORBIDDEN");
    private static final Error UNAUTHORIZED_HYPERWALLET_ERROR = new Error(
            "Please check your login credentials and try again", "INCORRECT_LOGIN_CREDENTIALS");
    private static final Error RELATED_RESOURCE_HYPERWALLET_ERROR = new Error(
            "The account information you provided is already registered. Based on the external account configuration "
                    + "duplications are not allowed.",
            null,
            "DUPLICATE_EXTERNAL_ACCOUNT_CREATION",
            Arrays.asList("trm-fake-token", "trm-fake-token"));
    private static final Error INVALID_BRANCH_ID_ERROR = new Error(
            "Please modify Routing Number to a valid ACH Routing Number of the branch of your bank.", "branchId",
            "CONSTRAINT_VIOLATIONS");

    @Rule
    public final ExternalResourceManager mResourceManager = new ExternalResourceManager();

    @Test
    public void testFromJsonString_forbiddenErrorResponse() throws Exception {
        Errors errors = fromJsonString(mResourceManager
                        .getResourceContent("errors/forbidden_error_response.json"),
                new TypeReference<Errors>() {
                });

        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), hasSize(1));
        assertThat(errors.getErrors().get(0).getMessage(), is(equalTo(FORBIDDEN_HYPERWALLET_ERROR.getMessage())));
        assertThat(errors.getErrors().get(0).getCode(), is(equalTo(FORBIDDEN_HYPERWALLET_ERROR.getCode())));
    }

    @Test
    public void testFromJsonString_unauthorizedErrorResponse() throws Exception {
        Errors errors = fromJsonString(mResourceManager
                        .getResourceContent("errors/unauthorized_error_response.json"),
                new TypeReference<Errors>() {
                });

        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), hasSize(1));
        assertThat(errors.getErrors().get(0).getMessage(), is(equalTo(UNAUTHORIZED_HYPERWALLET_ERROR.getMessage())));
        assertThat(errors.getErrors().get(0).getCode(), is(equalTo(UNAUTHORIZED_HYPERWALLET_ERROR.getCode())));
    }

    @Test
    public void testFromJsonString_constraintErrorResponse() throws Exception {
        Error patternValidationError = new Error(
                "Account Number is invalid. The maximum length of this field is 17.",
                "bankAccountId",
                "CONSTRAINT_VIOLATIONS");

        Error presenceValidationError = new Error(
                "Routing Number: You must provide a value for this field",
                "branchId",
                "CONSTRAINT_VIOLATIONS");

        List<Error> expectedErrorsList = new ArrayList<>();
        expectedErrorsList.add(patternValidationError);
        expectedErrorsList.add(presenceValidationError);

        Errors expectedErrors = new Errors(expectedErrorsList);
        Errors actualErrors = fromJsonString(mResourceManager
                        .getResourceContent("errors/constraint_error_response.json"),
                new TypeReference<Errors>() {
                });

        assertThat(actualErrors, is(notNullValue()));
        assertThat(actualErrors.getErrors(), hasSize(expectedErrors.getErrors().size()));

        // Pattern Assertion
        assertThat(actualErrors.getErrors().get(0).getMessage(),
                is(equalTo(expectedErrors.getErrors().get(0).getMessage())));
        assertThat(actualErrors.getErrors().get(0).getFieldName(),
                is(equalTo(expectedErrors.getErrors().get(0).getFieldName())));
        assertThat(actualErrors.getErrors().get(0).getCode(), is(equalTo(expectedErrors.getErrors().get(0).getCode())));

        // Presence Assertion
        assertThat(actualErrors.getErrors().get(1).getMessage(),
                is(equalTo(expectedErrors.getErrors().get(1).getMessage())));
        assertThat(actualErrors.getErrors().get(1).getFieldName(),
                is(equalTo(expectedErrors.getErrors().get(1).getFieldName())));
        assertThat(actualErrors.getErrors().get(1).getCode(), is(equalTo(expectedErrors.getErrors().get(1).getCode())));

    }

    @Test
    public void testFromJsonString_relatedResourcesErrorResponse() throws Exception {
        Errors actualErrors = fromJsonString(mResourceManager
                        .getResourceContent("errors/related_resources_error_response.json"),
                new TypeReference<Errors>() {
                });

        assertThat(actualErrors, is(notNullValue()));
        assertThat(actualErrors.getErrors(), hasSize(1));
        assertThat(actualErrors.getErrors().get(0).getMessage(),
                is(equalTo(RELATED_RESOURCE_HYPERWALLET_ERROR.getMessage())));
        assertThat(actualErrors.getErrors().get(0).getCode(),
                is(equalTo(RELATED_RESOURCE_HYPERWALLET_ERROR.getCode())));
        assertThat(actualErrors.getErrors().get(0).getRelatedResources(),
                is(equalTo(RELATED_RESOURCE_HYPERWALLET_ERROR.getRelatedResources())));
    }

    @Test
    public void testError_isParcelable() throws Exception {
        Errors actualErrors = fromJsonString(mResourceManager
                        .getResourceContent("errors/related_resources_error_response.json"),
                new TypeReference<Errors>() {
                });

        assertThat(actualErrors, is(notNullValue()));
        assertThat(actualErrors.getErrors(), hasSize(1));

        Error error = actualErrors.getErrors().get(0);
        error.setMessageId(R.string.authentication_token_provider_exception);

        Parcel parcel = Parcel.obtain();
        error.writeToParcel(parcel, error.describeContents());
        parcel.setDataPosition(0);

        Error bundledError = Error.CREATOR.createFromParcel(parcel);

        assertThat(bundledError, is(notNullValue()));
        assertThat(bundledError.getMessageId(), is(R.string.authentication_token_provider_exception));
        assertThat(bundledError.getMessage(), is(equalTo(RELATED_RESOURCE_HYPERWALLET_ERROR.getMessage())));
        assertThat(bundledError.getCode(), is(equalTo(RELATED_RESOURCE_HYPERWALLET_ERROR.getCode())));
        assertThat(bundledError.getRelatedResources(),
                is(equalTo(RELATED_RESOURCE_HYPERWALLET_ERROR.getRelatedResources())));
    }

    @Test
    public void testContainsInputError_withFieldInputError() {
        Errors errors = new Errors(Arrays.asList(INVALID_BRANCH_ID_ERROR));
        boolean hasFieldInputError = errors.containsInputError();
        assertTrue(hasFieldInputError);
    }

    @Test
    public void testContainsInputError_noFieldInputError() {
        Errors errors = new Errors(
                Arrays.asList(FORBIDDEN_HYPERWALLET_ERROR, UNAUTHORIZED_HYPERWALLET_ERROR));
        boolean hasFieldInputError = errors.containsInputError();
        assertFalse(hasFieldInputError);
    }
}


