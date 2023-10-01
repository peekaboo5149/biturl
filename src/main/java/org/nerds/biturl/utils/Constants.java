package org.nerds.biturl.utils;

public interface Constants {
    String API_VERSION = "/api/v1";
    String BASE_URL_SHORTENER_ROUTE = API_VERSION + "/shorten";
    String FULL_REDIRECTION_ROUTE = BASE_URL_SHORTENER_ROUTE + "/redirect";
    String JUST_REDIRECTION_ROUTE = "/redirect";
    String SHORT_URL_CACHE_IDENTIFICATION_KEY = "SHORT_URL_KEY";
    String SHORT_URL_CACHE_IDENTIFICATION_VALUE = "SHORT_URL_VALUE";
}
