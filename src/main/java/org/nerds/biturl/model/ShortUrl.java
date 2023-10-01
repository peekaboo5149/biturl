package org.nerds.biturl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "bt_urls")
public class ShortUrl implements Serializable {

    @Serial
    private static final long serialVersionUID = 987654322L;

    @Id
    @Column(name = "bt_urls_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "bt_urls_orig_url")
    private String originalUrl;
    @Column(name = "bt_urls_hash_code")
    private String hashedCode;

    @ManyToOne // This establishes a many-to-one relationship with User
    @JoinColumn(name = "bt_urls_created_by") // This is the foreign key column in the short_urls table
    private User user;
}
