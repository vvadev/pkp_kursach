package com.example.demo.service;

import com.example.demo.entity.Enrollment;
import com.example.demo.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public void saveEnrollment(Enrollment enrollment) {
        enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id).orElse(null);
    }

    public Enrollment updateEnrollment(Long id, Enrollment enrollment) {
        Enrollment existingEnrollment = enrollmentRepository.findById(id).orElse(null);
        if (existingEnrollment != null) {
            existingEnrollment.setStudent(enrollment.getStudent());
            existingEnrollment.setCourse(enrollment.getCourse());
            existingEnrollment.setEnrollmentDate(enrollment.getEnrollmentDate());
            return enrollmentRepository.save(existingEnrollment);
        }
        return null;
    }

    public String deleteEnrollment(Long id) {
        enrollmentRepository.deleteById(id);
        return "Enrollment with ID " + id + " has been deleted.";
    }
}
