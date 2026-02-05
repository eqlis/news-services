package com.news.consumer.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  private String title;
  private String summary;
  private LocalDateTime date;
  private String imageSrc;
}
