package com.anjan.mini_twitter.controller;

import com.anjan.mini_twitter.dto.TweetDTO;
import com.anjan.mini_twitter.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tweets")
public class TweetController {

    @Autowired
    private TweetService tweetService;

    @PostMapping
    public ResponseEntity<TweetDTO> createTweet(
            @RequestBody Map<String, String> body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tweetService.createTweet(body.get("content")));
    }

    @GetMapping("/timeline")
    public ResponseEntity<List<TweetDTO>> getTimeline() {
        return ResponseEntity.ok(tweetService.getTimeline());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TweetDTO>> getUserTweets(
            @PathVariable Long userId) {
        return ResponseEntity.ok(tweetService.getUserTweets(userId));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<TweetDTO> likeTweet(@PathVariable Long id) {
        return ResponseEntity.ok(tweetService.likeTweet(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable Long id) {
        tweetService.deleteTweet(id);
        return ResponseEntity.noContent().build();
    }
}