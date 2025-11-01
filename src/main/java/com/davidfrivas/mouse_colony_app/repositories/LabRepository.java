package com.davidfrivas.mouse_colony_app.repositories;

import com.davidfrivas.mouse_colony_app.entities.Lab;
import org.springframework.data.repository.CrudRepository;

public interface LabRepository extends CrudRepository<Lab, Long> {
}