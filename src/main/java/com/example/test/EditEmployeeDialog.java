package com.example.test;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.checkbox.Checkbox;

public class EditEmployeeDialog extends Dialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextField nameField;
    private TextField salaryField;
    private Select<String> departmentField = new Select<>();
    private TextField street1Field;
    private TextField street2Field;
    private TextField cityField;
    private TextField zipCodeField;
    private TextField stateField;
    private Button addButton;
    private Button exitButton;
    private Checkbox hrAccessCheckbox = new Checkbox();

    public EditEmployeeDialog() {
    	
        // Set the dialog properties
        setWidth("500px");
        setHeight("600px");
        setModal(true);

        // Create the form components
        nameField = new TextField("Name");
        salaryField = new TextField("Salary");
        Label departmentLabel = new Label("Department:");
        departmentField.setPlaceholder("Select department");
        departmentField.setItems("HR", "Sales", "Engineering", "IT", "Marketing", "Accounting");
        street1Field = new TextField("Street 1");
        street2Field = new TextField("Street 2");
        cityField = new TextField("City");
        zipCodeField = new TextField("Zip Code");
        stateField = new TextField("State");
        addButton = new Button("Update");
        Label hrAccessLabel = new Label("HR Access: ");
        hrAccessCheckbox = new Checkbox();

        
        exitButton = new Button("Exit", event -> close());

        // Add the components to the dialog
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(true);
        layout.setSpacing(true);
        layout.add(nameField, salaryField, departmentLabel, departmentField, street1Field, street2Field, cityField, zipCodeField, stateField, hrAccessLabel, hrAccessCheckbox);
        
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(addButton, exitButton);
        layout.add(buttonLayout);
        
        add(layout);
        
    }

    public TextField getNameField() {
        return nameField;
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
    
    public Checkbox getHrAccessCheckbox() {
        return hrAccessCheckbox;
    }


}