package com.hyperwallet.android.model.meta;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.PAPER_CHECK;

import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class FeeTest {

    @Rule
    public final HyperwalletExternalResourceManager mResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testFee_convertJsonObjectWithFlatFee() throws JSONException {
        String data = mResourceManager.getResourceContent("fee_flat_item.json");
        JSONObject jsonObject = new JSONObject(data);
        Fee flatFee = new Fee(jsonObject);
        assertThat(flatFee.getCountry(), is("US"));
        assertThat(flatFee.getCurrency(), is("USD"));
        assertThat(flatFee.getFeeRateType(), is(Fee.FeeRate.FLAT));
        assertThat(flatFee.getMaximum(), is(""));
        assertThat(flatFee.getMinimum(), is(""));
        assertThat(flatFee.getTransferMethodType(), is(BANK_ACCOUNT));
        assertThat(flatFee.getValue(), is("500.00"));
    }

    @Test
    public void testFee_convertJsonObjectWithPercentFee() throws JSONException {
        String data = mResourceManager.getResourceContent("fee_percent_item.json");
        JSONObject jsonObject = new JSONObject(data);
        Fee flatFee = new Fee(jsonObject);
        assertThat(flatFee.getCountry(), is("CA"));
        assertThat(flatFee.getCurrency(), is("CAD"));
        assertThat(flatFee.getFeeRateType(), is(Fee.FeeRate.PERCENT));
        assertThat(flatFee.getMaximum(), is("20"));
        assertThat(flatFee.getMinimum(), is("10"));
        assertThat(flatFee.getTransferMethodType(), is(PAPER_CHECK));
        assertThat(flatFee.getValue(), is("14.9"));
    }
}