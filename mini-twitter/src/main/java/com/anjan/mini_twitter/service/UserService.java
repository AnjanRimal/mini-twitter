package com.anjan.mini_twitter.service;

import com.anjan.mini_twitter.dto.UserDTO;
import com.anjan.mini_twitter.entity.User;
import com.anjan.mini_twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDTO getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    public UserDTO followUser(Long targetId) {
        User current = getCurrentUser();
        User target = userRepository.findById(targetId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (current.getId().equals(targetId))
            throw new RuntimeException("Cannot follow yourself");
        if (!current.getFollowing().contains(target)) {
            current.getFollowing().add(target);
            userRepository.save(current);
        }
        return convertToDTO(target);
    }

    public UserDTO unfollowUser(Long targetId) {
        User current = getCurrentUser();
        User target = userRepository.findById(targetId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        current.getFollowing().remove(target);
        userRepository.save(current);
        return convertToDTO(target);
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getBio(),
                user.getFollowers().size(),
                user.getFollowing().size()
        );
    }
}