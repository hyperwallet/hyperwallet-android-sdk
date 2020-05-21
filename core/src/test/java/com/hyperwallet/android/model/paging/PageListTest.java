package com.hyperwallet.android.model.paging;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.model.transfermethod.BankAccount;
import com.hyperwallet.android.rule.ExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;

@RunWith(RobolectricTestRunner.class)
public class PageListTest {

    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();

    @Test
    public void testPageList_convertJsonObject() throws JSONException, HyperwalletException {
        String json = mExternalResourceManager.getResourceContent("bank_account_list_response.json");
        JSONObject jsonObject = new JSONObject(json);
        PageList<BankAccount> pageList = new PageList<>(jsonObject,
                BankAccount.class);

        assertThat(pageList.getCount(), is(2));
        assertThat(pageList.getLimit(), is(10));
        assertThat(pageList.getOffset(), is(0));
        assertThat(pageList.getDataList(), is(notNullValue()));
    }

    @Test
    public void testPageList_verifyDefaultValues() {
        PageList<BankAccount> pageList = new PageList<>(
                Collections.<BankAccount>emptyList());

        assertThat(pageList.getCount(), is(0));
        assertThat(pageList.getLimit(), is(0));
        assertThat(pageList.getOffset(), is(0));
        assertThat(pageList.getDataList(), is(Collections.EMPTY_LIST));
    }

}