package com.hyperwallet.android.model.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.util.DateUtil;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PrepaidCardTest {
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();

    @Test
    public void testFromJsonString_prepaidCardResponse() throws Exception {
        PrepaidCard actualPrepaidCard = fromJsonString(
                mExternalResourceManager.getResourceContent("prepaid_card_response.json"),
                new TypeReference<PrepaidCard>() {
                });

        assertThat(actualPrepaidCard, is(notNullValue()));
        assertThat(actualPrepaidCard.getField(TransferMethod.TransferMethodFields.TOKEN),
                is("trm-fake-token"));
        assertThat(actualPrepaidCard.getType(), is("PREPAID_CARD"));
        assertThat(actualPrepaidCard.getStatus(), is("ACTIVATED"));
        assertThat(DateUtil.toDateTimeFormat(actualPrepaidCard.getCreatedOn()), is("2019-06-20T22:49:12"));
        assertThat(actualPrepaidCard.getTransferMethodCountry(), is("US"));
        assertThat(actualPrepaidCard.getTransferMethodCurrency(), is("USD"));
        assertThat(actualPrepaidCard.getCardType(), is("VIRTUAL"));
        assertThat(actualPrepaidCard.getCardPackage(), is("L1"));
        assertThat(actualPrepaidCard.getCardNumber(), is("************8766"));
        assertThat(actualPrepaidCard.getCardBrand(), is("VISA"));
        assertThat(actualPrepaidCard.getDateOfExpiry(), is("2023-06"));
    }

    @Test
    public void testFromJsonString_prepaidCardDefaultValue() throws Exception {
        PrepaidCard actualPrepaidCard = new PrepaidCard();

        assertThat(actualPrepaidCard, is(notNullValue()));
        assertThat(actualPrepaidCard.getCardPackage(), is("DEFAULT"));
        assertThat(actualPrepaidCard.getType(), is("PREPAID_CARD"));
    }

    @Test
    public void testFromJsonString_prepaidCardWithCardPackage() throws Exception {
        PrepaidCard actualPrepaidCard = new PrepaidCard("L1");

        assertThat(actualPrepaidCard, is(notNullValue()));
        assertThat(actualPrepaidCard.getCardPackage(), is("L1"));
        assertThat(actualPrepaidCard.getType(), is("PREPAID_CARD"));
    }

    @Test
    public void testFromJsonString_prepaidCardAllValuesByJsonObject() throws Exception {
        JSONObject params = new JSONObject();
        params.put(TransferMethod.TransferMethodFields.CARD_PACKAGE, "L1");
        params.put(TransferMethod.TransferMethodFields.DATE_OF_EXPIRY, "2023-06");
        PrepaidCard actualPrepaidCard = new PrepaidCard(params);

        assertThat(actualPrepaidCard, is(notNullValue()));
        assertThat(actualPrepaidCard.getCardPackage(), is("L1"));
        assertThat(actualPrepaidCard.getDateOfExpiry(), is("2023-06"));
    }
}