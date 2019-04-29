package com.hyperwallet.android.model.meta;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class TransferMethodConfigurationTest {
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();
    @Rule
    public MockitoRule mMockito = MockitoJUnit.rule();

    @Test
    public void testTransferMethodConfiguration_convertJsonObject() throws JSONException, HyperwalletException {
        JSONObject jsonObject = new JSONObject(mExternalResourceManager.getResourceContent(
                "transfer_method_configuration_item.json"));

        TransferMethodConfiguration transferMethodConfiguration = new TransferMethodConfiguration(jsonObject);

        assertThat(transferMethodConfiguration.getCountries(), is(notNullValue()));
        assertThat(transferMethodConfiguration.getCountries().size(), is(1));
        assertThat(transferMethodConfiguration.getCountries().get(0), is("US"));

        assertThat(transferMethodConfiguration.getCurrencies(), is(notNullValue()));
        assertThat(transferMethodConfiguration.getCurrencies().size(), is(1));
        assertThat(transferMethodConfiguration.getCurrencies().get(0), is("USD"));

        assertThat(transferMethodConfiguration.getFeeConnection(), is(notNullValue()));
        assertThat(transferMethodConfiguration.getFeeConnection().getCount(), is(0L));
        List<Fee> nodes = transferMethodConfiguration.getFeeConnection().getNodes();
        assertThat(nodes, is(notNullValue()));
        assertThat(nodes.size(), is(1));
        Fee fee = nodes.get(0);
        assertThat(fee.getCountry(), is("CA"));
        assertThat(fee.getCurrency(), is("CAD"));
        assertThat(fee.getFeeRateType(), is("PERCENT"));
        assertThat(fee.getMinimum(), is("10"));
        assertThat(fee.getMaximum(), is("20"));
        assertThat(fee.getTransferMethodType(), is("PAPER_CHECK"));
        assertThat(fee.getValue(), is("4.9"));

        assertThat(transferMethodConfiguration.getProcessingTime(), is("3 days"));
        assertThat(transferMethodConfiguration.getProfile(), is("INDIVIDUAL"));
        assertThat(transferMethodConfiguration.getTransferMethodType(), is("BANK_ACCOUNT"));

        assertThat(transferMethodConfiguration.getFields(), is(notNullValue()));
        assertThat(transferMethodConfiguration.getFields().size(), is(2));
        HyperwalletField hyperwalletField = transferMethodConfiguration.getFields().get(0);
        assertThat(hyperwalletField.getCategory(), is("ACCOUNT"));
        assertThat(hyperwalletField.getDataType(), is(EDataType.NUMBER));
        assertThat(hyperwalletField.isRequired(), is(true));
        assertThat(hyperwalletField.getLabel(), is("Account Number"));
        assertThat(hyperwalletField.getMaxLength(), is(17));
        assertThat(hyperwalletField.getMinLength(), is(4));
        assertThat(hyperwalletField.getName(), is("bankAccountId"));
        assertThat(hyperwalletField.getPlaceholder(), is(""));
        assertThat(hyperwalletField.getRegularExpression(), is("^(?![0-]+$)[0-9-]{4,17}$"));
        assertThat(hyperwalletField.getFieldSelectionOptions(), is(nullValue()));

        HyperwalletField optionsHyperwalletField = transferMethodConfiguration.getFields().get(1);
        assertThat(optionsHyperwalletField.getFieldSelectionOptions(), is(notNullValue()));
        assertThat(optionsHyperwalletField.getFieldSelectionOptions().size(), is(2));
        assertThat(optionsHyperwalletField.getFieldSelectionOptions().get(0).getLabel(), is("CHECKING"));
        assertThat(optionsHyperwalletField.getFieldSelectionOptions().get(0).getValue(), is("CHECKING"));

    }
}
