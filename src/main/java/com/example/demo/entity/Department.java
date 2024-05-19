package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "department_id")
    private Long departmentId;  // Уникальный идентификатор департамента

    @Column(name = "department_name", nullable = false)
    private String departmentName;  // Название департамента

    @Column(name = "department_head", nullable = false)
    private String departmentHead;  // Глава департамента
}
