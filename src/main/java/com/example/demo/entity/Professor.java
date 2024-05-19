package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "professors")
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "professor_id")
    private Long professorId;  // Уникальный идентификатор профессора

    @Column(name = "first_name", nullable = false)
    private String firstName;  // Имя профессора

    @Column(name = "last_name", nullable = false)
    private String lastName;  // Фамилия профессора

    @Column(name = "email", nullable = false)
    private String email;  // Электронная почта профессора

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;  // Ссылка на департамент
}
