package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "classrooms")
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "classroom_id")
    private Long classroomId;  // Уникальный идентификатор аудитории

    @Column(name = "building", nullable = false)
    private String building;  // Название здания

    @Column(name = "room_number", nullable = false)
    private String roomNumber;  // Номер комнаты

    @Column(name = "capacity", nullable = false)
    private int capacity;  // Вместимость аудитории
}
