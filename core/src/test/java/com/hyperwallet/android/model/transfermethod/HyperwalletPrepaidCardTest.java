package com.hyperwallet.android.model.transfermethod;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;
import com.hyperwallet.android.util.DateUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletPrepaidCardTest {
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testFromJsonString_prepaidCardResponse() throws Exception {
        HyperwalletPrepaidCard actualPrepaidCard = fromJsonString(
                mExternalResourceManager.getResourceContent("prepaid_card_response.json"),
                new TypeReference<HyperwalletPrepaidCard>() {
                });

        assertThat(actualPrepaidCard, is(notNullValue()));
        assertThat(actualPrepaidCard.getField(HyperwalletTransferMethod.TransferMethodFields.TOKEN),
                is("trm-17d10cf0-121d-45df-903c-589fd881a549"));
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
}