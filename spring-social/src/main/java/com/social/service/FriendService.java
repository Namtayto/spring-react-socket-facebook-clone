package com.social.service;

import com.social.model.user.User;
import com.social.model.user.friend.FriendRequest;
import com.social.model.user.friend.Friendship;
import com.social.model.user.friend.utils.RequestStatus;
import com.social.repository.FriendRequestRepository;
import com.social.repository.FriendshipRepository;
import com.social.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;

    /**
     * Add a new friend request from the given user to the specified friend.
     *
     * @param username       the username of User sending the friend request
     * @param friendUsername the username of User receiving your friend request
     * @throws IllegalArgumentException if the friend request already exists
     */

    @Transactional
    public void addFriendRequest(String username, String friendUsername) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Friend not found"));

        if (isSender(user, friend) || isReceiver(user, friend)) {
            throw new IllegalArgumentException("Friend request already exists.");
        }

        FriendRequest friendRequest = FriendRequest.builder()
                .user(user)
                .friend(friend)
                .status(RequestStatus.PENDING)
                .build();

        friendRequestRepository.save(friendRequest);
    }

    /**
     * Retrieve all pending friend requests received by the given user.
     *
     * @param username the username of User for whom to retrieve pending friend requests
     * @return a list of pending friend requests
     */
    public List<User> retrievePendingFriendRequests(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<FriendRequest> friendRequests = friendRequestRepository.findByFriend(user);

        List<User> requestUsers = friendRequests.stream()
                .map(FriendRequest::getUser)
                .collect(Collectors.toList());

        return requestUsers;
    }


    /**
     * Retrieve the list of friends for the given user.
     *
     * @param username the username of User for whom to retrieve the friend list
     * @return a list of users who are friends with the given user
     */
    public List<User> retrieveFriendList(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Friendship> friendships = friendshipRepository.findByUser(user);
        List<User> friends = friendships.stream()
                .map(Friendship::getFriend)
                .collect(Collectors.toList());

        return friends;
    }

    /**
     * Approve a pending friend request received by the given user from the specified friend.
     * This method also creates a bi-directional friendship between the two users.
     *
     * @param username       of the User approving the friend request
     * @param friendUsername the username of User who sent the friend request
     * @throws IllegalArgumentException if the friend request is not found or already processed
     */
    @Transactional
    public void approveFriendRequest(String username, String friendUsername) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Friend not found"));

        if (!isReceiver(user, friend)) {
            throw new IllegalArgumentException("Friend request not found or already processed.");
        }

        if (isSender(user, friend)) {
            throw new IllegalArgumentException("You can't approve friend request if you're a sender");
        }
        friendRequestRepository.deleteFriendRequest(user, friend);

        Friendship friendship1 = Friendship.builder()
                .user(user)
                .friend(friend)
                .build();

        Friendship friendship2 = Friendship.builder()
                .user(friend)
                .friend(user)
                .build();

        friendshipRepository.save(friendship1);
        friendshipRepository.save(friendship2);
    }

    /**
     * Reject a pending friend request received by the given user from the specified friend.
     *
     * @param username       of the user rejecting the friend request
     * @param friendUsername the id of user who sent the friend request
     * @throws IllegalArgumentException if the friend request is not found or already processed
     */
    @Transactional
    public void rejectFriendRequest(String username, String friendUsername) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new IllegalArgumentException("Friend not found"));

        if (!isReceiver(user, friend)) {
            throw new IllegalArgumentException("Friend request not found or already processed.");
        }

        friendRequestRepository.deleteFriendRequest(user, friend);

    }

    public boolean isSender(User sender, User receiver) {
        return friendRequestRepository.findByUserAndFriend(sender, receiver).isPresent();
    }

    public boolean isReceiver(User sender, User receiver) {
        return friendRequestRepository.findByUserAndFriend(receiver, sender).isPresent();
    }
}

