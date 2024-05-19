package com.example.demo.service;

import com.example.demo.entity.EventAttendance;
import com.example.demo.repository.EventAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventAttendanceService {
    private final EventAttendanceRepository eventAttendanceRepository;

    @Autowired
    public EventAttendanceService(EventAttendanceRepository eventAttendanceRepository) {
        this.eventAttendanceRepository = eventAttendanceRepository;
    }

    public void saveAttendance(EventAttendance attendance) {
        eventAttendanceRepository.save(attendance);
    }

    public List<EventAttendance> getAttendances() {
        return eventAttendanceRepository.findAll();
    }

    public EventAttendance getAttendanceById(Long id) {
        return eventAttendanceRepository.findById(id).orElse(null);
    }

    public EventAttendance updateAttendance(Long id, EventAttendance attendance) {
        EventAttendance existingAttendance = eventAttendanceRepository.findById(id).orElse(null);
        if (existingAttendance != null) {
            existingAttendance.setEvent(attendance.getEvent());
            existingAttendance.setStudent(attendance.getStudent());
            existingAttendance.setAttendanceDate(attendance.getAttendanceDate());
            return eventAttendanceRepository.save(existingAttendance);
        }
        return null;
    }

    public String deleteAttendance(Long id) {
        eventAttendanceRepository.deleteById(id);
        return "Attendance with ID " + id + " has been deleted.";
    }
}
