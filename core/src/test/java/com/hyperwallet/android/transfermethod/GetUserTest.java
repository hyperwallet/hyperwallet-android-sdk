package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.util.HttpMethod.GET;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
import com.hyperwallet.android.model.Errors;
import com.hyperwallet.android.model.user.User;
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
public class GetUserTest {
    private static final long AWAIT_TIMEOUT_MILLIS = 100l;
    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();

    @Mock
    private HyperwalletListener<User> mListener;
    @Captor
    private ArgumentCaptor<User> mUserArgumentCaptor;
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
                is("/rest/v3/users/test-user-token"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener).onSuccess(mUserArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        User userResponse = mUserArgumentCaptor.getValue();
        assertThat(userResponse, is(notNullValue()));
        assertThat(userResponse.getToken(), is("test-user-token"));
        assertThat(userResponse.getStatus(), is("PRE_ACTIVATED"));
        assertThat(userResponse.getVerificationStatus(), is("NOT_REQUIRED"));
        assertThat(userResponse.getCreatedOn(), is("2017-10-30T22:15:45"));
        assertThat(userResponse.getClientUserId(), is("123345789"));
        assertThat(userResponse.getProfileType(), is("INDIVIDUAL"));
        assertThat(userResponse.getFirstName(), is("Some"));
        assertThat(userResponse.getLastName(), is("Guy"));
        assertThat(userResponse.getDateOfBirth(), is("1991-01-01"));
        assertThat(userResponse.getEmail(), is("someguy@hyperwallet.com"));
        assertThat(userResponse.getAddressLine1(), is("575 Market Street"));
        assertThat(userResponse.getCity(), is("San Francisco"));
        assertThat(userResponse.getStateProvince(), is("CA"));
        assertThat(userResponse.getCountry(), is("US"));
        assertThat(userResponse.getPostalCode(), is("94105"));
        assertThat(userResponse.getLanguage(), is("en"));
        assertThat(userResponse.getProgramToken(), is("test-program-token"));
    }

    @Test
    public void testGetUser_returnsNoUser() throws InterruptedException {
        String responseBody = "";
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_NO_CONTENT).withBody(responseBody).mock();

        Hyperwallet.getDefault().getUser(mListener);
        mAwait.await(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener).onSuccess(mUserArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));

        User user = mUserArgumentCaptor.getValue();
        assertThat(user, is(nullValue()));
    }

    @Test
    public void testGetUser_returnsError() throws InterruptedException {
        String responseBody = mExternalResourceManager.getResourceContentError("system_error_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_BAD_REQUEST).withBody(responseBody).mock();

        Hyperwallet.getDefault().getUser(mListener);
        mAwait.await(AWAIT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        RecordedRequest recordedRequest = mServer.getRequest();
        assertThat(recordedRequest.getPath(),
                is("/rest/v3/users/test-user-token"));
        assertThat(recordedRequest.getMethod(), is(GET.name()));

        verify(mListener, never()).onSuccess(any(User.class));
        verify(mListener).onFailure(mExceptionCaptor.capture());

        HyperwalletException hyperwalletException = mExceptionCaptor.getValue();
        assertThat(hyperwalletException, is(notNullValue()));
        Errors errors = hyperwalletException.getErrors();
        assertThat(errors, is(notNullValue()));
        assertThat(errors.getErrors(), is(notNullValue()));
        assertThat(errors.getErrors().size(), is(1));

        Error error = errors.getErrors().get(0);
        assertThat(error.getCode(), is("SYSTEM_ERROR"));
        assertThat(error.getMessage(),
                is("A system error has occurred. Please try again. If you continue to receive this error, please "
                        + "contact customer support for assistance (Ref ID: 99b4ad5c-4aac-4cc2-aa9b-4b4f4844ac9b)."));
    }
}
