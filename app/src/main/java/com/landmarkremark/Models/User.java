package com.landmarkremark.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    String name;
    String email;
    String id;
    MarkedNote markedNote;

    public MarkedNote getMarkedNote() {
        return markedNote;
    }

    public void setMarkedNote(MarkedNote markedNote) {
        this.markedNote = markedNote;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void fromJson(JSONObject object) throws JSONException {

        setId(object.getString("id"));
        setEmail(object.getString("email"));
        setName(object.getString("name"));


    }
}
