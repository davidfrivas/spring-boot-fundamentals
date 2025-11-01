package com.davidfrivas.mouse_colony_app.repositories;

import com.davidfrivas.mouse_colony_app.entities.Litter;
import org.springframework.data.repository.CrudRepository;

public interface LitterRepository extends CrudRepository<Litter, Long> {
}