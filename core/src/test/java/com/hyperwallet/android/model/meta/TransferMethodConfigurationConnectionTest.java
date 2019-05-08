package com.hyperwallet.android.model.meta;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNotNull;

import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.BANK_CARD;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.WIRE_ACCOUNT;

import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.meta.field.EDataType;
import com.hyperwallet.android.model.meta.field.HyperwalletField;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;
import com.hyperwallet.android.util.JsonUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class TransferMethodConfigurationConnectionTest {
    @Rule
    public final HyperwalletExternalResourceManager hyperwalletResourceManager =
            new HyperwalletExternalResourceManager();
    private String mTransferMethodConfigurationResponse;
    private String mMultiCountryCurrencyConfigurationResponse;

    @Before
    public void setUp() {
        mTransferMethodConfigurationResponse = hyperwalletResourceManager.getResourceContent(
                "tmc_configuration_connection_response.json");
        mMultiCountryCurrencyConfigurationResponse = hyperwalletResourceManager.getResourceContent(
                "tmc_multi_currency_country_configuration.json");
    }

    @Test
    public void testFromJsonString_parseHardClass() throws Exception {
        TransferMethodConfigurationResult result = JsonUtils.fromJsonString(mTransferMethodConfigurationResponse,
                new TypeReference<TransferMethodConfigurationResult>() {
                });

        assertThat(result, is(notNullValue()));
        assertThat(result.getFields(), hasSize(2));

        // asserts connection
        assertThat(result.getCountries(), is(not(empty())));

        // assert HyperwalletConnection<HyperwalletTransferMethodConfiguration>
        List<TransferMethodConfiguration> data = result.getData().getRootType().getNodes();
        assertThat(data, is(not(empty())));
        assertThat(data.get(0).getCountries(), contains("CA"));
        assertThat(data.get(0).getCurrencies(), contains("CAD"));
        assertThat(data.get(0).getProfile(), is("INDIVIDUAL"));
        assertThat(data.get(0).getTransferMethodType(), is("PAPER_CHECK"));
        assertThat(data.get(0).getFields().isEmpty(), is(false));

        // assert Transfer Method Fields
        List<HyperwalletField> hyperwalletFields = result.getFields();
        assertThat(hyperwalletFields.size(), is(2));
        assertThat(hyperwalletFields.get(0).getCategory(), is("ACCOUNT"));
        assertThat(hyperwalletFields.get(0).getDataType(), is(EDataType.SELECTION));
        assertThat(hyperwalletFields.get(0).isRequired(), is(false));
        assertThat(hyperwalletFields.get(0).getLabel(), is("Shipping Method"));
        assertThat(hyperwalletFields.get(0).getMaxLength(), is(Integer.MAX_VALUE));
        assertThat(hyperwalletFields.get(0).getMinLength(), is(0));
        assertThat(hyperwalletFields.get(0).getName(), is("shippingMethod"));
        assertThat(hyperwalletFields.get(0).getPlaceholder(), is(""));
        assertThat(hyperwalletFields.get(0).getRegularExpression(), isEmptyString());

        assertThat(hyperwalletFields.get(1).getCategory(), is("ADDRESS"));
        assertThat(hyperwalletFields.get(1).getDataType(), is(EDataType.SELECTION));
        assertThat(hyperwalletFields.get(1).isRequired(), is(true));
        assertThat(hyperwalletFields.get(1).getLabel(), is("Country"));
        assertThat(hyperwalletFields.get(1).getMaxLength(), is(30));
        assertThat(hyperwalletFields.get(1).getMinLength(), is(2));
        assertThat(hyperwalletFields.get(1).getName(), is("country"));
        assertThat(hyperwalletFields.get(1).getPlaceholder(), is(""));
        assertThat(hyperwalletFields.get(1).getRegularExpression(), isEmptyString());

        // asserting fee info and processing time
        assertThat(result.getProcessingTime("CA", "CAD", "PAPER_CHECK", "INDIVIDUAL"),
                is("1-3 Business days"));
        List<Fee> fees = result.getFees("CA", "CAD", "PAPER_CHECK", "INDIVIDUAL");
        assertNotNull(fees);
        assertThat(fees.size(), is(1));
        Fee fee = fees.get(0);
        assertThat(fee.getTransferMethodType(), is("PAPER_CHECK"));
        assertThat(fee.getCountry(), is("CA"));
        assertThat(fee.getCurrency(), is("CAD"));
        assertThat(fee.getFeeRateType(), is("PERCENT"));
        assertThat(fee.getValue(), is("4.9"));
        assertThat(fee.getMaximum(), is("20"));
        assertThat(fee.getMinimum(), is("10"));
    }

    @Test
    public void testFromJsonString_parseGenericRepresentation() throws Exception {
        TypeReference<GqlResponse<TransferMethodConfigurationData>> typeReference =
                new TypeReference<GqlResponse<TransferMethodConfigurationData>>() {
                };
        GqlResponse<TransferMethodConfigurationData> response = JsonUtils.fromJsonString(
                mTransferMethodConfigurationResponse, typeReference);
        assertThat(response, is(notNullValue()));
        assertThat(response.getData().getRootType().getCount(), is(1L));
        assertThat(response.getData().getRootType().getNodes(), hasSize(1));
        assertThat(response.getData().getRootType().getNodes().get(0).getFields(), hasSize(2));
    }

    @Test
    public void testFromJsonString_parseMultiCurrencyCountryConfiguration() throws Exception {
        TransferMethodConfigurationResult result = JsonUtils.fromJsonString(mMultiCountryCurrencyConfigurationResponse,
                new TypeReference<TransferMethodConfigurationResult>() {
                });

        assertThat(result, is(notNullValue()));
        assertThat(result.getCountries(), hasSize(71));
        assertThat(result.getCurrencies("SG"), hasItems("USD"));
        assertThat(result.getCurrencies("AE"), hasItems("USD", "AED"));
        assertThat(result.getCurrencies("JP"), hasItems("USD", "JPY"));
        assertThat(result.getCurrencies("JO"), hasItems("JOD"));
        assertThat(result.getCurrencies("TH"), hasItems("USD"));
        assertThat(result.getCurrencies("CA"), hasItems("CAD", "USD"));
        assertThat(result.getTransferMethods("US", "USD", "INDIVIDUAL"),
                hasItems(BANK_ACCOUNT, BANK_CARD));
        assertThat(result.getTransferMethods("JP", "JPY", "INDIVIDUAL"),
                hasItems(WIRE_ACCOUNT));
        assertThat(result.getTransferMethods("US", "AUD", "INDIVIDUAL"),
                hasItems(WIRE_ACCOUNT));
    }
}

