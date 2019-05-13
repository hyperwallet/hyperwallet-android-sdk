package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.ADDRESS_LINE_1;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CITY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.MOBILE_NUMBER;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.PHONE_NUMBER;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.POSTAL_CODE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.STATE_PROVINCE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.util.HttpMethod.PUT;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.exception.HyperwalletRestException;
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
public class HyperwalletUpdateBankAccountTest {

    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();

    @Mock
    private HyperwalletListener<HyperwalletBankAccount> mockBankAccountListener;
    @Captor
    private ArgumentCaptor<HyperwalletBankAccount> mBankAccountCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mHyperwalletExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testUpdateBankAccount_withSuccess() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContent("bank_account_update_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        final HyperwalletBankAccount hyperwalletBankAccount = new HyperwalletBankAccount
                .Builder()
                .addressLine1("618 Confluence Way")
                .city("Calgary")
                .mobileNumber("604 666 7777")
                .phoneNumber("+1 403-452-3115")
                .postalCode("T2G0G1")
                .stateProvince("AB")
                .token("trm-854c4ec1-9161-49d6-92e2-b8d15aa4bf56")
                .build();

        assertThat(hyperwalletBankAccount.getField(ADDRESS_LINE_1), is("618 Confluence Way"));
        assertThat(hyperwalletBankAccount.getField(CITY), is("Calgary"));
        assertThat(hyperwalletBankAccount.getField(MOBILE_NUMBER), is("604 666 7777"));
        assertThat(hyperwalletBankAccount.getField(PHONE_NUMBER), is("+1 403-452-3115"));
        assertThat(hyperwalletBankAccount.getField(POSTAL_CODE), is("T2G0G1"));
        assertThat(hyperwalletBankAccount.getField(STATE_PROVINCE), is("AB"));
        assertThat(hyperwalletBankAccount.getField(TOKEN),
                is("trm-854c4ec1-9161-49d6-92e2-b8d15aa4bf56"));

        Hyperwallet.getDefault().updateBankAccount(hyperwalletBankAccount, mockBankAccountListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        verify(mockBankAccountListener).onSuccess(mBankAccountCaptor.capture());
        verify(mockBankAccountListener, never()).onFailure(any(HyperwalletException.class));
        assertThat(recordedRequest.getMethod(), is(PUT.name()));

        HyperwalletBankAccount bankAccountResponse = mBankAccountCaptor.getValue();
        assertThat(bankAccountResponse, is(notNullValue()));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-accounts/trm-854c4ec1-9161-49d6-92e2"
                        + "-b8d15aa4bf56"));

        assertThat(bankAccountResponse.getField(TOKEN), is("trm-854c4ec1-9161-49d6-92e2-b8d15aa4bf56"));
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

        final HyperwalletBankAccount hyperwalletBankAccount = new HyperwalletBankAccount
                .Builder()
                .postalCode("00G0G1")
                .stateProvince("WW")
                .token("trm-56b976c5-26b2-42fa-87cf-14b3366673c6")
                .build();

        assertThat(hyperwalletBankAccount.getField(POSTAL_CODE), is("00G0G1"));
        assertThat(hyperwalletBankAccount.getField(STATE_PROVINCE), is("WW"));
        assertThat(hyperwalletBankAccount.getField(TOKEN),
                is("trm-56b976c5-26b2-42fa-87cf-14b3366673c6"));

        Hyperwallet.getDefault().updateBankAccount(hyperwalletBankAccount, mockBankAccountListener);
        mAwait.await(500, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        verify(mockBankAccountListener, never()).onSuccess(any(HyperwalletBankAccount.class));
        verify(mockBankAccountListener).onFailure(mHyperwalletExceptionCaptor.capture());
        assertThat(recordedRequest.getMethod(), is(PUT.name()));

        HyperwalletException hyperwalletException = mHyperwalletExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        assertThat(((HyperwalletRestException) hyperwalletException).getHttpCode(),
                is(HttpURLConnection.HTTP_BAD_REQUEST));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7/bank-accounts/trm-56b976c5-26b2-42fa-87cf"
                        + "-14b3366673c6"));

        HyperwalletErrors hyperwalletErrors = hyperwalletException.getHyperwalletErrors();
        assertThat(hyperwalletErrors, is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors(), is(notNullValue()));
        assertThat(hyperwalletErrors.getErrors().size(), is(2));

        HyperwalletError hyperwalletError1 = hyperwalletErrors.getErrors().get(0);
        assertThat(hyperwalletError1.getCode(), is("CONSTRAINT_VIOLATIONS"));
        assertThat(hyperwalletError1.getFieldName(), is("postalCode"));
        assertThat(hyperwalletError1.getMessage(), is("Invalid Postal Code"));

        HyperwalletError hyperwalletError2 = hyperwalletErrors.getErrors().get(1);
        assertThat(hyperwalletError2.getCode(), is("VALIDATION_ERROR"));
        assertThat(hyperwalletError2.getFieldName(), is("stateProvince"));
        assertThat(hyperwalletError2.getMessage(), is("Invalid State Province"));
    }
}
