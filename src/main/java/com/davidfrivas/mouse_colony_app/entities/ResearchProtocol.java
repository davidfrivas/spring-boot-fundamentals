package com.davidfrivas.mouse_colony_app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "research_protocol")
public class ResearchProtocol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "protocol_id")
    private Long protocolId;

    @Column(name = "protocol_number", nullable = false, unique = true)
    private String protocolNumber;

    @Column(name = "title", nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    // Many research protocols can belong to one lab
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "approval_date")
    private LocalDate approvalDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // One research protocol can have many mice
    @OneToMany(mappedBy = "protocol")
    private Set<Mouse> mice;

    // One research protocol can have many personnel
    @OneToMany(mappedBy = "protocol", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProtocolPersonnel> personnel;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void  onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
