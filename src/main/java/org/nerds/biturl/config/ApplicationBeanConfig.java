package org.nerds.biturl.config;

import io.github.bucket4j.*;
import io.github.bucket4j.local.SynchronizedBucket;
import lombok.RequiredArgsConstructor;
import org.nerds.biturl.repository.UserRepository;
import org.nerds.biturl.service.ShortUrlAlgorithm;
import org.nerds.biturl.service.UrlValidator;
import org.nerds.biturl.service.algorithm.MD5URLShortenerAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class ApplicationBeanConfig {

    @Autowired
    private final UserRepository userRepository;

    @Bean
    public io.github.bucket4j.Bucket rateLimitBucket() {
        Bandwidth limit = Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1)));
        BucketConfiguration bucketConfig = BucketConfiguration.builder()
                .addLimit(limit)
                .build();
        return new SynchronizedBucket(bucketConfig, MathType.INTEGER_64_BITS, TimeMeter.SYSTEM_MILLISECONDS);
    }

    @Bean
    public <T, V> RedisTemplate<T, V> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<T, V> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public ShortUrlAlgorithm getShortUrlAlgorithmInstance() {
        /*return new RandomShortHashGenerator();*/
        return new MD5URLShortenerAlgorithm();
    }

    @Bean
    public UrlValidator getUrlValidator() {
        return new UrlValidator();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
