package com.davidfrivas.mouse_colony_app.services;

import com.davidfrivas.mouse_colony_app.entities.Lab;
import com.davidfrivas.mouse_colony_app.entities.Litter;
import com.davidfrivas.mouse_colony_app.entities.Mouse;
import com.davidfrivas.mouse_colony_app.entities.ResearchProtocol;
import com.davidfrivas.mouse_colony_app.repositories.LabRepository;
import com.davidfrivas.mouse_colony_app.repositories.LitterRepository;
import com.davidfrivas.mouse_colony_app.repositories.MouseRepository;
import com.davidfrivas.mouse_colony_app.repositories.ResearchProtocolRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class LitterService {
    private final LitterRepository litterRepository;
    private final MouseRepository mouseRepository;
    private final LabRepository labRepository;
    private final ResearchProtocolRepository protocolRepository;

    // Create a new litter
    public Litter createLitter(Litter litter, Long labId, Long motherId, Long fatherId, Long protocolId) {
        // Validation
        if (litter.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }

        // Fetch and set lab
        Lab lab = labRepository.findById(labId).orElseThrow(() -> new RuntimeException("Lab not found with id: " + labId));

        // Fetch and set mother
        Mouse mother = mouseRepository.findById(motherId).orElseThrow(() -> new RuntimeException("Mother not found with id: " + motherId));
        if (mother.getSex() != Mouse.Sex.F) {
            throw new IllegalArgumentException("Mother must be female");
        }
        litter.setMother(mother);

        // Fetch and set father
        Mouse father = mouseRepository.findById(fatherId).orElseThrow(() -> new RuntimeException("Father not found with id: " + fatherId));
        if (father.getSex() != Mouse.Sex.M) {
            throw new IllegalArgumentException("Father must be male");
        }
        litter.setFather(father);

        // Fetch and set protocol
        ResearchProtocol protocol = protocolRepository.findById(protocolId).orElseThrow(() -> new RuntimeException("Protocol not found with id: " + protocolId));
        litter.setProtocol(protocol);

        return litterRepository.save(litter);
    }

    // Find litter by ID
    public Litter findById(Long id) {
        return litterRepository.findById(id).orElseThrow(() -> new RuntimeException("Litter not found with id: " + id));
    }

    // Get all litters
    public List<Litter> findAll() {
        return (List<Litter>) litterRepository.findAll();
    }

    // Update existing litter
    public Litter updateLitter(Long id, Litter updatedLitter) {
        Litter existingLitter = findById(id);

        existingLitter.setDateOfBirth(updatedLitter.getDateOfBirth());
        existingLitter.setNotes(updatedLitter.getNotes());

        return litterRepository.save(existingLitter);
    }

    // Update litter notes
    public Litter updateNotes(Long id, String notes) {
        Litter litter = findById(id);
        litter.setNotes(notes);
        return litterRepository.save(litter);
    }

    // Delete litter
    public void deleteLitter(Long id) {
        Litter litter = findById(id);
        litterRepository.delete(litter);
    }

    // Business logic methods

    // Get litters by lab
    public List<Litter> getLittersByLab(Long labId) {
        return findAll().stream()
                .filter(l -> l.getLab().getLabId().equals(labId))
                .toList();
    }

    // Get litters by protocol
    public List<Litter> getLittersByProtocol(Long protocolId) {
        return findAll().stream()
                .filter(l -> l.getProtocol().getProtocolId().equals(protocolId))
                .toList();
    }

    // Get litters by mother
    public List<Litter> getLittersByMother(Long motherId) {
        return findAll().stream()
                .filter(l -> l.getMother().getMouseId().equals(motherId))
                .toList();
    }

    public List<Litter> getLittersByFather(Long fatherId) {
        return findAll().stream()
                .filter(l -> l.getFather().getMouseId().equals(fatherId))
                .toList();
    }

    public List<Litter> getLittersByBreedingPair(Long motherId, Long fatherId) {
        return findAll().stream()
                .filter(l -> l.getMother().getMouseId().equals(motherId) && l.getFather().getMouseId().equals(fatherId))
                .toList();
    }

    // Get mice in a litter
    public List<Mouse> getMiceInLitter(Long litterId) {
        Litter litter = findById(litterId);
        return litter.getOffspring().stream().toList();
    }

    // Check if litter exists
    public boolean existsById(Long id) {
        return litterRepository.existsById(id);
    }
}
