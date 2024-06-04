package com.hyperwallet.android;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.fail;

import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Date;

@RunWith(RobolectricTestRunner.class)
public class ConfigurationTest {

    private static Configuration mConfiguration;
    private static String mJwtToken;
    @Rule
    public final ExpectedException mThrown = ExpectedException.none();

    @BeforeClass
    public static void setup() {
        try {
            mJwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9."
                    + "eyJzdWIiOiJ0ZXN0LXVzZXItdG9rZW4iLCJpYXQiOjI1NDgzNjg2ODYsImV4cCI6MjU0ODM2OTI4NiwiYXVkIjoidGVzdC1"
                    + "jbGllbnQtdG9rZW4iLCJpc3MiOiJ0ZXN0LXByb2dyYW0tdG9rZW4iLCJyZXN0LXVyaSI6Imh0dHBzOi8vbG9jYWxob3N0Oj"
                    + "gxODEvcmVzdC92My8iLCJncmFwaHFsLXVyaSI6Imh0dHBzOi8vbG9jYWxob3N0OjgxODEvZ3JhcGhxbCJ9"
                    + ".3GSVpYoqVMx4hXyZrlaj_wjJWAQLCX5ivRqvtybKV76cmnWxWfnoZEr0-4ipMH_aY8GTBCDzsgab3NREGkgjSg";
            mConfiguration = new Configuration(mJwtToken);
        } catch (JSONException e) {
            fail("Unable to parse json response");
        }
    }

    @Test
    public void testGetAuthenticationToken_returnsJwtToken() {
        String authenticationToken = mConfiguration.getAuthenticationToken();
        assertThat(authenticationToken, is(equalTo(mJwtToken)));
    }

    @Test
    public void testGetCreatedOn_returnsCreatedDate() {
        Date createdOn = mConfiguration.getCreatedOn();
        assertThat(createdOn, is(equalTo(new Date(2548368686000L))));
    }

    @Test
    public void testGetExpiresOn_returnsExpiryDate() {
        Date createdOn = mConfiguration.getExpiresOn();
        assertThat(createdOn, is(equalTo(new Date(2548369286000L))));
    }

    @Test
    public void testGetGraphQlUri_returnsGraphQlUri() {
        String graphQlUri = mConfiguration.getGraphQlUri();
        assertThat(graphQlUri, is(equalTo("https://localhost:8181/graphql")));
    }

    @Test
    public void testGetProgramToken_returnsProgramToken() {
        String programToken = mConfiguration.getProgramToken();
        assertThat(programToken, is(equalTo("test-program-token")));
    }

    @Test
    public void testGetRestUri_returnsRestUri() {
        String restUri = mConfiguration.getRestUri();
        assertThat(restUri, is(equalTo("https://localhost:8181/rest/v3/")));
    }

    @Test
    public void testGetUserToken_returnsUserToken() {
        String userToken = mConfiguration.getUserToken();
        assertThat(userToken, is(equalTo("test-user-token")));
    }

    @Test
    public void testIsStale_returnsFalseWhenTokenNotExpired() {
        Boolean configurationStale = mConfiguration.isStale();
        assertThat(configurationStale, is(false));
    }


    @Test
    public void testParsePayload_parseTokenWithMissingPayload() throws JSONException {
        mThrown.expect(JSONException.class);
        mThrown.expectMessage("No value for iat");

        new Configuration("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9."
                + "eyJzdWIiOiJ1c3ItNGNkNGE4MzktZmRkMi00OGYxLWJkMzAtNzk1ODIxNmU3ODFmIiwiZXhwIjoyNTQ"
                + "4MzY5Mjg2LCJhdWQiOiJwZ3UtZjJmMDU2YjAtNWZmMC00NDdiLWFmZmItYjliNDNhN2UyYzQ5IiwiaX"
                + "NzIjoicHJnLTA0MGU5YjNkLTYxNGMtMTFlNS1hZjIzLTBmYWEyOGNhN2MwZiIsInJlc3QtdXJpIjoia"
                + "HR0cHM6Ly9hcGkuc2FuZGJveC5oeXBlcndhbGxldC5jb20vcmVzdC92My8iLCJncmFwaHFsLXVyaSI6"
                + "Imh0dHBzOi8vYXBpLnNhbmRib3guaHlwZXJ3YWxsZXQuY29tL2dyYXBocWwifQ.yCQgfoMsytc0w5ar"
                + "s-KPu7jxWFiGqPuRWgSWP9NFZzxuFb1KG9QguBIje-aq8LolKO-CpysCDhLL4knM2_38YQ");
    }

    @Test
    public void testParsePayload_parseTokenWithNonJsonPayload() throws JSONException {
        mThrown.expect(JSONException.class);
        mThrown.expectMessage(
                "Value this of type java.lang.String cannot be converted to JSONObject");

        new Configuration("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9."
                + "dGhpcyBpcyBhIHRlc3Q.2Mv6q63MjtSI1ER-61sy04TwtD8VY1mpQ2ZPrWYdJfjcdqq8aiIdx6-PsUn"
                + "8kTBt3pfaYB2ZRk-xgIeIbUirqA");
    }

