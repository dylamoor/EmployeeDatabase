package com.example.test;

import java.text.DecimalFormat;

public class Employee {
    private int ssn;
    private String name;
    private String department;
    private double salary;
    private String street1;
    private String street2;
    private String city;
    private int zipCode;
    private String state;
    private boolean hrAccess;

    
    public Employee() {
        this.ssn = 0;
        this.name = null;
        this.department = null;
        this.salary = 0;
        this.street1 = null;
        this.street2 = null;
        this.city = null;
        this.zipCode = 0;
        this.state = null;
    }

    public Employee(int id, String name, double salary, String department, String street1, String street2, String city, int zipCode, String state, boolean hrAccess) {
        this.ssn = id;
        this.name = name;
        this.salary = salary;
        this.department = department;
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.zipCode = zipCode;
        this.state = state;
        this.hrAccess = hrAccess;
    }

    public int getId() {
        return ssn;
    }
    
    public void setId(int l) {
        this.ssn = l;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }

    public double getSalary() {
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedNumber = df.format(salary);
        double roundedNumber = Double.parseDouble(formattedNumber);
        return roundedNumber;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public boolean isHrAccess() {
        return hrAccess;
    }

    public void setHrAccess(boolean hrAccess) {
        this.hrAccess = hrAccess;
    }
}