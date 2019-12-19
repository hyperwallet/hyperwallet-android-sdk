package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import android.os.Parcel;

import com.hyperwallet.android.rule.ExternalResourceManager;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class StatusTransitionTest {

    private static StatusTransition expectedStatusTransition;
    @Rule
    public final ExternalResourceManager mExternalResourceManager =
            new ExternalResourceManager();

    @BeforeClass
    public static void setup() {
        expectedStatusTransition = new StatusTransition.Builder()
                .transition("DE_ACTIVATED")
                .notes("TEST")
                .build();
    }

    @Test
    public void testFromJsonString_convertStatusTransition() throws Exception {
        StatusTransition statusTransition = fromJsonString(
                mExternalResourceManager.getResourceContent("status_transition_response.json"),
                new TypeReference<StatusTransition>() {
                });

        assertNotNull(statusTransition);
        assertThat(statusTransition.getTransition(), is(equalTo(expectedStatusTransition.getTransition())));
        assertThat(statusTransition.getFromStatus(), is(equalTo("ACTIVATED")));
        assertThat(statusTransition.getToStatus(), is(equalTo("DE_ACTIVATED")));
        assertThat(statusTransition.getNotes(), is(equalTo(expectedStatusTransition.getNotes())));
        assertThat(statusTransition.getToken(), is(equalTo("sts-be2080a7-3831-488e-b282-129f41d7dbc9")));
        assertThat(statusTransition.getStatusCode(), is(nullValue()));
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
    public void testStatusTransition_isParcelable() throws Exception {
        StatusTransition statusTransition = fromJsonString(
                mExternalResourceManager.getResourceContent("status_transition_response.json"),
                new TypeReference<StatusTransition>() {
                });

        assertThat(statusTransition, is(notNullValue()));
        assertThat(statusTransition.getCreatedOn(), is("2019-01-08T23:55:08"));
        assertThat(statusTransition.getFromStatus(), is("ACTIVATED"));
        assertThat(statusTransition.getToStatus(), is("DE_ACTIVATED"));
        assertThat(statusTransition.getNotes(), is("TEST"));
        assertThat(statusTransition.getToken(), is("sts-be2080a7-3831-488e-b282-129f41d7dbc9"));
        assertThat(statusTransition.getTransition(), is("DE_ACTIVATED"));
        assertThat(statusTransition.getStatusCode(), is(nullValue()));

        Parcel parcel = Parcel.obtain();
        statusTransition.writeToParcel(parcel, statusTransition.describeContents());
        parcel.setDataPosition(0);
        StatusTransition bundledStatusTransition =
                StatusTransition.CREATOR.createFromParcel(parcel);

        assertThat(bundledStatusTransition, is(notNullValue()));
        assertThat(bundledStatusTransition.getCreatedOn(), is("2019-01-08T23:55:08"));
        assertThat(bundledStatusTransition.getFromStatus(), is("ACTIVATED"));
        assertThat(bundledStatusTransition.getToStatus(), is("DE_ACTIVATED"));
        assertThat(bundledStatusTransition.getNotes(), is("TEST"));
        assertThat(bundledStatusTransition.getToken(), is("sts-be2080a7-3831-488e-b282-129f41d7dbc9"));
        assertThat(bundledStatusTransition.getTransition(), is("DE_ACTIVATED"));
        assertThat(bundledStatusTransition.getStatusCode(), is(nullValue()));
    }
}


