package com.hyperwallet.android.model.graphql.field;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.hyperwallet.android.rule.ExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class FieldTest {

    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();

    @Test
    public void testField_convertJsonObjectWithoutFieldSelection() throws JSONException {
        String jsonResponse = mExternalResourceManager.getResourceContent(
                "field_without_selection_response.json");
        JSONObject jsonResponseObject = new JSONObject(jsonResponse);
        Field field = new Field(jsonResponseObject);
        assertThat(field.getCategory(), is("ACCOUNT"));
        assertThat(field.getDataType(), is(DataType.NUMBER));
        assertTrue(field.isRequired());
        assertThat(field.getLabel(), is("Routing Number"));
        assertThat(field.getMaxLength(), is(9));
        assertThat(field.getMinLength(), is(9));
        assertThat(field.getName(), is("branchId"));
        assertThat(field.getPlaceholder(), is("A place holder"));
        assertThat(field.getRegularExpression(), is("^[0-9]{9}$"));

        assertNull(field.getFieldSelectionOptions());
        assertNull(field.getValidationMessage());
    }

    @Test
    public void testField_convertJsonObject() throws JSONException {
        String jsonResponse = mExternalResourceManager.getResourceContent(
                "field_selection_response.json");
        JSONObject jsonResponseObject = new JSONObject(jsonResponse);
        Field field = new Field(jsonResponseObject);

        assertThat(field.getCategory(), is("ADDRESS"));
        assertThat(field.getDataType(), is(DataType.SELECTION));
        assertTrue(field.isRequired());
        assertThat(field.getLabel(), is("Country"));
        assertThat(field.getMaxLength(), is(Integer.MAX_VALUE));
        assertThat(field.getMinLength(), is(0));
        assertThat(field.getName(), is("country"));
        assertThat(field.getPlaceholder(), is(emptyOrNullString()));
        assertThat(field.getRegularExpression(), is(emptyOrNullString()));

        assertThat(field.getFieldSelectionOptions(), is(hasSize(2)));
        assertThat(field.getValidationMessage(), is(notNullValue()));
    }

    @Test
    public void testField_convertJsonObjectWithoutMask() throws JSONException {
        String jsonResponse = mExternalResourceManager.getResourceContent(
                "mask_without_conditional_formatting_response.json");
        JSONObject jsonResponseObject = new JSONObject(jsonResponse);
        Field field = new Field(jsonResponseObject);

        assertTrue(field.containsMask());
        assertThat(field.getMask(), is(notNullValue()));
    }

    @Test
    public void testField_convertJsonObjectWithMask() throws JSONException {
        String jsonResponse = mExternalResourceManager.getResourceContent(
                "mask_with_conditional_formatting_response.json");
        JSONObject jsonResponseObject = new JSONObject(jsonResponse);
        Field field = new Field(jsonResponseObject);

        assertTrue(field.containsMask());
        assertThat(field.getMask(), is(notNullValue()));
    }

    @Test
    public void testField_convertJsonObjectWithFieldValueMasked() throws JSONException {
        String jsonResponse = mExternalResourceManager.getResourceContent(
                "mask_with_fieldvaluemasked_response.json");
        JSONObject jsonResponseObject = new JSONObject(jsonResponse);
        Field field = new Field(jsonResponseObject);

        assertTrue(field.isFieldValueMasked());
    }
}
