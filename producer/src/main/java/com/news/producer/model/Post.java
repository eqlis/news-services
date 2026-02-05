package com.news.producer.model;

import java.time.LocalDateTime;

public record Post(
  String title
  , String summary
  , LocalDateTime date
  , String imageSrc
) {
}
