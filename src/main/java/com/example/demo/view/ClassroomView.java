package com.example.demo.view;

import com.example.demo.entity.BookLoan;
import com.example.demo.entity.Classroom;
import com.example.demo.service.ClassroomService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("classrooms")
@PageTitle("Classroom View")
@RolesAllowed("ADMIN")
public class ClassroomView extends VerticalLayout {
    private final ClassroomService classroomService;
    private final Grid<Classroom> grid = new Grid<>(Classroom.class);

    @Autowired
    public ClassroomView(ClassroomService classroomService) {
        this.classroomService = classroomService;

        Span title = new Span("Classroom");

        add(title);
        GridContextMenu<Classroom> contextMenu = grid.addContextMenu();
        contextMenu.addItem(
                "Add", event -> addClassroom()
        );
        contextMenu.addItem(
                "Edit", event -> editClassroom()
        );
        contextMenu.addItem(
                "Delete", event -> deleteClassroom()
        );
        add(contextMenu);
        add(grid);
        listClassrooms();
    }

    private void listClassrooms() {
        grid.setItems(classroomService.getClassrooms());
    }

    private void addClassroom() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField buildingField = new TextField();
        buildingField.setLabel("Building");
        formLayout.add(buildingField);

        TextField roomNumberField = new TextField();
        roomNumberField.setLabel("Room Number");
        formLayout.add(roomNumberField);

        NumberField capacityField = new NumberField();
        capacityField.setLabel("Capacity");
        capacityField.setMin(1);
        formLayout.add(capacityField);

        Button saveButton = new Button("Save", click -> {
            Classroom classroom = new Classroom();
            classroom.setBuilding(buildingField.getValue());
            classroom.setRoomNumber(roomNumberField.getValue());
            classroom.setCapacity(capacityField.getValue().intValue());
            classroomService.saveClassroom(classroom);
            dialog.close();
            listClassrooms();
        });

        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void deleteClassroom() {
        Classroom selectedClassroom = grid.asSingleSelect().getValue();
        if (selectedClassroom != null) {
            classroomService.deleteClassroom(selectedClassroom.getClassroomId());
            listClassrooms();
        }
    }

    private void editClassroom() {
        Classroom selectedClassroom = grid.asSingleSelect().getValue();
        if (selectedClassroom != null) {
            Dialog dialog = new Dialog();
            FormLayout formLayout = new FormLayout();

            TextField buildingField = new TextField();
            buildingField.setLabel("Building");
            buildingField.setValue(selectedClassroom.getBuilding());
            formLayout.add(buildingField);

            TextField roomNumberField = new TextField();
            roomNumberField.setLabel("Room Number");
            roomNumberField.setValue(selectedClassroom.getRoomNumber());
            formLayout.add(roomNumberField);

            NumberField capacityField = new NumberField();
            capacityField.setLabel("Capacity");
            capacityField.setValue((double) selectedClassroom.getCapacity());
            capacityField.setMin(1);
            formLayout.add(capacityField);

            Button saveButton = new Button("Save", click -> {
                selectedClassroom.setBuilding(buildingField.getValue());
                selectedClassroom.setRoomNumber(roomNumberField.getValue());
                selectedClassroom.setCapacity(capacityField.getValue().intValue());
                classroomService.updateClassroom(selectedClassroom.getClassroomId(), selectedClassroom);
                dialog.close();
                listClassrooms();
            });

            formLayout.add(saveButton);
            dialog.add(formLayout);
            dialog.open();
        }
    }
}
