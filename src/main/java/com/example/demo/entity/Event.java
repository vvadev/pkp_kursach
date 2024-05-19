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
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_id")
    private Long eventId;  // Уникальный идентификатор события

    @Column(name = "event_name", nullable = false)
    private String eventName;  // Название события

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;  // Дата события

    @Column(name = "location", nullable = false)
    private String location;  // Место проведения
}
