package com.hyperwallet.android.model.graphql;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.model.user.HyperwalletUser.ProfileTypes.INDIVIDUAL;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.HyperwalletErrors;
import com.hyperwallet.android.model.graphql.field.HyperwalletField;
import com.hyperwallet.android.model.graphql.field.HyperwalletFieldGroup;
import com.hyperwallet.android.model.graphql.field.HyperwalletTransferMethodConfigurationFieldResult;
import com.hyperwallet.android.model.graphql.query.HyperwalletTransferMethodConfigurationFieldQuery;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;
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

@RunWith(RobolectricTestRunner.class)
public class HyperwalletRetrieveTransferMethodConfigurationFieldsTest {

    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public final HyperwalletExternalResourceManager mExternalResourceManager =
            new HyperwalletExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletTransferMethodConfigurationFieldQuery mMockedQuery;
    @Mock
    private HyperwalletListener<HyperwalletTransferMethodConfigurationField> mListener;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletTransferMethodConfigurationFieldResult> mResultArgumentCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testRetrieveTransferMethodConfigurationFields_returnsFields() throws Exception {
        // prepare test
        String responseBody = mExternalResourceManager.getResourceContent("tmc_get_fields_no_fees_v2_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        // run test
        Hyperwallet.getDefault().retrieveTransferMethodConfigurationFields(mMockedQuery, mListener);
        mAwait.await(100, TimeUnit.MILLISECONDS);

        // retrieve response
        verify(mListener).onSuccess(mResultArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));
        HyperwalletTransferMethodConfigurationField resultFields = mResultArgumentCaptor.getValue();

        // assert fields
        assertThat(resultFields.getFields(), is(notNullValue()));

        // assert fees
        assertThat(resultFields.getFees(), is(nullValue()));
    }

