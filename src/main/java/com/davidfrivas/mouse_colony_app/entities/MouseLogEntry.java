package com.davidfrivas.mouse_colony_app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "mouse_log_entry")
@IdClass(MouseLogEntry.MouseLogEntryId.class)
public class MouseLogEntry {
    @Id
    // Many MouseLogEntries can reference the same Log Entry
    @ManyToOne
    @JoinColumn(name = "log_id")
    private LogEntry logEntry;

    @Id
    // Many MouseLogEntries can reference the same mouse
    @ManyToOne
    @JoinColumn(name = "mouse_id")
    private Mouse mouse;

    public static class MouseLogEntryId implements Serializable {
        private Long logEntry;
        private Long mouse;
    }
}
