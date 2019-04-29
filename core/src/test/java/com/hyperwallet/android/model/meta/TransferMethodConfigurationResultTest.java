package com.hyperwallet.android.model.meta;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.BANK_ACCOUNT;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.BANK_CARD;
import static com.hyperwallet.android.model.HyperwalletTransferMethod.TransferMethodTypes.PAPER_CHECK;

import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class TransferMethodConfigurationResultTest {

    private static final String INDIVIDUAL = "INDIVIDUAL";

    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager =
            new HyperwalletExternalResourceManager();


    @Test
    public void testGetCountries_returnsAvailableCountries() throws Exception {

        JSONObject jsonObject = new JSONObject(
                mExternalResourceManager.getResourceContent("tmc_list_available_transfer_methods.json"));
        TransferMethodConfigurationResult transferMethodConfigurationResult = new TransferMethodConfigurationResult(
                jsonObject);

        assertThat(transferMethodConfigurationResult, is(notNullValue()));
        assertThat(transferMethodConfigurationResult.getCountries(), is(notNullValue()));
        assertThat(transferMethodConfigurationResult.getCountries().size(), is(2));
        String countryCa = transferMethodConfigurationResult.getCountries().get(0);
        assertThat(countryCa, is("CA"));
        String countryUs = transferMethodConfigurationResult.getCountries().get(1);
        assertThat(countryUs, is("US"));
    }

    @Test
    public void testGetCountries_noCountriesAvailable() throws Exception {

        JSONObject jsonObject = new JSONObject(mExternalResourceManager.getResourceContent(
                "tmc_no_transfer_methods_available.json"));
        TransferMethodConfigurationResult transferMethodConfigurationResult = new TransferMethodConfigurationResult(
                jsonObject);

        assertThat(transferMethodConfigurationResult, is(notNullValue()));
        assertThat(transferMethodConfigurationResult.getCountries(), is(notNullValue()));
        assertThat(transferMethodConfigurationResult.getCountries().size(), is(0));
    }

    @Test
    public void testGetCurrencies_returnsAvailableCurrencies() throws Exception {

        JSONObject jsonObject = new JSONObject(
                mExternalResourceManager.getResourceContent("tmc_configuration_connection_response.json"));
        TransferMethodConfigurationResult transferMethodConfigurationResult = new TransferMethodConfigurationResult(
                jsonObject);

        assertThat(transferMethodConfigurationResult.getCountries(), is(notNullValue()));
        String countryCode = transferMethodConfigurationResult.getCountries().get(0);
        assertThat(transferMethodConfigurationResult.getCurrencies(countryCode).size(), is(1));
        String currency = transferMethodConfigurationResult.getCurrencies(countryCode).get(0);
        assertThat(currency, is("CAD"));
    }

    @Test
    public void testGetCurrencies_noCurrenciesAvailable() throws Exception {

        JSONObject jsonObject = new JSONObject(
                mExternalResourceManager.getResourceContent("tmc_configuration_no_currency_response.json"));
        TransferMethodConfigurationResult transferMethodConfigurationResult = new TransferMethodConfigurationResult(
                jsonObject);

        assertThat(transferMethodConfigurationResult, is(notNullValue()));
        assertThat(transferMethodConfigurationResult.getCountries(), is(notNullValue()));
        assertThat(transferMethodConfigurationResult.getCountries().size(), is(1));
        assertThat(transferMethodConfigurationResult.getCountries().get(0), is("CA"));
        assertThat(transferMethodConfigurationResult.getCurrencies("CA"), is(notNullValue()));
        assertThat(transferMethodConfigurationResult.getCurrencies("CA").size(), is(0));
    }

    @Test
    public void testGetTransferMethods_returnsAvailableTransferMethods() throws Exception {

        JSONObject jsonObject = new JSONObject(
                mExternalResourceManager.getResourceContent("tmc_list_available_transfer_methods.json"));
        TransferMethodConfigurationResult transferMethodConfigurationResult = new TransferMethodConfigurationResult(
                jsonObject);

        assertThat(transferMethodConfigurationResult, is(notNullValue()));

        List<String> caCadTransferMethods = transferMethodConfigurationResult.getTransferMethods("CA", "CAD",
                INDIVIDUAL);
        assertThat(caCadTransferMethods.size(), is(1));
        assertThat(caCadTransferMethods.get(0), is(BANK_ACCOUNT));

        List<String> caUsdTransferMethods = transferMethodConfigurationResult.getTransferMethods("CA", "USD",
                INDIVIDUAL);
        assertThat(caUsdTransferMethods.size(), is(2));
        assertThat(caUsdTransferMethods.get(0), is(BANK_ACCOUNT));
        assertThat(caUsdTransferMethods.get(1), is(BANK_CARD));

        List<String> usUsdTransferMethods = transferMethodConfigurationResult.getTransferMethods("US", "USD",
                INDIVIDUAL);
        assertThat(usUsdTransferMethods.size(), is(2));
        assertThat(usUsdTransferMethods.get(0), is(BANK_ACCOUNT));
        assertThat(usUsdTransferMethods.get(1), is(BANK_CARD));

    }

    @Test
    public void testGetTransferMethods_noTransferMethodsAvailable() throws Exception {

        JSONObject jsonObject = new JSONObject(mExternalResourceManager.getResourceContent(
                "tmc_no_transfer_methods_available.json"));
        TransferMethodConfigurationResult transferMethodConfigurationResult = new TransferMethodConfigurationResult(
                jsonObject);

        assertThat(transferMethodConfigurationResult, is(notNullValue()));
        assertThat(transferMethodConfigurationResult.getTransferMethods("CA", "CAD", INDIVIDUAL), is(empty()));
        assertThat(transferMethodConfigurationResult.getTransferMethods("CA", "USD", INDIVIDUAL), is(empty()));
        assertThat(transferMethodConfigurationResult.getTransferMethods("US", "USD", INDIVIDUAL), is(empty()));

    }

    @Test
    public void testGetFields_returnsAvailableFields() throws Exception {

        JSONObject jsonObject = new JSONObject(
                mExternalResourceManager.getResourceContent("tmc_configuration_connection_response.json"));
        TransferMethodConfigurationResult transferMethodConfigurationResult = new TransferMethodConfigurationResult(
                jsonObject);

        assertThat(transferMethodConfigurationResult.getFields().size(), is(2));

        HyperwalletField field1 = transferMethodConfigurationResult.getFields().get(0);
        assertThat(field1.getCategory(), is("ACCOUNT"));
        assertThat(field1.getDataType(), is(EDataType.getDataType("SELECTION")));
        assertThat(field1.isRequired(), is(false));
        assertThat(field1.getLabel(), is("Shipping Method"));
        assertThat(field1.getMinLength(), is(0));
        assertThat(field1.getMaxLength(), is(Integer.MAX_VALUE));
        assertThat(field1.getName(), is("shippingMethod"));
        assertThat(field1.getPlaceholder(), isEmptyString());
        assertThat(field1.getRegularExpression(), isEmptyString());

        HyperwalletField field2 = transferMethodConfigurationResult.getFields().get(1);
        assertThat(field2.getCategory(), is("ADDRESS"));
        assertThat(field2.getDataType(), is(EDataType.getDataType("SELECTION")));
        assertThat(field2.isRequired(), is(true));
        assertThat(field2.getLabel(), is("Country"));
        assertThat(field2.getMinLength(), is(2));
        assertThat(field2.getMaxLength(), is(30));
        assertThat(field2.getName(), is("country"));
        assertThat(field2.getPlaceholder(), isEmptyString());
        assertThat(field2.getRegularExpression(), isEmptyString());
    }

    @Test
    public void testGetFields_noFieldsAvailable() throws Exception {

        JSONObject jsonObject = new JSONObject(mExternalResourceManager.getResourceContent(
                "tmc_no_transfer_methods_available.json"));
        TransferMethodConfigurationResult transferMethodConfigurationResult = new TransferMethodConfigurationResult(
                jsonObject);

        assertThat(transferMethodConfigurationResult, is(notNullValue()));
        assertThat(transferMethodConfigurationResult.getFields(), is(empty()));
    }

    @Test
    public void testGetFees_returnsAvailableFees() throws Exception {

        JSONObject jsonObject = new JSONObject(
                mExternalResourceManager.getResourceContent("tmc_configuration_connection_response.json"));
        TransferMethodConfigurationResult transferMethodConfigurationResult = new TransferMethodConfigurationResult(
                jsonObject);

        assertThat(transferMethodConfigurationResult.getFees("CA", "CAD", PAPER_CHECK, INDIVIDUAL), is(notNullValue()));
        List<Fee> fees = transferMethodConfigurationResult.getFees("CA", "CAD", PAPER_CHECK, INDIVIDUAL);
        assertThat(fees.size(), is(1));
        Fee fee = fees.get(0);
        assertThat(fee.getCountry(), is("CA"));
        assertThat(fee.getCurrency(), is("CAD"));
        assertThat(fee.getFeeRateType(), is("PERCENT"));
        assertThat(fee.getTransferMethodType(), is(PAPER_CHECK));
        assertThat(fee.getValue(), is("4.9"));
        assertThat(fee.getMinimum(), is("10"));
        assertThat(fee.getMaximum(), is("20"));

    }

    @Test
    public void testGetFees_noFeesAvailable() throws Exception {
        JSONObject jsonObject = new JSONObject(
                mExternalResourceManager.getResourceContent("tmc_list_available_transfer_methods.json"));
        TransferMethodConfigurationResult transferMethodConfigurationResult = new TransferMethodConfigurationResult(
                jsonObject);

        assertThat(transferMethodConfigurationResult, is(notNullValue()));

        assertThat(transferMethodConfigurationResult.getCountries(), not(empty()));
        String countryCode = transferMethodConfigurationResult.getCountries().get(0);
        assertThat(transferMethodConfigurationResult.getCurrencies(countryCode), not(empty()));
        String currencyCode = transferMethodConfigurationResult.getCurrencies(countryCode).get(0);
        assertThat(transferMethodConfigurationResult.getFees(countryCode, currencyCode, PAPER_CHECK, INDIVIDUAL),
                is(empty()));
    }

    @Test
    public void testGetProcessingTime_returnsAvailableProcessingTime() throws Exception {

        JSONObject jsonObject = new JSONObject(
                mExternalResourceManager.getResourceContent("tmc_configuration_connection_response.json"));
        TransferMethodConfigurationResult transferMethodConfigurationResult = new TransferMethodConfigurationResult(
                jsonObject);

        assertThat(transferMethodConfigurationResult.getCountries(), not(empty()));
        String countryCode = transferMethodConfigurationResult.getCountries().get(0);
        assertThat(transferMethodConfigurationResult.getCurrencies(countryCode), not(empty()));
        String currencyCode = transferMethodConfigurationResult.getCurrencies(countryCode).get(0);
        String processingTime = transferMethodConfigurationResult.getProcessingTime(countryCode, currencyCode,
                PAPER_CHECK, INDIVIDUAL);
        assertThat(processingTime, is("1-3 Business days"));
    }

    @Test
    public void testGetProcessingTime_noProcessingTimeAvailable() throws Exception {
        JSONObject jsonObject = new JSONObject(
                mExternalResourceManager.getResourceContent("tmc_list_available_transfer_methods.json"));
        TransferMethodConfigurationResult transferMethodConfigurationResult = new TransferMethodConfigurationResult(
                jsonObject);

        assertThat(transferMethodConfigurationResult, is(notNullValue()));

        assertThat(transferMethodConfigurationResult.getCountries(), not(empty()));
        String countryCode = transferMethodConfigurationResult.getCountries().get(0);
        assertThat(transferMethodConfigurationResult.getCurrencies(countryCode), not(empty()));
        String currencyCode = transferMethodConfigurationResult.getCurrencies(countryCode).get(0);
        assertThat(
                transferMethodConfigurationResult.getProcessingTime(countryCode, currencyCode, PAPER_CHECK, INDIVIDUAL),
                is(isEmptyOrNullString()));
    }


}
