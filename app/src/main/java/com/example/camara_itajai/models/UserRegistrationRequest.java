package com.example.camara_itajai.models;

public class UserRegistrationRequest {
    private final String name;
    private final String email;
    private final String phone;

    public UserRegistrationRequest(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getters e setters
}