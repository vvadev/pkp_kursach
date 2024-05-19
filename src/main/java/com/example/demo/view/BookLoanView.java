package com.example.demo.view;

import com.example.demo.entity.*;
import com.example.demo.service.BookLoanService;
import com.example.demo.service.LibraryBookService;
import com.example.demo.service.StudentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Route("bookloans")
@PageTitle("Book Loans View")
@RolesAllowed("TEACHER")
public class BookLoanView extends VerticalLayout {
    private final BookLoanService bookLoanService;
    private final StudentService studentService;
    private final LibraryBookService libraryBookService;
    private final Grid<BookLoan> grid = new Grid<>(BookLoan.class);

    @Autowired
    public BookLoanView(BookLoanService bookLoanService, StudentService studentService, LibraryBookService libraryBookService) {
        this.bookLoanService = bookLoanService;
        this.studentService = studentService;
        this.libraryBookService = libraryBookService;

        Span title = new Span("Bool Loan");

        add(title);
        GridContextMenu<BookLoan> contextMenu = grid.addContextMenu();
        contextMenu.addItem(
                "Add", event -> addLoan()
        );
        contextMenu.addItem(
                "Edit", event -> editLoan()
        );
        contextMenu.addItem(
                "Delete", event -> deleteLoan()
        );
        add(contextMenu);
        add(grid);
        listLoans();
    }

    private void listLoans() {
        grid.setItems(bookLoanService.getLoans());
    }

    private void addLoan() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        Select<Student> studentSelect = new Select<>();
        studentSelect.setLabel("Student");
        studentSelect.setItems(studentService.getStudents());
        studentSelect.setItemLabelGenerator(Student::getFirstName);
        formLayout.add(studentSelect);

        Select<LibraryBook> bookSelect = new Select<>();
        bookSelect.setLabel("Book");
        bookSelect.setItems(libraryBookService.getBooks());
        bookSelect.setItemLabelGenerator(LibraryBook::getTitle);
        formLayout.add(bookSelect);

        DatePicker loanDateField = new DatePicker();
        loanDateField.setLabel("Loan Date");
        formLayout.add(loanDateField);

        DatePicker returnDateField = new DatePicker();
        returnDateField.setLabel("Return Date");
        formLayout.add(returnDateField);

        Button saveButton = new Button("Save", click -> {
            BookLoan loan = new BookLoan();
            loan.setLoanDate(loanDateField.getValue());
            if (!returnDateField.isEmpty()) {
                loan.setReturnDate(returnDateField.getValue());
            }
            loan.setStudent(studentSelect.getValue());
            loan.setBook(bookSelect.getValue());
            bookLoanService.saveLoan(loan);
            dialog.close();
            listLoans();
        });

        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void deleteLoan() {
        BookLoan selectedLoan = grid.asSingleSelect().getValue();
        if (selectedLoan != null) {
            bookLoanService.deleteLoan(selectedLoan.getLoanId());
            listLoans();
        }
    }

    private void editLoan() {
        BookLoan selectedLoan = grid.asSingleSelect().getValue();
        if (selectedLoan != null) {
            Dialog dialog = new Dialog();
            FormLayout formLayout = new FormLayout();

            Select<Student> studentSelect = new Select<>();
            studentSelect.setLabel("Student");
            studentSelect.setItems(studentService.getStudents());
            studentSelect.setItemLabelGenerator(Student::getFirstName);
            studentSelect.setValue(selectedLoan.getStudent());
            formLayout.add(studentSelect);

            Select<LibraryBook> bookSelect = new Select<>();
            bookSelect.setLabel("Book");
            bookSelect.setItems(libraryBookService.getBooks());
            bookSelect.setItemLabelGenerator(LibraryBook::getTitle);
            bookSelect.setValue(selectedLoan.getBook());
            formLayout.add(bookSelect);

            DatePicker loanDateField = new DatePicker();
            loanDateField.setLabel("Loan Date");
            loanDateField.setValue(selectedLoan.getLoanDate());
            formLayout.add(loanDateField);

            DatePicker returnDateField = new DatePicker();
            returnDateField.setLabel("Return Date");
            if (selectedLoan.getReturnDate() != null) {
                returnDateField.setValue(selectedLoan.getReturnDate());
            }
            formLayout.add(returnDateField);

            Button saveButton = new Button("Save", click -> {
                selectedLoan.setLoanDate(loanDateField.getValue());
                if (!returnDateField.isEmpty()) {
                    selectedLoan.setReturnDate(returnDateField.getValue());
                } else {
                    selectedLoan.setReturnDate(null);
                }
                selectedLoan.setStudent(studentSelect.getValue());
                selectedLoan.setBook(bookSelect.getValue());
                bookLoanService.updateLoan(selectedLoan.getLoanId(), selectedLoan);
                dialog.close();
                listLoans();
            });

            formLayout.add(saveButton);
            dialog.add(formLayout);
            dialog.open();
        }
    }
}
