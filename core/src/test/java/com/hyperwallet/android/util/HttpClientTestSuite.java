package com.hyperwallet.android.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapWithSize.aMapWithSize;
import static org.junit.Assert.assertNotNull;

import com.hyperwallet.android.rule.ExternalResourceManager;
import com.hyperwallet.android.rule.MockWebServer;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        HttpClientTestSuite.HttpClientTest.class,
        HttpClientTestSuite.HttpClientBuilderTest.class,
})
public class HttpClientTestSuite {

    private static final Map<String, String> HTTP_HEADERS = new HashMap<String, String>() {{
        put("Accept", "application/json");
        put("Content-Type", "application/json");
        put("Authorization", "Basic cWFyZXN0NEAyY");
    }};

    private static final Map<String, String> QUERY_PARAMS = new HashMap<String, String>() {{
        put("orderBy", "date");
        put("limit", "10");
    }};


    @RunWith(JUnitParamsRunner.class)
    public static class HttpClientTest {

        @ClassRule
        public static final ExternalResourceManager hyperwalletResourceManager =
                new ExternalResourceManager();
        @Rule
        public final ExpectedException thrown = ExpectedException.none();
        @Rule
        public final MockWebServer server = new MockWebServer();

        @Test
        public void testGetHttpUrlConnection_verifyDefaultValues()
                throws IOException, URISyntaxException {

            String url = server.mockResponse().getRequestUrl();
            HttpClient client = new HttpClient.Builder(url)
                    .path("hyperwallet")
                    .build();

            HttpURLConnection httpURLConnection = client.getHttpUrlConnection();

            assertNotNull(httpURLConnection);
            assertThat(httpURLConnection.getConnectTimeout(), is(10_000));
            assertThat(httpURLConnection.getReadTimeout(), is(10_000));
            assertNotNull(httpURLConnection.getRequestProperties());
            assertThat(httpURLConnection.getRequestProperties(), aMapWithSize(0));
        }


        @Test
        public void testGetHeaderMap_returnInsertedValues() throws IOException, URISyntaxException {

            String url = server.mockResponse().getRequestUrl();
            HttpClient client = new HttpClient.Builder(url)
                    .path("hyperwallet")
                    .putHeaders(HTTP_HEADERS)
                    .build();

            Map<String, String> headers = client.getHeaderMap();

            assertNotNull(headers);
            assertThat(headers.get("Accept"), is("application/json"));
            assertThat(headers.get("Content-Type"), is("application/json"));
            assertThat(headers.get("Authorization"), is("Basic cWFyZXN0NEAyY"));

        }


        @Test
        public void testGetQueryMap_returnInsertedValues() throws IOException, URISyntaxException {

            String url = server.mockResponse().getRequestUrl();
            HttpClient client = new HttpClient.Builder(url)
                    .path("hyperwallet")
                    .putQueries(QUERY_PARAMS)
                    .build();

            Map<String, String> queryParams = client.getQueryMap();

            assertNotNull(queryParams);
            assertThat(queryParams.get("orderBy"), is("date"));
            assertThat(queryParams.get("limit"), is("10"));

        }


        @Test
        public void testGet_verifyHttpResponseCode() throws Exception {
            String requestUrl = server.mockResponse()
                    .withHttpResponseCode(HttpURLConnection.HTTP_NO_CONTENT).getRequestUrl();
            HttpClient client = new HttpClient.Builder(requestUrl).build();

            int httpResponseCode = client.get();

            assertThat(httpResponseCode, is(HttpURLConnection.HTTP_NO_CONTENT));
        }


        @Test
        public void testPost_verifyHttpResponseCode() throws Exception {
            String requestUrl = server.mockResponse()
                    .withHttpResponseCode(HttpURLConnection.HTTP_CREATED).getRequestUrl();
            HttpClient client = new HttpClient.Builder(requestUrl).build();

            int httpResponseCode = client.post("");

            assertThat(httpResponseCode, is(HttpURLConnection.HTTP_CREATED));
        }


        @Test
        public void testPut_verifyHttpResponseCode() throws Exception {
            String requestUrl = server.mockResponse()
                    .withHttpResponseCode(HttpURLConnection.HTTP_OK).getRequestUrl();
            HttpClient client = new HttpClient.Builder(requestUrl).build();

            int httpResponseCode = client.put("");

            assertThat(httpResponseCode, is(HttpURLConnection.HTTP_OK));
        }


        @Test
        public void testGetResponse_withBody() throws Exception {

            String body = hyperwalletResourceManager.getResourceContent("bank_account_response.json");
            String requestUrl = server.mockResponse().withBody(body).getRequestUrl();
            HttpClient client = new HttpClient.Builder(requestUrl).build();

            String response = client.getResponse();

            assertNotNull(requestUrl);
            assertThat(response, is(body));

        }


        @Test
        public void testGetResponse_withoutBody() throws Exception {

            String requestUrl = server.mockResponse().getRequestUrl();
            HttpClient client = new HttpClient.Builder(requestUrl).build();

            String response = client.getResponse();

            assertNotNull(requestUrl);
            assertThat(response, is(""));

        }

