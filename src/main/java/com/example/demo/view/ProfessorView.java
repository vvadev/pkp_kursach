package com.example.demo.view;

import com.example.demo.entity.Department;
import com.example.demo.entity.LibraryBook;
import com.example.demo.entity.Professor;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.ProfessorService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("professors")
@PageTitle("Professor View")
@RolesAllowed("ADMIN")
public class ProfessorView extends VerticalLayout {
    private final ProfessorService professorService;
    private final DepartmentService departmentService;
    private final Grid<Professor> grid = new Grid<>(Professor.class);

    @Autowired
    public ProfessorView(ProfessorService professorService, DepartmentService departmentService) {
        this.professorService = professorService;
        this.departmentService = departmentService;

        Span title = new Span("Professor");

        add(title);
        GridContextMenu<Professor> contextMenu = grid.addContextMenu();
        contextMenu.addItem(
                "Add", event -> addProfessor()
        );
        contextMenu.addItem(
                "Edit", event -> editProfessor()
        );
        contextMenu.addItem(
                "Delete", event -> deleteProfessor()
        );
        add(contextMenu);
        add(grid);
        listProfessors();
    }

    private void listProfessors() {
        grid.setItems(professorService.getProfessors());
    }

    private void addProfessor() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField firstNameField = new TextField();
        firstNameField.setLabel("First Name");
        formLayout.add(firstNameField);

        TextField lastNameField = new TextField();
        lastNameField.setLabel("Last Name");
        formLayout.add(lastNameField);

        EmailField emailField = new EmailField();
        emailField.setLabel("Email");
        formLayout.add(emailField);

        Select<Department> departmentSelect = new Select<>();
        departmentSelect.setLabel("Department");
        departmentSelect.setItems(departmentService.getDepartments());
        departmentSelect.setItemLabelGenerator(Department::getDepartmentName);
        formLayout.add(departmentSelect);

        Button saveButton = new Button("Save", click -> {
            Professor professor = new Professor();
            professor.setFirstName(firstNameField.getValue());
            professor.setLastName(lastNameField.getValue());
            professor.setEmail(emailField.getValue());
            professor.setDepartment(departmentSelect.getValue());
            professorService.saveProfessor(professor);
            dialog.close();
            listProfessors();
        });

        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void deleteProfessor() {
        Professor selectedProfessor = grid.asSingleSelect().getValue();
        if (selectedProfessor != null) {
            professorService.deleteProfessor(selectedProfessor.getProfessorId());
            listProfessors();
        }
    }

    private void editProfessor() {
        Professor selectedProfessor = grid.asSingleSelect().getValue();
        if (selectedProfessor != null) {
            Dialog dialog = new Dialog();
            FormLayout formLayout = new FormLayout();

            TextField firstNameField = new TextField();
            firstNameField.setLabel("First Name");
            firstNameField.setValue(selectedProfessor.getFirstName());
            formLayout.add(firstNameField);

            TextField lastNameField = new TextField();
            lastNameField.setLabel("Last Name");
            lastNameField.setValue(selectedProfessor.getLastName());
            formLayout.add(lastNameField);

            EmailField emailField = new EmailField();
            emailField.setLabel("Email");
            emailField.setValue(selectedProfessor.getEmail());
            formLayout.add(emailField);

            Select<Department> departmentSelect = new Select<>();
            departmentSelect.setLabel("Department");
            departmentSelect.setItems(departmentService.getDepartments());
            departmentSelect.setItemLabelGenerator(Department::getDepartmentName);
            departmentSelect.setValue(selectedProfessor.getDepartment());
            formLayout.add(departmentSelect);

            Button saveButton = new Button("Save", click -> {
                selectedProfessor.setFirstName(firstNameField.getValue());
                selectedProfessor.setLastName(lastNameField.getValue());
                selectedProfessor.setEmail(emailField.getValue());
                selectedProfessor.setDepartment(departmentSelect.getValue());
                professorService.updateProfessor(selectedProfessor.getProfessorId(), selectedProfessor);
                dialog.close();
                listProfessors();
            });

            formLayout.add(saveButton);
            dialog.add(formLayout);
            dialog.open();
        }
    }
}
