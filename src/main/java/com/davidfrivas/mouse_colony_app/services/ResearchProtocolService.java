package com.davidfrivas.mouse_colony_app.services;

import com.davidfrivas.mouse_colony_app.entities.Lab;
import com.davidfrivas.mouse_colony_app.entities.ResearchProtocol;
import com.davidfrivas.mouse_colony_app.repositories.LabRepository;
import com.davidfrivas.mouse_colony_app.repositories.ResearchProtocolRepository;
import com.davidfrivas.mouse_colony_app.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ResearchProtocolService {
    private final ResearchProtocolRepository protocolRepository;
    private final LabRepository labRepository;
    private final UserRepository userRepository;

    // Create a new protocol
    public ResearchProtocol createProtocol(ResearchProtocol protocol, Long labId) {
        // Validation
        if (protocol.getProtocolNumber() == null || protocol.getProtocolNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Protocol number cannot be empty");
        }
        if (protocol.getTitle() == null || protocol.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Protocol title cannot be empty");
        }

        // Fetch and set the lab
        Lab lab = labRepository.findById(labId).orElseThrow(() -> new IllegalStateException("Lab not found with id: " + labId));
        protocol.setLab(lab);

        // Set default status if not provided
        if (protocol.getStatus() == null)
            protocol.setStatus("PENDING");

        return protocolRepository.save(protocol);
    }

    // Find prptocol by ID
    public ResearchProtocol findById(Long id) {
        return protocolRepository.findById(id).orElseThrow(() -> new RuntimeException("Protocol not found with id: " + id));
    }

    // Get all protocols
    public List<ResearchProtocol> findAll() {
        return (List<ResearchProtocol>) protocolRepository.findAll();
    }

    // Update existing protocol
    public ResearchProtocol updateProtocol(Long id, ResearchProtocol updatedProtocol) {
        ResearchProtocol existingProtocol = findById(id);

        existingProtocol.setProtocolNumber(updatedProtocol.getProtocolNumber());
        existingProtocol.setTitle(updatedProtocol.getTitle());
        existingProtocol.setStatus(updatedProtocol.getStatus());
        existingProtocol.setDescription(updatedProtocol.getDescription());
        existingProtocol.setApprovalDate(updatedProtocol.getApprovalDate());
        existingProtocol.setExpirationDate(updatedProtocol.getExpirationDate());

        return protocolRepository.save(existingProtocol);
    }
}
