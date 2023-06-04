package com.example.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.checkbox.Checkbox;

public class AddEmployeeDialog extends Dialog {

    /**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private final String url = "jdbc:mysql://localhost:3306/employee";
    private final String user = "root";
    private final String password = "Pass1!word";

    private TextField nameField = new TextField();
    private TextField idField = new TextField();
    private TextField salaryField = new TextField();
    private Select<String> departmentField = new Select<>();
    private TextField street1Field = new TextField();
    private TextField street2Field = new TextField();
    private TextField cityField = new TextField();
    private TextField zipCodeField = new TextField();
    private TextField stateField = new TextField();
    private Button addButton;
    private Checkbox hrAccessCheckbox = new Checkbox();

    
    public AddEmployeeDialog() {
        setWidth("500px");
        setHeight("600px");
        setModal(true);

        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setSpacing(true);

        Label nameLabel = new Label("Name: ");
        nameField = new TextField();
        Label idLabel = new Label("ID: ");
        idField = new TextField();
        Label salaryLabel = new Label("Salary: ");
        salaryField = new TextField();
        Label departmentLabel = new Label("Department: ");
        departmentField.setItems("HR", "Sales", "Engineering", "IT", "Marketing", "Accounting");
        departmentField.setPlaceholder("Select department");
        Label street1Label = new Label("Street 1: ");
         street1Field = new TextField();
        Label street2Label = new Label("Street 2: ");
         street2Field = new TextField();
        Label cityLabel = new Label("City: ");
         cityField = new TextField();
        Label zipCodeLabel = new Label("Zip Code: ");
         zipCodeField = new TextField();
        Label stateLabel = new Label("State: ");
         stateField = new TextField();

        layout.add(nameLabel, nameField, idLabel, idField, salaryLabel, salaryField, departmentLabel, departmentField, street1Label, street1Field, street2Label, street2Field, cityLabel, cityField, zipCodeLabel, zipCodeField, stateLabel, stateField);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button addButton = new Button("Add");
        addButton.addClickListener(event -> {
        	
        	int newId = Integer.parseInt(idField.getValue()); // initialize the newId variable
            String newName = nameField.getValue(); // get the value from the nameField textfield
            double newSalary = Double.parseDouble(salaryField.getValue()); // get the value from the salaryField textfield
            String newDepartment = departmentField.getValue(); // get the value from drop down box
            String newStreet1 = street1Field.getValue(); // get the value from the street1 text field
            String newStreet2 = street2Field.getValue(); // get the value from the street2 text field
            String newCity = cityField.getValue(); // get the value from the city text field
            int newZipCode = Integer.parseInt(zipCodeField.getValue()); // get the value from the zipCode text field
            String newState = stateField.getValue(); // get the value from the state text field

         // Validate the input values
            if (newName.isEmpty()) {
                nameField.setErrorMessage("Name is required");
                return;
            } else {
                nameField.setErrorMessage(null);
            }

            if (newDepartment.isEmpty()) {
                departmentField.setErrorMessage("Department is required");
                return;
            } else {
                departmentField.setErrorMessage(null);
            }

            if (newStreet1.isEmpty()) {
                street1Field.setErrorMessage("Street 1 is required");
                return;
            } else {
                street1Field.setErrorMessage(null);
            }

            if (newCity.isEmpty()) {
                cityField.setErrorMessage("City is required");
                return;
            } else {
                cityField.setErrorMessage(null);
            }

            if (newState.isEmpty()) {
                stateField.setErrorMessage("State is required");
                return;
            } else {
                stateField.setErrorMessage(null);
            }

            try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = con.prepareStatement("SELECT id FROM employee WHERE id=?");
            stmt.setInt(1, newId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
            // id already exists in the database
            Notification.show("Error: Employee with ID " + newId + " already exists.");
            con.close();
            return;
            } else {
            	
            boolean hrAccess = hrAccessCheckbox.getValue();
            // id does not exist in the database, proceed with adding the new employee
            PreparedStatement insertStmt = con.prepareStatement("INSERT INTO employee (id, name, salary, department, street1, street2, city, zipcode, state, hr_access) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            insertStmt.setInt(1, newId);
            insertStmt.setString(2, newName);
            insertStmt.setDouble(3, newSalary);
            insertStmt.setString(4, newDepartment);
            insertStmt.setString(5, newStreet1);
            insertStmt.setString(6, newStreet2);
            insertStmt.setString(7, newCity);
            insertStmt.setInt(8, newZipCode);
            insertStmt.setString(9, newState);
            insertStmt.setBoolean(10, hrAccess);
            insertStmt.executeUpdate();
            Notification.show("Employee added successfully.");
            con.close();
            close(); // close the dialog
            }
            } catch (SQLException | ClassNotFoundException ex) {
            Notification.show("Error: " + ex.getMessage());
            }
        });
        
        // Add the HR Access checkbox to the layout
        Label hrAccessLabel = new Label("HR Access: ");
        hrAccessCheckbox = new Checkbox();
        layout.add(hrAccessLabel, hrAccessCheckbox);
        
        
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> this.close());

        buttonLayout.add(addButton, cancelButton);
        add(layout, buttonLayout);
        
    }
	
    public TextField getNameField() {
		return nameField;
	}

	public TextField getIdField() {
		return idField;
	}

	public TextField getSalaryField() {
		return salaryField;
	}

	public Select<String> getDepartmentField() {
		return departmentField;
	}

	public TextField getStreet1Field() {
		return street1Field;
	}

	public TextField getStreet2Field() {
		return street2Field;
	}

	public TextField getCityField() {
		return cityField;
	}

	public TextField getZipCodeField() {
		return zipCodeField;
	}

	public TextField getStateField() {
		return stateField;
	}

	public Button getAddButton() {
		return addButton;
	}
	
}
            