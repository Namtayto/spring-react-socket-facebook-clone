package com.social.model.user.friend.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipId implements java.io.Serializable {
    private String user;
    private String friend;
}