    @Test
    public void testRetrieveTransferMethodConfigurationFields_returnsFieldsAndFees() throws Exception {
        // prepare test
        String responseBody = mExternalResourceManager.getResourceContent("tmc_get_fields_v2_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        // run test
        Hyperwallet.getDefault().retrieveTransferMethodConfigurationFields(mMockedQuery, mListener);
        mAwait.await(100, TimeUnit.MILLISECONDS);

        // retrieve response
        verify(mListener).onSuccess(mResultArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));
        HyperwalletTransferMethodConfigurationField resultFields = mResultArgumentCaptor.getValue();

        // assert fields
        assertThat(resultFields.getFields(), is(notNullValue()));
        assertThat(resultFields.getFields().getCountry(), is("CA"));
        assertThat(resultFields.getFields().getCurrency(), is("CAD"));
        assertThat(resultFields.getFields().getProfile(), is(INDIVIDUAL));
        assertThat(resultFields.getFields().getTransferMethodType(), is(BANK_ACCOUNT));
        assertThat(resultFields.getFields().getTransferMethodType(), is(BANK_ACCOUNT));

        assertThat(resultFields.getFields().getFieldGroups(), is(notNullValue()));
        assertThat(resultFields.getFields().getFieldGroups(),
                Matchers.<HyperwalletFieldGroup>hasSize(6));
        // check group fields sequence
        assertThat(resultFields.getFields().getFieldGroups().get(0).getGroupName(),
                is(HyperwalletFieldGroup.GroupTypes.ADDRESS));
        assertThat(resultFields.getFields().getFieldGroups().get(0).getFields(),
                Matchers.<HyperwalletField>hasSize(5));
        assertThat(resultFields.getFields().getFieldGroups().get(0).getFields()
                .get(0).getName(), is("country"));
        assertThat(resultFields.getFields().getFieldGroups().get(0).getFields()
                .get(1).getName(), is("stateProvince"));
        assertThat(resultFields.getFields().getFieldGroups().get(0).getFields()
                .get(2).getName(), is("addressLine1"));
        assertThat(resultFields.getFields().getFieldGroups().get(0).getFields()
                .get(3).getName(), is("city"));
        assertThat(resultFields.getFields().getFieldGroups().get(0).getFields()
                .get(4).getName(), is("postalCode"));

        assertThat(resultFields.getFields().getFieldGroups().get(1).getGroupName(),
                is(HyperwalletFieldGroup.GroupTypes.IDENTIFICATION));
        assertThat(resultFields.getFields().getFieldGroups().get(1).getFields(),
                Matchers.<HyperwalletField>hasSize(6));
        assertThat(resultFields.getFields().getFieldGroups().get(1).getFields()
                .get(0).getName(), is("firstName"));
        assertThat(resultFields.getFields().getFieldGroups().get(1).getFields()
                .get(1).getName(), is("middleName"));
        assertThat(resultFields.getFields().getFieldGroups().get(1).getFields()
                .get(2).getName(), is("lastName"));
        assertThat(resultFields.getFields().getFieldGroups().get(1).getFields()
                .get(3).getName(), is("phoneNumber"));
        assertThat(resultFields.getFields().getFieldGroups().get(1).getFields()
                .get(4).getName(), is("mobileNumber"));
        assertThat(resultFields.getFields().getFieldGroups().get(1).getFields()
                .get(5).getName(), is("dateOfBirth"));

        assertThat(resultFields.getFields().getFieldGroups().get(2).getGroupName(),
                is(HyperwalletFieldGroup.GroupTypes.CONTACT_INFORMATION));
        assertThat(resultFields.getFields().getFieldGroups().get(2).getFields(),
                Matchers.<HyperwalletField>hasSize(4));

        assertThat(resultFields.getFields().getFieldGroups().get(3).getGroupName(),
                is(HyperwalletFieldGroup.GroupTypes.ACCOUNT_INFORMATION));
        assertThat(resultFields.getFields().getFieldGroups().get(3).getFields(),
                Matchers.<HyperwalletField>hasSize(4));

        assertThat(resultFields.getFields().getFieldGroups().get(4).getGroupName(),
                is(HyperwalletFieldGroup.GroupTypes.INTERMEDIARY_ACCOUNT));
        assertThat(resultFields.getFields().getFieldGroups().get(4).getFields(),
                Matchers.<HyperwalletField>hasSize(4));

        assertThat(resultFields.getFields().getFieldGroups().get(5).getGroupName(),
                is(HyperwalletFieldGroup.GroupTypes.ACCOUNT_HOLDER));
        assertThat(resultFields.getFields().getFieldGroups().get(5).getFields(),
                Matchers.<HyperwalletField>hasSize(5));
        assertThat(resultFields.getFields().getFieldGroups().get(5).getFields()
                .get(0).getName(), is("country"));
        assertThat(resultFields.getFields().getFieldGroups().get(5).getFields()
                .get(1).getName(), is("stateProvince"));
        assertThat(resultFields.getFields().getFieldGroups().get(5).getFields()
                .get(2).getName(), is("addressLine1"));
        assertThat(resultFields.getFields().getFieldGroups().get(5).getFields()
                .get(3).getName(), is("city"));
        assertThat(resultFields.getFields().getFieldGroups().get(5).getFields()
                .get(4).getName(), is("postalCode"));

        // assert fees
        assertThat(resultFields.getFees(), Matchers.<HyperwalletFee>hasSize(1));
        assertThat(resultFields.getFees().get(0).getValue(), is("5.00"));
        assertThat(resultFields.getFees().get(0).getFeeRateType(), is(HyperwalletFee.FeeRate.FLAT));
    }

    @Test
    public void testRetrieveTransferMethodConfigurationFields_withErrorReturningFields() throws InterruptedException {
        // prepare test
        String responseBody = mExternalResourceManager.getResourceContentError(
                "gql_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();
        // execute test
        Hyperwallet.getDefault().retrieveTransferMethodConfigurationFields(mMockedQuery, mListener);
        mAwait.await(100, TimeUnit.MILLISECONDS);

        // get response
        verify(mListener, never()).onSuccess(any(HyperwalletTransferMethodConfigurationFieldResult.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());
        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();

        // assert/verify
        assertThat(hyperwalletException, is(notNullValue()));
        HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertThat(hyperwalletErrors, is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors(), is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors().size(), is(1));

        HyperwalletError hyperwalletError = hyperwalletErrors.getErrors().get(0);
        assertThat(hyperwalletError.getCode(), is("DataFetchingException"));
        assertThat(hyperwalletError.getMessage(), is("Could not find any currency."));
    }
}
