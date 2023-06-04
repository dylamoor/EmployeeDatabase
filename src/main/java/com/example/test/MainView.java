package com.example.test;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Theme(value = Lumo.class, variant = Lumo.DARK)

@Route("")
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    /**
	 * 
	 */ 
	private Grid<Employee> grid;
    private Button addButton;
    private Button searchButton;
    private Button paySheetButton;
	

	public MainView() {
		
		String url = "jdbc:mysql://localhost:3306/employee";
        String user = "root";
        String password = "Pass1!word";
        

        
        List<Employee> employees = new ArrayList<>();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading MySQL Connector/J JDBC Driver: " + e);
        }
        
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM employee");
            while (rs.next()) {
            	Employee employee = new Employee();
                employee.setId(rs.getInt("id"));
                employee.setName(rs.getString("name"));
                employee.setSalary(rs.getDouble("salary"));
                employee.setDepartment(rs.getString("department"));
                employee.setStreet1(rs.getString("street1"));
                employee.setStreet2(rs.getString("street2"));
                employee.setCity(rs.getString("city"));
                employee.setZipCode(rs.getInt("zipcode"));
                employee.setState(rs.getString("state"));
                employee.setHrAccess(rs.getBoolean("hr_access")); // Make sure to set the hrAccess property of the Employee object
                employees.add(employee);
            }
            con.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        


        
        grid = new Grid<>(Employee.class);
        grid.setItems(employees);
        grid.setColumns("name", "id", "department");


        grid.addItemClickListener(event -> {
            Employee employee = event.getItem();
            Integer currentUserId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
            Boolean hrAccess = (Boolean) VaadinSession.getCurrent().getAttribute("hrAccess");

            if (hrAccess || (currentUserId != null && currentUserId == employee.getId())) {
                EmployeeDetailsWindow window = new EmployeeDetailsWindow(employee);
                window.open();
            } else {
                Notification.show("You do not have permission to view this employee's details.");
            }
        });
    
        
        Label header1 = new Label("HR Hero: An Employee Management System");
        header1.getElement().getStyle().set("text-align", "center");
        header1.setWidth("100%");
        header1.getElement().getStyle().set("font-size", "24px");
        
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setWidthFull();
        contentLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        contentLayout.add(header1);
        contentLayout.add(grid);
        
        add(contentLayout);
        
        addButton = new Button("Add Employee");
        addButton.setWidth("150px");
        addButton.addClickListener(event -> {
            AddEmployeeDialog addEmployeeDialog = new AddEmployeeDialog();
            addEmployeeDialog.open();
        });
        
        searchButton = new Button("Search by ID");
        searchButton.setWidth("200px");
        searchButton.addClickListener(event -> {
            SearchByIDDialog searchDialog = new SearchByIDDialog(employees);
            searchDialog.open();
        });

        Button logoutButton = new Button("Logout");
        logoutButton.addClickListener(event -> {
            VaadinSession.getCurrent().setAttribute("userId", null);
            VaadinSession.getCurrent().setAttribute("hrAccess", null);
            event.getSource().getUI().ifPresent(ui -> ui.navigate("login"));
        });
        

		paySheetButton = new Button("Create Pay Sheet");
		paySheetButton.setWidth("200px");
		paySheetButton.addClickListener(event -> {
		// Create the dialog window
			Dialog paySheetDialog = new Dialog();
			paySheetDialog.setCloseOnEsc(true);
			paySheetDialog.setCloseOnOutsideClick(true);
			paySheetDialog.setWidth("90vw"); // Set width to 90% of viewport width
			
			// Create the department selector dropdown
			ComboBox<String> departmentSelector = new ComboBox<>();
			departmentSelector.setLabel("Department");
			departmentSelector.setItems("Sales", "Marketing", "IT", "HR", "Engineering","Accounting", "All");
			departmentSelector.setValue("Sales");
			
			// Create the exit button
			Button exitButton = new Button("Exit");
			exitButton.addClickListener(exitEvent -> {
			    paySheetDialog.close();
			});
			
			Button submitButton = new Button("Submit");
			submitButton.addClickListener(submitEvent -> {
			    // Get selected department and employees
			    String selectedDepartment = departmentSelector.getValue();
			    List<Employee> departmentEmployees;
	
			    if (selectedDepartment.equals("All")) {
			        departmentEmployees = employees;
			    } else {
			        departmentEmployees = employees.stream()
			                .filter(employee -> selectedDepartment.equals(employee.getDepartment()))
			                .collect(Collectors.toList());
			    }
	
			    // Calculate salaries and create table
			    Grid<PaySheetRow> paySheetGrid = new Grid<>(PaySheetRow.class);
			    paySheetGrid.setItems(departmentEmployees.stream()
			            .map(employee -> new PaySheetRow(employee.getName(), employee.getId(), employee.getSalary() / 26.0))
			            .collect(Collectors.toList()));
			    paySheetGrid.getColumnByKey("id").setHeader("ID");
			    paySheetGrid.getColumnByKey("name").setHeader("Name");
			    paySheetGrid.getColumnByKey("biWeeklyPay").setHeader("Bi Weekly Pay");
			    paySheetGrid.setColumns("id", "name", "biWeeklyPay");
			    
			    // Calculate total salary for the department
			    double totalSalary = departmentEmployees.stream()
			            .mapToDouble(employee -> Double.valueOf(employee.getSalary()) / 26.00)
			            .sum();
			    Label totalLabel = new Label(String.format("Total: $%.2f", totalSalary));
			    totalLabel.getElement().getStyle().set("font-weight", "bold");
	
			    // Create the pay sheet dialog content layout
			    VerticalLayout paySheetContentLayout = new VerticalLayout();
			    paySheetContentLayout.add(paySheetGrid, totalLabel);
			    paySheetContentLayout.setAlignItems(Alignment.CENTER);
	
			    // Add the content layout to the dialog window
			    paySheetDialog.removeAll();
			    paySheetDialog.add(paySheetContentLayout, exitButton);
			});
	
	
			// Create the dialog buttons layout
			HorizontalLayout buttonsLayout = new HorizontalLayout();
			buttonsLayout.setWidthFull();
			buttonsLayout.setJustifyContentMode(JustifyContentMode.CENTER);
			buttonsLayout.add(submitButton, exitButton);
	
			// Create the pay sheet dialog layout
			VerticalLayout paySheetLayout = new VerticalLayout();
			paySheetLayout.setAlignItems(Alignment.CENTER);
			paySheetLayout.add(departmentSelector, buttonsLayout);
			paySheetDialog.add(paySheetLayout);
	
			// Open the pay sheet dialog
			paySheetDialog.open();
			
			});
       
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();
        buttonLayout.getElement().getStyle().set("justify-content", "center");
        buttonLayout.add(addButton, searchButton, paySheetButton, logoutButton);
        

        add(buttonLayout);
        
        Broadcaster.register(this::refreshView);
        
    }
	
	@Override
    public void beforeEnter(BeforeEnterEvent event) {
        Integer userId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
        Boolean hrAccess = (Boolean) VaadinSession.getCurrent().getAttribute("hrAccess");

        if (userId == null || hrAccess == null) {
            event.forwardTo("login");
        } else {
            updateComponentsVisibility(hrAccess);
        }
    }
	
	 private void updateComponentsVisibility(boolean hrAccess) {
	        grid.setEnabled(true);
	        addButton.setEnabled(hrAccess);
	        searchButton.setEnabled(hrAccess);
	        paySheetButton.setEnabled(hrAccess);
	    }
	 
	 private void refreshView(Boolean message) {
		    // Get the current user's ID and HR access from the session
		    Integer userId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
		    Boolean hrAccess = (Boolean) VaadinSession.getCurrent().getAttribute("hrAccess");

		    // Update the UI components' visibility based on the user's access level
		    updateComponentsVisibility(hrAccess);
		}
 }

