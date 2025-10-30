package com.davidfrivas.mouse_colony_app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "mouse_request")
public class MouseRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    // Many mouse request can be submitted by one requestor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;

    // Many mouse requests can be submitted for one mouse
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mouse_id", nullable = false)
    private Mouse mouse;

    // Many mouse requests can be submitted from the same lab
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_lab_id", nullable = false)
    private Lab fromLab;

    // Many mouse requests can be submitted to the same lab
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_lab_id", nullable = false)
    private Lab toLab;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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
