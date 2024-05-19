package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clubs")
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "club_id")
    private Long clubId;  // Уникальный идентификатор клуба

    @Column(name = "club_name", nullable = false)
    private String clubName;  // Название клуба

    @ManyToOne
    @JoinColumn(name = "advisor_id", nullable = false)
    private Professor advisor;  // Ссылка на преподавателя-консультанта
}
