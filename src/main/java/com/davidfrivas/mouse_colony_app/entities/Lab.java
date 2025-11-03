package com.davidfrivas.mouse_colony_app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "lab")
public class Lab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lab_id")
    private Long labId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    @Column(name = "institution", nullable = false)
    private String institution;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // One lab can have many users
    @OneToMany(mappedBy = "lab")
    @Builder.Default
    private Set<User> users = new HashSet<>();

    // One lab can have many protocols
    @OneToMany(mappedBy = "lab")
    @Builder.Default
    private Set<ResearchProtocol> protocols = new HashSet<>();

    // One lab can have many mice
    @OneToMany(mappedBy = "lab")
    @Builder.Default
    private Set<Mouse> mice = new HashSet<>();

    // One lab can have many litters
    @OneToMany(mappedBy = "lab")
    @Builder.Default
    private Set<Litter> litters = new HashSet<>();

    @PrePersist // Executes before a new entity is inserted into DB
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate // Executes before an existing entity is updated in DB
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
