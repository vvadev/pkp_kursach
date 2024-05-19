package com.example.demo.view;

import com.example.demo.entity.Grade;
import com.example.demo.entity.LibraryBook;
import com.example.demo.service.LibraryBookService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("librarybooks")
@PageTitle("Library Books View")
@RolesAllowed("ADMIN")
public class LibraryBookView extends VerticalLayout {
    private final LibraryBookService libraryBookService;
    private final Grid<LibraryBook> grid = new Grid<>(LibraryBook.class);

    @Autowired
    public LibraryBookView(LibraryBookService libraryBookService) {
        this.libraryBookService = libraryBookService;

        Span title = new Span("Library Book");

        add(title);
        GridContextMenu<LibraryBook> contextMenu = grid.addContextMenu();
        contextMenu.addItem(
                "Add", event -> addBook()
        );
        contextMenu.addItem(
                "Edit", event -> editBook()
        );
        contextMenu.addItem(
                "Delete", event -> deleteBook()
        );
        add(contextMenu);
        add(grid);
        listBooks();
    }

    private void listBooks() {
        grid.setItems(libraryBookService.getBooks());
    }

    private void addBook() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField titleField = new TextField();
        titleField.setLabel("Title");
        formLayout.add(titleField);

        TextField authorField = new TextField();
        authorField.setLabel("Author");
        formLayout.add(authorField);

        TextField isbnField = new TextField();
        isbnField.setLabel("ISBN");
        formLayout.add(isbnField);

        TextField publishedYearField = new TextField();
        publishedYearField.setLabel("Published Year");
        formLayout.add(publishedYearField);

        Button saveButton = new Button("Save", click -> {
            LibraryBook book = new LibraryBook();
            book.setTitle(titleField.getValue());
            book.setAuthor(authorField.getValue());
            book.setIsbn(isbnField.getValue());
            book.setPublishedYear(Integer.parseInt(publishedYearField.getValue()));
            libraryBookService.saveBook(book);
            dialog.close();
            listBooks();
        });

        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void deleteBook() {
        LibraryBook selectedBook = grid.asSingleSelect().getValue();
        if (selectedBook != null) {
            libraryBookService.deleteBook(selectedBook.getBookId());
            listBooks();
        }
    }

    private void editBook() {
        LibraryBook selectedBook = grid.asSingleSelect().getValue();
        if (selectedBook != null) {
            Dialog dialog = new Dialog();
            FormLayout formLayout = new FormLayout();

            TextField titleField = new TextField();
            titleField.setLabel("Title");
            titleField.setValue(selectedBook.getTitle());
            formLayout.add(titleField);

            TextField authorField = new TextField();
            authorField.setLabel("Author");
            authorField.setValue(selectedBook.getAuthor());
            formLayout.add(authorField);

            TextField isbnField = new TextField();
            isbnField.setLabel("ISBN");
            isbnField.setValue(selectedBook.getIsbn());
            formLayout.add(isbnField);

            TextField publishedYearField = new TextField();
            publishedYearField.setLabel("Published Year");
            publishedYearField.setValue(String.valueOf(selectedBook.getPublishedYear()));
            formLayout.add(publishedYearField);

            Button saveButton = new Button("Save", click -> {
                selectedBook.setTitle(titleField.getValue());
                selectedBook.setAuthor(authorField.getValue());
                selectedBook.setIsbn(isbnField.getValue());
                selectedBook.setPublishedYear(Integer.parseInt(publishedYearField.getValue()));
                libraryBookService.updateBook(selectedBook.getBookId(), selectedBook);
                dialog.close();
                listBooks();
            });

            formLayout.add(saveButton);
            dialog.add(formLayout);
            dialog.open();
        }
    }
}
