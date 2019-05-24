package com.hyperwallet.android.model.paging;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.hyperwallet.android.model.HyperwalletBankAccount;
import com.hyperwallet.android.model.HyperwalletTransferMethod;
import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.util.JsonUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletBankAccountPagingTest {

    @Test
    public void testParseBankPaging() throws Exception {
        HyperwalletPageList<HyperwalletBankAccount> bankAccountHyperwalletPageList = JsonUtils.fromJsonString(getBankAccounts(), new TypeReference<HyperwalletPageList<HyperwalletBankAccount>>() {
        });
        // assert
        assertAll(bankAccountHyperwalletPageList);
    }

    private void assertAll(HyperwalletPageList<HyperwalletBankAccount> bankAccountHyperwalletPageList) {
        assertThat(bankAccountHyperwalletPageList, is(notNullValue()));
        assertThat(bankAccountHyperwalletPageList.getCount(), is(1));
        assertThat(bankAccountHyperwalletPageList.getLimit(), is(10));
        assertThat(bankAccountHyperwalletPageList.getOffset(), is(0));
        assertThat(bankAccountHyperwalletPageList.getPageLinks().isEmpty(), is(false));

        // assert page links
        List<HyperwalletPageLink> hyperwalletPageLinks = bankAccountHyperwalletPageList.getPageLinks();
        assertThat(hyperwalletPageLinks, hasSize(1));
        //assertThat(hyperwalletPageLinks.get(0).getPageHref(), is("https://localhost:8181/rest/v3/users/usr-e8715a30-3e79-4c4f-a7b7-0f4c42c3a5a5/bank-accounts?offset=0&limit=10"));
        assertThat(hyperwalletPageLinks.get(0).getPageParameter(), is(notNullValue()));
        assertThat(hyperwalletPageLinks.get(0).getPageParameter().getRel(), is("self"));

        // assert bank account contents
        List<HyperwalletBankAccount> bankAccounts = bankAccountHyperwalletPageList.getDataList();
        assertThat(bankAccounts, hasSize(1));
        assertThat(bankAccounts.get(0).getField(HyperwalletTransferMethod.TransferMethodFields.TOKEN), is("trm-c47aee4e-2b4c-4d0a-b3ee-d8fdb2be0422"));
        assertThat(bankAccounts.get(0).getField(HyperwalletTransferMethod.TransferMethodFields.TYPE), is("BANK_ACCOUNT"));
        assertThat(bankAccounts.get(0).getField(HyperwalletTransferMethod.TransferMethodFields.STATUS), is("ACTIVATED"));
        assertThat(bankAccounts.get(0).getField(HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(bankAccounts.get(0).getField(HyperwalletTransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY), is("USD"));
    }

    public String getBankAccounts() {
        return "{\n" +
                "  \"count\" : 1,\n" +
                "  \"offset\" : 0,\n" +
                "  \"limit\" : 10,\n" +
                "  \"data\" : [ {\n" +
                "    \"token\" : \"trm-c47aee4e-2b4c-4d0a-b3ee-d8fdb2be0422\",\n" +
                "    \"type\" : \"BANK_ACCOUNT\",\n" +
                "    \"status\" : \"ACTIVATED\",\n" +
                "    \"createdOn\" : \"2019-01-04T14:53:48\",\n" +
                "    \"transferMethodCountry\" : \"US\",\n" +
                "    \"transferMethodCurrency\" : \"USD\",\n" +
                "    \"bankId\" : \"021000021\",\n" +
                "    \"branchId\" : \"021000021\",\n" +
                "    \"bankAccountId\" : \"4670010453603\",\n" +
                "    \"bankAccountRelationship\" : \"SELF\",\n" +
                "    \"bankAccountPurpose\" : \"SAVINGS\",\n" +
                "    \"profileType\" : \"INDIVIDUAL\",\n" +
                "    \"firstName\" : \"Kevin\",\n" +
                "    \"lastName\" : \"Mccarthy\",\n" +
                "    \"dateOfBirth\" : \"1999-01-01\",\n" +
                "    \"gender\" : \"MALE\",\n" +
                "    \"phoneNumber\" : \"6041231234\",\n" +
                "    \"governmentId\" : \"111222333\",\n" +
                "    \"addressLine1\" : \"950 Granville St\",\n" +
                "    \"city\" : \"San Jose\",\n" +
                "    \"stateProvince\" : \"California\",\n" +
                "    \"country\" : \"CA\",\n" +
                "    \"postalCode\" : \"H0H0H0\",\n" +
                "    \"links\" : [ {\n" +
                "      \"params\" : {\n" +
                "        \"rel\" : \"self\"\n" +
                "      },\n" +
                "      \"href\" : \"https://localhost:8181/rest/v3/users/usr-e8715a30-3e79-4c4f-a7b7-0f4c42c3a5a5/bank-accounts/trm-c47aee4e-2b4c-4d0a-b3ee-d8fdb2be0422\"\n" +
                "    } ]\n" +
                "  } ],\n" +
                "  \"links\" : [ {\n" +
                "    \"params\" : {\n" +
                "      \"rel\" : \"self\"\n" +
                "    },\n" +
                "    \"href\" : \"https://localhost:8181/rest/v3/users/usr-e8715a30-3e79-4c4f-a7b7-0f4c42c3a5a5/bank-accounts?offset=0&limit=10\"\n" +
                "  } ]\n" +
                "}";
    }
}


