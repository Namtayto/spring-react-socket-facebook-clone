package com.social.repository;

import com.social.model.user.User;
import com.social.model.user.friend.Friendship;
import com.social.model.user.friend.utils.FriendshipId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {
    List<Friendship> findByUser(User user);
}