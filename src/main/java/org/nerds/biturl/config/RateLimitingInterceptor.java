package org.nerds.biturl.config;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.nerds.biturl.utils.Constants;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitingInterceptor implements HandlerInterceptor {

    private final Bucket rateLimitBucket;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (rateLimitBucket.tryConsume(1)) {
            return true; // Request accepted
        } else {
            response.setStatus(Constants.RATE_LIMIT_EXCEEDED_STATUS_CODE);
            response.getWriter().write(Constants.RATE_LIMIT_EXCEEDED_MESSAGE);
            return false; // Rate limit exceeded, reject the request
        }
    }
}
