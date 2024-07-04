package com.example.myproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "board")
@Getter
@Setter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    private User user;

    @Column(name = "password")
    private String password;

    @Column(name = "content")
    private String content;

    @Column(name = "tag")
    private String tag;

    @Column(name = "cal")
    private int cal;

    @Column(name = "comment")
    private String comment;

    @ElementCollection
    @CollectionTable(name = "liked_users", joinColumns = @JoinColumn(name = "board_id"))
    @Column(name = "username")
    private Set<String> likedUsers = new HashSet<>();

    @Column(name = "liked")
    private int liked;

    @Column(name = "write_time")
    private Date writeTime;

    @Column(name = "temporary")
    private boolean temporary;

    public Board() {
        this.writeTime = new Date();
        this.temporary = false;
    }
    public void addLikedUser(String username) {
        likedUsers.add(username);
    }
    public void removeLikedUser(String username) {
        likedUsers.remove(username);
    }
    public int countLikedUsers() {
        return likedUsers.size();
    }

    @Entity
    @Getter
    @Setter
    @Table(name = "image")
    public static class Image {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String fileName;
        private String filePath;

        @OneToOne(mappedBy = "profileImage")
        private User user;

    }
}
