package com.example.myproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "admin")
@NoArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin")
    private boolean admin;

    @Column(name = "username")
    private String username;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Admin(User user) {
        this.user = user;
        this.admin = false;
        this.username = user.getUsername();
    }
}
