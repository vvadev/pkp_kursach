package com.example.demo.repository;

import com.example.demo.entity.EventAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventAttendanceRepository extends JpaRepository<EventAttendance, Long> {
}
