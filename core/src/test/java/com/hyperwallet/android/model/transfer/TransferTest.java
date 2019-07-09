package com.hyperwallet.android.model.transfer;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import static com.hyperwallet.android.model.transfer.ForeignExchange.ForeignExchangeFields.DESTINATION_AMOUNT;
import static com.hyperwallet.android.model.transfer.ForeignExchange.ForeignExchangeFields.DESTINATION_CURRENCY;
import static com.hyperwallet.android.model.transfer.ForeignExchange.ForeignExchangeFields.RATE;
import static com.hyperwallet.android.model.transfer.ForeignExchange.ForeignExchangeFields.SOURCE_CURRENCY;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.CLIENT_TRANSFER_ID;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.CREATED_ON;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.DESTINATION_TOKEN;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.EXPIRES_ON;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.FOREIGN_EXCHANGES;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.MEMO;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.NOTES;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.SOURCE_AMOUNT;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.SOURCE_TOKEN;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.STATUS;
import static com.hyperwallet.android.model.transfer.Transfer.TransferFields.TOKEN;
import static com.hyperwallet.android.model.transfer.Transfer.TransferStatuses.QUOTED;
import static com.hyperwallet.android.util.DateUtil.fromDateTimeString;
import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import android.os.Parcel;

import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
public class TransferTest {
    @Rule
    public final HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testFromJsonString_transferResponse() throws Exception {
        Transfer transfer = fromJsonString(
                mExternalResourceManager.getResourceContent("transfer_response.json"),
                new TypeReference<Transfer>() {
                });

        assertThat(transfer, is(notNullValue()));
        assertThat(transfer.getField(TOKEN), is("trf-123"));
        assertThat(transfer.getField(STATUS), is(QUOTED));
        assertThat(transfer.getField(CREATED_ON), is("2019-07-01T00:00:00"));
        assertThat(transfer.getField(CLIENT_TRANSFER_ID), is("1234567890123"));
        assertThat(transfer.getField(SOURCE_AMOUNT), is("80"));
        assertThat(transfer.getField(SOURCE_CURRENCY), is("CAD"));
        assertThat(transfer.getField(SOURCE_TOKEN), is("usr-4321"));
        assertThat(transfer.getField(DESTINATION_AMOUNT), is("62.29"));
        assertThat(transfer.getField(DESTINATION_CURRENCY), is("USD"));
        assertThat(transfer.getField(DESTINATION_TOKEN), is("trm-246"));
        assertThat(transfer.getField(NOTES), is("Partial-Balance Transfer"));
        assertThat(transfer.getField(MEMO), is("TransferClientId321"));
        assertThat(transfer.getField(EXPIRES_ON), is("2019-07-01T00:02:00"));
        assertThat(transfer.getField("incorrectField"), is(nullValue()));


        assertThat(transfer.getToken(), is("trf-123"));
        assertThat(transfer.getStatus(), is(QUOTED));
        assertThat(transfer.getCreatedOn(), is(fromDateTimeString("2019-07-01T00:00:00")));
        assertThat(transfer.getClientTransferId(), is("1234567890123"));
        assertThat(transfer.getSourceAmount(), is("80"));
        assertThat(transfer.getSourceCurrency(), is("CAD"));
        assertThat(transfer.getSourceToken(), is("usr-4321"));
        assertThat(transfer.getDestinationAmount(), is("62.29"));
        assertThat(transfer.getDestinationCurrency(), is("USD"));
        assertThat(transfer.getDestinationToken(), is("trm-246"));
        assertThat(transfer.getNotes(), is("Partial-Balance Transfer"));
        assertThat(transfer.getMemo(), is("TransferClientId321"));
        assertThat(transfer.getExpiresOn(), is(fromDateTimeString("2019-07-01T00:02:00")));

        List<ForeignExchange> foreignExchanges = transfer.getForeignExchanges();
        assertThat(foreignExchanges, hasSize(1));
        ForeignExchange foreignExchange = foreignExchanges.get(0);
        assertThat(foreignExchange.getSourceAmount(), is("100.00"));
        assertThat(foreignExchange.getSourceCurrency(), is("CAD"));
        assertThat(foreignExchange.getDestinationAmount(), is("63.49"));
        assertThat(foreignExchange.getDestinationCurrency(), is("USD"));
        assertThat(foreignExchange.getRate(), is("0.79"));
    }

