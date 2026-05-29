package com.anjan.mini_twitter.controller;

import com.anjan.mini_twitter.dto.UserDTO;
import com.anjan.mini_twitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getProfile(
            @PathVariable String username) {
        return ResponseEntity.ok(userService.getProfile(username));
    }

    @PostMapping("/{id}/follow")
    public ResponseEntity<UserDTO> followUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.followUser(id));
    }

    @PostMapping("/{id}/unfollow")
    public ResponseEntity<UserDTO> unfollowUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.unfollowUser(id));
    }
}