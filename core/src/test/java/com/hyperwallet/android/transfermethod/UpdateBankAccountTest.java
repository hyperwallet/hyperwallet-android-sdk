package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.ADDRESS_LINE_1;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CITY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.MOBILE_NUMBER;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.PHONE_NUMBER;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.POSTAL_CODE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.STATE_PROVINCE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.util.HttpMethod.PUT;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletRestException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.transfermethod.BankAccount;
import com.hyperwallet.android.rule.ExternalResourceManager;
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
public class UpdateBankAccountTest {

    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();

    @Mock
    private HyperwalletListener<BankAccount> mockBankAccountListener;
    @Captor
    private ArgumentCaptor<BankAccount> mBankAccountCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mHyperwalletExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testUpdateBankAccount_withSuccess() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContent("bank_account_update_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        final BankAccount bankAccount = new BankAccount
                .Builder()
                .addressLine1("618 Confluence Way")
                .city("Calgary")
                .mobileNumber("604 666 7777")
                .phoneNumber("+1 403-452-3115")
                .postalCode("T2G0G1")
                .stateProvince("AB")
                .token("trm-fake-token")
                .build();

        assertThat(bankAccount.getField(ADDRESS_LINE_1), is("618 Confluence Way"));
        assertThat(bankAccount.getField(CITY), is("Calgary"));
        assertThat(bankAccount.getField(MOBILE_NUMBER), is("604 666 7777"));
        assertThat(bankAccount.getField(PHONE_NUMBER), is("+1 403-452-3115"));
        assertThat(bankAccount.getField(POSTAL_CODE), is("T2G0G1"));
        assertThat(bankAccount.getField(STATE_PROVINCE), is("AB"));
        assertThat(bankAccount.getField(TOKEN),
                is("trm-fake-token"));

        Hyperwallet.getDefault().updateBankAccount(bankAccount, mockBankAccountListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        verify(mockBankAccountListener).onSuccess(mBankAccountCaptor.capture());
        verify(mockBankAccountListener, never()).onFailure(any(HyperwalletException.class));
        assertThat(recordedRequest.getMethod(), is(PUT.name()));

        BankAccount bankAccountResponse = mBankAccountCaptor.getValue();
        assertThat(bankAccountResponse, is(notNullValue()));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/bank-accounts/trm-fake-token"));

        assertThat(bankAccountResponse.getField(TOKEN), is("trm-fake-token"));
        assertThat(bankAccountResponse.getField(STATUS), is("ACTIVATED"));
        assertThat(bankAccountResponse.getField(TYPE), is(BANK_ACCOUNT));
        assertThat(bankAccountResponse.getField(ADDRESS_LINE_1), is("618 Confluence Way"));
        assertThat(bankAccountResponse.getField(CITY), is("Calgary"));
        assertThat(bankAccountResponse.getField(MOBILE_NUMBER), is("604 666 7777"));
        assertThat(bankAccountResponse.getField(PHONE_NUMBER), is("+1 403-452-3115"));
        assertThat(bankAccountResponse.getField(POSTAL_CODE), is("T2G0G1"));
        assertThat(bankAccountResponse.getField(STATE_PROVINCE), is("AB"));
    }

    @Test
    public void testUpdateBankAccount_withValidationError() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContentError(
                "bank_account_update_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        final BankAccount bankAccount = new BankAccount
                .Builder()
                .postalCode("00G0G1")
                .stateProvince("WW")
                .token("trm-fake-token")
                .build();

        assertThat(bankAccount.getField(POSTAL_CODE), is("00G0G1"));
        assertThat(bankAccount.getField(STATE_PROVINCE), is("WW"));
        assertThat(bankAccount.getField(TOKEN),
                is("trm-fake-token"));

        Hyperwallet.getDefault().updateBankAccount(bankAccount, mockBankAccountListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        verify(mockBankAccountListener, never()).onSuccess(any(BankAccount.class));
        verify(mockBankAccountListener).onFailure(mHyperwalletExceptionCaptor.capture());
        assertThat(recordedRequest.getMethod(), is(PUT.name()));

        HyperwalletException hyperwalletException = mHyperwalletExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        assertThat(((HyperwalletRestException) hyperwalletException).getHttpCode(),
                is(HttpURLConnection.HTTP_BAD_REQUEST));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/bank-accounts/trm-fake-token"));

        Errors errors = hyperwalletException.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), is(notNullValue()));
        assertThat(errors.getErrors().size(), is(2));

        Error error1 = errors.getErrors().get(0);
        assertThat(error1.getCode(), is("CONSTRAINT_VIOLATIONS"));
        assertThat(error1.getFieldName(), is("postalCode"));
        assertThat(error1.getMessage(), is("Invalid Postal Code"));

        Error error2 = errors.getErrors().get(1);
        assertThat(error2.getCode(), is("VALIDATION_ERROR"));
        assertThat(error2.getFieldName(), is("stateProvince"));
        assertThat(error2.getMessage(), is("Invalid State Province"));
    }
}
