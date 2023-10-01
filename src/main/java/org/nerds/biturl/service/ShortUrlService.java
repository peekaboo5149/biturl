package org.nerds.biturl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nerds.biturl.expection.InvalidUrlException;
import org.nerds.biturl.model.ShortUrl;
import org.nerds.biturl.model.User;
import org.nerds.biturl.repository.ShortUrlRepository;
import org.nerds.biturl.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShortUrlService {

    @Autowired
    private final ShortUrlRepository repository;
    @Autowired
    private final ShortUrlAlgorithm algorithm;
    @Autowired
    private final UrlValidator urlValidator;
    @Autowired
    private final JwtService jwtService;

    public String createShortUrl(String originalUrl, String token) {
        if (originalUrl == null) {
            throw new InvalidUrlException("NULL", "Url cannot be null");
        }
        String baseUrl = Utility.getRedirectionUrl() + "/";

        // TODO : Check if url exist in cache
        // If not then check in persistent database
        final User user = jwtService.getUserDetails(token);
        String hashedUrl = repository.getHashedCodeByOriginalUrl(originalUrl, user.getId());
        if (Utility.isStringNonEmpty(hashedUrl)) {
            log.info("Url found in database");
            return baseUrl + hashedUrl;
        }
        //  Try to check if the url is reachable and authentic
        String error = urlValidator.validate(originalUrl);
        if (Utility.isStringNonEmpty(error)) {
            throw new InvalidUrlException(originalUrl, error);
        }
        // If still url do not exist hash the url and return a hashed url
        try {
            String generatedHash = algorithm.generate(originalUrl, user.getId());

            ShortUrl shortUrl = ShortUrl.builder()
                    .originalUrl(originalUrl)
                    .hashedCode(generatedHash)
                    .user(user)
                    .build();
            repository.save(shortUrl);
            log.info("Url saved in persistent database successfully");
            return baseUrl + generatedHash;
        } catch (Exception e) {
            log.error("Error Creating a new short url ", e);
            throw new RuntimeException(e);
        }
    }

    public String getOriginalUrlFor(String hashCode) {
        // TODO: GET FROM CACHE
        String originalUrl = repository.getOriginalUrl(hashCode);
        if (Utility.isStringEmpty(originalUrl)) {
            return null;
        }
        final String error = urlValidator.validate(originalUrl);
        if (Utility.isStringNonEmpty(error)) {
            throw new InvalidUrlException(originalUrl, error);
        } else {
            return originalUrl;
        }
    }
}
