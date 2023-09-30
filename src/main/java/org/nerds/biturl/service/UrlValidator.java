package org.nerds.biturl.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

@Slf4j
public class UrlValidator {
    // Test if a url is reachable and authentic
    public String validate(String url) {
        try {
            int timeoutInSeconds = 10;
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(timeoutInSeconds * 1000)
                    .setSocketTimeout(timeoutInSeconds * 1000)
                    .build();

            HttpClient httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .build();

            HttpGet httpGet = new HttpGet(url);
            // Execute the HTTP request
            HttpResponse response = httpClient.execute(httpGet);

            // Check if the response status code is in the 2xx range (indicating success)
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                // The URL is reachable and authentic
                return null;
            } else {
                // The URL is not reachable or authentic
                return "Invalid ERROR: StatusCode = " + statusCode;
            }
        } catch (IOException e) {
            // An exception occurred (e.g., network error)
            log.error("Invalid url", e);
            return "Invalid " + e;
        }
    }

}
