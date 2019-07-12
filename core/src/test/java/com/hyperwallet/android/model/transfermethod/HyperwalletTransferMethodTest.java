package com.hyperwallet.android.model.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.PROFILE_TYPE;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.HyperwalletTransferMethod.TransferMethodFields.TYPE;

import android.os.Parcel;

import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;
import com.hyperwallet.android.util.JsonUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletTransferMethodTest {
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testHyperwalletTransfer_isParcelable() throws Exception {

        String json = mExternalResourceManager.getResourceContent("bank_card_response.json");

        HyperwalletTransferMethod transferMethod = JsonUtils
                .fromJsonString(json, new TypeReference<HyperwalletTransferMethod>() {
                });

        assertThat(transferMethod, is(notNullValue()));
        assertThat(transferMethod.getField(TOKEN), is("trm-d8c65e1e-b3e5-460d-8b24-bee7cdae1636"));
        assertThat(transferMethod.getField(TYPE), is("BANK_CARD"));
        assertThat(transferMethod.getField(STATUS), is("ACTIVATED"));

        assertThat(transferMethod.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(transferMethod.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(transferMethod.getField(STATUS), is("ACTIVATED"));
        assertThat(transferMethod.getField(CREATED_ON), is("2019-01-08T00:56:15"));
        assertThat(transferMethod.getField(PROFILE_TYPE), is("INDIVIDUAL"));

        Parcel parcel = Parcel.obtain();
        transferMethod.writeToParcel(parcel, transferMethod.describeContents());
        parcel.setDataPosition(0);
        HyperwalletTransferMethod bundledTransferMethod = HyperwalletTransferMethod.CREATOR.createFromParcel(parcel);

        assertThat(bundledTransferMethod, is(notNullValue()));
        assertThat(bundledTransferMethod.getField(TOKEN), is("trm-d8c65e1e-b3e5-460d-8b24-bee7cdae1636"));
        assertThat(bundledTransferMethod.getField(TYPE), is("BANK_CARD"));
        assertThat(bundledTransferMethod.getField(STATUS), is("ACTIVATED"));

        assertThat(bundledTransferMethod.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(bundledTransferMethod.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(bundledTransferMethod.getField(STATUS), is("ACTIVATED"));
        assertThat(bundledTransferMethod.getField(CREATED_ON), is("2019-01-08T00:56:15"));
        assertThat(bundledTransferMethod.getField(PROFILE_TYPE), is("INDIVIDUAL"));

    }
}
