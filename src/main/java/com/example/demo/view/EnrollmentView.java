package com.example.demo.view;

import com.example.demo.entity.Course;
import com.example.demo.entity.Department;
import com.example.demo.entity.Enrollment;
import com.example.demo.entity.Student;
import com.example.demo.service.CourseService;
import com.example.demo.service.EnrollmentService;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Route("enrollments")
@PageTitle("Enrollment View")
@RolesAllowed("ADMIN")
public class EnrollmentView extends VerticalLayout {
    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final Grid<Enrollment> grid = new Grid<>(Enrollment.class);

    @Autowired
    public EnrollmentView(EnrollmentService enrollmentService, StudentService studentService, CourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.courseService = courseService;

        Span title = new Span("Enrollment");

        add(title);
        GridContextMenu<Enrollment> contextMenu = grid.addContextMenu();
        contextMenu.addItem(
                "Add", event -> addEnrollment()
        );
        contextMenu.addItem(
                "Edit", event -> editEnrollment()
        );
        contextMenu.addItem(
                "Delete", event -> deleteEnrollment()
        );
        add(contextMenu);
        add(grid);
        listEnrollments();
    }

    private void listEnrollments() {
        grid.setItems(enrollmentService.getEnrollments());
    }

    private void addEnrollment() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        Select<Student> studentSelect = new Select<>();
        studentSelect.setLabel("Student");
        studentSelect.setItems(studentService.getStudents());
        studentSelect.setItemLabelGenerator(student -> student.getFirstName() + " " + student.getLastName());
        formLayout.add(studentSelect);

        Select<Course> courseSelect = new Select<>();
        courseSelect.setLabel("Course");
        courseSelect.setItems(courseService.getCourses());
        courseSelect.setItemLabelGenerator(Course::getCourseName);
        formLayout.add(courseSelect);

        DatePicker enrollmentDatePicker = new DatePicker();
        enrollmentDatePicker.setLabel("Enrollment Date");
        enrollmentDatePicker.setValue(LocalDate.now());
        formLayout.add(enrollmentDatePicker);

        Button saveButton = new Button("Save", click -> {
            Enrollment enrollment = new Enrollment();
            enrollment.setStudent(studentSelect.getValue());
            enrollment.setCourse(courseSelect.getValue());
            enrollment.setEnrollmentDate(enrollmentDatePicker.getValue());
            enrollmentService.saveEnrollment(enrollment);
            dialog.close();
            listEnrollments();
        });

        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void deleteEnrollment() {
        Enrollment selectedEnrollment = grid.asSingleSelect().getValue();
        if (selectedEnrollment != null) {
            enrollmentService.deleteEnrollment(selectedEnrollment.getEnrollmentId());
            listEnrollments();
        }
    }

    private void editEnrollment() {
        Enrollment selectedEnrollment = grid.asSingleSelect().getValue();
        if (selectedEnrollment != null) {
            Dialog dialog = new Dialog();
            FormLayout formLayout = new FormLayout();

            Select<Student> studentSelect = new Select<>();
            studentSelect.setLabel("Student");
            studentSelect.setItems(studentService.getStudents());
            studentSelect.setItemLabelGenerator(student -> student.getFirstName() + " " + student.getLastName());
            studentSelect.setValue(selectedEnrollment.getStudent());
            formLayout.add(studentSelect);

            Select<Course> courseSelect = new Select<>();
            courseSelect.setLabel("Course");
            courseSelect.setItems(courseService.getCourses());
            courseSelect.setItemLabelGenerator(Course::getCourseName);
            courseSelect.setValue(selectedEnrollment.getCourse());
            formLayout.add(courseSelect);

            DatePicker enrollmentDatePicker = new DatePicker();
            enrollmentDatePicker.setLabel("Enrollment Date");
            enrollmentDatePicker.setValue(selectedEnrollment.getEnrollmentDate());
            formLayout.add(enrollmentDatePicker);

            Button saveButton = new Button("Save", click -> {
                selectedEnrollment.setStudent(studentSelect.getValue());
                selectedEnrollment.setCourse(courseSelect.getValue());
                selectedEnrollment.setEnrollmentDate(enrollmentDatePicker.getValue());
                enrollmentService.updateEnrollment(selectedEnrollment.getEnrollmentId(), selectedEnrollment);
                dialog.close();
                listEnrollments();
            });

            formLayout.add(saveButton);
            dialog.add(formLayout);
            dialog.open();
        }
    }
}
