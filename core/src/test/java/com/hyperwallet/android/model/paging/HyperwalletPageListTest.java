package com.hyperwallet.android.model.paging;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.hyperwallet.android.exception.HyperwalletException;
import com.hyperwallet.android.model.HyperwalletBankAccount;
import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Collections;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletPageListTest {

    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testHyperwalletPageList_convertJsonObject() throws JSONException, HyperwalletException {
        String json = mExternalResourceManager.getResourceContent("bank_account_list_response.json");
        JSONObject jsonObject = new JSONObject(json);
        HyperwalletPageList<HyperwalletBankAccount> pageList = new HyperwalletPageList<>(jsonObject,
                HyperwalletBankAccount.class);

        assertThat(pageList.getCount(), is(2));
        assertThat(pageList.getLimit(), is(10));
        assertThat(pageList.getOffset(), is(0));
        assertThat(pageList.getDataList(), is(notNullValue()));
    }

    @Test
    public void testHyperwalletPageList_verifyDefaultValues() {
        HyperwalletPageList<HyperwalletBankAccount> pageList = new HyperwalletPageList<>(
                Collections.<HyperwalletBankAccount>emptyList());

        assertThat(pageList.getCount(), is(0));
        assertThat(pageList.getLimit(), is(0));
        assertThat(pageList.getOffset(), is(0));
        assertThat(pageList.getDataList(), is(Collections.EMPTY_LIST));
    }

}