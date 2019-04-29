package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CARD_BRAND;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CARD_NUMBER;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CARD_TYPE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.CREATED_ON;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.DATE_OF_EXPIRY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.STATUS;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TOKEN;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodFields.TYPE;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.BANK_CARD;
import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletBankCardTest {

    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testFromJsonString_bankCardResponse() throws Exception {
        HyperwalletBankCard actualBankCard = fromJsonString(
                mExternalResourceManager.getResourceContent("bank_card_response.json"),
                new TypeReference<HyperwalletBankCard>() {
                });

        assertThat(actualBankCard, is(notNullValue()));
        assertThat(actualBankCard.getField(CARD_BRAND), is("VISA"));
        assertThat(actualBankCard.getField(CARD_NUMBER), is("************1114"));
        assertThat(actualBankCard.getField(CARD_TYPE), is("DEBIT"));
        assertThat(actualBankCard.getField(DATE_OF_EXPIRY), is("2019-11"));
        assertThat(actualBankCard.getField(TOKEN), is("trm-d8c65e1e-b3e5-460d-8b24-bee7cdae1636"));
        assertThat(actualBankCard.getField(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(actualBankCard.getField(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(actualBankCard.getField(TYPE), is(BANK_CARD));
        assertThat(actualBankCard.getField(CREATED_ON), is("2019-01-08T00:56:15"));
        assertThat(actualBankCard.getField(STATUS), is("ACTIVATED"));
    }

    @Test
    public void testToJsonObject_bankCard() throws Exception {

        final HyperwalletBankCard expectedBankCard = new HyperwalletBankCard.Builder()
                .cardBrand("VISA")
                .cardNumber("4216701111111114")
                .cardType("DEBIT")
                .dateOfExpiry("2019-11")
                .token("trm-d8c65e1e-b3e5-460d-8b24-bee7cdae1636")
                .transferMethodCountry("US")
                .transferMethodCurrency("USD")
                .build();

        JSONObject jsonObject = expectedBankCard.toJsonObject();

        assertThat(jsonObject.getString(CARD_BRAND), is("VISA"));
        assertThat(jsonObject.getString(CARD_NUMBER), is("4216701111111114"));
        assertThat(jsonObject.getString(CARD_TYPE), is("DEBIT"));
        assertThat(jsonObject.getString(DATE_OF_EXPIRY), is("2019-11"));
        assertThat(jsonObject.getString(TOKEN), is("trm-d8c65e1e-b3e5-460d-8b24-bee7cdae1636"));
        assertThat(jsonObject.getString(TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(jsonObject.getString(TRANSFER_METHOD_CURRENCY), is("USD"));
        assertThat(jsonObject.getString(TYPE), is(BANK_CARD));

    }
}


