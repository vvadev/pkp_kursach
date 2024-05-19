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
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "enrollment_id")
    private Long enrollmentId;  // Уникальный идентификатор зачисления

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;  // Ссылка на студента

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;  // Ссылка на курс

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;  // Дата зачисления
}
