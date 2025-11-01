package com.davidfrivas.mouse_colony_app.repositories;

import com.davidfrivas.mouse_colony_app.entities.Mouse;
import org.springframework.data.repository.CrudRepository;

public interface MouseRepository extends CrudRepository<Mouse, Long> {
}