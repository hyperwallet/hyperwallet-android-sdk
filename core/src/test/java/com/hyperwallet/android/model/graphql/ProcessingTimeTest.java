package com.hyperwallet.android.model.graphql;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodTypes.BANK_CARD;

import com.hyperwallet.android.rule.ExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ProcessingTimeTest {
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();

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


    @Test
    public void testProcessingTime_isEqual() throws JSONException {

        ProcessingTime actualProcessingTime = new ProcessingTime(new JSONObject("{\n"
                + "  \"transferMethodType\": \"BANK_CARD\",\n"
                + "  \"country\": \"CA\",\n"
                + "  \"currency\": \"CAD\",\n"
                + "  \"value\": \"1-3 Business days\"\n"
                + "}"));

        ProcessingTime expectedProcessingTime = new ProcessingTime(new JSONObject("{\n"
                + "  \"transferMethodType\": \"BANK_CARD\",\n"
                + "  \"country\": \"CA\",\n"
                + "  \"currency\": \"CAD\",\n"
                + "  \"value\": \"1-2\"\n"
                + "}"));
        assertThat(actualProcessingTime.equals(expectedProcessingTime), is(true));
        assertThat(actualProcessingTime.hashCode(), is(expectedProcessingTime.hashCode()));


    }

    @Test
    public void testProcessingTime_isNotEqual() throws JSONException {

        ProcessingTime actualProcessingTime = new ProcessingTime(new JSONObject("{\n"
                + "  \"transferMethodType\": \"BANK_CARD\",\n"
                + "  \"country\": \"US\",\n"
                + "  \"currency\": \"USD\",\n"
                + "  \"value\": \"1-2\"\n"
                + "}"));

        ProcessingTime anotherProcessingTime = new ProcessingTime(new JSONObject("{\n"
                + "  \"transferMethodType\": \"BANK_CARD\",\n"
                + "  \"country\": \"US\",\n"
                + "  \"currency\": \"CAD\",\n"
                + "  \"value\": \"1-2\"\n"
                + "}"));
        assertThat(actualProcessingTime.equals(anotherProcessingTime), is(false));
        assertThat(actualProcessingTime.hashCode(), is(not(anotherProcessingTime.hashCode())));
        assertThat(actualProcessingTime.equals(null), is(false));
    }

}
