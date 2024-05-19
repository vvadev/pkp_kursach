package com.example.demo.service;

import com.example.demo.entity.BookLoan;
import com.example.demo.repository.BookLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookLoanService {
    private final BookLoanRepository bookLoanRepository;

    @Autowired
    public BookLoanService(BookLoanRepository bookLoanRepository) {
        this.bookLoanRepository = bookLoanRepository;
    }

    public void saveLoan(BookLoan loan) {
        bookLoanRepository.save(loan);
    }

    public List<BookLoan> getLoans() {
        return bookLoanRepository.findAll();
    }

    public BookLoan getLoanById(Long id) {
        return bookLoanRepository.findById(id).orElse(null);
    }

    public BookLoan updateLoan(Long id, BookLoan loan) {
        BookLoan existingLoan = bookLoanRepository.findById(id).orElse(null);
        if (existingLoan != null) {
            existingLoan.setStudent(loan.getStudent());
            existingLoan.setBook(loan.getBook());
            existingLoan.setLoanDate(loan.getLoanDate());
            existingLoan.setReturnDate(loan.getReturnDate());
            return bookLoanRepository.save(existingLoan);
        }
        return null;
    }

    public String deleteLoan(Long id) {
        bookLoanRepository.deleteById(id);
        return "Loan with ID " + id + " has been deleted.";
    }
}
