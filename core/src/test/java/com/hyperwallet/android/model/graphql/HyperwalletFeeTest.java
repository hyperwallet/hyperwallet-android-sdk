package com.hyperwallet.android.model.graphql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static com.hyperwallet.android.model.graphql.HyperwalletFee.FeeRate.FLAT;

import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletFeeTest {

    @Rule
    public final HyperwalletExternalResourceManager mResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testHyperwalletFee_convertJsonObject() throws JSONException {
        String data = mResourceManager.getResourceContent("fee_item.json");
        JSONObject jsonObject = new JSONObject(data);
        HyperwalletFee fee = new HyperwalletFee(jsonObject);
        assertThat(fee.getValue(), is("5.00"));
        assertThat(fee.getFeeRateType(), is(FLAT));
    }

    @Test
    public void testHyperwalletFee_equalsObjects() throws JSONException {
        String data = mResourceManager.getResourceContent("fee_item.json");
        JSONObject jsonObject = new JSONObject(data);
        HyperwalletFee fee = new HyperwalletFee(jsonObject);
        assertThat(fee.equals("some object"), is(false));

        HyperwalletFee duplicateFee = fee;
        assertThat(fee.equals(duplicateFee), is(true));
    }

    @Test
    public void testHyperwalletFee_NonEqualsObjects() throws JSONException {
        String data = mResourceManager.getResourceContent("fee_another_item.json");
        JSONObject jsonObject = new JSONObject(data);
        jsonObject.put("country", "CA");
        jsonObject.put("currency", "CAD");
        HyperwalletFee firstFee = new HyperwalletFee(new JSONObject("{\n"
                + "  \"country\": \"CA\",\n"
                + "  \"currency\": \"CAD\",\n"
                + "  \"value\": \"15.00\",\n"
                + "  \"feeRateType\": \"PERCENT\",\n"
                + "  \"idToken\": \"12345\"\n"
                + "}"));

        HyperwalletFee secondFee = new HyperwalletFee(new JSONObject("{\n"
                + "  \"country\": \"US\",\n"
                + "  \"value\": \"15.00\",\n"
                + "  \"feeRateType\": \"PERCENT\",\n"
                + "  \"idToken\": \"12345\"\n"
                + "}"));

        assertThat(firstFee.equals(secondFee), is(false));

        secondFee = new HyperwalletFee(new JSONObject("{\n"
                + "  \"country\": \"CA\",\n"
                + "  \"currency\": \"USD\",\n"
                + "  \"value\": \"15.00\",\n"
                + "  \"feeRateType\": \"PERCENT\",\n"
                + "  \"idToken\": \"12345\"\n"
                + "}"));

        assertThat(firstFee.equals(secondFee), is(false));

        secondFee = new HyperwalletFee(new JSONObject("{\n"
                + "  \"country\": \"CA\",\n"
                + "  \"currency\": \"CAD\",\n"
                + "  \"value\": \"15.00\",\n"
                + "  \"feeRateType\": \"FLAT\",\n"
                + "  \"idToken\": \"12345\"\n"
                + "}"));

        assertThat(firstFee.equals(secondFee), is(false));
    }

}