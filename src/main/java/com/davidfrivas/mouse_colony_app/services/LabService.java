package com.davidfrivas.mouse_colony_app.services;

import com.davidfrivas.mouse_colony_app.entities.Lab;
import com.davidfrivas.mouse_colony_app.repositories.LabRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LabService {
    private final LabRepository labRepository;

    // Create a new lab
    public Lab createLab(Lab lab) {
        // Validate
        if (lab.getName() == null || lab.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Lab name cannot be empty");
        }
        if (lab.getContactEmail() == null || !lab.getContactEmail().contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }

        return labRepository.save(lab); // Save lab to repository
    }

    // Find lab by ID
    public Lab findById(Long id) {
        return labRepository.findById(id).orElseThrow(() -> new RuntimeException("Lab not found with id: " + id));
    }

    // Get all labs
    public List<Lab> findAll() {
        return (List<Lab>) labRepository.findAll();
    }

    // Update existing lab
    public Lab updateLab(Long id, Lab updatedLab) {
        Lab existingLab = findById(id);

        existingLab.setName(updatedLab.getName());
        existingLab.setContactEmail(updatedLab.getContactEmail());
        existingLab.setDepartment(updatedLab.getDepartment());
        existingLab.setDescription(updatedLab.getDescription());
        existingLab.setAddress(updatedLab.getAddress());
        existingLab.setInstitution(updatedLab.getInstitution());

        return labRepository.save(existingLab); // Save existing lab
    }

    // Delete lab
    public void deleteLab(Long id) {
        Lab lab = findById(id);
        labRepository.delete(lab);
    }

    // Check if lab exists
    public boolean existsById(Long id) {
        return labRepository.existsById(id);
    }
}
