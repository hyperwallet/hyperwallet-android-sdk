package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import android.os.Parcel;

import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletStatusTransitionTest {

    @Rule
    public final HyperwalletExternalResourceManager mExternalResourceManager =
            new HyperwalletExternalResourceManager();

    private static HyperwalletStatusTransition expectedStatusTransition;

    @BeforeClass
    public static void setup() {
        expectedStatusTransition = new HyperwalletStatusTransition.Builder()
                .transition("DE_ACTIVATED")
                .notes("TEST")
                .build();
    }

    @Test
    public void testFromJsonString_convertStatusTransition() throws Exception {
        HyperwalletStatusTransition statusTransition = fromJsonString(
                mExternalResourceManager.getResourceContent("status_transition_response.json"),
                new TypeReference<HyperwalletStatusTransition>() {
                });

        assertNotNull(statusTransition);
        assertThat(statusTransition.getTransition(), is(equalTo(expectedStatusTransition.getTransition())));
        assertThat(statusTransition.getFromStatus(), is(equalTo("ACTIVATED")));
        assertThat(statusTransition.getToStatus(), is(equalTo("DE_ACTIVATED")));
        assertThat(statusTransition.getNotes(), is(equalTo(expectedStatusTransition.getNotes())));
        assertThat(statusTransition.getToken(), is(equalTo("sts-be2080a7-3831-488e-b282-129f41d7dbc9")));
        assertThat(statusTransition.getCreatedOn(), is(equalTo("2019-01-08T23:55:08")));
    }

    @Test
    public void testToJsonObject_convertStatusTransition() throws Exception {
        JSONObject jsonObject = expectedStatusTransition.toJsonObject();

        assertNotNull(jsonObject);
        assertThat(jsonObject.getString("transition"), is(equalTo(expectedStatusTransition.getTransition())));
        assertThat(jsonObject.getString("notes"), is(equalTo(expectedStatusTransition.getNotes())));
    }

    @Test
    public void testHyperwalletStatusTransition_isParcelable() throws Exception {
        HyperwalletStatusTransition statusTransition = fromJsonString(
                mExternalResourceManager.getResourceContent("status_transition_response.json"),
                new TypeReference<HyperwalletStatusTransition>() {
                });

        assertThat(statusTransition, is(notNullValue()));
        assertThat(statusTransition.getCreatedOn(), is("2019-01-08T23:55:08"));
        assertThat(statusTransition.getFromStatus(), is("ACTIVATED"));
        assertThat(statusTransition.getToStatus(), is("DE_ACTIVATED"));
        assertThat(statusTransition.getNotes(), is("TEST"));
        assertThat(statusTransition.getToken(), is("sts-be2080a7-3831-488e-b282-129f41d7dbc9"));
        assertThat(statusTransition.getTransition(), is("DE_ACTIVATED"));

        Parcel parcel = Parcel.obtain();
        statusTransition.writeToParcel(parcel, statusTransition.describeContents());
        parcel.setDataPosition(0);
        HyperwalletStatusTransition bundledHyperwalletStatusTransition =
                HyperwalletStatusTransition.CREATOR.createFromParcel(parcel);

        assertThat(bundledHyperwalletStatusTransition, is(notNullValue()));
        assertThat(bundledHyperwalletStatusTransition.getCreatedOn(), is("2019-01-08T23:55:08"));
        assertThat(bundledHyperwalletStatusTransition.getFromStatus(), is("ACTIVATED"));
        assertThat(bundledHyperwalletStatusTransition.getToStatus(), is("DE_ACTIVATED"));
        assertThat(bundledHyperwalletStatusTransition.getNotes(), is("TEST"));
        assertThat(bundledHyperwalletStatusTransition.getToken(), is("sts-be2080a7-3831-488e-b282-129f41d7dbc9"));
        assertThat(bundledHyperwalletStatusTransition.getTransition(), is("DE_ACTIVATED"));

    }
}


