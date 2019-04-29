package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.HyperwalletBankAccount.Purpose.SAVINGS;
import static com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.ADDRESS_LINE_1;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.BANK_ACCOUNT_ID;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.BANK_ACCOUNT_PURPOSE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.BANK_ACCOUNT_RELATIONSHIP;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.BANK_ID;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.BRANCH_ID;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CITY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.COUNTRY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.DATE_OF_BIRTH;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.FIRST_NAME;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.GENDER;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.GOVERNMENT_ID;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.LAST_NAME;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.PHONE_NUMBER;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.POSTAL_CODE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.STATE_PROVINCE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletBankAccount;
import com.hyperwallet.android.model.HyperwalletBankAccountPagination;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.HyperwalletErrors;
import com.hyperwallet.android.model.paging.HyperwalletPageLink;
import com.hyperwallet.android.model.paging.HyperwalletPageList;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.RecordedRequest;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletListBankAccountsTest {

    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<HyperwalletPageList<HyperwalletBankAccount>> mListener;
    @Captor
    private ArgumentCaptor<HyperwalletPageList<HyperwalletBankAccount>> mListBankAccountCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testListBankAccounts_returnsActivatedAccounts() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContent(
                "bank_account_list_single_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        Map<String, String> query = new HashMap<>();
        query.put(TYPE, BANK_ACCOUNT);

        final HyperwalletBankAccountPagination hyperwalletBankAccountPagination =
                new HyperwalletBankAccountPagination(query);
        assertThat(hyperwalletBankAccountPagination.getType(), is(BANK_ACCOUNT));

        Hyperwallet.getDefault().listBankAccounts(hyperwalletBankAccountPagination, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-accounts?limit=10&offset=0&type"
                        + "=BANK_ACCOUNT&status=ACTIVATED"));

        verify(mListener).onSuccess(mListBankAccountCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletPageList<HyperwalletBankAccount> listBankAccountResponse = mListBankAccountCaptor.getValue();
        assertThat(listBankAccountResponse, is(notNullValue()));
        assertThat(listBankAccountResponse.getCount(), is(1));
        assertThat(listBankAccountResponse.getLimit(), is(10));
        assertThat(listBankAccountResponse.getOffset(), is(0));

        assertThat(listBankAccountResponse.getDataList(), is(notNullValue()));
        assertThat(listBankAccountResponse.getDataList().size(), is(1));

        HyperwalletBankAccount hyperwalletBankAccount = listBankAccountResponse.getDataList().get(0);
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
                HyperwalletBankAccount.BankAccountRelationships.SELF));
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
        HyperwalletPageLink hyperwalletPageLink = listBankAccountResponse.getPageLinks().get(0);
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
        Map<String, String> query = new HashMap<>();
        query.put(TYPE, BANK_ACCOUNT);

        final HyperwalletBankAccountPagination hyperwalletBankAccountPagination =
                new HyperwalletBankAccountPagination(query);
        assertThat(hyperwalletBankAccountPagination.getType(), is(BANK_ACCOUNT));

        Hyperwallet.getDefault().listBankAccounts(hyperwalletBankAccountPagination, mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-accounts?"));
        assertThat(recordedRequest.getPath(), containsString("type=BANK_ACCOUNT"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
        assertThat(recordedRequest.getPath(), containsString("status=ACTIVATED"));

        verify(mListener).onSuccess(mListBankAccountCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletPageList<HyperwalletBankAccount> emptyListBankAccountResponse =
                mListBankAccountCaptor.getValue();
        assertThat(emptyListBankAccountResponse, is(nullValue()));
    }

    @Test
    public void testListBankAccounts_returnsError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR).withBody(
                responseBody).mock();


        Map<String, String> query = new HashMap<>();
        query.put(TYPE, BANK_ACCOUNT);

        final HyperwalletBankAccountPagination hyperwalletBankAccountPagination =
                new HyperwalletBankAccountPagination(query);
        assertThat(hyperwalletBankAccountPagination.getType(), is(BANK_ACCOUNT));

        Hyperwallet.getDefault().listBankAccounts(hyperwalletBankAccountPagination, mListener);
        mAwait.await(1000, TimeUnit.MILLISECONDS);

        verify(mListener, never()).onSuccess(any(HyperwalletPageList.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertThat(hyperwalletErrors.getErrors().size(), is(1));

        HyperwalletError hyperwalletError = hyperwalletErrors.getErrors().get(0);
        assertThat(hyperwalletError.getCode(), is("SYSTEM_ERROR"));
        assertThat(hyperwalletError.getMessage(),
                is("A system error has occurred. Please try again. If you continue to receive this error, please "
                        + "contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b)."));

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                containsString("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-accounts?"));
        assertThat(recordedRequest.getPath(), containsString("limit=10"));
        assertThat(recordedRequest.getPath(), containsString("offset=0"));
        assertThat(recordedRequest.getPath(), containsString("status=ACTIVATED"));
        assertThat(recordedRequest.getPath(), containsString("type=BANK_ACCOUNT"));
    }
}
