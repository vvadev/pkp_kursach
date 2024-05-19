package com.example.demo.view;

import com.example.demo.entity.*;
import com.example.demo.service.AssignmentService;
import com.example.demo.service.EnrollmentService;
import com.example.demo.service.GradeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("grades")
@PageTitle("Grade View")
@RolesAllowed("TEACHER")
public class GradeView extends VerticalLayout {
    private final GradeService gradeService;
    private final AssignmentService assignmentService;
    private final EnrollmentService enrollmentService;
    private final Grid<Grade> grid = new Grid<>(Grade.class);

    @Autowired
    public GradeView(GradeService gradeService, AssignmentService assignmentService, EnrollmentService enrollmentService) {
        this.gradeService = gradeService;
        this.assignmentService = assignmentService;
        this.enrollmentService = enrollmentService;

        Span title = new Span("Grade");

        add(title);
        GridContextMenu<Grade> contextMenu = grid.addContextMenu();
        contextMenu.addItem(
                "Add", event -> addGrade()
        );
        contextMenu.addItem(
                "Edit", event -> editGrade()
        );
        contextMenu.addItem(
                "Delete", event -> deleteGrade()
        );
        add(contextMenu);
        add(grid);
        listGrades();
    }

    private void listGrades() {
        grid.setItems(gradeService.getGrades());
    }

    private void addGrade() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        NumberField gradeField = new NumberField();
        gradeField.setLabel("Grade");
        formLayout.add(gradeField);

        Select<Enrollment> enrollmentSelect = new Select<>();
        enrollmentSelect.setLabel("Enrollment");
        enrollmentSelect.setItems(enrollmentService.getEnrollments());
        enrollmentSelect.setItemLabelGenerator(enrollment -> String.valueOf(enrollment.getEnrollmentId()));
        formLayout.add(enrollmentSelect);

        Select<Assignment> assignmentSelect = new Select<>();
        assignmentSelect.setLabel("Assignment");
        assignmentSelect.setItems(assignmentService.getAssignments());
        assignmentSelect.setItemLabelGenerator(Assignment::getAssignmentTitle);
        formLayout.add(assignmentSelect);

        Button saveButton = new Button("Save", click -> {
            Grade grade = new Grade();
            grade.setGrade(gradeField.getValue());
            grade.setEnrollment(enrollmentSelect.getValue());
            grade.setAssignment(assignmentSelect.getValue());
            gradeService.saveGrade(grade);
            dialog.close();
            listGrades();
        });

        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void deleteGrade() {
        Grade selectedGrade = grid.asSingleSelect().getValue();
        if (selectedGrade != null) {
            gradeService.deleteGrade(selectedGrade.getGradeId());
            listGrades();
        }
    }

    private void editGrade() {
        Grade selectedGrade = grid.asSingleSelect().getValue();
        if (selectedGrade != null) {
            Dialog dialog = new Dialog();
            FormLayout formLayout = new FormLayout();

            NumberField gradeField = new NumberField();
            gradeField.setLabel("Grade");
            gradeField.setValue(selectedGrade.getGrade());
            formLayout.add(gradeField);

            Select<Enrollment> enrollmentSelect = new Select<>();
            enrollmentSelect.setLabel("Enrollment");
            enrollmentSelect.setItems(enrollmentService.getEnrollments());
            enrollmentSelect.setItemLabelGenerator(enrollment -> String.valueOf(enrollment.getEnrollmentId()));
            enrollmentSelect.setValue(selectedGrade.getEnrollment());
            formLayout.add(enrollmentSelect);

            Select<Assignment> assignmentSelect = new Select<>();
            assignmentSelect.setLabel("Assignment");
            assignmentSelect.setItems(assignmentService.getAssignments());
            assignmentSelect.setItemLabelGenerator(Assignment::getAssignmentTitle);
            assignmentSelect.setValue(selectedGrade.getAssignment());
            formLayout.add(assignmentSelect);

            Button saveButton = new Button("Save", click -> {
                selectedGrade.setGrade(gradeField.getValue());
                selectedGrade.setEnrollment(enrollmentSelect.getValue());
                selectedGrade.setAssignment(assignmentSelect.getValue());
                gradeService.updateGrade(selectedGrade.getGradeId(), selectedGrade);
                dialog.close();
                listGrades();
            });

            formLayout.add(saveButton);
            dialog.add(formLayout);
            dialog.open();
        }
    }
}
