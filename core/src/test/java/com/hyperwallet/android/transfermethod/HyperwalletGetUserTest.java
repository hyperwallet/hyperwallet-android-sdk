package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.model.HyperwalletUser.UserFields.ADDRESS_LINE_1;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.CITY;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.CLIENT_USER_ID;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.COUNTRY;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.CREATED_ON;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.DATE_OF_BIRTH;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.EMAIL;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.FIRST_NAME;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.LANGUAGE;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.LAST_NAME;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.POSTAL_CODE;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.PROFILE_TYPE;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.PROGRAM_TOKEN;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.STATE_PROVINCE;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.STATUS;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.TOKEN;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.VERIFICATION_STATUS;
import static com.hyperwallet.android.util.HttpMethod.GET;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletError;
import com.hyperwallet.android.model.HyperwalletErrors;
import com.hyperwallet.android.model.HyperwalletUser;
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
public class HyperwalletGetUserTest {
    private static final long AWAIT_TIMEOUT_MILLIS = 100l;
    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();

    @Mock
    private HyperwalletListener<HyperwalletUser> mListener;
    @Captor
    private ArgumentCaptor<HyperwalletUser> mUserArgumentCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testGetUser_returnsUser() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContent("user_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();

        Hyperwallet.getDefault().getUser(mListener);
        mAwait.await(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener).onSuccess(mUserArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletUser userResponse = mUserArgumentCaptor.getValue();
        assertThat(userResponse, is(notNullValue()));
        assertThat(userResponse.getField(TOKEN), is("usr-f9154016-94e8-4686-a840-075688ac07b5"));
        assertThat(userResponse.getField(STATUS), is("PRE_ACTIVATED"));
        assertThat(userResponse.getField(VERIFICATION_STATUS), is("NOT_REQUIRED"));
        assertThat(userResponse.getField(CREATED_ON), is("2017-10-30T22:15:45"));
        assertThat(userResponse.getField(CLIENT_USER_ID), is("123345789"));
        assertThat(userResponse.getField(PROFILE_TYPE), is("INDIVIDUAL"));
        assertThat(userResponse.getField(FIRST_NAME), is("Some"));
        assertThat(userResponse.getField(LAST_NAME), is("Guy"));
        assertThat(userResponse.getField(DATE_OF_BIRTH), is("1991-01-01"));
        assertThat(userResponse.getField(EMAIL), is("someguy@hyperwallet.com"));
        assertThat(userResponse.getField(ADDRESS_LINE_1), is("575 Market Street"));
        assertThat(userResponse.getField(CITY), is("San Francisco"));
        assertThat(userResponse.getField(STATE_PROVINCE), is("CA"));
        assertThat(userResponse.getField(COUNTRY), is("US"));
        assertThat(userResponse.getField(POSTAL_CODE), is("94105"));
        assertThat(userResponse.getField(LANGUAGE), is("en"));
        assertThat(userResponse.getField(PROGRAM_TOKEN), is("prg-83836cdf-2ce2-4696-8bc5-f1b86077238c"));
    }

    @Test
    public void testGetUser_returnsNoUser() throws InterruptedException {
        String responseBody = "";
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_NO_CONTENT).withBody(responseBody).mock();

        Hyperwallet.getDefault().getUser(mListener);
        mAwait.await(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener).onSuccess(mUserArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        HyperwalletUser hyperwalletUser = mUserArgumentCaptor.getValue();
        assertThat(hyperwalletUser, is(nullValue()));
    }

    @Test
    public void testGetUser_returnsError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        Hyperwallet.getDefault().getUser(mListener);
        mAwait.await(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/usr-fbfd5848-60d0-43c5-8462-099c959b49c7"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener, never()).onSuccess(any(HyperwalletUser.class));
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
    }
}
