package com.davidfrivas.mouse_colony_app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "litter")
public class Litter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "litter_id")
    private Long litterId;

    // Many litters can belong to one lab
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;

    // Many litters can share the same mother
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mother_id", nullable = false)
    private Mouse mother;

    // Many litters can share the same father
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "father_id", nullable = false)
    private Mouse father;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    // Many litters can share the same protocol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "protocol_id", nullable = false)
    private ResearchProtocol protocol;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // One litter can have many mice
    @OneToMany(mappedBy = "litter")
    @Builder.Default
    private Set<Mouse> mice = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
