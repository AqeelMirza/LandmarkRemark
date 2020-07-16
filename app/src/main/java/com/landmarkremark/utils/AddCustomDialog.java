package com.landmarkremark.utils;

import com.landmarkremark.models.MarkedNote;
import com.landmarkremark.models.User;

public interface AddCustomDialog {

    //Adding Notes to the user
    void promptAndAcceptNote(User user, MarkedNote note, Runnable runnable);

    //Viewing Notes
    void viewNotes(MarkedNote note);
}
