package com.news.graphql.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post implements Serializable {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private long id;
  private String title;
  private String summary;
  private LocalDateTime date;
  private String imageSrc;
  private int likes;
  private int dislikes;

  @OneToMany(mappedBy = "post", fetch = EAGER)
  private Set<Category> categories;

  @OneToMany(mappedBy = "post", fetch = EAGER)
  private List<Comment> comments;

  public void addCategory(Category category) {
    categories.add(category);
  }

  public void addComment(Comment comment) {
    comments.add(comment);
  }
}
