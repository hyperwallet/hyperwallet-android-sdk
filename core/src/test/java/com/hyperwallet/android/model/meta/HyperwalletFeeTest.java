package com.hyperwallet.android.model.meta;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
    public HyperwalletExternalResourceManager mResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testHyperwalletFee_convertJsonObjectWithBankAccount() throws JSONException {
        String data = mResourceManager.getResourceContent("fee_item.json");
        JSONObject jsonObject = new JSONObject(data);
        HyperwalletFee fee = new HyperwalletFee(jsonObject);
        assertThat(fee.getValue(), is("5.00"));
        assertThat(fee.getFeeRateType(), is("FLAT"));
    }

    @Test
    public void testHyperwalletFee_equalsObjects() throws JSONException {
        String data = mResourceManager.getResourceContent("fee_item.json");
        JSONObject jsonObject = new JSONObject(data);
        HyperwalletFee fee = new HyperwalletFee(jsonObject);
        assertThat(fee.equals("some object"), is(false));

        HyperwalletFee duplicateFee = fee;
        assertThat(fee.equals(duplicateFee), is(true));

        jsonObject.put("value", "40.00");
        HyperwalletFee anotherValueFee = new HyperwalletFee(jsonObject);
        assertThat(fee.equals(anotherValueFee), is(false));

        jsonObject.put("value", "5.00");
        jsonObject.put("feeRateType", "PERCENT");
        HyperwalletFee anotherRateTypeFee = new HyperwalletFee(jsonObject);
        assertThat(fee.equals(anotherRateTypeFee), is(false));

        jsonObject.put("feeRateType", "FLAT");
        jsonObject.put("idToken", "ID_TOKEN");
        HyperwalletFee anotherTokenTypeFee = new HyperwalletFee(jsonObject);
        assertThat(fee.equals(anotherTokenTypeFee), is(false));
    }

}