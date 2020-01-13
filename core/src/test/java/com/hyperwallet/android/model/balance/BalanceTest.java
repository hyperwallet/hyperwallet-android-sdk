package com.hyperwallet.android.model.balance;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import android.os.Parcel;

import com.hyperwallet.android.model.TypeReference;
import com.hyperwallet.android.rule.ExternalResourceManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class BalanceTest {
    @Rule
    public ExternalResourceManager mExternalResourceManager = new ExternalResourceManager();

    @Test
    public void testFromJsonString_balanceResponse() throws Exception {
        Balance balance = fromJsonString(
                mExternalResourceManager.getResourceContent("balance_response.json"),
                new TypeReference<Balance>() {
                });

        assertThat(balance, is(notNullValue()));
        assertThat(balance.getCurrency(), is("CAD"));
        assertThat(balance.getAmount(), is("988.03"));
    }

    @Test
    public void testBalance_isParcelable() throws Exception {
        Balance balance = fromJsonString(
                mExternalResourceManager.getResourceContent("balance_response.json"),
                new TypeReference<Balance>() {
                });

        assertThat(balance.getCurrency(), is("CAD"));
        assertThat(balance.getAmount(), is("988.03"));

        Parcel parcel = Parcel.obtain();
        balance.writeToParcel(parcel, balance.describeContents());
        parcel.setDataPosition(0);
        Balance bundledBalance = Balance.CREATOR.createFromParcel(parcel);

        assertThat(bundledBalance.getCurrency(), is("CAD"));
        assertThat(bundledBalance.getAmount(), is("988.03"));
    }

    @Test
    public void testBalance_isCheckWithEmptyResponse() throws Exception {
        Balance balance = fromJsonString("{}", new TypeReference<Balance>() {});

        assertThat(balance.getCurrency(), is(nullValue()));
        assertThat(balance.getAmount(), is(nullValue()));
    }
}