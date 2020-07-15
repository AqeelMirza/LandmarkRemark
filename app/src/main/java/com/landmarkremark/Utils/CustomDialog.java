package com.landmarkremark.Utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.landmarkremark.MainActivity;
import com.landmarkremark.Models.MarkedNote;
import com.landmarkremark.Models.User;
import com.landmarkremark.R;
import com.landmarkremark.ui.home.MapsFragment;

public class CustomDialog implements AddCustomDialog {
    DatabaseReference databaseReference;
    Context context;
    TextView address, name;
    EditText title_et;
    EditText notes_et;
    Button dialogButton;
    Dialog dialog;
    MarkedNote markedNote;

    public CustomDialog(DatabaseReference databaseReference, Context con) {
        this.databaseReference = databaseReference;
        this.context = con;
        //create the Custom Dialog
        createDialog();
    }

    public CustomDialog(Context con, MarkedNote note) {
        this.context = con;
        this.markedNote = note;
        //create the Custom Dialog
        createDialog();
    }

    @Override
    public void addNotes(final User user) {
        //setting the address
        address.setText("Address: " + MapsFragment.note.getAddress());
        // if button is clicked, close the custom dialog
        dialogButton.setText("Add Marker");
        name.setVisibility(View.GONE);//updating view pref
        title_et.setEnabled(true);
        notes_et.setEnabled(true);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calling add of DB function to save the notes
                String title = title_et.getText().toString().trim(); //title
                String notes = notes_et.getText().toString(); //notes
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    add(user, title, notes, dialog);
                }
            }
        });

        dialog.show();
    }

    @Override
    public void viewNotes() {

//updating view pref
        title_et.setEnabled(false);
        notes_et.setEnabled(false);
        name.setVisibility(View.VISIBLE);
        dialogButton.setText("Ok");

        //setting values
        name.setText("Name: " + markedNote.getName());
        address.setText("Address: " + markedNote.getAddress());
        title_et.setText(markedNote.getTitle());
        notes_et.setText(markedNote.getDescription());
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    void createDialog() {

        // custom dialog
        dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.fragment_addmarker);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT); //<--Controlling width and height.
        //Dialog view 0bjects
// set the custom dialog components
        name = dialog.findViewById(R.id.addMarker_name_tv);
        address = dialog.findViewById(R.id.addMarker_addr_tv);
        title_et = dialog.findViewById(R.id.addMarker_title_et);
        notes_et = dialog.findViewById(R.id.addMarker_desc_et);
        dialogButton = dialog.findViewById(R.id.addMarker_btn);


    }

    public void add(User user, String title, String note, Dialog dialog) {
        MarkedNote markedNote = MapsFragment.note;
        String userid = SharedPref.getloggedId();//ID
        markedNote.setId(SharedPref.getID());
        markedNote.setTitle(title);
        markedNote.setName(user.getName());
        markedNote.setDescription(note);

        //Creating a new Note
        databaseReference.child(userid).child(SharedPref.MarkedNote).child(markedNote.getId()).setValue(markedNote);

        dialog.dismiss();
        Toast.makeText(context, "Notes successfully updated. ", Toast.LENGTH_SHORT).show();

    }
}
