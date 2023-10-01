package org.nerds.biturl.service.algorithm;

import org.nerds.biturl.service.ShortUrlAlgorithm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5URLShortenerAlgorithm implements ShortUrlAlgorithm {
    /*private static int SHORT_URL_CHAR_SIZE = 7;*/

    public static String convert(String longURL, String userID) throws NoSuchAlgorithmException {
        // Create MD5 Hash
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update((longURL + userID).getBytes());
        byte messageDigest[] = digest.digest();
        // Create Hex String
        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            hexString.append(Integer.toHexString(0xFF & b));
        }
        // TODO: Need to check for collision
        return hexString.toString();
    }

    @Override
    public String generate(String originalUrl, String userID) throws Exception {
        return convert(originalUrl, userID);
    }
}
