package com.example.demo.view;

import com.example.demo.entity.*;
import com.example.demo.service.EventAttendanceService;
import com.example.demo.service.EventService;
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

@Route("eventattendance")
@PageTitle("Event Attendance View")
@RolesAllowed("TEACHER")
public class EventAttendanceView extends VerticalLayout {
    private final EventAttendanceService eventAttendanceService;
    private final EventService eventService;
    private final StudentService studentService;
    private final Grid<EventAttendance> grid = new Grid<>(EventAttendance.class);

    @Autowired
    public EventAttendanceView(EventAttendanceService eventAttendanceService, EventService eventService, StudentService studentService) {
        this.eventAttendanceService = eventAttendanceService;
        this.eventService = eventService;
        this.studentService = studentService;

        Span title = new Span("Event Attendance");

        add(title);
        GridContextMenu<EventAttendance> contextMenu = grid.addContextMenu();
        contextMenu.addItem(
                "Add", event -> addAttendance()
        );
        contextMenu.addItem(
                "Edit", event -> editAttendance()
        );
        contextMenu.addItem(
                "Delete", event -> deleteAttendance()
        );
        add(contextMenu);
        add(grid);
        listAttendances();
    }

    private void listAttendances() {
        grid.setItems(eventAttendanceService.getAttendances());
    }

    private void addAttendance() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        Select<Event> eventSelect = new Select<>();
        eventSelect.setLabel("Event");
        eventSelect.setItems(eventService.getEvents());
        eventSelect.setItemLabelGenerator(Event::getEventName);
        formLayout.add(eventSelect);

        Select<Student> studentSelect = new Select<>();
        studentSelect.setLabel("Student");
        studentSelect.setItems(studentService.getStudents());
        studentSelect.setItemLabelGenerator(Student::getFirstName);
        formLayout.add(studentSelect);

        DatePicker attendanceDateField = new DatePicker();
        attendanceDateField.setLabel("Attendance Date");
        formLayout.add(attendanceDateField);

        Button saveButton = new Button("Save", click -> {
            EventAttendance attendance = new EventAttendance();
            attendance.setAttendanceDate(attendanceDateField.getValue());
            attendance.setEvent(eventSelect.getValue());
            attendance.setStudent(studentSelect.getValue());
            eventAttendanceService.saveAttendance(attendance);
            dialog.close();
            listAttendances();
        });

        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void deleteAttendance() {
        EventAttendance selectedAttendance = grid.asSingleSelect().getValue();
        if (selectedAttendance != null) {
            eventAttendanceService.deleteAttendance(selectedAttendance.getAttendanceId());
            listAttendances();
        }
    }

    private void editAttendance() {
        EventAttendance selectedAttendance = grid.asSingleSelect().getValue();
        if (selectedAttendance != null) {
            Dialog dialog = new Dialog();
            FormLayout formLayout = new FormLayout();

            Select<Event> eventSelect = new Select<>();
            eventSelect.setLabel("Event");
            eventSelect.setItems(eventService.getEvents());
            eventSelect.setItemLabelGenerator(Event::getEventName);
            eventSelect.setValue(selectedAttendance.getEvent());
            formLayout.add(eventSelect);

            Select<Student> studentSelect = new Select<>();
            studentSelect.setLabel("Student");
            studentSelect.setItems(studentService.getStudents());
            studentSelect.setItemLabelGenerator(Student::getFirstName);
            studentSelect.setValue(selectedAttendance.getStudent());
            formLayout.add(studentSelect);

            DatePicker attendanceDateField = new DatePicker();
            attendanceDateField.setLabel("Attendance Date");
            attendanceDateField.setValue(selectedAttendance.getAttendanceDate());
            formLayout.add(attendanceDateField);

            Button saveButton = new Button("Save", click -> {
                selectedAttendance.setAttendanceDate(attendanceDateField.getValue());
                selectedAttendance.setEvent(eventSelect.getValue());
                selectedAttendance.setStudent(studentSelect.getValue());
                eventAttendanceService.updateAttendance(selectedAttendance.getAttendanceId(), selectedAttendance);
                dialog.close();
                listAttendances();
            });

            formLayout.add(saveButton);
            dialog.add(formLayout);
            dialog.open();
        }
    }
}
