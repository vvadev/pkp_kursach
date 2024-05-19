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
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "student_id")
    private Long studentId;  // Уникальный идентификатор студента

    @Column(name = "first_name", nullable = false)
    private String firstName;  // Имя студента

    @Column(name = "last_name", nullable = false)
    private String lastName;  // Фамилия студента

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;  // Дата рождения студента

    @Column(name = "gender", nullable = false)
    private String gender;  // Пол студента

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;  // Дата зачисления студента
}
