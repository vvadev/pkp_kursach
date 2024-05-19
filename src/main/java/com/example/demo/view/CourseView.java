package com.example.demo.view;

import com.example.demo.entity.ClubMembership;
import com.example.demo.entity.Course;
import com.example.demo.service.CourseService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("courses")
@PageTitle("Course View")
@RolesAllowed("ADMIN")
public class CourseView extends VerticalLayout {
    private final CourseService courseService;
    private final Grid<Course> grid = new Grid<>(Course.class);

    @Autowired
    public CourseView(CourseService courseService) {
        this.courseService = courseService;

        Span title = new Span("Course");

        add(title);
        GridContextMenu<Course> contextMenu = grid.addContextMenu();
        contextMenu.addItem(
                "Add", event -> addCourse()
        );
        contextMenu.addItem(
                "Edit", event -> editCourse()
        );
        contextMenu.addItem(
                "Delete", event -> deleteCourse()
        );
        add(contextMenu);
        add(grid);
        listCourses();
    }

    private void listCourses() {
        grid.setItems(courseService.getCourses());
    }

    private void addCourse() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField courseNameField = new TextField();
        courseNameField.setLabel("Course Name");
        formLayout.add(courseNameField);

        TextArea courseDescriptionField = new TextArea();
        courseDescriptionField.setLabel("Course Description");
        formLayout.add(courseDescriptionField);

        TextField creditsField = new TextField();
        creditsField.setLabel("Credits");
        formLayout.add(creditsField);

        Button saveButton = new Button("Save", click -> {
            Course course = new Course();
            course.setCourseName(courseNameField.getValue());
            course.setCourseDescription(courseDescriptionField.getValue());
            course.setCredits(Integer.valueOf(creditsField.getValue()));
            courseService.saveCourse(course);
            dialog.close();
            listCourses();
        });

        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void deleteCourse() {
        Course selectedCourse = grid.asSingleSelect().getValue();
        if (selectedCourse != null) {
            courseService.deleteCourse(selectedCourse.getCourseId());
            listCourses();
        }
    }

    private void editCourse() {
        Course selectedCourse = grid.asSingleSelect().getValue();
        if (selectedCourse != null) {
            Dialog dialog = new Dialog();
            FormLayout formLayout = new FormLayout();

            TextField courseNameField = new TextField();
            courseNameField.setLabel("Course Name");
            courseNameField.setValue(selectedCourse.getCourseName());
            formLayout.add(courseNameField);

            TextArea courseDescriptionField = new TextArea();
            courseDescriptionField.setLabel("Course Description");
            courseDescriptionField.setValue(selectedCourse.getCourseDescription());
            formLayout.add(courseDescriptionField);

            TextField creditsField = new TextField();
            creditsField.setLabel("Credits");
            creditsField.setValue(selectedCourse.getCredits().toString());
            formLayout.add(creditsField);

            Button saveButton = new Button("Save", click -> {
                selectedCourse.setCourseName(courseNameField.getValue());
                selectedCourse.setCourseDescription(courseDescriptionField.getValue());
                selectedCourse.setCredits(Integer.valueOf(creditsField.getValue()));
                courseService.updateCourse(selectedCourse.getCourseId(), selectedCourse);
                dialog.close();
                listCourses();
            });

            formLayout.add(saveButton);
            dialog.add(formLayout);
            dialog.open();
        }
    }
}
