package com.example.demo.model.enums;

public enum MembershipDuration {
    MONTHLY(1),
    QUARTERLY(3),
    YEARLY(12);

    private final int months;

    MembershipDuration(int months) {
        this.months = months;
    }

    public int getMonths() {
        return months;
    }
}