    @Test
    public void testToJsonObject_transfer() throws Exception {
        Map<String, Object> fieldsMap = new HashMap<>();
        fieldsMap.put(ForeignExchange.ForeignExchangeFields.SOURCE_AMOUNT, "63.49");
        fieldsMap.put(SOURCE_CURRENCY, "USD");
        fieldsMap.put(DESTINATION_AMOUNT, "100.00");
        fieldsMap.put(DESTINATION_CURRENCY, "CAD");
        fieldsMap.put(RATE, "1.266");

        List<ForeignExchange> foreignExchanges = new ArrayList<>();
        foreignExchanges.add(new ForeignExchange(fieldsMap));

        final Transfer.Builder builder = new Transfer.Builder()
                .token("trf-123")
                .createdOn(fromDateTimeString("2019-07-01T00:00:00"))
                .clientTransferID("1234567890123")
                .sourceAmount("80")
                .sourceCurrency("CAD")
                .sourceToken("usr-4321")
                .destinationAmount("62.29")
                .destinationCurrency("USD")
                .destinationToken("trm-246")
                .foreignExchanges(foreignExchanges)
                .expiresOn(fromDateTimeString("2019-07-01T00:02:00"))
                .memo("Transfer")
                .notes("Partial-Balance Transfer");

        final Transfer transfer = builder.build();

        JSONObject jsonObject = transfer.toJsonObject();

        assertThat(jsonObject.getString(TOKEN), is("trf-123"));
        assertThat(jsonObject.getString(CREATED_ON), is("2019-07-01T00:00:00"));
        assertThat(jsonObject.getString(CLIENT_TRANSFER_ID), is("1234567890123"));
        assertThat(jsonObject.getString(SOURCE_AMOUNT), is("80"));
        assertThat(jsonObject.getString(SOURCE_CURRENCY), is("CAD"));
        assertThat(jsonObject.getString(SOURCE_TOKEN), is("usr-4321"));
        assertThat(jsonObject.getString(DESTINATION_AMOUNT), is("62.29"));
        assertThat(jsonObject.getString(DESTINATION_CURRENCY), is("USD"));
        assertThat(jsonObject.getString(DESTINATION_TOKEN), is("trm-246"));
        assertThat(jsonObject.getString(EXPIRES_ON), is("2019-07-01T00:02:00"));
        assertThat(jsonObject.getString(MEMO), is("Transfer"));
        assertThat(jsonObject.getString(NOTES), is("Partial-Balance Transfer"));

        final JSONArray foreignExchangesJson = jsonObject.getJSONArray(FOREIGN_EXCHANGES);
        assertThat(foreignExchanges, hasSize(1));
        final JSONObject foreignExchangeJson = foreignExchangesJson.getJSONObject(0);
        assertThat(foreignExchangeJson.getString(ForeignExchange.ForeignExchangeFields.SOURCE_AMOUNT), is("63.49"));
        assertThat(foreignExchangeJson.getString(ForeignExchange.ForeignExchangeFields.SOURCE_CURRENCY), is("USD"));
        assertThat(foreignExchangeJson.getString(ForeignExchange.ForeignExchangeFields.DESTINATION_AMOUNT),
                is("100.00"));
        assertThat(foreignExchangeJson.getString(ForeignExchange.ForeignExchangeFields.DESTINATION_CURRENCY),
                is("CAD"));
        assertThat(foreignExchangeJson.getString(ForeignExchange.ForeignExchangeFields.RATE), is("1.266"));
    }

