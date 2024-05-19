package com.example.demo.service;

import com.example.demo.entity.Classroom;
import com.example.demo.repository.ClassroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomService {
    private final ClassroomRepository classroomRepository;

    @Autowired
    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public void saveClassroom(Classroom classroom) {
        classroomRepository.save(classroom);
    }

    public List<Classroom> getClassrooms() {
        return classroomRepository.findAll();
    }

    public Classroom getClassroomById(Long id) {
        return classroomRepository.findById(id).orElse(null);
    }

    public Classroom updateClassroom(Long id, Classroom classroom) {
        Classroom existingClassroom = classroomRepository.findById(id).orElse(null);
        if (existingClassroom != null) {
            existingClassroom.setBuilding(classroom.getBuilding());
            existingClassroom.setRoomNumber(classroom.getRoomNumber());
            existingClassroom.setCapacity(classroom.getCapacity());
            return classroomRepository.save(existingClassroom);
        }
        return null;
    }

    public String deleteClassroom(Long id) {
        classroomRepository.deleteById(id);
        return "Classroom with ID " + id + " has been deleted.";
    }
}
