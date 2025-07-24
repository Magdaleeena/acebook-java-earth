package com.makersacademy.acebook.model;

import jakarta.persistence.*;

import lombok.Data;
import lombok.*;

import java.util.ArrayList;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Post(String content, User user) {
        this.content = content;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getDisplay_name() {
        return user != null ? user.getDisplay_name() : null;
    }
}
