package com.hyperwallet.android.rule;

import android.util.Base64;

import com.hyperwallet.android.Hyperwallet;
import com.hyperwallet.android.HyperwalletAuthenticationTokenListener;
import com.hyperwallet.android.HyperwalletAuthenticationTokenProvider;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class HyperwalletSdkMock extends TestWatcher {

    private HyperwalletMockWebServer server;
    private boolean failAuthentication;

    public HyperwalletSdkMock(HyperwalletMockWebServer server) {
        this.server = server;
        failAuthentication = false;
    }

    public void setFailAuthentication(final boolean failAuthentication) {
        this.failAuthentication = failAuthentication;
    }

    @Override
    protected void starting(Description description) {
        super.starting(description);
        Hyperwallet.getInstance(new AuthenticationProvider(server));
    }


    private class AuthenticationProvider implements HyperwalletAuthenticationTokenProvider {

        private static final String USER_TOKEN = "sub";
        private static final String ISSUER = "iss";
        private static final String CREATE_ON = "iat";
        private static final String EXPIRES_ON = "exp";
        private static final String CLIENT_TOKEN = "aud";
        private static final String REST_URL = "rest-uri";
        private static final String GRAPH_QL_URL = "graphql-uri";
        private static final String INSIGHT_API_URL = "insights-uri";
        private static final String ENVIRONMENT = "environment";
        private HyperwalletMockWebServer server;

        private AuthenticationProvider(HyperwalletMockWebServer server) {
            this.server = server;
        }


        @Override
        public void retrieveAuthenticationToken(
                HyperwalletAuthenticationTokenListener authenticationTokenListener) {
            String authToken = buildJwtToken();
            if (failAuthentication) {
                authenticationTokenListener.onFailure(UUID.randomUUID(), "Error in authentication");
            } else {
                if (authenticationTokenListener != null) {
                    authenticationTokenListener.onSuccess(authToken);
                }
            }
        }

        private String buildJwtToken() {
            try {
                String jwtHeader = "{\"alg\":\"HS256\"}";

                JSONObject jwtPayload = new JSONObject();
                jwtPayload.put(GRAPH_QL_URL, server.getServer().url("graphql/"));
                jwtPayload.put(REST_URL, server.getServer().url("rest/v3/"));
                jwtPayload.put(CLIENT_TOKEN, "test-client-token");
                jwtPayload.put(USER_TOKEN, "test-user-token");
                jwtPayload.put(ISSUER, "test-program-token");
                jwtPayload.put(INSIGHT_API_URL, "insights.test.com/track/events");
                jwtPayload.put(ENVIRONMENT, "TEST-DEV");
                Calendar cal = Calendar.getInstance();
                long createOn = TimeUnit.SECONDS.toMillis(cal.getTimeInMillis());
                jwtPayload.put(CREATE_ON, createOn);
                //Add 10 minutes
                cal.add(Calendar.MINUTE, 10);
                long expireOn = TimeUnit.SECONDS.toMillis(cal.getTimeInMillis());
                jwtPayload.put(EXPIRES_ON, expireOn);

                String encodedHeader = base64encode(jwtHeader);
                String encodedClaims = base64encode(jwtPayload.toString());
                String encodedSignature = base64encode("signature");

                return encodedHeader + "." + encodedClaims + "." + encodedSignature;
            } catch (JSONException e) {
                throw new IllegalStateException(
                        "Something went wrong when initializing the Hyperwallet SDK rule", e);
            }
        }

        private String base64encode(String value) {
            return Base64.encodeToString(value.getBytes(StandardCharsets.UTF_8),
                    Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
        }

    }
}
