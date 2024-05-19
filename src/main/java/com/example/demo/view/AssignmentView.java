package com.example.demo.view;

import com.example.demo.entity.Assignment;
import com.example.demo.entity.Course;
import com.example.demo.service.AssignmentService;
import com.example.demo.service.CourseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Route("assignments")
@PageTitle("Assignment View")
@RolesAllowed("TEACHER")
public class AssignmentView extends VerticalLayout {
    private final AssignmentService assignmentService;
    private final CourseService courseService;
    private final Grid<Assignment> grid = new Grid<>(Assignment.class);

    @Autowired
    public AssignmentView(AssignmentService assignmentService, CourseService courseService) {
        this.assignmentService = assignmentService;
        this.courseService = courseService;

        Span title = new Span("Assignments");

        add(title);
        GridContextMenu<Assignment> contextMenu = grid.addContextMenu();
        contextMenu.addItem(
                "Add", event -> addAssignment()
        );
        contextMenu.addItem(
                "Edit", event -> editAssignment()
        );
        contextMenu.addItem(
                "Delete", event -> deleteAssignment()
        );
        add(contextMenu);
        add(grid);
        listAssignments();
    }

    private void listAssignments() {
        grid.setItems(assignmentService.getAssignments());
    }

    private void addAssignment() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField titleField = new TextField();
        titleField.setLabel("Title");
        formLayout.add(titleField);

        TextArea descriptionArea = new TextArea();
        descriptionArea.setLabel("Description");
        formLayout.add(descriptionArea);

        Select<Course> courseSelect = new Select<>();
        courseSelect.setLabel("Course");
        courseSelect.setItems(courseService.getCourses());
        courseSelect.setItemLabelGenerator(Course::getCourseName);
        formLayout.add(courseSelect);

        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setLabel("Due Date");
        formLayout.add(dueDatePicker);

        Button saveButton = new Button("Save", click -> {
            Assignment assignment = new Assignment();
            assignment.setAssignmentTitle(titleField.getValue());
            assignment.setAssignmentDescription(descriptionArea.getValue());
            assignment.setCourse(courseSelect.getValue());
            assignment.setDueDate(dueDatePicker.getValue());
            assignmentService.saveAssignment(assignment);
            dialog.close();
            listAssignments();
        });

        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void deleteAssignment() {
        Assignment selectedAssignment = grid.asSingleSelect().getValue();
        if (selectedAssignment != null) {
            assignmentService.deleteAssignment(selectedAssignment.getAssignmentId());
            listAssignments();
        }
    }

    private void editAssignment() {
        Assignment selectedAssignment = grid.asSingleSelect().getValue();
        if (selectedAssignment != null) {
            Dialog dialog = new Dialog();
            FormLayout formLayout = new FormLayout();

            TextField titleField = new TextField();
            titleField.setLabel("Title");
            titleField.setValue(selectedAssignment.getAssignmentTitle());
            formLayout.add(titleField);

            TextArea descriptionArea = new TextArea();
            descriptionArea.setLabel("Description");
            descriptionArea.setValue(selectedAssignment.getAssignmentDescription());
            formLayout.add(descriptionArea);

            Select<Course> courseSelect = new Select<>();
            courseSelect.setLabel("Course");
            courseSelect.setItems(courseService.getCourses());
            courseSelect.setItemLabelGenerator(Course::getCourseName);
            courseSelect.setValue(selectedAssignment.getCourse());
            formLayout.add(courseSelect);

            DatePicker dueDatePicker = new DatePicker();
            dueDatePicker.setLabel("Due Date");
            dueDatePicker.setValue(selectedAssignment.getDueDate());
            formLayout.add(dueDatePicker);

            Button saveButton = new Button("Save", click -> {
                selectedAssignment.setAssignmentTitle(titleField.getValue());
                selectedAssignment.setAssignmentDescription(descriptionArea.getValue());
                selectedAssignment.setCourse(courseSelect.getValue());
                selectedAssignment.setDueDate(dueDatePicker.getValue());
                assignmentService.updateAssignment(selectedAssignment.getAssignmentId(), selectedAssignment);
                dialog.close();
                listAssignments();
            });

            formLayout.add(saveButton);
            dialog.add(formLayout);
            dialog.open();
        }
    }
}
