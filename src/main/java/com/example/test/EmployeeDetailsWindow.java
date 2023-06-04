package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;

public class EmployeeDetailsWindow extends Dialog {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String url = "jdbc:mysql://localhost:3306/employee";
    private final String user = "root";
    private final String password = "Pass1!word";

	    public EmployeeDetailsWindow(Employee employee) {
	    	
	    	Boolean hrAccess = (Boolean) VaadinSession.getCurrent().getAttribute("hrAccess");
	    	
	        setWidth("500px");
	        setHeight("600px");
	        setModal(true);

	        VerticalLayout layout = new VerticalLayout();
	        layout.setPadding(true);
	        layout.setSpacing(true);

	        Label nameLabel = new Label("Name: " + employee.getName());
	        Label idLabel = new Label("ID: " + employee.getId());
	        Label salaryLabel = new Label("Salary: " + employee.getSalary());
	        Label departmentLabel = new Label("Department: " + employee.getDepartment());
	        Label street1Label = new Label("Street 1: " + employee.getStreet1());
	        Label street2Label = new Label("Street 2: " + employee.getStreet2());
	        Label cityLabel = new Label("City: " + employee.getCity());
	        Label zipCodeLabel = new Label("Zip Code: " + employee.getZipCode());
	        Label stateLabel = new Label("State: " + employee.getState());
	        Label hrAccessLabel = new Label("HR Access: " + employee.isHrAccess());
	        
	        layout.add(nameLabel, idLabel, salaryLabel, departmentLabel, street1Label, street2Label, cityLabel, zipCodeLabel, stateLabel, hrAccessLabel);
	        
	        HorizontalLayout buttonLayout = new HorizontalLayout();
	        Button exitButton = new Button("Exit");
	        exitButton.addClickListener(event -> close());
	        
	        Button editPasswordButton = new Button("Edit Password");
	        editPasswordButton.addClickListener(event -> {
	            PasswordDialog passwordDialog = new PasswordDialog(employee.getId());
	            passwordDialog.open();
	        });
	        
	        Button editButton = new Button("Edit");
	        editButton.setEnabled(hrAccess);
	        editButton.addClickListener(event -> {
	            // Open the EditEmployeeDialog and pre-populate the fields with the current employee information
	            EditEmployeeDialog editDialog = new EditEmployeeDialog();
	            editDialog.open();
	            // Pre-populate the fields with the current employee information
	            editDialog.getNameField().setValue(employee.getName());
	            editDialog.getSalaryField().setValue(String.valueOf(employee.getSalary()));
	            editDialog.getDepartmentField().setValue(employee.getDepartment());
	            editDialog.getStreet1Field().setValue(employee.getStreet1());
	            editDialog.getStreet2Field().setValue(employee.getStreet2());
	            editDialog.getCityField().setValue(employee.getCity());
	            editDialog.getZipCodeField().setValue(String.valueOf(employee.getZipCode()));
	            editDialog.getStateField().setValue(employee.getState());
	            editDialog.getHrAccessCheckbox().setValue(employee.isHrAccess());

	            // Add a click listener to the "Update" button
	            editDialog.getAddButton().addClickListener(event2 -> {
	                // Get the updated information from the text fields
	                String newName = editDialog.getNameField().getValue();
	                double newSalary = Double.parseDouble(editDialog.getSalaryField().getValue());
	                String newDepartment = editDialog.getDepartmentField().getValue();
	                String newStreet1 = editDialog.getStreet1Field().getValue();
	                String newStreet2 = editDialog.getStreet2Field().getValue();
	                String newCity = editDialog.getCityField().getValue();
	                int newZipCode = Integer.parseInt(editDialog.getZipCodeField().getValue());
	                String newState = editDialog.getStateField().getValue();
	                boolean newHrAccess = editDialog.getHrAccessCheckbox().getValue();

	                // Update the employee in the database
	                try {
	                    Connection conn = DriverManager.getConnection(url, user, password);
	                    PreparedStatement stmt = conn.prepareStatement("UPDATE employee SET name=?, salary=?, "
	                            + "department=?, street1=?, street2=?, city=?, zipcode=?, state=?, hr_access=? WHERE id=?");
	                    stmt.setString(1, newName);
	                    stmt.setDouble(2, newSalary);
	                    stmt.setString(3, newDepartment);
	                    stmt.setString(4, newStreet1);
	                    stmt.setString(5, newStreet2);
	                    stmt.setString(6, newCity);
	                    stmt.setInt(7, newZipCode);
	                    stmt.setString(8, newState);
	                    stmt.setBoolean(9, newHrAccess); // Set the hr_access value before setting the id value
	                    stmt.setInt(10, employee.getId());
	                    stmt.executeUpdate();
	                    Notification.show("Employee updated successfully.");
	                    conn.close();
	                    editDialog.close(); // Close the dialog
	                } catch (SQLException ex) {
	                    Notification.show("Error: " + ex.getMessage());
	                }
	            });
	        });
	        
	    
	        

	        
	        Button deleteButton = new Button("Delete");
	        deleteButton.setEnabled(hrAccess);
	        deleteButton.addClickListener(event -> {
	            try {
	                Class.forName("com.mysql.cj.jdbc.Driver");
	                Connection con = DriverManager.getConnection(url, user, password);
	                PreparedStatement stmt = con.prepareStatement("DELETE FROM employee WHERE id=?");
	                stmt.setInt(1, employee.getId());
	                stmt.executeUpdate();
	                con.close();
	                close();
	                getUI().ifPresent(ui -> ui.navigate(""));
	            } catch (ClassNotFoundException | SQLException e) {
	                System.out.println("Error: " + e.getMessage());
	            }
	            
	         // Refresh the page after deleting the employee
	            getUI().ifPresent(ui -> ui.navigate(""));
	        });
	        
	        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
	        buttonLayout.add(editButton, editPasswordButton, deleteButton, exitButton);
	        layout.add(buttonLayout);
	        
	        
	        add(layout);
	    }
	}