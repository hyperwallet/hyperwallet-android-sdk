package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.StatusTransition.StatusDefinition.ACTIVATED;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.ADDRESS_LINE_1;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.ADDRESS_LINE_2;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BANK_ACCOUNT_ID;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BANK_ACCOUNT_PURPOSE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BANK_ACCOUNT_RELATIONSHIP;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BANK_ID;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BANK_NAME;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BRANCH_ADDRESS_LINE_1;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BRANCH_ADDRESS_LINE_2;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BRANCH_CITY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BRANCH_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BRANCH_ID;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BRANCH_NAME;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BRANCH_POSTAL_CODE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.BRANCH_STATE_PROVINCE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CITY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.COUNTRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.COUNTRY_OF_BIRTH;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.COUNTRY_OF_NATIONALITY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.DATE_OF_BIRTH;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.DRIVER_LICENSE_ID;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.EMPLOYER_ID;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.FIRST_NAME;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.GENDER;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.GOVERNMENT_ID;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.GOVERNMENT_ID_TYPE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.IS_DEFAULT_TRANSFER_METHOD;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.LAST_NAME;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.MIDDLE_NAME;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.MOBILE_NUMBER;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.PASSPORT_ID;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.PHONE_NUMBER;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.POSTAL_CODE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.STATE_PROVINCE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.util.HttpMethod.POST;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
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
public class CreateBankAccountTest {

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
    public void testCreateBankAccount_withSuccess() throws InterruptedException {

        String responseBody = mExternalResourceManager.getResourceContent("bank_account_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_CREATED).withBody(responseBody).mock();

        final BankAccount bankAccount = new BankAccount
                .Builder("US", "USD", "8017110254")
                .addressLine1("950 Granville Street")
                .bankAccountPurpose(BankAccount.Purpose.SAVINGS)
                .bankAccountRelationship("SELF")
                .bankId("211179539")
                .bankName("GREATER WATERBURY HEALTHCARE FCU")
                .branchId("211179539")
                .branchName("TEST BRANCH")
                .city("Vancouver")
                .country("CA")
                .countryOfBirth("US")
                .countryOfNationality("CA")
                .dateOfBirth("1980-01-01")
                .gender("MALE")
                .governmentId("987654321")
                .firstName("Marsden")
                .lastName("Griffin")
                .mobileNumber("604 666 6666")
                .phoneNumber("+1 604 6666666")
                .postalCode("V6Z1L2")
                .stateProvince("BC")
                .token("trm-fake-token")
                .build();

        Hyperwallet.getDefault().createBankAccount(bankAccount, mockBankAccountListener);
        mAwait.await(1000, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        verify(mockBankAccountListener).onSuccess(mBankAccountCaptor.capture());
        verify(mockBankAccountListener, never()).onFailure(any(HyperwalletException.class));
        assertThat(recordedRequest.getMethod(), is(POST.name()));

        BankAccount bankAccountResponse = mBankAccountCaptor.getValue();
        assertThat(bankAccountResponse, is(notNullValue()));

        //bank info
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/bank-accounts"));
        assertThat(bankAccountResponse.getField(BANK_ACCOUNT_ID), is("8017110254"));
        assertThat(bankAccountResponse.getField(BANK_ACCOUNT_PURPOSE), is(BankAccount.Purpose.SAVINGS));
        assertThat(bankAccountResponse.getField(BANK_ACCOUNT_RELATIONSHIP), is("SELF"));
        assertThat(bankAccountResponse.getField(BANK_ID), is("211179539"));
        assertThat(bankAccountResponse.getField(BANK_NAME), is("GREATER WATERBURY HEALTHCARE FCU"));
        assertThat(bankAccountResponse.getField(BRANCH_ID), is("211179539"));
        assertThat(bankAccountResponse.getField(BRANCH_NAME), is("TEST BRANCH"));
        assertThat(bankAccountResponse.getField(BRANCH_ADDRESS_LINE_1), is(nullValue()));
        assertThat(bankAccountResponse.getField(BRANCH_ADDRESS_LINE_2), is(nullValue()));
        assertThat(bankAccountResponse.getField(BRANCH_CITY), is(nullValue()));
        assertThat(bankAccountResponse.getField(BRANCH_COUNTRY), is(nullValue()));
        assertThat(bankAccountResponse.getField(BRANCH_POSTAL_CODE), is(nullValue()));
        assertThat(bankAccountResponse.getField(BRANCH_STATE_PROVINCE), is(nullValue()));

        assertThat(bankAccountResponse.getField(IS_DEFAULT_TRANSFER_METHOD), is(nullValue()));
        assertThat(bankAccountResponse.getField(STATUS), is(ACTIVATED));
        assertThat(bankAccountResponse.getField(TOKEN), is("trm-fake-token"));
        assertThat(bankAccountResponse.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(bankAccountResponse.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(bankAccountResponse.getField(TYPE), is(BANK_ACCOUNT));
        assertThat(bankAccountResponse.getField(ADDRESS_LINE_1), is("950 Granville Street"));
        assertThat(bankAccountResponse.getField(ADDRESS_LINE_2), is(nullValue()));
        assertThat(bankAccountResponse.getField(CITY), is("Vancouver"));
        assertThat(bankAccountResponse.getField(COUNTRY), is("CA"));
        assertThat(bankAccountResponse.getField(COUNTRY_OF_BIRTH), is("US"));
        assertThat(bankAccountResponse.getField(COUNTRY_OF_NATIONALITY), is("CA"));
        assertThat(bankAccountResponse.getField(CREATED_ON), is(notNullValue()));
        assertThat(bankAccountResponse.getField(DATE_OF_BIRTH), is("1980-01-01"));
        assertThat(bankAccountResponse.getField(DRIVER_LICENSE_ID), is(nullValue()));
        assertThat(bankAccountResponse.getField(EMPLOYER_ID), is(nullValue()));
        assertThat(bankAccountResponse.getField(FIRST_NAME), is("Marsden"));
        assertThat(bankAccountResponse.getField(GENDER), is("MALE"));
        assertThat(bankAccountResponse.getField(GOVERNMENT_ID), is("987654321"));
        assertThat(bankAccountResponse.getField(GOVERNMENT_ID_TYPE), is(nullValue()));
        assertThat(bankAccountResponse.getField(LAST_NAME), is("Griffin"));
        assertThat(bankAccountResponse.getField(MIDDLE_NAME), is(nullValue()));
        assertThat(bankAccountResponse.getField(MOBILE_NUMBER), is("604 666 6666"));
        assertThat(bankAccountResponse.getField(PASSPORT_ID), is(nullValue()));
        assertThat(bankAccountResponse.getField(PHONE_NUMBER), is("+1 604 6666666"));
        assertThat(bankAccountResponse.getField(POSTAL_CODE), is("V6Z1L2"));
        assertThat(bankAccountResponse.getField(STATE_PROVINCE), is("BC"));
    }

    @Test
    public void testCreateBankAccount_withValidationError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("transfer_method_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        final BankAccount bankAccount = new BankAccount
                .Builder(null, "USD", "8017110254")
                .branchId("211179539")
                .bankAccountPurpose(BankAccount.Purpose.CHECKING)
                .build();

        Hyperwallet.getDefault().createBankAccount(bankAccount, mockBankAccountListener);
        mAwait.await(1000, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        verify(mockBankAccountListener, never()).onSuccess(any(BankAccount.class));
        verify(mockBankAccountListener).onFailure(mHyperwalletExceptionCaptor.capture());
        assertThat(recordedRequest.getMethod(), is(POST.name()));

        HyperwalletException hyperwalletException = mHyperwalletExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));

        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token/bank-accounts"));

        Errors errors = hyperwalletException.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), is(notNullValue()));
        assertThat(errors.getErrors().size(), is(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("CONSTRAINT_VIOLATIONS"));
        assertThat(error.getFieldName(), is("transferMethodCountry"));
        assertThat(error.getMessage(), is("You must provide a value for this field"));
    }

}
