package com.hyperwallet.android.model.graphql;

import androidx.core.widget.TextViewCompat;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.listener.HyperwalletListener;
import com.hyperwallet.android.model.graphql.field.TransferMethodConfigurationFieldResult;
import com.hyperwallet.android.model.graphql.field.TransferMethodUpdateConfigurationField;
import com.hyperwallet.android.model.graphql.field.TransferMethodUpdateConfigurationFieldResult;
import com.hyperwallet.android.model.graphql.query.TransferMethodConfigurationFieldQuery;
import com.hyperwallet.android.model.graphql.query.TransferMethodUpdateConfigurationFieldQuery;
import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.rule.HyperwalletMockWebServer;
import com.hyperwallet.android.rule.HyperwalletSdkMock;

import org.checkerframework.common.value.qual.StaticallyExecutable;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class RetrieveTransferMethodUpdateConfigurationFieldsTest {
    @Rule
    public final ExternalResourceManager mExternalResourceManager =
            new ExternalResourceManager();
    @Rule
    public HyperwalletMockWebServer mServer = new HyperwalletMockWebServer();
    @Rule
    public HyperwalletSdkMock mHyperwalletSdkMock = new HyperwalletSdkMock(mServer);
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();
    @Mock
    private TransferMethodUpdateConfigurationFieldQuery mMockedQuery;
    @Mock
    private HyperwalletListener<TransferMethodUpdateConfigurationField> mListener;
    @Captor
    private ArgumentCaptor<HyperwalletException> mExceptionCaptor;
    @Captor
    private ArgumentCaptor<TransferMethodUpdateConfigurationFieldResult> mResultArgumentCaptor;

    private CountDownLatch mAwait = new CountDownLatch(1);

    @Test
    public void testTransferMethodUpdateConfigurationFields_returnFields() throws Exception {

        String responseBody = mExternalResourceManager.getResourceContent("tmc_update_field_bankaccount_response.json");
        mServer.mockResponse().withHttpResponseCode(HttpURLConnection.HTTP_OK).withBody(responseBody).mock();
        Hyperwallet.getDefault().retrieveTransferMethodUpdateConfigurationFields(mMockedQuery, mListener);

        mAwait.await(100, TimeUnit.MILLISECONDS);

        /*
        // retrieve response
        verify(mListener).onSuccess(mResultArgumentCaptor.capture());
        verify(mListener, never()).onFailure(any(HyperwalletException.class));
        TransferMethodUpdateConfigurationFieldResult resultFields = mResultArgumentCaptor.getValue();

        // assert fields
        assertThat(resultFields.getFields(), is(notNullValue()));

         */
    }

}
