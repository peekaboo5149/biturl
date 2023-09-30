package org.nerds.biturl.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "bt_urls")
public class ShortUrl {
    @Id
    @Column(name = "bt_urls_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "bt_urls_orig_url")
    private String originalUrl;
    @Column(name = "bt_urls_hash_code")
    private String hashedCode;
}
