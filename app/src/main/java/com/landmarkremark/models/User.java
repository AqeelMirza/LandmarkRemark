package com.landmarkremark.models;

import java.util.List;

public class User {

    private String id;
    private String name;
    private String email;
    private List<MarkedNote> markedNotes;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<MarkedNote> getMarkedNotes() {
        return markedNotes;
    }

    public void setMarkedNotes(List<MarkedNote> markedNotes) {
        this.markedNotes = markedNotes;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
