package com.hyperwallet.android.model.graphql;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_CARD;

import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ProcessingTimeTest {
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testProcessingTime_convertJsonObject() throws JSONException {
        String jsonResponse = mExternalResourceManager.getResourceContent(
                "processing_time_response.json");
        JSONObject jsonResponseObject = new JSONObject(jsonResponse);
        ProcessingTime processingTime = new ProcessingTime(jsonResponseObject);

        assertThat(processingTime.getTransferMethodType(), is(BANK_CARD));
        assertThat(processingTime.getCountry(), is("CA"));
        assertThat(processingTime.getCurrency(), is("CAD"));
        assertThat(processingTime.getValue(), is("1-3 Business days"));
    }
}