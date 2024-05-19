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
@Table(name = "book_loans")
public class BookLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "loan_id")
    private Long loanId;  // Уникальный идентификатор выдачи книги

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;  // Ссылка на студента

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private LibraryBook book;  // Ссылка на книгу

    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;  // Дата выдачи

    @Column(name = "return_date")
    private LocalDate returnDate;  // Дата возврата
}
