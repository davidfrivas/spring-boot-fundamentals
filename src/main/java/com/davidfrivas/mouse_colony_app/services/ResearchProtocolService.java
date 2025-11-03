package com.davidfrivas.mouse_colony_app.services;

import com.davidfrivas.mouse_colony_app.entities.Lab;
import com.davidfrivas.mouse_colony_app.entities.ProtocolPersonnel;
import com.davidfrivas.mouse_colony_app.entities.ResearchProtocol;
import com.davidfrivas.mouse_colony_app.entities.User;
import com.davidfrivas.mouse_colony_app.repositories.LabRepository;
import com.davidfrivas.mouse_colony_app.repositories.ResearchProtocolRepository;
import com.davidfrivas.mouse_colony_app.repositories.UserRepository;
import jakarta.transaction.Transactional;
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

    // Find protocol by ID
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

    // Update protocol status
    public ResearchProtocol updateStatus(Long id, String newStatus) {
        ResearchProtocol protocol = findById(id);
        protocol.setStatus(newStatus);
        return protocolRepository.save(protocol);
    }

    // Delete protocol
    public void deleteProtocol(Long id) {
        ResearchProtocol protocol = findById(id);
        protocolRepository.delete(protocol);
    }

    // Add personnel to protocol
    @Transactional
    public ResearchProtocol addPersonnel(Long protocolId, Long userId, String role) {
        ResearchProtocol protocol = findById(protocolId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Create personnel record
        ProtocolPersonnel personnel = ProtocolPersonnel.builder()
                .protocol(protocol)
                .user(user)
                .role(role)
                .build();

        // Add to protocol's personnel
        protocol.getPersonnel().add(personnel);

        return protocolRepository.save(protocol);
    }

    // Remove personnel from protocol
    @Transactional
    public ResearchProtocol removePersonnel(Long protocolId, Long userId) {
        ResearchProtocol protocol = findById(protocolId);

        // Remove personnel with matching user ID
        protocol.getPersonnel().removeIf(p -> p.getUser().getUserId().equals(userId));

        return protocolRepository.save(protocol);
    }

    // Check if protocol exists
    public boolean existsById(Long id) {
        return protocolRepository.existsById(id);
    }
}
