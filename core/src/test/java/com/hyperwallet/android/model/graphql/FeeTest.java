package com.hyperwallet.android.model.graphql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static com.hyperwallet.android.model.graphql.Fee.FeeRate.FLAT;

import com.hyperwallet.android.rule.ExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class FeeTest {

    @Rule
    public final ExternalResourceManager mResourceManager = new ExternalResourceManager();

    @Test
    public void testFee_convertJsonObject() throws JSONException {
        String data = mResourceManager.getResourceContent("fee_item.json");
        JSONObject jsonObject = new JSONObject(data);
        Fee fee = new Fee(jsonObject);
        assertThat(fee.getValue(), is("5.00"));
        assertThat(fee.getFeeRateType(), is(FLAT));
    }

    @Test
    public void testFee_equalsObjects() throws JSONException {
        String data = mResourceManager.getResourceContent("fee_item.json");
        JSONObject jsonObject = new JSONObject(data);
        Fee fee = new Fee(jsonObject);
        assertThat(fee.equals("some object"), is(false));

        Fee duplicateFee = fee;
        assertThat(fee.equals(duplicateFee), is(true));

        String anotherData = mResourceManager.getResourceContent("fee_another_item.json");
        JSONObject anotherJsonObject = new JSONObject(anotherData);
        Fee anotherFee = new Fee(anotherJsonObject);
        assertThat(fee.equals(anotherFee), is(false));
    }

}