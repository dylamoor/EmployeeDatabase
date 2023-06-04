package com.example.test;

public class PaySheetRow {
    private String name;
    private int id;
    private String biWeeklyPay;

    public PaySheetRow(String name, int id, double salary) {
        this.name = name;
        this.id = id;
        this.biWeeklyPay = String.format("$%.2f", salary);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getBiWeeklyPay() {
        return biWeeklyPay;
    }
}