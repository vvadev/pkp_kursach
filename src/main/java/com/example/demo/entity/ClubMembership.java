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
@Table(name = "club_memberships")
public class ClubMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "membership_id")
    private Long membershipId;  // Уникальный идентификатор членства в клубе

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;  // Ссылка на студента

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;  // Ссылка на клуб

    @Column(name = "join_date", nullable = false)
    private LocalDate joinDate;  // Дата вступления в клуб
}
