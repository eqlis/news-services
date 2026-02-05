package com.news.producer.scraper;

import com.news.producer.model.Post;

import java.io.IOException;
import java.util.List;

public interface NewsScraper {
  List<Post> fetchPosts() throws IOException;
}
