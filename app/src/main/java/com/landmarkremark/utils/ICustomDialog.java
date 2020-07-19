package com.landmarkremark.utils;

import com.landmarkremark.models.MarkedNote;
import com.landmarkremark.models.User;

public interface ICustomDialog {

    //Viewing Notes
    void viewNotes(MarkedNote note);

    void createDialog();
}
