package com.social.model.user.friend;

import com.social.model.user.User;
import com.social.model.user.friend.utils.FriendRequestId;
import com.social.model.user.friend.utils.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "friend_requests_ntt")
@IdClass(FriendRequestId.class)
public class FriendRequest {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDateTime createdAt;
}
