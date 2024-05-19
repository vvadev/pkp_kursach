package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_attendance")
public class EventAttendance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "attendance_id")
    private Long attendanceId;  // Уникальный идентификатор посещения

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;  // Ссылка на событие

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;  // Ссылка на студента

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;  // Дата посещения
}
