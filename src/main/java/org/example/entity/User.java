package org.example.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "country_code", length = 2)
    private String countryCode;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT IS NOW()")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Defining ONE-TO-MANY relationship with Orders
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;
}
