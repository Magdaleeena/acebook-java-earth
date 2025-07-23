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
    private String username;

    public Post(String content, String username) {
        this.content = content;
        this.username = username;
    }

}
