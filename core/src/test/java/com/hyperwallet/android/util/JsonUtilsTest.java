package com.hyperwallet.android.util;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.transfermethod.BankAccount;
import com.hyperwallet.android.rule.ExternalResourceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Map;

@RunWith(RobolectricTestRunner.class)
public class JsonUtilsTest {

    @Rule
    public final ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();

    @Test
    public void testMapToJsonObject_withValidBankAccountObject() throws JSONException {
        final BankAccount request = new BankAccount
                .Builder("CA", "CAD", "12345")
                .branchId("678910")
                .bankAccountPurpose(BankAccount.Purpose.CHECKING)
                .build();
        JSONObject jsonRequest = request.toJsonObject();
        assertNotNull(jsonRequest);
        assertEquals("CAD", jsonRequest.get("transferMethodCurrency"));
        assertEquals("CA", jsonRequest.get("transferMethodCountry"));
    }

    @Test
    public void testFromJsonString_validBankAccountJson() throws Exception {
        String response = mExternalResourceManager.getResourceContent("bank_account_response.json");
        BankAccount bankAccount = JsonUtils.fromJsonString(response,
                new TypeReference<BankAccount>() {
                });
        assertNotNull(bankAccount);
        assertEquals("BANK_ACCOUNT", bankAccount.getField("type"));
        assertEquals("ACTIVATED", bankAccount.getField("status"));
    }

    @Test
    public void testJsonObjectToMap_validBankAccountJson() throws JSONException {
        String listResponse = mExternalResourceManager.getResourceContent("bank_account_list_response.json");
        Map<String, Object> map = JsonUtils.jsonObjectToMap(new JSONObject(listResponse));
        assertNotNull(map);
    }

    @Test
    public void testMapToJsonObject_withValidBankAccountJson() throws Exception {
        String response = mExternalResourceManager.getResourceContent("bank_account_response.json");
        BankAccount bankAccount = JsonUtils.fromJsonString(response,
                new TypeReference<BankAccount>() {
                });
        assertNotNull(bankAccount);
        JSONObject jsonRequest = bankAccount.toJsonObject();
        assertEquals("USD", jsonRequest.get("transferMethodCurrency"));
        assertEquals("US", jsonRequest.get("transferMethodCountry"));
        JSONArray jsonArray = jsonRequest.getJSONArray("links");
        assertNotNull(jsonArray);
        assertThat(jsonArray.length(), is(1));
    }
}
