package com.Entity;

public class User {
    private int id;
    private String name;
    private String email;
    private String location;
    private String fatherName;
    private String motherName;

    public User(int id, String name, String email, String location, String fatherName, String motherName) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.location = location;
        this.fatherName = fatherName;
        this.motherName = motherName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }
}

