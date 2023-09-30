package org.nerds.biturl.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ShortUrlResponse {
    private String originalUrl;
    private String hashedUrl;
    private final LocalDateTime createdAt = LocalDateTime.now();
}
