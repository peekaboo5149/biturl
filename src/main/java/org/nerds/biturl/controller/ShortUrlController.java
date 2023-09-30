package org.nerds.biturl.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nerds.biturl.dto.CreateShortUrlDto;
import org.nerds.biturl.dto.ShortUrlResponse;
import org.nerds.biturl.expection.UrlNotFoundException;
import org.nerds.biturl.service.ShortUrlService;
import org.nerds.biturl.utils.Constants;
import org.nerds.biturl.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(Constants.BASE_URL_SHORTENER_ROUTE)
@RequiredArgsConstructor
@Slf4j
public class ShortUrlController {

    @Autowired
    private final ShortUrlService shortUrlService;

    @PostMapping
    public ResponseEntity<ShortUrlResponse> createShortUrl(@RequestBody @Valid CreateShortUrlDto requestBody) {
        log.info("Create Short Url Controller \uD83C\uDFC2");
        long start = System.currentTimeMillis();
        try {
            final String originalUrl = requestBody.getUrl();
            String hashedUrl = shortUrlService.createShortUrl(originalUrl);
            return ResponseEntity.ok(ShortUrlResponse.builder()
                    .originalUrl(originalUrl)
                    .hashedUrl(hashedUrl)
                    .build());
        } finally {
            log.info(String.format("Time taken to complete POST /shorten = %d ms", (System.currentTimeMillis() - start)));
        }
    }

    @GetMapping(Constants.JUST_REDIRECTION_ROUTE + "/{hashCode}")
    public ResponseEntity<Void> redirectUrls(@PathVariable("hashCode") String hashCode) {
        long start = System.currentTimeMillis();
        try {
            final String originalUrl = shortUrlService.getOriginalUrlFor(hashCode);
            log.debug("Found url = " + originalUrl);
            if (Utility.isStringEmpty(originalUrl)) {
                throw new UrlNotFoundException("Could not find any url for = " + hashCode);
            } else {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(originalUrl))
                        .build();
            }
        } finally {
            log.info("Time taken to REDIRECT " + Constants.FULL_REDIRECTION_ROUTE + "/" + hashCode + " = " + (System.currentTimeMillis() - start) + " ms");
        }
    }

}
