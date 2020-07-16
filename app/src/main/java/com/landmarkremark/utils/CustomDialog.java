package com.landmarkremark.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.landmarkremark.models.MarkedNote;
import com.landmarkremark.models.User;
import com.landmarkremark.R;
import com.landmarkremark.repository.UserRepo;

public class CustomDialog implements AddCustomDialog {
    Context context;
    TextView address, name;
    EditText title_et;
    EditText notes_et;
    Button dialogButton;
    Dialog dialog;
    UserRepo userRepo;

    public CustomDialog(Context context) {
        this.context = context;
        createDialog();
    }

    @Override
    public void promptAndAcceptNote(final User user, MarkedNote note, final Runnable runnable) {
        //setting the address
        address.setText("Address: " + note.getAddress());
        // if button is clicked, close the custom dialog
        dialogButton.setText("Add Marker");
        name.setVisibility(View.GONE);//updating view pref
        title_et.setEnabled(true);
        notes_et.setEnabled(true);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = title_et.getText().toString().trim();
                String notes = notes_et.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    note.setTitle(title);
                    note.setDescription(notes);
                    runnable.run();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void viewNotes(MarkedNote markedNote) {

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
}
