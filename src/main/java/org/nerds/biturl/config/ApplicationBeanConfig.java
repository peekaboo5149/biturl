package org.nerds.biturl.config;

import org.nerds.biturl.service.ShortUrlAlgorithm;
import org.nerds.biturl.service.UrlValidator;
import org.nerds.biturl.service.algorithm.MD5URLShortenerAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfig {

    @Bean
    public ShortUrlAlgorithm getShortUrlAlgorithmInstance() {
        /*return new RandomShortHashGenerator();*/
        return new MD5URLShortenerAlgorithm();
    }

    @Bean
    public UrlValidator getUrlValidator() {
        return new UrlValidator();
    }

}