    @Test
    public void testToJsonObject_emptyTransfer() throws Exception {
        final Transfer.Builder emptyBuilder = new Transfer.Builder();
        final Transfer emptyTransfer = emptyBuilder.build();
        JSONObject emptyTransferJsonObject = emptyTransfer.toJsonObject();
        assertThat(emptyTransferJsonObject.has(STATUS), is(false));
        assertThat(emptyTransferJsonObject.has(TOKEN), is(false));
        assertThat(emptyTransferJsonObject.has(CREATED_ON), is(false));
        assertThat(emptyTransferJsonObject.has(CLIENT_TRANSFER_ID), is(false));
        assertThat(emptyTransferJsonObject.has(SOURCE_AMOUNT), is(false));
        assertThat(emptyTransferJsonObject.has(SOURCE_CURRENCY), is(false));
        assertThat(emptyTransferJsonObject.has(DESTINATION_AMOUNT), is(false));
        assertThat(emptyTransferJsonObject.has(DESTINATION_CURRENCY), is(false));
        assertThat(emptyTransferJsonObject.has(DESTINATION_TOKEN), is(false));
        assertThat(emptyTransferJsonObject.has(NOTES), is(false));
        assertThat(emptyTransferJsonObject.has(MEMO), is(false));
        assertThat(emptyTransferJsonObject.has(EXPIRES_ON), is(false));
        assertThat(emptyTransferJsonObject.has(FOREIGN_EXCHANGES), is(false));
    }

    @Test
    public void testTransfer_isParcelable() throws Exception {
        Transfer transfer = fromJsonString(
                mExternalResourceManager.getResourceContent("transfer_response.json"),
                new TypeReference<Transfer>() {
                });

        assertThat(transfer.getToken(), is("trf-123"));
        assertThat(transfer.getStatus(), is(QUOTED));
        assertThat(transfer.getCreatedOn(), is(fromDateTimeString("2019-07-01T00:00:00")));
        assertThat(transfer.getClientTransferId(), is("1234567890123"));
        assertThat(transfer.getSourceAmount(), is("80"));
        assertThat(transfer.getSourceCurrency(), is("CAD"));
        assertThat(transfer.getSourceToken(), is("usr-4321"));
        assertThat(transfer.getDestinationAmount(), is("62.29"));
        assertThat(transfer.getDestinationCurrency(), is("USD"));
        assertThat(transfer.getDestinationToken(), is("trm-246"));
        assertThat(transfer.getNotes(), is("Partial-Balance Transfer"));
        assertThat(transfer.getMemo(), is("TransferClientId321"));
        assertThat(transfer.getExpiresOn(), is(fromDateTimeString("2019-07-01T00:02:00")));

        Parcel parcel = Parcel.obtain();
        transfer.writeToParcel(parcel, transfer.describeContents());
        parcel.setDataPosition(0);
        Transfer bundledTransfer =
                Transfer.CREATOR.createFromParcel(parcel);

        assertThat(bundledTransfer.getToken(), is("trf-123"));
        assertThat(bundledTransfer.getStatus(), is(QUOTED));
        assertThat(bundledTransfer.getCreatedOn(), is(fromDateTimeString("2019-07-01T00:00:00")));
        assertThat(bundledTransfer.getClientTransferId(), is("1234567890123"));
        assertThat(bundledTransfer.getSourceAmount(), is("80"));
        assertThat(bundledTransfer.getSourceCurrency(), is("CAD"));
        assertThat(bundledTransfer.getSourceToken(), is("usr-4321"));
        assertThat(bundledTransfer.getDestinationAmount(), is("62.29"));
        assertThat(bundledTransfer.getDestinationCurrency(), is("USD"));
        assertThat(bundledTransfer.getDestinationToken(), is("trm-246"));
        assertThat(bundledTransfer.getNotes(), is("Partial-Balance Transfer"));
        assertThat(bundledTransfer.getMemo(), is("TransferClientId321"));
        assertThat(bundledTransfer.getExpiresOn(), is(fromDateTimeString("2019-07-01T00:02:00")));

    }
}