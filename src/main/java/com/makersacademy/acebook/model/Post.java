package com.makersacademy.acebook.model;

import jakarta.persistence.*;

import lombok.Data;
import lombok.*;

@Data
@Entity
@Table(name = "POSTS")
@Getter @Setter @NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private int likeCount;

    public Post(String content) {
        this.content = content;
    }
    public Post(Integer likeCount) {this.likeCount = likeCount;}
}
