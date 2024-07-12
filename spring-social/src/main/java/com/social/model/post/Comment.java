package com.social.model.post;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    @PrimaryKey
    private UUID id;

    private UUID userId; // User who made the comment
    private String content;

    @Builder.Default
    private final LocalDateTime publishComment = LocalDateTime.now();


}

