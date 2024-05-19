package com.example.demo.view;

import com.example.demo.entity.Classroom;
import com.example.demo.entity.Course;
import com.example.demo.entity.Professor;
import com.example.demo.entity.Schedule;
import com.example.demo.service.ClassroomService;
import com.example.demo.service.CourseService;
import com.example.demo.service.ProfessorService;
import com.example.demo.service.ScheduleService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Route("schedules")
@PageTitle("Schedule View")
@RolesAllowed("USER")
public class ScheduleView extends VerticalLayout {
    private final ScheduleService scheduleService;
    private final CourseService courseService;
    private final ClassroomService classroomService;
    private final ProfessorService professorService;
    private final Grid<Schedule> grid = new Grid<>(Schedule.class);

    @Autowired
    public ScheduleView(ScheduleService scheduleService, CourseService courseService,
                        ClassroomService classroomService, ProfessorService professorService) {
        this.scheduleService = scheduleService;
        this.courseService = courseService;
        this.classroomService = classroomService;
        this.professorService = professorService;

        Span title = new Span("Schedule");

        add(title);
        GridContextMenu<Schedule> contextMenu = grid.addContextMenu();
        contextMenu.addItem(
                "Add", event -> addSchedule()
        );
        contextMenu.addItem(
                "Edit", event -> editSchedule()
        );
        contextMenu.addItem(
                "Delete", event -> deleteSchedule()
        );
        add(contextMenu);
        add(grid);
        listSchedules();
    }

    private void listSchedules() {
        grid.setItems(scheduleService.getSchedules());
    }

    private void addSchedule() {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        Select<Course> courseSelect = new Select<>();
        courseSelect.setLabel("Course");
        courseSelect.setItems(courseService.getCourses());
        courseSelect.setItemLabelGenerator(Course::getCourseName);
        formLayout.add(courseSelect);

        Select<Classroom> classroomSelect = new Select<>();
        classroomSelect.setLabel("Classroom");
        classroomSelect.setItems(classroomService.getClassrooms());
        classroomSelect.setItemLabelGenerator(classroom -> classroom.getBuilding() + " " + classroom.getRoomNumber());
        formLayout.add(classroomSelect);

        Select<Professor> professorSelect = new Select<>();
        professorSelect.setLabel("Professor");
        professorSelect.setItems(professorService.getProfessors());
        professorSelect.setItemLabelGenerator(professor -> professor.getFirstName() + " " + professor.getLastName());
        formLayout.add(professorSelect);

        Select<DayOfWeek> dayOfWeekSelect = new Select<>();
        dayOfWeekSelect.setLabel("Day of Week");
        dayOfWeekSelect.setItems(DayOfWeek.values());
        formLayout.add(dayOfWeekSelect);

        TimePicker startTimePicker = new TimePicker();
        startTimePicker.setLabel("Start Time");
        formLayout.add(startTimePicker);

        TimePicker endTimePicker = new TimePicker();
        endTimePicker.setLabel("End Time");
        formLayout.add(endTimePicker);

        Button saveButton = new Button("Save", click -> {
            Schedule schedule = new Schedule();
            schedule.setCourse(courseSelect.getValue());
            schedule.setClassroom(classroomSelect.getValue());
            schedule.setProfessor(professorSelect.getValue());
            schedule.setDayOfWeek(dayOfWeekSelect.getValue());
            schedule.setStartTime(startTimePicker.getValue());
            schedule.setEndTime(endTimePicker.getValue());
            scheduleService.saveSchedule(schedule);
            dialog.close();
            listSchedules();
        });

        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }

    private void deleteSchedule() {
        Schedule selectedSchedule = grid.asSingleSelect().getValue();
        if (selectedSchedule != null) {
            scheduleService.deleteSchedule(selectedSchedule.getScheduleId());
            listSchedules();
        }
    }

    private void editSchedule() {
        Schedule selectedSchedule = grid.asSingleSelect().getValue();
        if (selectedSchedule != null) {
            Dialog dialog = new Dialog();
            FormLayout formLayout = new FormLayout();

            Select<Course> courseSelect = new Select<>();
            courseSelect.setLabel("Course");
            courseSelect.setItems(courseService.getCourses());
            courseSelect.setItemLabelGenerator(Course::getCourseName);
            courseSelect.setValue(selectedSchedule.getCourse());
            formLayout.add(courseSelect);

            Select<Classroom> classroomSelect = new Select<>();
            classroomSelect.setLabel("Classroom");
            classroomSelect.setItems(classroomService.getClassrooms());
            classroomSelect.setItemLabelGenerator(classroom -> classroom.getBuilding() + " " + classroom.getRoomNumber());
            classroomSelect.setValue(selectedSchedule.getClassroom());
            formLayout.add(classroomSelect);

            Select<Professor> professorSelect = new Select<>();
            professorSelect.setLabel("Professor");
            professorSelect.setItems(professorService.getProfessors());
            professorSelect.setItemLabelGenerator(professor -> professor.getFirstName() + " " + professor.getLastName());
            professorSelect.setValue(selectedSchedule.getProfessor());
            formLayout.add(professorSelect);

            Select<DayOfWeek> dayOfWeekSelect = new Select<>();
            dayOfWeekSelect.setLabel("Day of Week");
            dayOfWeekSelect.setItems(DayOfWeek.values());
            dayOfWeekSelect.setValue(selectedSchedule.getDayOfWeek());
            formLayout.add(dayOfWeekSelect);

            TimePicker startTimePicker = new TimePicker();
            startTimePicker.setLabel("Start Time");
            startTimePicker.setValue(selectedSchedule.getStartTime());
            formLayout.add(startTimePicker);

            TimePicker endTimePicker = new TimePicker();
            endTimePicker.setLabel("End Time");
            endTimePicker.setValue(selectedSchedule.getEndTime());
            formLayout.add(endTimePicker);

            Button saveButton = new Button("Save", click -> {
                selectedSchedule.setCourse(courseSelect.getValue());
                selectedSchedule.setClassroom(classroomSelect.getValue());
                selectedSchedule.setProfessor(professorSelect.getValue());
                selectedSchedule.setDayOfWeek(dayOfWeekSelect.getValue());
                selectedSchedule.setStartTime(startTimePicker.getValue());
                selectedSchedule.setEndTime(endTimePicker.getValue());
                scheduleService.updateSchedule(selectedSchedule.getScheduleId(), selectedSchedule);
                dialog.close();
                listSchedules();
            });

            formLayout.add(saveButton);
            dialog.add(formLayout);
            dialog.open();
        }
    }
}
