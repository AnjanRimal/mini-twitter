package com.anjan.mini_twitter.repository;

import com.anjan.mini_twitter.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    @Query("SELECT t FROM Tweet t WHERE t.author.id IN :userIds ORDER BY t.createdAt DESC")
    List<Tweet> findTimelineByUserIds(List<Long> userIds);
}
