package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.*;
import java.util.List;

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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public Post(String content) {
        this.content = content;
    }
    public Post(Integer likeCount) {this.likeCount = likeCount;}
}
