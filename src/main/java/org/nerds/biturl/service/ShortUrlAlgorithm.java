package org.nerds.biturl.service;

public interface ShortUrlAlgorithm {

    String generate(String originalUrl, String userId) throws Exception;
}
