package com.hyperwallet.android.model.meta.keyed;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.model.meta.HyperwalletFee;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletTransferMethodTypeTest {

    @Rule
    public HyperwalletExternalResourceManager mResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public final ExpectedException mThrown = ExpectedException.none();

    @Test
    public void testHyperwalletTransferMethodType_convertJsonObjectWithBankAccount()
            throws JSONException, HyperwalletException {
        String data = mResourceManager.getResourceContent("tm_type_item.json");
        JSONObject jsonObject = new JSONObject(data);
        HyperwalletTransferMethodType transferMethodType = new HyperwalletTransferMethodType(jsonObject);
        assertThat(transferMethodType.getCode(), is("BANK_ACCOUNT"));
        assertThat(transferMethodType.getName(), is("Bank Account"));
        assertThat(transferMethodType.getProcessingTime(), is("1-3"));
        assertThat(transferMethodType.getFees(), hasSize(1));
        final Set<HyperwalletFee> fees = transferMethodType.getFees();
        List<HyperwalletFee> feeList = new ArrayList<>(fees);
        assertThat(feeList.get(0).getValue(), is("5.00"));
        assertThat(feeList.get(0).getFeeRateType(), is("FLAT"));
    }

    @Test
    public void testHyperwalletTransferMethodType_convertJsonObjectWithError()
            throws JSONException, HyperwalletException {
        mThrown.expect(HyperwalletException.class);

        String data = mResourceManager.getResourceContent("tm_type_item.json");
        JSONObject jsonObject = new JSONObject(data);
        jsonObject.remove("code");
        new HyperwalletTransferMethodType(jsonObject);
    }


    @Test
    public void testHyperwalletTransferMethodType_convertJsonObjectWithoutConnection()
            throws JSONException, HyperwalletException {
        String data = mResourceManager.getResourceContent("tm_type_without_fees_item.json");
        JSONObject jsonObject = new JSONObject(data);
        HyperwalletTransferMethodType transferMethodType = new HyperwalletTransferMethodType(jsonObject);
        assertThat(transferMethodType.getCode(), is("BANK_ACCOUNT"));
        assertThat(transferMethodType.getName(), is("Bank Account"));
        assertThat(transferMethodType.getProcessingTime(), is("1-3"));
        assertThat(transferMethodType.getFees(), hasSize(0));
    }


    @Test
    public void testHyperwalletTransferMethodType_equalsObjects()
            throws JSONException, HyperwalletException {
        String data = mResourceManager.getResourceContent("tm_type_item.json");
        JSONObject jsonObject = new JSONObject(data);
        HyperwalletTransferMethodType transferMethodType = new HyperwalletTransferMethodType(jsonObject);
        assertThat(transferMethodType.equals("another object"), is(false));

        HyperwalletTransferMethodType duplicateTransferMethodType = transferMethodType;

        assertThat(transferMethodType.equals(duplicateTransferMethodType), is(true));

        jsonObject.put("code", "BANK_CARD");
        HyperwalletTransferMethodType anotherCodeTransferMethodType = new HyperwalletTransferMethodType(jsonObject);
        assertThat(transferMethodType.equals(anotherCodeTransferMethodType), is(false));

        jsonObject.put("code", "BANK_ACCOUNT");
        jsonObject.put("name", "Bank Card");
        HyperwalletTransferMethodType anotherNameTransferMethodType = new HyperwalletTransferMethodType(jsonObject);
        assertThat(transferMethodType.equals(anotherNameTransferMethodType), is(false));

        jsonObject.put("name", "Bank Account");
        jsonObject.put("processingTime", "2-5");
        HyperwalletTransferMethodType anotherTimeTransferMethodType = new HyperwalletTransferMethodType(jsonObject);
        assertThat(transferMethodType.equals(anotherTimeTransferMethodType), is(false));

    }

}