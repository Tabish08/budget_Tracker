package com.prakhar.budget_tracker;

public class Expense {
    private final String category;
    private final String amount;
    private final String datetime;
    private final String note;

    public Expense(String category, String amount, String datetime, String note) {
        this.category = category;
        this.amount = amount;
        this.datetime = datetime;
        this.note = note;
    }

    public String getCategory() {
        return category;
    }

    public String getAmount() {
        return amount;
    }

    public String getDateTime() {
        return datetime;
    }

    public String getNote() {
        return note;
    }
}
