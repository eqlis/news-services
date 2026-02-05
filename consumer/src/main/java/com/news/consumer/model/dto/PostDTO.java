package com.news.consumer.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.news.consumer.model.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
  private String title;
  private String summary;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime date;
  private String imageSrc;

  public Post toPost() {
    return Post.builder()
      .title(title)
      .summary(summary)
      .date(date)
      .imageSrc(imageSrc)
      .build();
  }
}
