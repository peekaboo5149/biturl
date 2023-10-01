package org.nerds.biturl.repository;

import org.nerds.biturl.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, String> {
    @Query(value = """
            SELECT bt_urls_hash_code FROM bt_urls WHERE bt_urls_orig_url = ?1 AND bt_urls_created_by = ?2
            """, nativeQuery = true)
    String getHashedCodeByOriginalUrl(String originalUrl, String userId);

    @Query(value = """
            SELECT bt_urls_orig_url FROM bt_urls
            WHERE bt_urls_hash_code = ?1
            """, nativeQuery = true)
    String getOriginalUrl(String hashCode);
}
