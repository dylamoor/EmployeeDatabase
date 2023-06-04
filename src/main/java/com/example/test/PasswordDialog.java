package com.example.test;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PasswordDialog extends Dialog {

    private final String url = "jdbc:mysql://localhost:3306/employee";
    private final String user = "root";
    private final String password = "Pass1!word";
    private final int userId;

    public PasswordDialog(int userId) {
        this.userId = userId;
        setWidth("400px");
        setHeight("200px");
        setModal(true);

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setSpacing(true);

        PasswordField newPasswordField = new PasswordField("New Password");
        layout.add(newPasswordField);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button saveButton = new Button("Save");
        saveButton.addClickListener(event -> {
            String newPassword = newPasswordField.getValue();
            try {
                Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement stmt = conn.prepareStatement("UPDATE employee SET password=? WHERE id=?");
                stmt.setString(1, newPassword);
                stmt.setInt(2, userId);
                stmt.executeUpdate();
                Notification.show("Password updated successfully.");
                conn.close();
                close(); // Close the dialog
            } catch (SQLException ex) {
                Notification.show("Error: " + ex.getMessage());
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> close());
        buttonLayout.add(saveButton, cancelButton);
        layout.add(buttonLayout);

        add(layout);
    }
}
