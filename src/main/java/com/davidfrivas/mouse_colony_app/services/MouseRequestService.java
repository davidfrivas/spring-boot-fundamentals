package com.davidfrivas.mouse_colony_app.services;

import com.davidfrivas.mouse_colony_app.entities.Lab;
import com.davidfrivas.mouse_colony_app.entities.MouseRequest;
import com.davidfrivas.mouse_colony_app.entities.User;
import com.davidfrivas.mouse_colony_app.entities.Mouse;
import com.davidfrivas.mouse_colony_app.repositories.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class MouseRequestService {
    private final MouseRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final MouseRepository mouseRepository;
    private final LabRepository labRepository;
    private final MouseService mouseService;

    // Create a new mouse request
    public MouseRequest createRequest(MouseRequest request, Long requestorId, Long mouseId, Long fromLabId, Long toLabId) {
        // Validation
        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }

        // Fetch requestor
        User requestor = userRepository.findById(requestorId).orElseThrow(() -> new RuntimeException("Requestor not found with id: " + requestorId));
        request.setRequestor(requestor);

        // Fetch mouse
        Mouse mouse = mouseRepository.findById(mouseId).orElseThrow(() -> new RuntimeException("Mouse not found with id: " + mouseId));

        // Validate mouse is available
        if (!mouse.getAvailability()) {
            throw new IllegalArgumentException("Mouse is not available for transfer");
        }
        request.setMouse(mouse);

        // Fetch labs
        Lab fromLab = labRepository.findById(fromLabId).orElseThrow(() -> new RuntimeException("From lab not found with id: " + fromLabId));
        request.setFromLab(fromLab);

        Lab toLab = labRepository.findById(toLabId).orElseThrow(() -> new RuntimeException("To lab not found with id: " + toLabId));
        request.setToLab(toLab);

        // Validate mouse belongs to fromLab
        if (!mouse.getLab().getLabId().equals(fromLabId)) {
            throw new IllegalStateException("Mouse does not belong to the specified lab");
        }

        // Set default status if not provided
        if (request.getStatus() == null)
            request.setStatus("PENDING");

        return requestRepository.save(request);
    }

    // Find request by ID
    public MouseRequest findById(Long id) {
        return requestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
    }

    // Get all requests
    public List<MouseRequest> findAll() {
        return (List<MouseRequest>) requestRepository.findAll();
    }

    // Update request message
    public MouseRequest updateMessage(Long id, String newMessage) {
        MouseRequest request = findById(id);

        // Only allow updates if still pending
        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException("Cannot update message for non-pending requests");
        }

        request.setMessage(newMessage);
        return requestRepository.save(request);
    }

    // Approve quest (transfers the mouse)
    @Transactional
    public MouseRequest approveRequest(Long id) {
        MouseRequest request = findById(id);

        // Validate quest is pending
        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException("Only pending requests can be approved");
        }

        // Validate mouse is still available
        Mouse mouse = request.getMouse();
        if (!mouse.getAvailability()) {
            throw new IllegalStateException("Mouse is no longer available");
        }

        // Transfer the mouse to the new lab
        mouseService.transferToLab(mouse.getMouseId(), request.getToLab().getLabId());

        // Update request status
        request.setStatus("APPROVED");
        return requestRepository.save(request);
    }

    // Reject request
    public MouseRequest rejectRequest(Long id) {
        MouseRequest request = findById(id);

        // Validate request is still pending
        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException("Only pending requests can be rejected");
        }

        request.setStatus("REJECTED");
        return requestRepository.save(request);
    }

    // Cancel request (by requestor)
    public MouseRequest cancelRequest(Long id, Long requestorId) {
        MouseRequest request = findById(id);

        // Validate request is pending
        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException("Only pending requests can be cancelled");
        }

        // Validate requestor
        if (!request.getRequestor().getUserId().equals(requestorId)) {
            throw new IllegalStateException("Only the requestor can cancel this request");
        }

        request.setStatus("CANCELLED");
        return requestRepository.save(request);
    }

    // Delete request
    public void deleteRequest(Long id) {
        MouseRequest request = findById(id);
        requestRepository.delete(request);
    }

    // Business logic methods

    // Get requests by status
    public List<MouseRequest> getRequestsByStatus(String status) {
        return findAll().stream()
                .filter(r -> r.getStatus().equals(status))
                .toList();
    }

    // Get pending requests
    public List<MouseRequest> getPendingRequests() {
        return getRequestsByStatus("PENDING");
    }

    // Get requests sent by a user
    public List<MouseRequest> getRequestsByRequestor(Long requestorId) {
        return findAll().stream()
                .filter(r -> r.getRequestor().getUserId().equals(requestorId))
                .toList();
    }

    // Get requests for a specific mouse
    public List<MouseRequest> getRequestsForMouse(Long mouseId) {
        return findAll().stream()
                .filter(r ->r.getMouse().getMouseId().equals(mouseId))
                .toList();
    }

    // Get requests sent from a lab
    public List<MouseRequest> getRequestsFromLab(Long labId) {
        return findAll().stream()
                .filter(r -> r.getFromLab().getLabId().equals(labId))
                .toList();
    }

    public List<MouseRequest> getRequestsToLab(Long labId) {
        return findAll().stream()
                .filter(r -> r.getToLab().getLabId().equals(labId))
                .toList();
    }

    // Get pending requests for a lab to review (incoming requests)
    public List<MouseRequest> getPendingRequestsForLab(Long labId) {
        return findAll().stream()
                .filter(r -> r.getFromLab().getLabId().equals(labId))
                .filter(r -> "PENDING".equals(r.getStatus()))
                .toList();
    }

    // Check if request exists
    public boolean existsById(Long id) {
        return requestRepository.existsById(id);
    }
}
