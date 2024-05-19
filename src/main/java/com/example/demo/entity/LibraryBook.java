package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "library_books")
public class LibraryBook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "book_id")
    private Long bookId;  // Уникальный идентификатор книги

    @Column(name = "title", nullable = false)
    private String title;  // Название книги

    @Column(name = "author", nullable = false)
    private String author;  // Автор книги

    @Column(name = "isbn", nullable = false)
    private String isbn;  // ISBN книги

    @Column(name = "published_year", nullable = false)
    private int publishedYear;  // Год издания
}
