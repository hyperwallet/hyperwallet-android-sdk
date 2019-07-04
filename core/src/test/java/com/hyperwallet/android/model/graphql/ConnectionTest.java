package com.hyperwallet.android.model.graphql;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ConnectionTest {

    @Test
    public void testHasNodes_returnsTrue() throws Exception {

        String data = "{ \"nodes\": [ {\"value\": \"1-3 Business days\",  \"country\": \"CA\"  }    ]  }";
        JSONObject processingTime = new JSONObject(data);

        Connection<ProcessingTime> processingTimeConnection = new Connection<>(processingTime, ProcessingTime.class);
        assertThat(Connection.hasNodes(processingTimeConnection), is(true));
    }

    @Test
    public void testHasNodes_returnsFalse() throws Exception {
        assertThat(Connection.hasNodes(null), is(false));

        Connection<ProcessingTime> processingTimeConnection = new Connection<>(
                new JSONObject("{ \"nodes\": [] }"),
                ProcessingTime.class);
        assertThat(Connection.hasNodes(processingTimeConnection), is(false));
    }
}