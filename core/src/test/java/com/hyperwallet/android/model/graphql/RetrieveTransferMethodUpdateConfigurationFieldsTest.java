package com.hyperwallet.android.model.graphql;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.graphql.field.FieldGroup;
import com.hyperwallet.android.model.graphql.field.TransferMethodUpdateConfigurationFieldResult;
import com.hyperwallet.android.model.graphql.query.TransferMethodUpdateConfigurationFieldQuery;
import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.rule.HyperwalletMockWebServer;
import com.hyperwallet.android.rule.HyperwalletSdkMock;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.net.HttpURLConnection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class RetrieveTransferMethodUpdateConfigurationFieldsTest {
    @Rule
    public final ExternalResourceManager mExternalResourceManager =
            new ExternalResourceManager();
    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private TransferMethodUpdateConfigurationFieldQuery mMockedQuery;
    @Mock
    private HyperwalletListener<HyperwalletTransferMethodConfigurationField> mListener;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    @Captor
    private ArgumentCaptor<TransferMethodUpdateConfigurationFieldResult> mResultArgumentCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testTransferMethodUpdateConfigurationFields_returnFields() throws Exception {

        String responseBody = mExternalResourceManager.getResourceContent("tmc_update_field_bankaccount_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();
        Hyperwallet.getDefault().retrieveUpdateTransferMethodConfigurationFields(mMockedQuery, mListener);

        mAwait.await(100, TimeUnit.MILLISECONDS);

        // retrieve response
        verify(mListener).onSuccess(mResultArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));
        TransferMethodUpdateConfigurationFieldResult resultFields = mResultArgumentCaptor.getValue();

        // assert fields
        assertThat(resultFields.getFields(), is(notNullValue()));
        assertThat(resultFields.getFees(), is(nullValue()));
        assertThat(resultFields.getProcessingTime(), is(nullValue()));

        assertThat(resultFields.getFields(), is(notNullValue()));
        assertThat(resultFields.getFields().getFieldGroups(), is(notNullValue()));
        assertThat(resultFields.getFields().getFieldGroups(),
                Matchers.<FieldGroup>hasSize(3));

        assertThat(resultFields.getFields().getFieldGroups().get(0).getGroupName(),
                is(FieldGroup.GroupTypes.ACCOUNT_INFORMATION));
        assertThat(resultFields.getFields().getFieldGroups().get(0).getFields()
                .get(0).getName(), is("branchId"));
        assertThat(resultFields.getFields().getFieldGroups().get(0).getFields()
                .get(1).getName(), is("bankAccountId"));
        assertThat(resultFields.getFields().getFieldGroups().get(0).getFields()
                .get(2).getName(), is("bankAccountPurpose"));

        assertThat(resultFields.getFields().getFieldGroups().get(1).getGroupName(),
                is(FieldGroup.GroupTypes.ACCOUNT_HOLDER));
        assertThat(resultFields.getFields().getFieldGroups().get(1).getFields()
                .get(0).getName(), is("firstName"));
        assertThat(resultFields.getFields().getFieldGroups().get(1).getFields()
                .get(1).getName(), is("middleName"));
        assertThat(resultFields.getFields().getFieldGroups().get(1).getFields()
                .get(2).getName(), is("lastName"));

        assertThat(resultFields.getFields().getFieldGroups().get(2).getGroupName(),
                is(FieldGroup.GroupTypes.ADDRESS));
        assertThat(resultFields.getFields().getFieldGroups().get(2).getFields()
                .get(0).getName(), is("country"));
        assertThat(resultFields.getFields().getFieldGroups().get(2).getFields()
                .get(1).getName(), is("stateProvince"));
        assertThat(resultFields.getFields().getFieldGroups().get(2).getFields()
                .get(2).getName(), is("addressLine1"));
        assertThat(resultFields.getFields().getFieldGroups().get(2).getFields()
                .get(3).getName(), is("addressLine2"));
        assertThat(resultFields.getFields().getFieldGroups().get(2).getFields()
                .get(4).getName(), is("city"));
        assertThat(resultFields.getFields().getFieldGroups().get(2).getFields()
                .get(5).getName(), is("postalCode"));
    }

    @Test
    public void testRetrieveTransferMethodUpdateConfigurationFields_withErrorReturningFields() throws InterruptedException {
        // prepare test
        String responseBody = mExternalResourceManager.getResourceContentError(
                "gql_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();
        // execute test
        Hyperwallet.getDefault().retrieveUpdateTransferMethodConfigurationFields(mMockedQuery, mListener);
        mAwait.await(100, TimeUnit.MILLISECONDS);

        // get response
        verify(mListener, never()).onSuccess(any(TransferMethodUpdateConfigurationFieldResult.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());
        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();

        // assert/verify
        assertThat(hyperwalletException, is(notNullValue()));
        Errors errors = hyperwalletException.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), is(notNullValue()));
        assertThat(errors.getErrors().size(), is(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("DataFetchingException"));
        assertThat(error.getMessage(), is("Could not find any currency."));
    }

}
