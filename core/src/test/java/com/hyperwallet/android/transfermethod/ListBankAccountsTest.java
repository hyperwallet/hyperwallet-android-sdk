package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.transfermethod.BankAccount.Purpose.SAVINGS;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.ADDRESS_LINE_1;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BANK_ACCOUNT_ID;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BANK_ACCOUNT_PURPOSE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BANK_ACCOUNT_RELATIONSHIP;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BANK_ID;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BRANCH_ID;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CITY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.COUNTRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.DATE_OF_BIRTH;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.FIRST_NAME;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.GENDER;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.GOVERNMENT_ID;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.LAST_NAME;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.PHONE_NUMBER;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.POSTAL_CODE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.STATE_PROVINCE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.util.HttpMethod.GET;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.paging.PageLink;
import com.hyperwallet.android.model.paging.PageList;
import com.hyperwallet.android.model.transfermethod.BankAccount;
import com.hyperwallet.android.model.transfermethod.BankAccountQueryParam;
import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.rule.MockWebServer;
import com.hyperwallet.android.rule.SdkMock;

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
public class ListBankAccountsTest {

    @Rule
    public MockWebServer mServer = new MockWebServer();
    @Rule
    public SdkMock mSdkMock = new SdkMock(mServer);
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<PageList<BankAccount>> mListener;
    @Captor
    private ArgumentCaptor<PageList<BankAccount>> mListBankAccountCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testListBankAccounts_returnsActivatedAccounts() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContent(
                "bank_account_list_single_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        final BankAccountQueryParam queryParam = new BankAccountQueryParam.Builder()
                .status(ACTIVATED)
                .build();
        assertThat(queryParam.getType(), is(BANK_ACCOUNT));

        Hyperwallet.getDefault().listBankAccounts(queryParam, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-accounts?limit=10&offset=0&type"
                        + "=BANK_ACCOUNT&status=ACTIVATED"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener).onSuccess(mListBankAccountCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<BankAccount> listBankAccountResponse = mListBankAccountCaptor.getValue();
        assertThat(listBankAccountResponse, is(notNullValue()));
        assertThat(listBankAccountResponse.getCount(), is(1));
        assertThat(listBankAccountResponse.getLimit(), is(10));
        assertThat(listBankAccountResponse.getOffset(), is(0));

        assertThat(listBankAccountResponse.getDataList(), is(notNullValue()));
        assertThat(listBankAccountResponse.getDataList().size(), is(1));

        BankAccount hyperwalletBankAccount = listBankAccountResponse.getDataList().get(0);
        assertThat(hyperwalletBankAccount.getField(TOKEN), is("usr-fbfd5848-60d0-43c5-8462-099c959b49c7"));
        assertThat(hyperwalletBankAccount.getField(TYPE), is(BANK_ACCOUNT));
        assertThat(hyperwalletBankAccount.getField(STATUS), is(ACTIVATED));
        assertThat(hyperwalletBankAccount.getField("verificationStatus"), is("NOT_REQUIRED"));
        assertThat(hyperwalletBankAccount.getField(CREATED_ON), is("2019-02-21T12:12:00"));
        assertThat(hyperwalletBankAccount.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(hyperwalletBankAccount.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(hyperwalletBankAccount.getField(BANK_ID), is("021000021"));
        assertThat(hyperwalletBankAccount.getField(BRANCH_ID), is("021000021"));
        assertThat(hyperwalletBankAccount.getField(BANK_ACCOUNT_ID), is("5288606281332"));
        assertThat(hyperwalletBankAccount.getField(BANK_ACCOUNT_RELATIONSHIP), is(
                BankAccount.BankAccountRelationships.SELF));
        assertThat(hyperwalletBankAccount.getField(BANK_ACCOUNT_PURPOSE), is(SAVINGS));
        assertThat(hyperwalletBankAccount.getField("profileType"), is("INDIVIDUAL"));
        assertThat(hyperwalletBankAccount.getField(FIRST_NAME), is("Kevin"));
        assertThat(hyperwalletBankAccount.getField(LAST_NAME), is("Wilcox"));
        assertThat(hyperwalletBankAccount.getField(DATE_OF_BIRTH), is("1999-01-01"));
        assertThat(hyperwalletBankAccount.getField(GENDER), is("MALE"));
        assertThat(hyperwalletBankAccount.getField(PHONE_NUMBER), is("6041231234"));
        assertThat(hyperwalletBankAccount.getField(GOVERNMENT_ID), is("111222333"));
        assertThat(hyperwalletBankAccount.getField(ADDRESS_LINE_1), is("950 Granville St"));
        assertThat(hyperwalletBankAccount.getField(CITY), is("San Jose"));
        assertThat(hyperwalletBankAccount.getField(STATE_PROVINCE), is("California"));
        assertThat(hyperwalletBankAccount.getField(COUNTRY), is("CA"));
        assertThat(hyperwalletBankAccount.getField(POSTAL_CODE), is("H0H0H0"));

        assertThat(listBankAccountResponse.getPageLinks(), is(notNullValue()));
        assertThat(listBankAccountResponse.getPageLinks().size(), is(1));
        PageLink hyperwalletPageLink = listBankAccountResponse.getPageLinks().get(0);
        assertThat(hyperwalletPageLink.getPageHref(),
                is("https://localhost:8181/rest/v3/users/usr-fbfd5848-60d0-43c5-8462"
                        + "-099c959b49c7/bank-accounts?offset=0&limit=10"));
        assertThat(hyperwalletPageLink.getPageParameter(), is(notNullValue()));
        assertThat(hyperwalletPageLink.getPageParameter().getRel(), is("self"));
    }

    @Test
    public void testListBankAccounts_returnsNoAccounts() throws InterruptedException {
        String responseBody = "";
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_NO_CONTENT).withBody(responseBody).mock();

        final BankAccountQueryParam queryParam = new BankAccountQueryParam.Builder()
                .status(ACTIVATED)
                .build();
        assertThat(queryParam.getType(), is(BANK_ACCOUNT));

        Hyperwallet.getDefault().listBankAccounts(queryParam, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-accounts?"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        assertThat(recordedRequest.getPath(), containsString("type=BANK_ACCOUNT"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
        assertThat(recordedRequest.getPath(), containsString("status=ACTIVATED"));

        verify(mListener).onSuccess(mListBankAccountCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        PageList<BankAccount> emptyListBankAccountResponse =
                mListBankAccountCaptor.getValue();
        assertThat(emptyListBankAccountResponse, is(nullValue()));
    }

    @Test
    public void testListBankAccounts_returnsError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR).withBody(
                responseBody).mock();

        final BankAccountQueryParam queryParam = new BankAccountQueryParam.Builder()
                .status(ACTIVATED)
                .build();
        assertThat(queryParam.getType(), is(BANK_ACCOUNT));

        Hyperwallet.getDefault().listBankAccounts(queryParam, mListener);
        mAwait.await(1000, TimeUnit.MILLISECONDS);

        verify(mListener, never()).onSuccess(any(PageList.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        Errors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertThat(hyperwalletErrors.getErrors().size(), is(1));

        Error hyperwalletError = hyperwalletErrors.getErrors().get(0);
        assertThat(hyperwalletError.getCode(), is("SYSTEM_ERROR"));
        assertThat(hyperwalletError.getMessage(),
                is("A system error has occurred. Please try again. If you continue to receive this error, please "
                        + "contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b)."));

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-accounts?"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
        assertThat(recordedRequest.getPath(), containsString("status=ACTIVATED"));
        assertThat(recordedRequest.getPath(), containsString("type=BANK_ACCOUNT"));
    }
}
