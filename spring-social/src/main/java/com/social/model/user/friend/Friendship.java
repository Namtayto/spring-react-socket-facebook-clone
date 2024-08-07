package com.social.model.user.friend;

import com.social.model.user.User;
import com.social.model.user.friend.utils.FriendshipId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(FriendshipId.class)
@Table(
        name = "friendships_ntt",
        indexes = {
                @Index(name = "idx_friendship_user_friend", columnList = "user_id, friend_id")
        }
)

public class Friendship {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend;

}
