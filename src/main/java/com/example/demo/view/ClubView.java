package com.example.demo.view;

import com.example.demo.entity.Club;
import com.example.demo.entity.Course;
import com.example.demo.entity.Professor;
import com.example.demo.service.ClubService;
import com.example.demo.service.ProfessorService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("clubs")
@PageTitle("Club View")
@RolesAllowed("TEACHER")
public class ClubView extends VerticalLayout {
    private final ClubService clubService;
    private final ProfessorService professorService;
    private final Grid<Club> grid = new Grid<>(Club.class);

    @Autowired
    public ClubView(ClubService clubService, ProfessorService professorService) {
        this.clubService = clubService;
        this.professorService = professorService;

        Button addButton = new Button("Add Club", click -> addClub());
        Button deleteButton = new Button("Delete Selected", click -> deleteClub());
        Button editButton = new Button("Edit Selected", click -> editClub());

        HorizontalLayout buttonsLayout = new HorizontalLayout(addButton, deleteButton, editButton);
        add(buttonsLayout);
        add(grid);
        listClubs();
    }

    private void listClubs() {
        grid.setItems(clubService.getClubs());
    }

    private void addClub() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField();
        nameField.setLabel("Club Name");
        formLayout.add(nameField);

        Select<Professor> advisorSelect = new Select<>();
        advisorSelect.setLabel("Professor");
        advisorSelect.setItems(professorService.getProfessors());
        advisorSelect.setItemLabelGenerator(Professor::getFirstName);
        formLayout.add(advisorSelect);

        Button saveButton = new Button("Save", click -> {
            Club club = new Club();
            club.setClubName(nameField.getValue());
            club.setAdvisor(advisorSelect.getValue());
            clubService.saveClub(club);
            dialog.close();
            listClubs();
        });

        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void deleteClub() {
        Club selectedClub = grid.asSingleSelect().getValue();
        if (selectedClub != null) {
            clubService.deleteClub(selectedClub.getClubId());
            listClubs();
        }
    }

    private void editClub() {
        Club selectedClub = grid.asSingleSelect().getValue();
        if (selectedClub != null) {
            Dialog dialog = new Dialog();
            FormLayout formLayout = new FormLayout();

            TextField nameField = new TextField();
            nameField.setLabel("Club Name");
            nameField.setValue(selectedClub.getClubName());
            formLayout.add(nameField);

            Select<Professor> advisorSelect = new Select<>();
            advisorSelect.setLabel("Professor");
            advisorSelect.setItems(professorService.getProfessors());
            advisorSelect.setItemLabelGenerator(Professor::getFirstName);
            advisorSelect.setValue(selectedClub.getAdvisor());
            formLayout.add(advisorSelect);

            Button saveButton = new Button("Save", click -> {
                selectedClub.setClubName(nameField.getValue());
                selectedClub.setAdvisor(advisorSelect.getValue());
                clubService.updateClub(selectedClub.getClubId(), selectedClub);
                dialog.close();
                listClubs();
            });

            formLayout.add(saveButton);
            dialog.add(formLayout);
            dialog.open();
        }
    }
}
