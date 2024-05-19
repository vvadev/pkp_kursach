package com.example.demo.view;

import com.example.demo.entity.Event;
import com.example.demo.entity.EventAttendance;
import com.example.demo.service.EventService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
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

import java.time.LocalDate;

@Route("events")
@PageTitle("Events View")
@RolesAllowed("USER")
public class EventView extends VerticalLayout {
    private final EventService eventService;
    private final Grid<Event> grid = new Grid<>(Event.class);

    @Autowired
    public EventView(EventService eventService) {
        this.eventService = eventService;

        Span title = new Span("Event");

        add(title);
        GridContextMenu<Event> contextMenu = grid.addContextMenu();
        contextMenu.addItem(
                "Add", event -> addEvent()
        );
        contextMenu.addItem(
                "Edit", event -> editEvent()
        );
        contextMenu.addItem(
                "Delete", event -> deleteEvent()
        );
        add(contextMenu);
        add(grid);
        listEvents();
    }

    private void listEvents() {
        grid.setItems(eventService.getEvents());
    }

    private void addEvent() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField eventNameField = new TextField();
        eventNameField.setLabel("Event Name");
        formLayout.add(eventNameField);

        DatePicker eventDateField = new DatePicker();
        eventDateField.setLabel("Event Date");
        formLayout.add(eventDateField);

        TextField locationField = new TextField();
        locationField.setLabel("Location");
        formLayout.add(locationField);

        Button saveButton = new Button("Save", click -> {
            Event event = new Event();
            event.setEventName(eventNameField.getValue());
            event.setEventDate(eventDateField.getValue());
            event.setLocation(locationField.getValue());
            eventService.saveEvent(event);
            dialog.close();
            listEvents();
        });

        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void deleteEvent() {
        Event selectedEvent = grid.asSingleSelect().getValue();
        if (selectedEvent != null) {
            eventService.deleteEvent(selectedEvent.getEventId());
            listEvents();
        }
    }

    private void editEvent() {
        Event selectedEvent = grid.asSingleSelect().getValue();
        if (selectedEvent != null) {
            Dialog dialog = new Dialog();
            FormLayout formLayout = new FormLayout();

            TextField eventNameField = new TextField();
            eventNameField.setLabel("Event Name");
            eventNameField.setValue(selectedEvent.getEventName());
            formLayout.add(eventNameField);

            DatePicker eventDateField = new DatePicker();
            eventDateField.setLabel("Event Date");
            eventDateField.setValue(selectedEvent.getEventDate());
            formLayout.add(eventDateField);

            TextField locationField = new TextField();
            locationField.setLabel("Location");
            locationField.setValue(selectedEvent.getLocation());
            formLayout.add(locationField);

            Button saveButton = new Button("Save", click -> {
                selectedEvent.setEventName(eventNameField.getValue());
                selectedEvent.setEventDate(eventDateField.getValue());
                selectedEvent.setLocation(locationField.getValue());
                eventService.updateEvent(selectedEvent.getEventId(), selectedEvent);
                dialog.close();
                listEvents();
            });

            formLayout.add(saveButton);
            dialog.add(formLayout);
            dialog.open();
        }
    }
}
