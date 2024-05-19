package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "schedule_id")
    private Long scheduleId;  // Уникальный идентификатор расписания

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;  // Ссылка на курс

    @ManyToOne
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;  // Ссылка на аудиторию

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;  // Ссылка на преподавателя

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;  // День недели

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;  // Время начала занятия

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;  // Время окончания занятия
}
