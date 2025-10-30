package com.davidfrivas.mouse_colony_app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "protocol_personnel")
// Composite PK
@IdClass(ProtocolPersonnel.ProtocolPersonnelId.class)
public class ProtocolPersonnel {
    @Id
    // Many protocol personnel records can point to one research protocol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "protocol_id", nullable = false)
    private ResearchProtocol protocol;

    @Id
    // Many protocol personnel records can reference the same user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "assigned_at", nullable = false, updatable = false)
    private LocalDateTime assignedAt;

    @PrePersist
    protected void onCreate() {
        assignedAt = LocalDateTime.now();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode // Generates equals() and hashCode() methods
    // Composite key class
    public static class ProtocolPersonnelId implements Serializable {
        private Long protocol;
        private Long user;
    }
}
