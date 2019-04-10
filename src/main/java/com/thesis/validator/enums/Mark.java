package com.thesis.validator.enums;

public enum Mark {
    ZERO(0.0),
    LOW(5),
    MID(7.5),
    HIGH(10.0);

    private double val;

    Mark(final double val) {
        this.val = val;
    }

    public double getValue() {
        return val;
    }
}
