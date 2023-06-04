package com.example.test;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Route("login")
public class LoginView extends VerticalLayout {
    private NumberField idField;
    private PasswordField passwordField;

    public LoginView() {

        Label header = new Label("Welcome to HR Hero");
        header.getStyle().set("font-size", "24px").set("font-weight", "bold");

        idField = new NumberField("Employee ID");
        passwordField = new PasswordField("Password");
        Button loginButton = new Button("Login", event -> loginUser());

        VerticalLayout container = new VerticalLayout();
        container.add(header, idField, passwordField, loginButton);
        container.setAlignItems(Alignment.CENTER);
        container.setJustifyContentMode(JustifyContentMode.CENTER);
        container.setHeight("100vh");

        add(container);

        // Center the layout vertically and horizontally
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private void loginUser() {
        int employeeId = idField.getValue().intValue();
        String password = passwordField.getValue();

        String url = "jdbc:mysql://localhost:3306/employee";
        String user = "root";
        String dbPassword = "Pass1!word";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, dbPassword);

            PreparedStatement stmt = con.prepareStatement("SELECT id, hr_access FROM employee WHERE id=? AND password=?");
            stmt.setInt(1, employeeId);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                boolean hrAccess = rs.getBoolean("hr_access");
                VaadinSession.getCurrent().setAttribute("userId", id);
                VaadinSession.getCurrent().setAttribute("hrAccess", hrAccess);
                getUI().ifPresent(ui -> ui.navigate(""));
            } else {
                Notification.show("Invalid employee ID or password");
            }

            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            Notification.show("Error: " + e.getMessage());
        }
    }
}


