package com.example.demo.service;

import com.example.demo.entity.LibraryBook;
import com.example.demo.repository.LibraryBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryBookService {
    private final LibraryBookRepository libraryBookRepository;

    @Autowired
    public LibraryBookService(LibraryBookRepository libraryBookRepository) {
        this.libraryBookRepository = libraryBookRepository;
    }

    public void saveBook(LibraryBook book) {
        libraryBookRepository.save(book);
    }

    public List<LibraryBook> getBooks() {
        return libraryBookRepository.findAll();
    }

    public LibraryBook getBookById(Long id) {
        return libraryBookRepository.findById(id).orElse(null);
    }

    public LibraryBook updateBook(Long id, LibraryBook book) {
        LibraryBook existingBook = libraryBookRepository.findById(id).orElse(null);
        if (existingBook != null) {
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setIsbn(book.getIsbn());
            existingBook.setPublishedYear(book.getPublishedYear());
            return libraryBookRepository.save(existingBook);
        }
        return null;
    }

    public String deleteBook(Long id) {
        libraryBookRepository.deleteById(id);
        return "Book with ID " + id + " has been deleted.";
    }
}
