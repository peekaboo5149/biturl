package org.nerds.biturl.service.algorithm;

import org.nerds.biturl.service.ShortUrlAlgorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5URLShortenerAlgorithm implements ShortUrlAlgorithm {
    /*private static int SHORT_URL_CHAR_SIZE = 7;*/

    public static String convert(String longURL) throws NoSuchAlgorithmException {
        // Create MD5 Hash
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(longURL.getBytes());
        byte messageDigest[] = digest.digest();
        // Create Hex String
        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            hexString.append(Integer.toHexString(0xFF & b));
        }
        return hexString.toString();

    }

    @Override
    public String generate(String originalUrl) throws Exception {
        return convert(originalUrl);
    }

    /*
     * public String generateRandomShortUrl(String longURL) {
     * return convert(longURL);
     * int numberOfCharsInHash = hash.length();
     * int counter = 0;
     * while (counter < numberOfCharsInHash - SHORT_URL_CHAR_SIZE) {
     * if (!DB.exists(hash.substring(counter, counter + SHORT_URL_CHAR_SIZE))) {
     * return hash.substring(counter, counter + SHORT_URL_CHAR_SIZE);
     * }
     * counter++;
     * }
     * }
     */
}
