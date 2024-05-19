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
@Table(name = "assignments")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "assignment_id")
    private Long assignmentId;  // Уникальный идентификатор задания

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;  // Ссылка на курс

    @Column(name = "assignment_title", nullable = false)
    private String assignmentTitle;  // Название задания

    @Column(name = "assignment_description")
    private String assignmentDescription;  // Описание задания

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;  // Дата сдачи
}
