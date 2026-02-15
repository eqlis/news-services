package com.news.graphql.service;

import com.news.graphql.model.Post;
import com.news.graphql.repository.PostRepository;
import com.redis.testcontainers.RedisContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class PostServiceIT {
  @MockBean
  private PostRepository repository;
  @Autowired
  private PostService service;
  @Autowired
  private CacheManager cacheManager;

  @Container
  static RedisContainer redis = new RedisContainer("redis:7.2-rc-alpine");

  @DynamicPropertySource
  static void redisProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.redis.host", redis::getHost);
    registry.add("spring.redis.port", redis::getFirstMappedPort);
  }

  @BeforeEach
  void clearCache() {
    Objects.requireNonNull(cacheManager.getCache("postsCache")).clear();
  }

  @Test
  void getPostsIsCacheable() {
    List<Post> posts = List.of(Post.builder().id(-1L).build(), Post.builder().id(-2L).build());
    when(repository.findAll()).thenReturn(posts);

    List<Post> firstResult = service.getPosts();
    List<Post> secondResult = service.getPosts();

    Assertions.assertThat(firstResult).usingRecursiveFieldByFieldElementComparator().isEqualTo(posts);
    Assertions.assertThat(secondResult).usingRecursiveFieldByFieldElementComparator().isEqualTo(posts);
    verify(repository, times(1)).findAll();
  }
}
