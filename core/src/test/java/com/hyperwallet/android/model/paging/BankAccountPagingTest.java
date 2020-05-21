package com.hyperwallet.android.model.paging;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.model.transfermethod.BankAccount;
import com.hyperwallet.android.model.transfermethod.TransferMethod;
import com.hyperwallet.android.util.JsonUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class BankAccountPagingTest {

    @Test
    public void testParseBankPaging() throws Exception {
        PageList<BankAccount>
                bankAccountPageList = JsonUtils.fromJsonString(getBankAccounts(),
                new TypeReference<PageList<BankAccount>>() {
                });
        // assert
        assertAll(bankAccountPageList);
    }

    private void assertAll(PageList<BankAccount> bankAccountPageList) {
        assertThat(bankAccountPageList, is(notNullValue()));
        assertThat(bankAccountPageList.getCount(), is(1));
        assertThat(bankAccountPageList.getLimit(), is(10));
        assertThat(bankAccountPageList.getOffset(), is(0));
        assertThat(bankAccountPageList.getPageLinks().isEmpty(), is(false));

        // assert page links
        List<PageLink> pageLinks = bankAccountPageList.getPageLinks();
        assertThat(pageLinks, hasSize(1));
        assertThat(pageLinks.get(0).getPageHref(),
                is("https://localhost:8181/rest/v3/users/test-user-token/bank-accounts"
                        + "?offset=0&limit=10"));
        assertThat(pageLinks.get(0).getPageParameter(), is(notNullValue()));
        assertThat(pageLinks.get(0).getPageParameter().getRel(), is("self"));

        // assert bank account contents
        List<BankAccount> bankAccounts = bankAccountPageList.getDataList();
        assertThat(bankAccounts, hasSize(1));
        assertThat(bankAccounts.get(0).getField(TransferMethod.TransferMethodFields.TOKEN),
                is("trm-fake-token"));
        assertThat(bankAccounts.get(0).getField(TransferMethod.TransferMethodFields.TYPE), is("BANK_ACCOUNT"));
        assertThat(bankAccounts.get(0).getField(TransferMethod.TransferMethodFields.STATUS), is("ACTIVATED"));
        assertThat(bankAccounts.get(0).getField(TransferMethod.TransferMethodFields.TRANSFER_METHOD_COUNTRY), is("US"));
        assertThat(bankAccounts.get(0).getField(TransferMethod.TransferMethodFields.TRANSFER_METHOD_CURRENCY),
                is("USD"));
    }

    public String getBankAccounts() {
        return "{\n" +
                "  \"count\" : 1,\n" +
                "  \"offset\" : 0,\n" +
                "  \"limit\" : 10,\n" +
                "  \"data\" : [ {\n" +
                "    \"token\" : \"trm-fake-token\",\n" +
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
                "      \"href\" : \"https://localhost:8181/rest/v3/users/test-user-token/bank-accounts/trm-fake-token\"\n" +
                "    } ]\n" +
                "  } ],\n" +
                "  \"links\" : [ {\n" +
                "    \"params\" : {\n" +
                "      \"rel\" : \"self\"\n" +
                "    },\n" +
                "    \"href\" : \"https://localhost:8181/rest/v3/users/test-user-token/bank-accounts?offset=0&limit=10\"\n" +
                "  } ]\n" +
                "}";
    }
}


