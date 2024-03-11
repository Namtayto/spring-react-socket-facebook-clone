package com.facebook.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "posts_fb")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String postContent;
    private LocalDateTime postDate;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
