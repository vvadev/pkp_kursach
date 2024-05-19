package com.example.demo.service;

import com.example.demo.entity.Professor;
import com.example.demo.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfessorService {
    private final ProfessorRepository professorRepository;

    @Autowired
    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    public void saveProfessor(Professor professor) {
        professorRepository.save(professor);
    }

    public List<Professor> getProfessors() {
        return professorRepository.findAll();
    }

    public Professor getProfessorById(Long id) {
        return professorRepository.findById(id).orElse(null);
    }

    public Professor updateProfessor(Long id, Professor professor) {
        Professor existingProfessor = professorRepository.findById(id).orElse(null);
        if (existingProfessor != null) {
            existingProfessor.setFirstName(professor.getFirstName());
            existingProfessor.setLastName(professor.getLastName());
            existingProfessor.setEmail(professor.getEmail());
            existingProfessor.setDepartment(professor.getDepartment());
            return professorRepository.save(existingProfessor);
        }
        return null;
    }

    public String deleteProfessor(Long id) {
        professorRepository.deleteById(id);
        return "Professor with ID " + id + " has been deleted.";
    }
}
