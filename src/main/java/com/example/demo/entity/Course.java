package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "course_id")
    private Long courseId;  // Уникальный идентификатор курса

    @Column(name = "course_name", nullable = false)
    private String courseName;  // Название курса

    @Column(name = "course_description", nullable = false)
    private String courseDescription;  // Описание курса

    @Column(name = "credits", nullable = false)
    private Integer credits;  // Количество кредитов
}
