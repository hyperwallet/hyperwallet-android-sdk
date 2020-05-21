package com.hyperwallet.android;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;


public class PathFormatterTest {

    @Test
    public void testFormat_pathWithoutArguments() {

        String arguments = null;
        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-cards", arguments);
        String formattedPath = pathFormatter.format("test-user-token");
        assertThat(formattedPath, is("users/test-user-token/bank-cards"));
    }


    @Test
    public void testFormat_pathWithArguments() {

        PathFormatter pathFormatter = new PathFormatter("users/{0}/bank-accounts/{1}/status-transitions", "trm-fake" +
                "-token");
        String formattedPath = pathFormatter.format("test-user-token");
        assertThat(formattedPath, is("users/test-user-token/bank-accounts/trm-fake-token/status-transitions"));
    }

}