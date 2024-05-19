package com.example.demo.service;

import com.example.demo.entity.Grade;
import com.example.demo.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public void saveGrade(Grade grade) {
        gradeRepository.save(grade);
    }

    public List<Grade> getGrades() {
        return gradeRepository.findAll();
    }

    public Grade getGradeById(Long id) {
        return gradeRepository.findById(id).orElse(null);
    }

    public Grade updateGrade(Long id, Grade grade) {
        Grade existingGrade = gradeRepository.findById(id).orElse(null);
        if (existingGrade != null) {
            existingGrade.setEnrollment(grade.getEnrollment());
            existingGrade.setAssignment(grade.getAssignment());
            existingGrade.setGrade(grade.getGrade());
            return gradeRepository.save(existingGrade);
        }
        return null;
    }

    public String deleteGrade(Long id) {
        gradeRepository.deleteById(id);
        return "Grade with ID " + id + " has been deleted.";
    }
}
