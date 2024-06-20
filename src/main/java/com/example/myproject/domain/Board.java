package com.example.myproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
    @JoinColumn (name = "username")
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

    @Column(name = "liked")
    private int liked;

    @Column(name = "write_time")
    private Date writeTime;

    public Board() {
        this.writeTime = new Date();
    }
}
