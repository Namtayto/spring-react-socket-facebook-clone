package com.social.repository;

import com.social.model.user.User;
import com.social.model.user.friend.FriendRequest;
import com.social.model.user.friend.utils.FriendRequestId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, FriendRequestId> {

    Optional<FriendRequest> findByUserAndFriend(User user, User friend);

    @Transactional
    @Modifying
    @Query("DELETE FROM FriendRequest fr WHERE (fr.user = :user AND fr.friend = :friend) OR (fr.user = :friend AND fr.friend = :user)")
    void deleteFriendRequest(@Param("user") User user, @Param("friend") User friend);

    List<FriendRequest> findByFriend(User friend);
}
