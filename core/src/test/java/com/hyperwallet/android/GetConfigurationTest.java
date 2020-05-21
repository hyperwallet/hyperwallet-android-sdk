package com.hyperwallet.android;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import static com.hyperwallet.android.ExceptionMapper.EC_AUTHENTICATION_TOKEN_PROVIDER_EXCEPTION;

import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.Error;
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
public class GetConfigurationTest {

    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private HyperwalletListener<Configuration> mMockConfigurationListener;
    @Captor
    private ArgumentCaptor<Configuration> mConfigurationCaptor;
    @Captor
    private ArgumentCaptor<HyperwalletException> mHyperwalletExceptionCaptor;
    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testGetConfiguration_success() throws InterruptedException {
        mHyperwalletSdkMock.setFailAuthentication(false);

        Hyperwallet.getDefault().getConfiguration(mMockConfigurationListener);
        mAwait.await(100, TimeUnit.MILLISECONDS);
        verify(mMockConfigurationListener).onSuccess(mConfigurationCaptor.capture());
        verify(mMockConfigurationListener, never()).onFailure(any(HyperwalletException.class));

        Configuration capturedConfiguration = mConfigurationCaptor.getValue();
        assertThat(capturedConfiguration, is(notNullValue()));
        assertThat(capturedConfiguration.getEnvironment(), is("TEST-DEV"));
        assertThat(capturedConfiguration.getInsightApiUri(), is("insights.test.com/track/events"));
    }

    @Test
    public void testGetConfiguration_failure() throws InterruptedException {
        mHyperwalletSdkMock.setFailAuthentication(true);

        Hyperwallet.getDefault().getConfiguration(mMockConfigurationListener);
        mAwait.await(100, TimeUnit.MILLISECONDS);
        verify(mMockConfigurationListener).onFailure(mHyperwalletExceptionCaptor.capture());
        verify(mMockConfigurationListener, never()).onSuccess(any(Configuration.class));

        HyperwalletException captorValue = mHyperwalletExceptionCaptor.getValue();
        assertThat(captorValue, is(notNullValue()));
        assertThat(captorValue.getErrors().getErrors(), Matchers.<Error>hasSize(1));
        assertThat(captorValue.getErrors().getErrors().get(0).getCode(),
                is(EC_AUTHENTICATION_TOKEN_PROVIDER_EXCEPTION));
    }
}
