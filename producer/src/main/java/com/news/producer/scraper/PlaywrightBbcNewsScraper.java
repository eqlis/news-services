package com.news.producer.scraper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.news.producer.model.Post;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "scraper.type", havingValue = "playwright")
public class PlaywrightBbcNewsScraper implements NewsScraper {
  private final ObjectMapper objectMapper;
  private final Browser browser;

  public PlaywrightBbcNewsScraper(ObjectMapper objectMapper, PlaywrightBrowserProvider provider) {
    this.objectMapper = objectMapper;
    this.browser = provider.getBrowser();
  }

  @Override
  public List<Post> fetchPosts() throws JsonProcessingException {
    Page page = browser.newPage();
    page.navigate("https://www.bbc.com/news");
    page.waitForLoadState(LoadState.DOMCONTENTLOADED);

    String nextDataJson = page.locator("script#__NEXT_DATA__").textContent();

    if (nextDataJson == null || nextDataJson.isBlank()) {
      throw new IllegalStateException("__NEXT_DATA__ not found");
    }

    JsonNode root = objectMapper.readTree(nextDataJson);
    JsonNode sections = root
      .path("props")
      .path("pageProps")
      .path("page")
      .path("@\"news\",")
      .path("sections");

    List<Post> posts = new ArrayList<>();

    for (JsonNode section : sections) {
      for (JsonNode item : section.path("content")) {

        JsonNode metadata = item.path("metadata");

        if (!"article".equals(metadata.path("contentType").asText())) {
          continue;
        }
        if (!"news".equals(metadata.path("subtype").asText())) {
          continue;
        }

        String title = item.path("title").asText(null);
        String summary = item.path("description").asText(null);

        long lastUpdatedMillis = metadata.path("lastUpdated").asLong(0);
        LocalDateTime date = lastUpdatedMillis > 0
          ? Instant.ofEpochMilli(lastUpdatedMillis)
          .atZone(ZoneId.systemDefault())
          .toLocalDateTime()
          : null;

        String imageSrc = item
          .path("image")
          .path("model")
          .path("blocks")
          .path("src")
          .asText(null);

        posts.add(new Post(title, summary, date, imageSrc));
      }
    }

    return posts;
  }
}
