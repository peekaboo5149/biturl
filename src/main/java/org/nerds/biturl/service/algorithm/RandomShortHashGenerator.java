package org.nerds.biturl.service.algorithm;

import org.nerds.biturl.service.ShortUrlAlgorithm;

import java.security.SecureRandom;


public class RandomShortHashGenerator implements ShortUrlAlgorithm {

    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int HASH_LENGTH = 6; // Adjust the desired length of the short hash


    @Override
    public String generate(String originalUrl) throws Exception {
        SecureRandom random = new SecureRandom();
        StringBuilder shortHash = new StringBuilder();

        for (int i = 0; i < HASH_LENGTH; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            shortHash.append(randomChar);
        }

        return shortHash.toString();
    }
}
