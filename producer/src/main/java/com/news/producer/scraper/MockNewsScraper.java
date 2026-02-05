package com.news.producer.scraper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.news.producer.model.Post;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@AllArgsConstructor
@ConditionalOnProperty(name = "scraper.type", havingValue = "mock", matchIfMissing = true)
public class MockNewsScraper implements NewsScraper {
  private final ObjectMapper objectMapper;

  @Override
  public List<Post> fetchPosts() throws IOException {
    try (InputStream is = new ClassPathResource("mock/news.json").getInputStream()) {
      return objectMapper.readValue(is, new TypeReference<>() {});
    }
  }
}
