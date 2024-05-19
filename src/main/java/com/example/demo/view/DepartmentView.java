package com.example.demo.view;

import com.example.demo.entity.Course;
import com.example.demo.entity.Department;
import com.example.demo.service.DepartmentService;
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

@Route("departments")
@PageTitle("Department View")
@RolesAllowed("ADMIN")
public class DepartmentView extends VerticalLayout {
    private final DepartmentService departmentService;
    private final Grid<Department> grid = new Grid<>(Department.class);

    @Autowired
    public DepartmentView(DepartmentService departmentService) {
        this.departmentService = departmentService;

        Span title = new Span("Department");

        add(title);
        GridContextMenu<Department> contextMenu = grid.addContextMenu();
        contextMenu.addItem(
                "Add", event -> addDepartment()
        );
        contextMenu.addItem(
                "Edit", event -> editDepartment()
        );
        contextMenu.addItem(
                "Delete", event -> deleteDepartment()
        );
        add(contextMenu);
        add(grid);
        listDepartments();
    }

    private void listDepartments() {
        grid.setItems(departmentService.getDepartments());
    }

    private void addDepartment() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField departmentNameField = new TextField();
        departmentNameField.setLabel("Department Name");
        formLayout.add(departmentNameField);

        TextField departmentHeadField = new TextField();
        departmentHeadField.setLabel("Department Head");
        formLayout.add(departmentHeadField);

        Button saveButton = new Button("Save", click -> {
            Department department = new Department();
            department.setDepartmentName(departmentNameField.getValue());
            department.setDepartmentHead(departmentHeadField.getValue());
            departmentService.saveDepartment(department);
            dialog.close();
            listDepartments();
        });

        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void deleteDepartment() {
        Department selectedDepartment = grid.asSingleSelect().getValue();
        if (selectedDepartment != null) {
            departmentService.deleteDepartment(selectedDepartment.getDepartmentId());
            listDepartments();
        }
    }

    private void editDepartment() {
        Department selectedDepartment = grid.asSingleSelect().getValue();
        if (selectedDepartment != null) {
            Dialog dialog = new Dialog();
            FormLayout formLayout = new FormLayout();

            TextField departmentNameField = new TextField();
            departmentNameField.setLabel("Department Name");
            departmentNameField.setValue(selectedDepartment.getDepartmentName());
            formLayout.add(departmentNameField);

            TextField departmentHeadField = new TextField();
            departmentHeadField.setLabel("Department Head");
            departmentHeadField.setValue(selectedDepartment.getDepartmentHead());
            formLayout.add(departmentHeadField);

            Button saveButton = new Button("Save", click -> {
                selectedDepartment.setDepartmentName(departmentNameField.getValue());
                selectedDepartment.setDepartmentHead(departmentHeadField.getValue());
                departmentService.updateDepartment(selectedDepartment.getDepartmentId(), selectedDepartment);
                dialog.close();
                listDepartments();
            });

            formLayout.add(saveButton);
            dialog.add(formLayout);
            dialog.open();
        }
    }
}
