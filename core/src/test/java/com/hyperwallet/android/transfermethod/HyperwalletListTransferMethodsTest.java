package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.transfermethod.HyperwalletBankAccount.Purpose.SAVINGS;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.ADDRESS_LINE_1;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BANK_ACCOUNT_ID;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BANK_ACCOUNT_PURPOSE;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BANK_ACCOUNT_RELATIONSHIP;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BANK_ID;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.BRANCH_ID;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.CITY;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.COUNTRY;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.DATE_OF_BIRTH;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.FIRST_NAME;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.GENDER;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.GOVERNMENT_ID;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.LAST_NAME;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.PHONE_NUMBER;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.POSTAL_CODE;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.STATE_PROVINCE;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.util.HttpMethod.GET;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.HyperwalletErrors;
import com.hyperwallet.android.model.paging.HyperwalletPageLink;
import com.hyperwallet.android.model.paging.HyperwalletPageList;
import com.hyperwallet.android.model.transfermethod.HyperwalletBankAccount;
import com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod;
import com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethodQueryParam;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;
import com.hyperwallet.android.rule.HyperwalletMockWebServer;
import com.hyperwallet.android.rule.HyperwalletSdkMock;

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

import okhttp3.mockwebserver.RecordedRequest;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletListTransferMethodsTest {

    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();

    @Mock
    private HyperwalletListener<HyperwalletPageList<HyperwalletTransferMethod>> mListener;
    @Captor
    private ArgumentCaptor<HyperwalletPageList<HyperwalletTransferMethod>> mListTransferMethodCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testListTransferMethods_returnsActivatedTransferMethods() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContent("transfer_method_list_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        final HyperwalletTransferMethodQueryParam hyperwalletTransferMethodQueryParam =
                HyperwalletTransferMethodQueryParam.builder().build();
        assertThat(hyperwalletTransferMethodQueryParam.getType(), is(nullValue()));

        Hyperwallet.getDefault().listTransferMethods(hyperwalletTransferMethodQueryParam,
                mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/transfer-methods?"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
        assertThat(recordedRequest.getPath(), containsString("status=ACTIVATED"));

        verify(mListener).onSuccess(mListTransferMethodCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletPageList<HyperwalletTransferMethod> listTransferMethodResponse =
                mListTransferMethodCaptor.getValue();
        assertThat(listTransferMethodResponse, is(notNullValue()));
        assertThat(listTransferMethodResponse.getCount(), is(24));
        assertThat(listTransferMethodResponse.getLimit(), is(10));
        assertThat(listTransferMethodResponse.getOffset(), is(0));

        assertThat(listTransferMethodResponse.getDataList(), is(notNullValue()));
        assertThat(listTransferMethodResponse.getDataList().size(), is(10));

        HyperwalletTransferMethod hyperwalletTransferMethod = listTransferMethodResponse.getDataList().get(0);
        assertThat(hyperwalletTransferMethod.getField(TOKEN), is("trm-6a4cb004-e577-45a1-9270-cb0dd5aebced"));
        assertThat(hyperwalletTransferMethod.getField(TYPE), is(BANK_ACCOUNT));
        assertThat(hyperwalletTransferMethod.getField(STATUS), is(ACTIVATED));
        assertThat(hyperwalletTransferMethod.getField("verificationStatus"), is("NOT_REQUIRED"));
        assertThat(hyperwalletTransferMethod.getField(CREATED_ON), is("2019-02-21T12:12:00"));
        assertThat(hyperwalletTransferMethod.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(hyperwalletTransferMethod.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(hyperwalletTransferMethod.getField(BANK_ID), is("021000021"));
        assertThat(hyperwalletTransferMethod.getField(BRANCH_ID), is("021000021"));
        assertThat(hyperwalletTransferMethod.getField(BANK_ACCOUNT_ID), is("5288606281332"));
        assertThat(hyperwalletTransferMethod.getField(BANK_ACCOUNT_RELATIONSHIP), is(
                HyperwalletBankAccount.BankAccountRelationships.SELF));
        assertThat(hyperwalletTransferMethod.getField(BANK_ACCOUNT_PURPOSE), is(SAVINGS));
        assertThat(hyperwalletTransferMethod.getField("profileType"), is("INDIVIDUAL"));
        assertThat(hyperwalletTransferMethod.getField(FIRST_NAME), is("Kevin"));
        assertThat(hyperwalletTransferMethod.getField(LAST_NAME), is("Wilcox"));
        assertThat(hyperwalletTransferMethod.getField(DATE_OF_BIRTH), is("1999-01-01"));
        assertThat(hyperwalletTransferMethod.getField(GENDER), is("MALE"));
        assertThat(hyperwalletTransferMethod.getField(PHONE_NUMBER), is("6041231234"));
        assertThat(hyperwalletTransferMethod.getField(GOVERNMENT_ID), is("111222333"));
        assertThat(hyperwalletTransferMethod.getField(ADDRESS_LINE_1), is("950 Granville St"));
        assertThat(hyperwalletTransferMethod.getField(CITY), is("San Jose"));
        assertThat(hyperwalletTransferMethod.getField(STATE_PROVINCE), is("California"));
        assertThat(hyperwalletTransferMethod.getField(COUNTRY), is("CA"));
        assertThat(hyperwalletTransferMethod.getField(POSTAL_CODE), is("H0H0H0"));

        assertThat(listTransferMethodResponse.getPageLinks(), is(notNullValue()));
        assertThat(listTransferMethodResponse.getPageLinks().size(), is(3));
        HyperwalletPageLink hyperwalletPageLink = listTransferMethodResponse.getPageLinks().get(0);
        assertThat(hyperwalletPageLink.getPageHref(), containsString("https://localhost:8181/rest/v3/users/"
                + "usr-a51c7522-ccba-4bcf-a6a7-bc59dae8f9b0/transfer-methods?"));
        assertThat(hyperwalletPageLink.getPageHref(), containsString("offset=0"));
        assertThat(hyperwalletPageLink.getPageHref(), containsString("limit=10"));
        assertThat(hyperwalletPageLink.getPageParameter(), is(notNullValue()));
        assertThat(hyperwalletPageLink.getPageParameter().getRel(), is("self"));
    }

    @Test
    public void testListTransferMethods_returnsNoTransferMethods() throws InterruptedException {
        String responseBody = "";
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_NO_CONTENT).withBody(responseBody).mock();

        final HyperwalletTransferMethodQueryParam hyperwalletTransferMethodQueryParam =
                HyperwalletTransferMethodQueryParam.builder().status(ACTIVATED).build();
        assertThat(hyperwalletTransferMethodQueryParam.getType(), is(nullValue()));

        Hyperwallet.getDefault().listTransferMethods(hyperwalletTransferMethodQueryParam,
                mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/transfer-methods?"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
        assertThat(recordedRequest.getPath(), containsString("status=ACTIVATED"));
        verify(mListener).onSuccess(mListTransferMethodCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletPageList<HyperwalletTransferMethod> emptyListTransferMethodResponse =
                mListTransferMethodCaptor.getValue();
        assertThat(emptyListTransferMethodResponse, is(nullValue()));
    }

    @Test
    public void testListBankAccounts_returnsError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        final HyperwalletTransferMethodQueryParam hyperwalletTransferMethodQueryParam =
                HyperwalletTransferMethodQueryParam.builder().build();
        assertThat(hyperwalletTransferMethodQueryParam.getType(), is(nullValue()));

        Hyperwallet.getDefault().listTransferMethods(hyperwalletTransferMethodQueryParam,
                mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        verify(mListener, never()).onSuccess(any(HyperwalletPageList.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertThat(hyperwalletErrors, is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors(), is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors().size(), is(1));

        HyperwalletError hyperwalletError = hyperwalletErrors.getErrors().get(0);
        assertThat(hyperwalletError.getCode(), is("SYSTEM_ERROR"));
        assertThat(hyperwalletError.getMessage(),
                is("A system error has occurred. Please try again. If you continue to receive this error, please "
                        + "contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b)."));

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/transfer-methods?"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
        assertThat(recordedRequest.getPath(), containsString("status=ACTIVATED"));
    }
}
