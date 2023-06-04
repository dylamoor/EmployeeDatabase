package com.example.test;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import java.util.List;

public class SearchByIDDialog extends Dialog {

    /**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	public SearchByIDDialog(List<Employee> employees) {
        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        layout.setPadding(true);

        Label header = new Label("Search by ID");
        header.getElement().getStyle().set("font-size", "20px");
        layout.add(header);

        NumberField idField = new NumberField();
        idField.setLabel("Enter ID");
        layout.add(idField);

        Button searchButton = new Button("Search");
        searchButton.addClickListener(event -> {
            int id = idField.getValue().intValue();
            Employee employee = null;
            for (Employee e : employees) {
                if (e.getId() == id) {
                    employee = e;
                    break;
                }
            }
            if (employee != null) {
                EmployeeDetailsWindow window = new EmployeeDetailsWindow(employee);
                window.open();
                this.close();
            } else {
                Notification.show("Employee with ID " + id + " not found.");
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> this.close());

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(searchButton, cancelButton);
        layout.add(buttonLayout);

        add(layout);
    }
}