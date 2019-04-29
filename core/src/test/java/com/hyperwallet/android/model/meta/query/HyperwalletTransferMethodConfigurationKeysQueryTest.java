package com.hyperwallet.android.model.meta.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static com.hyperwallet.android.model.meta.query.HyperwalletTransferMethodConfigurationKeysQuery.GRAPH_QL_REQUEST_CONFIGURATION;

import org.junit.Test;

public class HyperwalletTransferMethodConfigurationKeysQueryTest {

    @Test
    public void testToQuery_returnsQuery() {
        HyperwalletTransferMethodConfigurationKeysQuery keysQuery =
                new HyperwalletTransferMethodConfigurationKeysQuery();

        keysQuery.setLimit(50L);
        String token = String.valueOf("token".hashCode());
        String query = keysQuery.toQuery(token);

        String requestConfigurationFormatted = String.format(GRAPH_QL_REQUEST_CONFIGURATION, token, 50L);

        assertThat(keysQuery.getLimit(), is(50L));
        assertThat(query, is(requestConfigurationFormatted));
    }
}
