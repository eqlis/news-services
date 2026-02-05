package com.news.graphql.repository;

import com.news.graphql.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
  Optional<Like> findByPostIdAndUserId(long postId, long userId);
}
