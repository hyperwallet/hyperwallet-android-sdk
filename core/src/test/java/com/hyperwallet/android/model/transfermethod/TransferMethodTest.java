package com.hyperwallet.android.model.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.PROFILE_TYPE;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.transfermethod.TransferMethod.TransferMethodFields.TYPE;

import android.os.Parcel;

import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.util.JsonUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class TransferMethodTest {
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();

    @Test
    public void testTransfer_isParcelable() throws Exception {

        String json = mExternalResourceManager.getResourceContent("bank_card_response.json");

        TransferMethod transferMethod = JsonUtils
                .fromJsonString(json, new TypeReference<TransferMethod>() {
                });

        assertThat(transferMethod, is(notNullValue()));
        assertThat(transferMethod.getField(TOKEN), is("trm-fake-token"));
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
        TransferMethod bundledTransferMethod = TransferMethod.CREATOR.createFromParcel(parcel);

        assertThat(bundledTransferMethod, is(notNullValue()));
        assertThat(bundledTransferMethod.getField(TOKEN), is("trm-fake-token"));
        assertThat(bundledTransferMethod.getField(TYPE), is("BANK_CARD"));
        assertThat(bundledTransferMethod.getField(STATUS), is("ACTIVATED"));

        assertThat(bundledTransferMethod.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(bundledTransferMethod.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(bundledTransferMethod.getField(STATUS), is("ACTIVATED"));
        assertThat(bundledTransferMethod.getField(CREATED_ON), is("2019-01-08T00:56:15"));
        assertThat(bundledTransferMethod.getField(PROFILE_TYPE), is("INDIVIDUAL"));

    }
}
