package com.hyperwallet.android.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.ExceptionMapper.EC_AUTHENTICATION_TOKEN_PROVIDER_EXCEPTION;

import com.hyperwallet.android.Configuration;
import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.HyperwalletError;
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletGetConfigurationTest {

    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<Configuration> mockConfigurationListener;
    @Captor
    private ArgumentCaptor<Configuration> mConfigurationCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mHyperwalletExceptionCaptor;
    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testGetConfiguration_Success() throws InterruptedException {
        mHyperwalletSdkMock.setFailAuthentication(false);

        Hyperwallet.getDefault().getConfiguration(mockConfigurationListener);
        mAwait.await(100, TimeUnit.MILLISECONDS);
        verify(mockConfigurationListener).onSuccess(mConfigurationCaptor.capture());
        verify(mockConfigurationListener, never()).onFailure(any(HyperwalletException.class));

        Configuration capturedConfiguration = mConfigurationCaptor.getValue();
        assertThat(capturedConfiguration, is(notNullValue()));
        assertThat(capturedConfiguration.getEnvironment(), is("TEST-DEV"));
        assertThat(capturedConfiguration.getInsightApiUrl(), is("insights.test.com/track/events"));
    }

    @Test
    public void testGetConfiguration_Failure() throws InterruptedException {
        mHyperwalletSdkMock.setFailAuthentication(true);

        Hyperwallet.getDefault().getConfiguration(mockConfigurationListener);
        mAwait.await(100, TimeUnit.MILLISECONDS);
        verify(mockConfigurationListener).onFailure(mHyperwalletExceptionCaptor.capture());
        verify(mockConfigurationListener, never()).onSuccess(any(Configuration.class));

        HyperwalletException captorValue = mHyperwalletExceptionCaptor.getValue();
        assertThat(captorValue, is(notNullValue()));
        assertThat(captorValue.getHyperwalletErrors().getErrors(), Matchers.<HyperwalletError>hasSize(1));
        assertThat(captorValue.getHyperwalletErrors().getErrors().get(0).getCode(),
                is(EC_AUTHENTICATION_TOKEN_PROVIDER_EXCEPTION));
    }
}
