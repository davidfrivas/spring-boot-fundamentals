package com.davidfrivas.mouse_colony_app.repositories;

import com.davidfrivas.mouse_colony_app.entities.MouseRequest;
import org.springframework.data.repository.CrudRepository;

public interface MouseRequestRepository extends CrudRepository<MouseRequest, Long> {
}