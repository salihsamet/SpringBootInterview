package com.sample.enums;

public enum UserRole {
    DELIVERY_MAN("DELIVERY_MAN"),
    USER("USER");

    private String value;

    private UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