    @Test
    public void testConfiguration_parseValidToken() throws JSONException {
        Configuration configuration = new Configuration("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9."
                + "eyJzdWIiOiJ0ZXN0LXVzZXItdG9rZW4iLCJpYXQiOjI1NDgzNjg2ODYsImV4cCI6MjU0ODM2OTI4NiwiYXVkIjoidGVzdC1"
                + "jbGllbnQtdG9rZW4iLCJpc3MiOiJ0ZXN0LXByb2dyYW0tdG9rZW4iLCJyZXN0LXVyaSI6Imh0dHBzOi8vbG9jYWxob3N0Oj"
                + "gxODEvcmVzdC92My8iLCJncmFwaHFsLXVyaSI6Imh0dHBzOi8vbG9jYWxob3N0OjgxODEvZ3JhcGhxbCJ9"
                +".3GSVpYoqVMx4hXyZrlaj_wjJWAQLCX5ivRqvtybKV76cmnWxWfnoZEr0-4ipMH_aY8GTBCDzsgab3NREGkgjSg");

        assertThat(configuration.getAuthenticationToken(), is("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9."
                + "eyJzdWIiOiJ0ZXN0LXVzZXItdG9rZW4iLCJpYXQiOjI1NDgzNjg2ODYsImV4cCI6MjU0ODM2OTI4NiwiYXVkIjoidGVzdC1"
                + "jbGllbnQtdG9rZW4iLCJpc3MiOiJ0ZXN0LXByb2dyYW0tdG9rZW4iLCJyZXN0LXVyaSI6Imh0dHBzOi8vbG9jYWxob3N0Oj"
                + "gxODEvcmVzdC92My8iLCJncmFwaHFsLXVyaSI6Imh0dHBzOi8vbG9jYWxob3N0OjgxODEvZ3JhcGhxbCJ9"
                +".3GSVpYoqVMx4hXyZrlaj_wjJWAQLCX5ivRqvtybKV76cmnWxWfnoZEr0-4ipMH_aY8GTBCDzsgab3NREGkgjSg"));

        assertThat(configuration.getUserToken(), is("test-user-token"));
    }

    @Test
    public void testConfiguration_parseEmpty() throws JSONException {
        mThrown.expect(IllegalArgumentException.class);
        new Configuration("");
    }

    @Test
    public void testConfiguration_parseInvalidToken() throws JSONException {
        mThrown.expect(IllegalArgumentException.class);
        new Configuration(" . . .");
    }

    @Test
    public void testConfiguration_parseIncompleteToken() throws JSONException {
        mThrown.expect(IllegalArgumentException.class);
        new Configuration("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.");
    }

    @Test
    public void testConfiguration_checkIsStale() throws JSONException {
        Configuration configuration = new Configuration("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3ItNG"
                + "NkNGE4MzktZmRkMi00OGYxLWJkMzAtNzk1ODIxNmU3ODFmIiwiaWF0IjoxNTQ4MzY4Njg2LCJleHAiOjE1NDgzNjg2ODcsImF1ZC"
                + "I6InBndS1mMmYwNTZiMC01ZmYwLTQ0N2ItYWZmYi1iOWI0M2E3ZTJjNDkiLCJpc3MiOiJwcmctMDQwZTliM2QtNjE0Yy0xMWU1LW"
                + "FmMjMtMGZhYTI4Y2E3YzBmIiwicmVzdC11cmkiOiJodHRwczovL2FwaS5zYW5kYm94Lmh5cGVyd2FsbGV0LmNvbS9yZXN0L3YzLy"
                + "IsImdyYXBocWwtdXJpIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5oeXBlcndhbGxldC5jb20vZ3JhcGhxbCJ9.P377JI0bqJsHtMQcr"
                + "U7B-fEEQVEPgHsyRJND1g5UEafD5zWmFhIn-S6Mj-upJYCPJupOSwPVqDW-9D57hcHD6A");

        assertThat(configuration.isStale(), is(true));
    }

    @Test
    public void testConfiguration_checkIsNotStale() throws JSONException {
        Configuration configuration = new Configuration("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9."
                + "eyJzdWIiOiJ0ZXN0LXVzZXItdG9rZW4iLCJpYXQiOjI1NDgzNjg2ODYsImV4cCI6MjU0ODM2OTI4NiwiYXVkIjoidGVzdC10b2tl"
                + "biIsImlzcyI6InRlc3QtcHJvZ3JhbS10b2tlbiIsInJlc3QtdXJpIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6ODE4MS9yZXN0L3YzLyIs"
                + "ImdyYXBocWwtdXJpIjoiaHR0cHM6Ly9sb2NhbGhvc3Q6ODE4MS9ncmFwaHFsIn0=.3GSVpYoqVMx4hXyZrlaj_wjJWAQLCX5ivRq"
                + "vtybKV76cmnWxWfnoZEr0-4ipMH_aY8GTBCDzsgab3NREGkgjSg");

        assertThat(configuration.isStale(), is(false));
    }

    @Test
    public void testConfiguration_parseInsightParameters() throws JSONException {
        Configuration configuration = new Configuration("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzd"
                + "WIiOiJ1c3ItdG9rZW4iLCJpYXQiOjE1NzI5OTI0MzYsImV4cCI6MTU3Mjk5MjczNiwiYXVkIjoicGd1LXRva2VuIiwia"
                + "XNzIjoicGdyLXRva2VuIiwicmVzdC11cmkiOiJyZXN0LnRlc3QuY29tIiwiZ3JhcGhxbC11cmkiOiJncmFwaHFsLnRlc3Qu"
                + "Y29tIiwiaW5zaWdodHMtdXJpIjoiaW5zaWdodHMudGVzdC5jb20vdHJhY2svZXZlbnRzIiwiZW52aXJvbm1lbnQiOi"
                + "JERVYifQ.7V2fZ9KmcMdRh40RnQmwVQjbanoGGDJcNmNbCiHVVEIII45OgWo0VF7KFpijVoNYFqkkiZEDpct7e44E5MPLgw");

        assertThat(configuration, is(notNullValue()));
        assertThat(configuration.getEnvironment(), is("DEV"));
        assertThat(configuration.getInsightApiUri(), is("insights.test.com/track/events"));
    }
}