package com.davidfrivas.store.entities;

import jakarta.persistence.*;
import lombok.*;

@Setter // Lombok setter
@Getter // Lombok getter
// Lombok constructors
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity // JPA Entity
@Table(name = "users") // Name of DB table
public class User {
    @Id // Primary key (PK)
    // Auto-generate value for PK
    // IDENTITY: DB auto-increment feature generates the ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

}
