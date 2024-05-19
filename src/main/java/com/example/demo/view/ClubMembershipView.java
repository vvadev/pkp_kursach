package com.example.demo.view;

import com.example.demo.entity.*;
import com.example.demo.service.ClubMembershipService;
import com.example.demo.service.ClubService;
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

@Route("clubmemberships")
@PageTitle("Club Membership View")
@RolesAllowed("USER")
public class ClubMembershipView extends VerticalLayout {
    private final ClubMembershipService clubMembershipService;
    private final ClubService clubService;
    private final StudentService studentService;
    private final Grid<ClubMembership> grid = new Grid<>(ClubMembership.class);

    @Autowired
    public ClubMembershipView(ClubMembershipService clubMembershipService, ClubService clubService, StudentService studentService) {
        this.clubMembershipService = clubMembershipService;
        this.clubService = clubService;
        this.studentService = studentService;

        Span title = new Span("Club Membership");

        add(title);
        GridContextMenu<ClubMembership> contextMenu = grid.addContextMenu();
        contextMenu.addItem(
                "Add", event -> addMembership()
        );
        contextMenu.addItem(
                "Edit", event -> editMembership()
        );
        contextMenu.addItem(
                "Delete", event -> deleteMembership()
        );
        add(contextMenu);
        add(grid);
        listMemberships();
    }

    private void listMemberships() {
        grid.setItems(clubMembershipService.getMemberships());
    }

    private void addMembership() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        Select<Student> studentSelect = new Select<>();
        studentSelect.setLabel("Student");
        studentSelect.setItems(studentService.getStudents());
        studentSelect.setItemLabelGenerator(Student::getFirstName);
        formLayout.add(studentSelect);

        Select<Club> clubSelect = new Select<>();
        clubSelect.setLabel("Club");
        clubSelect.setItems(clubService.getClubs());
        clubSelect.setItemLabelGenerator(Club::getClubName);
        formLayout.add(clubSelect);

        DatePicker joinDateField = new DatePicker();
        joinDateField.setLabel("Join Date");
        formLayout.add(joinDateField);

        Button saveButton = new Button("Save", click -> {
            ClubMembership membership = new ClubMembership();
            membership.setJoinDate(joinDateField.getValue());
            membership.setStudent(studentSelect.getValue());
            membership.setClub(clubSelect.getValue());
            clubMembershipService.saveMembership(membership);
            dialog.close();
            listMemberships();
        });

        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void deleteMembership() {
        ClubMembership selectedMembership = grid.asSingleSelect().getValue();
        if (selectedMembership != null) {
            clubMembershipService.deleteMembership(selectedMembership.getMembershipId());
            listMemberships();
        }
    }

    private void editMembership() {
        ClubMembership selectedMembership = grid.asSingleSelect().getValue();
        if (selectedMembership != null) {
            Dialog dialog = new Dialog();
            FormLayout formLayout = new FormLayout();

            Select<Student> studentSelect = new Select<>();
            studentSelect.setLabel("Student");
            studentSelect.setItems(studentService.getStudents());
            studentSelect.setItemLabelGenerator(Student::getFirstName);
            studentSelect.setValue(selectedMembership.getStudent());
            formLayout.add(studentSelect);

            Select<Club> clubSelect = new Select<>();
            clubSelect.setLabel("Club");
            clubSelect.setItems(clubService.getClubs());
            clubSelect.setItemLabelGenerator(Club::getClubName);
            clubSelect.setValue(selectedMembership.getClub());
            formLayout.add(clubSelect);

            DatePicker joinDateField = new DatePicker();
            joinDateField.setLabel("Join Date");
            joinDateField.setValue(selectedMembership.getJoinDate());
            formLayout.add(joinDateField);

            Button saveButton = new Button("Save", click -> {
                selectedMembership.setJoinDate(joinDateField.getValue());
                selectedMembership.setStudent(studentSelect.getValue());
                selectedMembership.setClub(clubSelect.getValue());
                clubMembershipService.updateMembership(selectedMembership.getMembershipId(), selectedMembership);
                dialog.close();
                listMemberships();
            });

            formLayout.add(saveButton);
            dialog.add(formLayout);
            dialog.open();
        }
    }
}
