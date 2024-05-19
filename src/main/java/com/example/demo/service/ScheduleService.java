package com.example.demo.service;

import com.example.demo.entity.Schedule;
import com.example.demo.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public void saveSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    public List<Schedule> getSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    public Schedule updateSchedule(Long id, Schedule schedule) {
        Schedule existingSchedule = scheduleRepository.findById(id).orElse(null);
        if (existingSchedule != null) {
            existingSchedule.setCourse(schedule.getCourse());
            existingSchedule.setClassroom(schedule.getClassroom());
            existingSchedule.setProfessor(schedule.getProfessor());
            existingSchedule.setDayOfWeek(schedule.getDayOfWeek());
            existingSchedule.setStartTime(schedule.getStartTime());
            existingSchedule.setEndTime(schedule.getEndTime());
            return scheduleRepository.save(existingSchedule);
        }
        return null;
    }

    public String deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
        return "Schedule with ID " + id + " has been deleted.";
    }
}
