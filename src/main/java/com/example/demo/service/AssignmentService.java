package com.example.demo.service;

import com.example.demo.entity.Assignment;
import com.example.demo.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    public void saveAssignment(Assignment assignment) {
        assignmentRepository.save(assignment);
    }

    public List<Assignment> getAssignments() {
        return assignmentRepository.findAll();
    }

    public Assignment getAssignmentById(Long id) {
        return assignmentRepository.findById(id).orElse(null);
    }

    public Assignment updateAssignment(Long id, Assignment assignment) {
        Assignment existingAssignment = assignmentRepository.findById(id).orElse(null);
        if (existingAssignment != null) {
            existingAssignment.setCourse(assignment.getCourse());
            existingAssignment.setAssignmentTitle(assignment.getAssignmentTitle());
            existingAssignment.setAssignmentDescription(assignment.getAssignmentDescription());
            existingAssignment.setDueDate(assignment.getDueDate());
            return assignmentRepository.save(existingAssignment);
        }
        return null;
    }

    public String deleteAssignment(Long id) {
        assignmentRepository.deleteById(id);
        return "Assignment with ID " + id + " has been deleted.";
    }
}
