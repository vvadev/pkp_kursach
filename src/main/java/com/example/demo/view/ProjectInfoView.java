package com.example.demo.view;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("projectinfo")
@PageTitle("Project Info")
@AnonymousAllowed
public class ProjectInfoView extends VerticalLayout {

    public ProjectInfoView() {
        setSpacing(true);

        // Информация о проекте
        Paragraph projectInfo = new Paragraph("Этот проект является демонстрацией использования Vaadin с Spring Boot. Разработано в рамках курсовой работы по дисциплине Программирование Кроссплатформенных Приложений");
        add(projectInfo);

        // Перенос строки
        add(new Paragraph(""));

        // Ссылки на все существующие роуты
        Paragraph linksText = new Paragraph(" Ссылки на все существующие страницы:");
        add(linksText);


        // Ссылки на каждую страницу
        add(createLink("Students", "students"));
        add(createLink("Courses", "courses"));
        add(createLink("Departments", "departments"));
        add(createLink("Professors", "professors"));
        add(createLink("Classrooms", "classrooms"));
        add(createLink("Enrollments", "enrollments"));
        add(createLink("Schedules", "schedules"));
        add(createLink("Assignments", "assignments"));
        add(createLink("Grades", "grades"));
        add(createLink("Clubs", "clubs"));
        add(createLink("Club Memberships", "clubmemberships"));
        add(createLink("Library Books", "librarybooks"));
        add(createLink("Book Loans", "bookloans"));
        add(createLink("Events", "events"));
        add(createLink("Event Attendance", "eventattendance"));
    }

    private Anchor createLink(String text, String route) {
        return new Anchor(route, text);
    }
}
