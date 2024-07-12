package com.social.model.post;


import lombok.*;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {

    @PrimaryKey
    private UUID id;

    private String title;
    private String content;
    private UUID userId; // Reference to User entity in MySQL

    @Builder.Default
    private final LocalDateTime publishDate = LocalDateTime.now();

    @CassandraType(type = CassandraType.Name.BLOB)
    private byte[] image;

    private Set<UUID> likes; // Store user IDs who liked the post
    private Set<UUID> comments; // Store comment IDs


    // Additional methods for adding likes and comments
    public void addLike(UUID userId) {
        likes.add(userId);
    }

    public void addComment(UUID comment) {
        comments.add(comment);
    }

}
