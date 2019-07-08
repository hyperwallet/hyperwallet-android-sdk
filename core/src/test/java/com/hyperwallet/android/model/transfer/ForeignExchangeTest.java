package com.hyperwallet.android.model.transfer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import static com.hyperwallet.android.model.transfer.ForeignExchange.ForeignExchangeFields.DESTINATION_AMOUNT;
import static com.hyperwallet.android.model.transfer.ForeignExchange.ForeignExchangeFields.DESTINATION_CURRENCY;
import static com.hyperwallet.android.model.transfer.ForeignExchange.ForeignExchangeFields.RATE;
import static com.hyperwallet.android.model.transfer.ForeignExchange.ForeignExchangeFields.SOURCE_AMOUNT;
import static com.hyperwallet.android.model.transfer.ForeignExchange.ForeignExchangeFields.SOURCE_CURRENCY;
import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
public class ForeignExchangeTest {

    @Rule
    public final HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testFromJsonString_foreignExchangeResponse() throws Exception {
        ForeignExchange foreignExchange = fromJsonString(
                mExternalResourceManager.getResourceContent("foreign_exchange_item.json"),
                new TypeReference<ForeignExchange>() {
                });

        assertThat(foreignExchange, is(notNullValue()));
        assertThat(foreignExchange.getField(SOURCE_AMOUNT), is("100.00"));
        assertThat(foreignExchange.getField(SOURCE_CURRENCY), is("CAD"));
        assertThat(foreignExchange.getField(DESTINATION_AMOUNT), is("63.49"));
        assertThat(foreignExchange.getField(DESTINATION_CURRENCY), is("USD"));
        assertThat(foreignExchange.getField(RATE), is("0.79"));

        assertThat(foreignExchange.getSourceAmount(), is("100.00"));
        assertThat(foreignExchange.getSourceCurrency(), is("CAD"));
        assertThat(foreignExchange.getDestinationAmount(), is("63.49"));
        assertThat(foreignExchange.getDestinationCurrency(), is("USD"));
        assertThat(foreignExchange.getRate(), is("0.79"));
    }

    @Test
    public void testToJsonObject_foreignExchange() throws Exception {

        Map<String, Object> fieldsMap = new HashMap<>();
        fieldsMap.put(SOURCE_AMOUNT, "63.49");
        fieldsMap.put(SOURCE_CURRENCY, "USD");
        fieldsMap.put(DESTINATION_AMOUNT, "100.00");
        fieldsMap.put(DESTINATION_CURRENCY, "CAD");
        fieldsMap.put(RATE, "1.266");

        final ForeignExchange foreignExchange = new ForeignExchange(fieldsMap);

        JSONObject jsonObject = foreignExchange.toJsonObject();

        assertThat(jsonObject.getString(SOURCE_AMOUNT), is("63.49"));
        assertThat(jsonObject.getString(SOURCE_CURRENCY), is("USD"));
        assertThat(jsonObject.getString(DESTINATION_AMOUNT), is("100.00"));
        assertThat(jsonObject.getString(DESTINATION_CURRENCY), is("CAD"));
        assertThat(jsonObject.getString(RATE), is("1.266"));
    }

}