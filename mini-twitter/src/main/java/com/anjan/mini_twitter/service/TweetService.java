package com.anjan.mini_twitter.service;

import com.anjan.mini_twitter.dto.TweetDTO;
import com.anjan.mini_twitter.entity.Tweet;
import com.anjan.mini_twitter.entity.User;
import com.anjan.mini_twitter.repository.TweetRepository;
import com.anjan.mini_twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TweetService {

    @Autowired private TweetRepository tweetRepository;
    @Autowired private UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public TweetDTO createTweet(String content) {
        User user = getCurrentUser();
        Tweet tweet = new Tweet();
        tweet.setContent(content);
        tweet.setAuthor(user);
        return convertToDTO(tweetRepository.save(tweet));
    }

    public List<TweetDTO> getTimeline() {
        User user = getCurrentUser();
        List<Long> followingIds = user.getFollowing()
                .stream().map(User::getId)
                .collect(Collectors.toList());
        followingIds.add(user.getId());
        return tweetRepository.findTimelineByUserIds(followingIds)
                .stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TweetDTO> getUserTweets(Long userId) {
        return tweetRepository.findByAuthorIdOrderByCreatedAtDesc(userId)
                .stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TweetDTO likeTweet(Long tweetId) {
        User user = getCurrentUser();
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet not found"));
        if (tweet.getLikes().contains(user)) {
            tweet.getLikes().remove(user);
        } else {
            tweet.getLikes().add(user);
        }
        return convertToDTO(tweetRepository.save(tweet));
    }

    public void deleteTweet(Long tweetId) {
        User user = getCurrentUser();
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new RuntimeException("Tweet not found"));
        if (!tweet.getAuthor().getId().equals(user.getId()))
            throw new RuntimeException("Not authorized");
        tweetRepository.deleteById(tweetId);
    }

    private TweetDTO convertToDTO(Tweet tweet) {
        return new TweetDTO(
                tweet.getId(),
                tweet.getContent(),
                tweet.getAuthor().getUsername(),
                tweet.getCreatedAt(),
                tweet.getLikes().size()
        );
    }
}