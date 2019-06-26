package com.hyperwallet.android.model.graphql.keyed;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static com.hyperwallet.android.model.graphql.HyperwalletFee.FeeRate.FLAT;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodTypes.BANK_CARD;

import com.hyperwallet.android.model.graphql.HyperwalletFee;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

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
    public final HyperwalletExternalResourceManager mResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public final ExpectedException mThrown = ExpectedException.none();

    @Test
    public void testHyperwalletTransferMethodType_convertJsonObject() throws Exception {
        String data = mResourceManager.getResourceContent("tm_type_item.json");
        JSONObject jsonObject = new JSONObject(data);
        HyperwalletTransferMethodType transferMethodType = new HyperwalletTransferMethodType(jsonObject);
        assertThat(transferMethodType.getCode(), is(BANK_ACCOUNT));
        assertThat(transferMethodType.getName(), is("Bank Account"));
        assertThat(transferMethodType.getProcessingTime().getValue(), is("1-3 Business days"));
        assertThat(transferMethodType.getProcessingTime().getCountry(), is("US"));
        assertThat(transferMethodType.getProcessingTime().getCurrency(), is("USD"));
        assertThat(transferMethodType.getFees(), hasSize(1));
        final Set<HyperwalletFee> fees = transferMethodType.getFees();
        List<HyperwalletFee> feeList = new ArrayList<>(fees);
        assertThat(feeList.get(0).getValue(), is("5.00"));
        assertThat(feeList.get(0).getFeeRateType(), is(FLAT));
    }

    @Test
    public void testHyperwalletTransferMethodType_convertJsonObjectWithoutConnection() throws Exception {
        String data = mResourceManager.getResourceContent("tm_type_without_fees_item.json");
        JSONObject jsonObject = new JSONObject(data);
        HyperwalletTransferMethodType transferMethodType = new HyperwalletTransferMethodType(jsonObject);
        assertThat(transferMethodType.getCode(), is(BANK_ACCOUNT));
        assertThat(transferMethodType.getName(), is("Bank Account"));
        assertThat(transferMethodType.getProcessingTime().getValue(), is("1-3 Business days"));
        assertThat(transferMethodType.getFees(), is(empty()));
    }


    @Test
    public void testHyperwalletTransferMethodType_equalsObjects() throws Exception {
        String data = mResourceManager.getResourceContent("tm_type_item.json");
        JSONObject jsonObject = new JSONObject(data);
        HyperwalletTransferMethodType transferMethodType = new HyperwalletTransferMethodType(jsonObject);
        assertThat(transferMethodType.equals("another object"), is(false));

        HyperwalletTransferMethodType duplicateTransferMethodType = transferMethodType;

        assertThat(transferMethodType.equals(duplicateTransferMethodType), is(true));

        jsonObject.put("code", BANK_CARD);
        HyperwalletTransferMethodType anotherCodeTransferMethodType = new HyperwalletTransferMethodType(jsonObject);
        assertThat(transferMethodType.equals(anotherCodeTransferMethodType), is(false));

        jsonObject.put("code", BANK_ACCOUNT);
        jsonObject.put("name", "Bank Card");
        HyperwalletTransferMethodType anotherNameTransferMethodType = new HyperwalletTransferMethodType(jsonObject);
        assertThat(transferMethodType.equals(anotherNameTransferMethodType), is(false));

        jsonObject.put("name", "Bank Account");
        final JSONObject processingTimeJsonObject = new JSONObject("{\n"
                + "  \"transferMethodType\": \"BANK_ACCOUNT\",\n"
                + "  \"country\": \"CA\",\n"
                + "  \"currency\": \"USD\",\n"
                + "  \"value\": \"2-5 Business days\"\n"
                + "}");

        jsonObject.put("processingTimes", processingTimeJsonObject);

        HyperwalletTransferMethodType anotherTimeTransferMethodType = new HyperwalletTransferMethodType(jsonObject);
        assertThat(transferMethodType.equals(anotherTimeTransferMethodType), is(false));
    }
}