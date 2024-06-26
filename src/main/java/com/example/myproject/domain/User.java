package com.example.myproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "wanted_weight")
    private Integer wantedWeight;

    @Column(name = "cal")
    private Integer cal;

    @Column(name = "liked")
    private Integer liked;

    @Column(name = "title")
    private String title;

    @Column(name = "regi_date")
    private Date regiDate;

    @Column(name = "user_image")
    private String userImage;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Board> boards;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Admin adminDetails;

    public User() {
        this.liked = 0;
        this.cal = 10000;
        this.regiDate = new Date();
    }
}
