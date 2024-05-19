package com.example.demo.view;

import com.example.demo.entity.Schedule;
import com.example.demo.entity.Student;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("students")
@PageTitle("Student View")
@RolesAllowed("TEACHER")
public class StudentView extends VerticalLayout {
    private final StudentService studentService;
    private final Grid<Student> grid = new Grid<>(Student.class);

    @Autowired
    public StudentView(StudentService studentService) {
        this.studentService = studentService;
        Span title = new Span("Student");

        add(title);
        GridContextMenu<Student> contextMenu = grid.addContextMenu();
        contextMenu.addItem(
                "Add", event -> addStudent()
        );
        contextMenu.addItem(
                "Edit", event -> editStudent()
        );
        contextMenu.addItem(
                "Delete", event -> deleteStudent()
        );
        add(contextMenu);
        add(grid);
        listStudents();
    }

    private void listStudents() {
        grid.setItems(studentService.getStudents());
    }

    private void addStudent() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField firstNameField = new TextField();
        firstNameField.setLabel("First Name");
        formLayout.add(firstNameField);

        TextField lastNameField = new TextField();
        lastNameField.setLabel("Last Name");
        formLayout.add(lastNameField);

        DatePicker dateOfBirthField = new DatePicker();
        dateOfBirthField.setLabel("Date of Birth");
        formLayout.add(dateOfBirthField);

        TextField genderField = new TextField();
        genderField.setLabel("Gender");
        formLayout.add(genderField);

        DatePicker enrollmentDateField = new DatePicker();
        enrollmentDateField.setLabel("Enrollment Date");
        formLayout.add(enrollmentDateField);

        Button saveButton = new Button("Save", click -> {
            Student student = new Student();
            student.setFirstName(firstNameField.getValue());
            student.setLastName(lastNameField.getValue());
            student.setDateOfBirth(dateOfBirthField.getValue());
            student.setGender(genderField.getValue());
            student.setEnrollmentDate(enrollmentDateField.getValue());
            studentService.saveStudent(student);
            dialog.close();
            listStudents();
        });

        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void deleteStudent() {
        Student selectedStudent = grid.asSingleSelect().getValue();
        if (selectedStudent != null) {
            studentService.deleteStudent(selectedStudent.getStudentId());
            listStudents();
        }
    }

    private void editStudent() {
        Student selectedStudent = grid.asSingleSelect().getValue();
        if (selectedStudent != null) {
            Dialog dialog = new Dialog();
            FormLayout formLayout = new FormLayout();

            TextField firstNameField = new TextField();
            firstNameField.setLabel("First Name");
            firstNameField.setValue(selectedStudent.getFirstName());
            formLayout.add(firstNameField);

            TextField lastNameField = new TextField();
            lastNameField.setLabel("Last Name");
            lastNameField.setValue(selectedStudent.getLastName());
            formLayout.add(lastNameField);

            DatePicker dateOfBirthField = new DatePicker();
            dateOfBirthField.setLabel("Date of Birth");
            dateOfBirthField.setValue(selectedStudent.getDateOfBirth());
            formLayout.add(dateOfBirthField);

            TextField genderField = new TextField();
            genderField.setLabel("Gender");
            genderField.setValue(selectedStudent.getGender());
            formLayout.add(genderField);

            DatePicker enrollmentDateField = new DatePicker();
            enrollmentDateField.setLabel("Enrollment Date");
            enrollmentDateField.setValue(selectedStudent.getEnrollmentDate());
            formLayout.add(enrollmentDateField);

            Button saveButton = new Button("Save", click -> {
                selectedStudent.setFirstName(firstNameField.getValue());
                selectedStudent.setLastName(lastNameField.getValue());
                selectedStudent.setDateOfBirth(dateOfBirthField.getValue());
                selectedStudent.setGender(genderField.getValue());
                selectedStudent.setEnrollmentDate(enrollmentDateField.getValue());
                studentService.updateStudent(selectedStudent.getStudentId(), selectedStudent);
                dialog.close();
                listStudents();
            });

            formLayout.add(saveButton);
            dialog.add(formLayout);
            dialog.open();
        }
    }
}
