package com.hyperwallet.android.model.graphql;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.hyperwallet.android.model.graphql.field.EDataType;
import com.hyperwallet.android.model.graphql.field.HyperwalletField;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletFieldTest {

    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testHyperwalletField_convertJsonObjectWithoutFieldSelection() throws JSONException {
        String jsonResponse = mExternalResourceManager.getResourceContent(
                "field_without_selection_response.json");
        JSONObject jsonResponseObject = new JSONObject(jsonResponse);
        HyperwalletField hyperwalletField = new HyperwalletField(jsonResponseObject);
        assertThat(hyperwalletField.getCategory(), is("ACCOUNT"));
        assertThat(hyperwalletField.getDataType(), is(EDataType.NUMBER));
        assertTrue(hyperwalletField.isRequired());
        assertThat(hyperwalletField.getLabel(), is("Routing Number"));
        assertThat(hyperwalletField.getMaxLength(), is(9));
        assertThat(hyperwalletField.getMinLength(), is(9));
        assertThat(hyperwalletField.getName(), is("branchId"));
        assertThat(hyperwalletField.getPlaceholder(), is("A place holder"));
        assertThat(hyperwalletField.getRegularExpression(), is("^[0-9]{9}$"));

        assertNull(hyperwalletField.getFieldSelectionOptions());
        assertNull(hyperwalletField.getHyperwalletValidationMessage());
    }

    @Test
    public void testHyperwalletField_convertJsonObject() throws JSONException {
        String jsonResponse = mExternalResourceManager.getResourceContent(
                "field_selection_response.json");
        JSONObject jsonResponseObject = new JSONObject(jsonResponse);
        HyperwalletField hyperwalletField = new HyperwalletField(jsonResponseObject);

        assertThat(hyperwalletField.getCategory(), is("ADDRESS"));
        assertThat(hyperwalletField.getDataType(), is(EDataType.SELECTION));
        assertTrue(hyperwalletField.isRequired());
        assertThat(hyperwalletField.getLabel(), is("Country"));
        assertThat(hyperwalletField.getMaxLength(), is(Integer.MAX_VALUE));
        assertThat(hyperwalletField.getMinLength(), is(0));
        assertThat(hyperwalletField.getName(), is("country"));
        assertThat(hyperwalletField.getPlaceholder(), is(emptyOrNullString()));
        assertThat(hyperwalletField.getRegularExpression(), is(emptyOrNullString()));

        assertThat(hyperwalletField.getFieldSelectionOptions(), is(hasSize(2)));
        assertThat(hyperwalletField.getHyperwalletValidationMessage(), is(notNullValue()));
    }
}
