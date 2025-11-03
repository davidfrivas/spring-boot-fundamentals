package com.davidfrivas.mouse_colony_app.services;

import com.davidfrivas.mouse_colony_app.entities.*;
import com.davidfrivas.mouse_colony_app.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MouseService {
    private final MouseRepository mouseRepository;
    private final LabRepository labRepository;
    private final ResearchProtocolRepository protocolRepository;
    private final UserRepository userRepository;
    private final LitterRepository litterRepository;

    // Create a new mouse
    public Mouse createMouse(Mouse mouse, Long labId, Long protocolId, Long userId) {
        // Validation
        if (mouse.getName() == null || mouse.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Mouse name cannot be empty");
        }
        if (mouse.getGenotype() == null) {
            throw new IllegalArgumentException("Genotype is required");
        }

        // Fetch and set required entities
        Lab lab = labRepository.findById(labId).orElseThrow(() -> new RuntimeException("Lab not found with id: " + labId));
        mouse.setLab(lab);

        ResearchProtocol protocol = protocolRepository.findById(protocolId).orElseThrow(() -> new RuntimeException("Protocol not found with id: " + protocolId));
        mouse.setProtocol(protocol);

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        mouse.setUser(user);

        // Set default availability if not provided
        if (mouse.getAvailability() == null)
            mouse.setAvailability(true);

        return mouseRepository.save(mouse);
    }

    // Create mouse with parents
    public Mouse createMouseWithParents(Mouse mouse, Long labId, Long protocolId, Long userId, Long motherId, Long fatherId) {
        // First create a basic mouse
        mouse = createMouse(mouse, labId, protocolId, userId);

        // Set parents if provided
        if (motherId != null) {
            Mouse mother = findById(motherId);
            mouse.setMother(mother);
        }
        if (fatherId != null) {
            Mouse father = findById(fatherId);
            mouse.setFather(father);
        }

        return mouseRepository.save(mouse);
    }

    // Create mouse from litter
    public Mouse createMouseFromLitter(Mouse mouse, Long labId, Long protocolId, Long userId, Long litterId) {
        // Create a basic mouse
        mouse = createMouse(mouse, labId, protocolId, userId);

        // Set litter and automatically set parents from litter
        Litter litter = litterRepository.findById(litterId).orElseThrow(() -> new RuntimeException("Litter not found with id: " + litterId));

        mouse.setLitter(litter);
        mouse.setMother(litter.getMother());
        mouse.setFather(litter.getFather());

        return mouseRepository.save(mouse);
    }

    // Find mouse by ID
    public Mouse findById(Long id) {
        return mouseRepository.findById(id).orElseThrow(() -> new RuntimeException("Mouse not found with id: " + id));
    }

    // Get all mice
    public List<Mouse> findAll() {
        return (List<Mouse>) mouseRepository.findAll();
    }

    // Update existing mouse
    public Mouse updateMouse(Long id, Mouse updatedMouse) {
        Mouse existingMouse = findById(id);

        existingMouse.setName(updatedMouse.getName());
        existingMouse.setGenotype(updatedMouse.getGenotype());
        existingMouse.setAvailability(updatedMouse.getAvailability());
        existingMouse.setStrain(updatedMouse.getStrain());
        existingMouse.setSex(updatedMouse.getSex());
        existingMouse.setDateOfBirth(updatedMouse.getDateOfBirth());
        existingMouse.setNotes(updatedMouse.getNotes());

        return mouseRepository.save(existingMouse);
    }

    // Update mouse availability
    public Mouse updateAvailability(Long id, Boolean available) {
        Mouse existingMouse = findById(id);
        existingMouse.setAvailability(available);
        return mouseRepository.save(existingMouse);
    }

    // Transfer mouse to different lab
    public Mouse transferToLab(Long mouseId, Long newLabId) {
        Mouse mouse = findById(mouseId);
        Lab newLab = labRepository.findById(newLabId).orElseThrow(() -> new RuntimeException("Lab not found with id: " + newLabId));

        mouse.setLab(newLab);
        return mouseRepository.save(mouse);
    }

    // Assign mouse to protocol
    public Mouse assignToProtocol(Long mouseId, Long protocolId) {
        Mouse mouse = findById(mouseId);
        ResearchProtocol protocol = protocolRepository.findById(protocolId).orElseThrow(() -> new RuntimeException("Protocol not found with id: " + protocolId));

        mouse.setProtocol(protocol);
        return mouseRepository.save(mouse);
    }

    // Delete mouse
    public void deleteMouse(Long id) {
        Mouse mouse = findById(id);
        mouseRepository.delete(mouse);
    }

    // Business logic methods

    // Get available mice in a lab
    public List<Mouse> getAvailableMiceByLab(Long labId) {
        Lab lab = labRepository.findById(labId).orElseThrow(() -> new RuntimeException("Lab not found with id: " + labId));

        // TODO: create a custom repository method to filter
        return findAll().stream()
                .filter(m -> m.getLab().getLabId().equals(labId))
                .filter(Mouse::getAvailability)
                .toList();
    }

    // Get mice by protocol
    public List<Mouse> getMiceByProtocol(Long protocolId) {
        return findAll().stream()
                .filter(m -> m.getProtocol().getProtocolId().equals(protocolId))
                .toList();
    }

    // Get offspring of a mouse
    public List<Mouse> getOffspring(Long mouseId) {
        return findAll().stream()
                .filter(m -> m.getMother() != null && m.getMother().getMouseId().equals(mouseId) || m.getFather() != null && m.getFather().getMouseId().equals(mouseId))
                .toList();
    }

    // Check if mouse exists
    public boolean existsById(Long id) {
        return mouseRepository.existsById(id);
    }
}