        @Test
        public void testGetHttpUrlConnection_verifyConnectTimeout() throws Exception {

            String requestUrl = server.mockResponse().getRequestUrl();
            HttpClient client = new HttpClient.Builder(requestUrl).connectTimeout(2018).build();

            client.getHttpUrlConnection().getConnectTimeout();
            assertNotNull(client.getHttpUrlConnection());
            int connectTimeout = client.getHttpUrlConnection().getConnectTimeout();
            assertThat(connectTimeout, is(2018));
        }


        @Test
        public void testGetHttpUrlConnection_verifyReadTimeout() throws Exception {

            String requestUrl = server.mockResponse().getRequestUrl();
            HttpClient client = new HttpClient.Builder(requestUrl).readTimeout(2000).build();

            assertNotNull(client.getHttpUrlConnection());
            int readTimeout = client.getHttpUrlConnection().getReadTimeout();
            assertThat(readTimeout, is(2000));

        }

        @Test
        public void testGetHttpUrlConnection_verifyUrl() throws Exception {

            String requestUrl = server.mockResponse().withPath("hyperwallet").getRequestUrl();
            HttpClient client = new HttpClient.Builder(requestUrl).build();

            assertNotNull(client.getHttpUrlConnection());
            URL url = client.getHttpUrlConnection().getURL();
            assertThat(url.toString(), is(requestUrl));
        }


        private Collection<Object[]> testHttpResponseCodeWithinSuccessRangeData() {
            return Arrays.asList(new Object[][]{
                    {HttpURLConnection.HTTP_OK, true},
                    {HttpURLConnection.HTTP_CREATED, true},
                    {HttpURLConnection.HTTP_ACCEPTED, true},
                    {HttpURLConnection.HTTP_NOT_AUTHORITATIVE, true},
                    {HttpURLConnection.HTTP_NO_CONTENT, true},
                    {HttpURLConnection.HTTP_RESET, true},
                    {HttpURLConnection.HTTP_PARTIAL, true},
                    {HttpURLConnection.HTTP_MULT_CHOICE, false},
                    {HttpURLConnection.HTTP_MOVED_PERM, false},
                    {HttpURLConnection.HTTP_MOVED_TEMP, false},
                    {HttpURLConnection.HTTP_SEE_OTHER, false},
                    {HttpURLConnection.HTTP_NOT_MODIFIED, false},
                    {HttpURLConnection.HTTP_USE_PROXY, false},
                    {HttpURLConnection.HTTP_BAD_REQUEST, false},
                    {HttpURLConnection.HTTP_UNAUTHORIZED, false},
                    {HttpURLConnection.HTTP_PAYMENT_REQUIRED, false},
                    {HttpURLConnection.HTTP_FORBIDDEN, false},
                    {HttpURLConnection.HTTP_NOT_FOUND, false},
                    {HttpURLConnection.HTTP_BAD_METHOD, false},
                    {HttpURLConnection.HTTP_NOT_ACCEPTABLE, false},
                    {HttpURLConnection.HTTP_PROXY_AUTH, false},
                    {HttpURLConnection.HTTP_CLIENT_TIMEOUT, false},
                    {HttpURLConnection.HTTP_CONFLICT, false},
                    {HttpURLConnection.HTTP_GONE, false},
                    {HttpURLConnection.HTTP_LENGTH_REQUIRED, false},
                    {HttpURLConnection.HTTP_PRECON_FAILED, false},
                    {HttpURLConnection.HTTP_ENTITY_TOO_LARGE, false},
                    {HttpURLConnection.HTTP_REQ_TOO_LONG, false},
                    {HttpURLConnection.HTTP_UNSUPPORTED_TYPE, false},
                    {HttpURLConnection.HTTP_INTERNAL_ERROR, false},
                    {HttpURLConnection.HTTP_NOT_IMPLEMENTED, false},
                    {HttpURLConnection.HTTP_BAD_GATEWAY, false},
                    {HttpURLConnection.HTTP_UNAVAILABLE, false},
                    {HttpURLConnection.HTTP_GATEWAY_TIMEOUT, false},
                    {HttpURLConnection.HTTP_VERSION, false}
            });
        }

        @Test
        @Parameters(method = "testHttpResponseCodeWithinSuccessRangeData")
        public void testIsSuccess_httpResponseCodeWithinSuccessRange(
                final int responseCode, final boolean isWithinSuccessResponseCodeRange) {
            assertThat(HttpClient.isSuccess(responseCode), is(isWithinSuccessResponseCodeRange));
        }


    }

    public static class HttpClientBuilderTest {

        @Rule
        public final ExpectedException thrown = ExpectedException.none();
        @Rule
        public final MockWebServer server = new MockWebServer();


        @Test
        public void testBuild_attemptToBuildClientWithNegativeConnectionTimeout()
                throws IOException, URISyntaxException {

            thrown.expect(IllegalArgumentException.class);
            thrown.expectMessage("connect timeout can not be negative");

            new HttpClient.Builder(server.mockResponse().getRequestUrl())
                    .path("hyperwallet")
                    .connectTimeout(-1)
                    .build();
        }


        @Test
        public void testBuild_attemptToBuildClientWithNegativeReadTimeout()
                throws IOException, URISyntaxException {

            thrown.expect(IllegalArgumentException.class);
            thrown.expectMessage("read timeout can not be negative");

            new HttpClient.Builder(server.mockResponse().getRequestUrl())
                    .path("hyperwallet")
                    .readTimeout(-1)
                    .build();

        }

    }

}
