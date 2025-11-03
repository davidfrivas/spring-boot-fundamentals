package com.davidfrivas.mouse_colony_app.services;

import com.davidfrivas.mouse_colony_app.entities.Lab;
import com.davidfrivas.mouse_colony_app.entities.LogEntry;
import com.davidfrivas.mouse_colony_app.entities.Mouse;
import com.davidfrivas.mouse_colony_app.entities.User;
import com.davidfrivas.mouse_colony_app.repositories.LabRepository;
import com.davidfrivas.mouse_colony_app.repositories.LogEntryRepository;
import com.davidfrivas.mouse_colony_app.repositories.MouseRepository;
import com.davidfrivas.mouse_colony_app.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class LogEntryService {
    private final LogEntryRepository logEntryRepository;
    private final UserRepository userRepository;
    private final LabRepository labRepository;
    public final MouseRepository mouseRepository;

    // Create a new log entry
    public LogEntry createLogEntry(LogEntry logEntry, Long userId, Long labId) {
        // Validation
        if (logEntry.getContent() == null || logEntry.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Log entry content cannot be empty");
        }

        // Fetch and set user
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        logEntry.setUser(user);

        // Fetch and set lab
        Lab lab = labRepository.findById(labId).orElseThrow(() -> new RuntimeException("Lab not found with id " + labId));

        return logEntryRepository.save(logEntry);
    }

    // Create log entry with mice
    @Transactional
    public LogEntry createLogEntryWithMice(LogEntry logEntry, Long userId, Long labId, Set<Long> mouseIds) {
        // First create a basic log entry
        logEntry = createLogEntry(logEntry, userId, labId);

        // Add mice if provided
        if (mouseIds != null && !mouseIds.isEmpty()) {
            for (Long mouseId : mouseIds) {
                Mouse mouse = mouseRepository.findById(mouseId).orElseThrow(() -> new RuntimeException("Mouse not found with id " + mouseId));
                logEntry.getMice().add(mouse);
            }
            logEntry = logEntryRepository.save(logEntry);
        }
        return logEntry;
    }

    // Find log entry by ID
    public LogEntry findById(Long id) {
        return logEntryRepository.findById(id).orElseThrow(() -> new RuntimeException("Log entry not found with id " + id));
    }

    // Get all log entries
    public List<LogEntry> findAll() {
        return (List<LogEntry>) logEntryRepository.findAll();
    }

    // Update log entry content
    public LogEntry updateContent(Long id, String newContent) {
        LogEntry logEntry = findById(id);
        logEntry.setContent(newContent);
        return logEntryRepository.save(logEntry);
    }

    // Add mouse to log entry
    @Transactional
    public LogEntry addMouseToLog(Long logId, Long mouseId) {
        LogEntry logEntry = findById(logId);
        Mouse mouse = mouseRepository.findById(mouseId).orElseThrow(() -> new RuntimeException("Mouse not found with id " + mouseId));

        logEntry.getMice().add(mouse);
        return logEntryRepository.save(logEntry);
    }

    // Remove mouse from log entry
    @Transactional
    public LogEntry removeMouseFromLog(Long logId, Long mouseId) {
        LogEntry logEntry = findById(logId);

        logEntry.getMice().removeIf(m -> m.getMouseId().equals(mouseId));
        return logEntryRepository.save(logEntry);
    }

    // Delete log entry
    public void deleteLogEntry(Long id) {
        LogEntry logEntry = findById(id);
        logEntryRepository.delete(logEntry);
    }

    // Business logic methods

    // Get log entries by user
    public List<LogEntry> getLogEntriesByUser(Long userId) {
        return findAll().stream()
                .filter(log -> log.getUser().getUserId().equals(userId))
                .toList();
    }

    // Get log entries by lab
    public List<LogEntry> getLogEntriesByLab(Long labId) {
        return findAll().stream()
                .filter(log -> log.getLab().getLabId().equals(labId))
                .toList();
    }

    // Get log entries mentioning a specific mouse
    public List<LogEntry> getLogEntriesForMouse(Long mouseId) {
        return findAll().stream()
                .filter(log -> log.getMice().stream()
                        .anyMatch(m -> m.getMouseId().equals(mouseId)))
                .toList();
    }

    // Get recent log entries for a lab (last N entries)
    public List<LogEntry> getRecentLogEntriesByLab(Long labId, int limit) {
        return getLogEntriesByLab(labId).stream()
                .sorted((a,b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(limit)
                .toList();
    }

    // Search log entries by content keyword
    public List<LogEntry> searchLogEntriesByKeyword(String keyword) {
        return findAll().stream()
                .filter(log -> log.getContent().toLowerCase()
                        .contains(keyword.toLowerCase()))
                .toList();
    }

    // Search log entries by keyword within a lab
    public List<LogEntry> searchLogEntriesByKeywordInLab(String keyword, Long labId) {
        return getLogEntriesByLab(labId).stream()
                .filter(log -> log.getContent().toLowerCase()
                        .contains(keyword.toLowerCase()))
                .toList();
    }

    // Check if log entry exists
    public boolean existsById(Long id) {
        return logEntryRepository.existsById(id);
    }
}
