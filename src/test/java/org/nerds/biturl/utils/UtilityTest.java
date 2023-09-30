package org.nerds.biturl.utils;

import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilityTest {

    @Test
    public void testRedirectionUrl() {
        final String redirectionUrl = Utility.getRedirectionUrl();
        System.out.println("redirectionUrl = " + redirectionUrl);
        assertEquals(redirectionUrl, "http://localhost:9000/shorten/redirect");
    }

}