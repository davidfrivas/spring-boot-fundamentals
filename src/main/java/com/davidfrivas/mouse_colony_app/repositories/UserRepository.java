package com.davidfrivas.mouse_colony_app.repositories;

import com.davidfrivas.mouse_colony_app.entities.User;
import org.springframework.data.repository.CrudRepository;

// Two generic type parameters
// User: entity for which we need to create a repository
// Long: type of PK in that entity
public interface UserRepository extends CrudRepository<User, Long> {
}
