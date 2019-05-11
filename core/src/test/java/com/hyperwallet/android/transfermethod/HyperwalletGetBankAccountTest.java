package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.HyperwalletStatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.ADDRESS_LINE_1;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.BANK_ACCOUNT_ID;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.BANK_ACCOUNT_PURPOSE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.BANK_ACCOUNT_RELATIONSHIP;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.BANK_ID;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.BANK_NAME;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.BRANCH_ID;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.BRANCH_NAME;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CITY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.COUNTRY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.COUNTRY_OF_BIRTH;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.COUNTRY_OF_NATIONALITY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.DATE_OF_BIRTH;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.FIRST_NAME;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.GENDER;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.GOVERNMENT_ID;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.LAST_NAME;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.MOBILE_NUMBER;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.PHONE_NUMBER;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.POSTAL_CODE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.STATE_PROVINCE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.util.HttpMethod.GET;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletBankAccount;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.HyperwalletErrors;
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
public class HyperwalletGetBankAccountTest {

    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();

    @Mock
    private HyperwalletListener<HyperwalletBankAccount> mListener;
    @Captor
    private ArgumentCaptor<HyperwalletBankAccount> mBankCardArgumentCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testGetBankAccount_returnsAccount() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("bank_account_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        Hyperwallet.getDefault().getBankAccount("trm-854c4ec1-9161-49d6-92e2-b8d15aa4bf56", mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-accounts/trm-854c4ec1-9161-49d6-92e2"
                        + "-b8d15aa4bf56"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener).onSuccess(mBankCardArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletBankAccount bankAccountResponse = mBankCardArgumentCaptor.getValue();
        assertThat(bankAccountResponse, is(notNullValue()));
        assertThat(bankAccountResponse.getField(TYPE), is(BANK_ACCOUNT));
        assertThat(bankAccountResponse.getField(TOKEN), is("trm-854c4ec1-9161-49d6-92e2-b8d15aa4bf56"));
        assertThat(bankAccountResponse.getField(STATUS), is(ACTIVATED));

        assertThat(bankAccountResponse.getField(CREATED_ON), is("2019-01-07T22:03:13"));
        assertThat(bankAccountResponse.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(bankAccountResponse.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(bankAccountResponse.getField(BANK_NAME), is("GREATER WATERBURY HEALTHCARE FCU"));
        assertThat(bankAccountResponse.getField(BANK_ID), is("211179539"));
        assertThat(bankAccountResponse.getField(BRANCH_ID), is("211179539"));
        assertThat(bankAccountResponse.getField(BRANCH_NAME), is("TEST BRANCH"));
        assertThat(bankAccountResponse.getField(BRANCH_NAME), is("TEST BRANCH"));
        assertThat(bankAccountResponse.getField(BANK_ACCOUNT_ID), is("8017110254"));
        assertThat(bankAccountResponse.getField(BANK_ACCOUNT_RELATIONSHIP), is("SELF"));
        assertThat(bankAccountResponse.getField(BANK_ACCOUNT_PURPOSE), is("SAVINGS"));
        assertThat(bankAccountResponse.getField("profileType"), is("INDIVIDUAL"));
        assertThat(bankAccountResponse.getField(FIRST_NAME), is("Marsden"));
        assertThat(bankAccountResponse.getField(LAST_NAME), is("Griffin"));
        assertThat(bankAccountResponse.getField(DATE_OF_BIRTH), is("1980-01-01"));
        assertThat(bankAccountResponse.getField(COUNTRY_OF_BIRTH), is("US"));
        assertThat(bankAccountResponse.getField(COUNTRY_OF_NATIONALITY), is("CA"));
        assertThat(bankAccountResponse.getField(GENDER), is("MALE"));
        assertThat(bankAccountResponse.getField(PHONE_NUMBER), is("+1 604 6666666"));
        assertThat(bankAccountResponse.getField(MOBILE_NUMBER), is("604 666 6666"));
        assertThat(bankAccountResponse.getField(GOVERNMENT_ID), is("987654321"));
        assertThat(bankAccountResponse.getField(ADDRESS_LINE_1), is("950 Granville Street"));
        assertThat(bankAccountResponse.getField(CITY), is("Vancouver"));
        assertThat(bankAccountResponse.getField(COUNTRY), is("CA"));
        assertThat(bankAccountResponse.getField(STATE_PROVINCE), is("BC"));
        assertThat(bankAccountResponse.getField(POSTAL_CODE), is("V6Z1L2"));
    }

    @Test
    public void testGetBankAccount_returnsNoAccount() throws InterruptedException {

        String responseBody = "";
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_NO_CONTENT).withBody(responseBody).mock();

        Hyperwallet.getDefault().getBankAccount("trm-854c4ec1-9161-49d6-92e2-b8d15aa4bf56", mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-accounts/trm-854c4ec1-9161-49d6-92e2"
                        + "-b8d15aa4bf56"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener).onSuccess(mBankCardArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletBankAccount bankCard = mBankCardArgumentCaptor.getValue();
        assertThat(bankCard, is(nullValue()));
    }

    @Test
    public void testGetBankAccount_returnsError() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        Hyperwallet.getDefault().getBankAccount("trm-854c4ec1-9161-49d6-92e2-b8d15aa4bf56", mListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-accounts/trm-854c4ec1-9161-49d6-92e2"
                        + "-b8d15aa4bf56"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener, never()).onSuccess(any(HyperwalletBankAccount.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException exception = mExceptionCaptor.getValue();
        assertThat(exception, is(notNullValue()));
        HyperwalletErrors errors = exception.getHyperwalletErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), is(notNullValue()));
        assertThat(errors.getErrors().size(), is(1));

        HyperwalletError error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("SYSTEM_ERROR"));
        assertThat(error.getMessage(),
                is("A system error has occurred. Please try again. If you continue to receive this error, please "
                        + "contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b)."));

    }
}
