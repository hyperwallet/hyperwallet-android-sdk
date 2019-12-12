package com.hyperwallet.android.model.graphql.field;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.hyperwallet.android.rule.ExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ValidationMessageTest {

    @Rule
    public final ExternalResourceManager mResourceManager = new ExternalResourceManager();

    @Test
    public void testHyperwalletValidationMessage_convertJsonObject() throws JSONException {
        String data = mResourceManager.getResourceContent("validation_message_data.json");
        JSONObject jsonObject = new JSONObject(data);
        ValidationMessage validationMessage = new ValidationMessage(jsonObject);

        assertThat(validationMessage, is(notNullValue()));
        assertThat(validationMessage.getLength(),
                is("The minimum length of this field is 4 and maximum length is 17."));
        assertThat(validationMessage.getPattern(), is("accountNumber is invalid format."));
        assertThat(validationMessage.getEmpty(), is("You must provide a value for this field"));
    }
}
