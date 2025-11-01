package com.davidfrivas.mouse_colony_app.repositories;

import com.davidfrivas.mouse_colony_app.entities.LogEntry;
import org.springframework.data.repository.CrudRepository;

public interface LogEntryRepository extends CrudRepository<LogEntry, Long> {
}