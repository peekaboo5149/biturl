package org.nerds.biturl.repository.dao;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.nerds.biturl.model.ShortUrl;
import org.nerds.biturl.utils.Constants;
import org.nerds.biturl.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ShortUrlCacheDao {

    private final HashOperations<String, String, String> hashOperations;

    public ShortUrlCacheDao(@Autowired RedisTemplate<String, String> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void storeHashedUrl(ShortUrl shortUrl) {
        try {
            String originalUrl = shortUrl.getOriginalUrl();
            String hashedCode = shortUrl.getHashedCode();
            String userID = shortUrl.getUser().getId();
            // Redis store structure
            // key : hashedCode
            // value : userId-originalUrl
            String value = userID.concat("-[").concat(originalUrl).concat("]");
            hashOperations.put(Constants.SHORT_URL_CACHE_IDENTIFICATION_KEY, hashedCode, value);
            hashOperations.put(Constants.SHORT_URL_CACHE_IDENTIFICATION_VALUE, value, hashedCode);
        } catch (Exception e) {
            log.warn("cache error = ", e);
        }
    }

    // Assuming all hashCode are unique
    public String getOriginalUrl(@NonNull String hashCode) {
        try {
            String cachedValue = hashOperations.get(Constants.SHORT_URL_CACHE_IDENTIFICATION_KEY, hashCode);
            if (Utility.isStringNonEmpty(cachedValue) && cachedValue.contains("-")) {
                cachedValue = cachedValue.split("-\\[")[1];
                return cachedValue.substring(0, cachedValue.length() - 1);
            } else return null;
        } catch (Throwable e) {
            log.warn("cache error = ", e);
            return null;
        }
    }

    public String getHashedUrl(@NonNull String originalUrl, @NonNull String userID) {
        try {
            String uniqueValue = userID.concat("-").concat(originalUrl);
            String result = hashOperations.get(Constants.SHORT_URL_CACHE_IDENTIFICATION_VALUE, uniqueValue);
            if (Utility.isStringNonEmpty(result)) return result;
            else return null;
        } catch (Throwable e) {
            log.warn("cache error = ", e);
            return null;
        }
    }
}
