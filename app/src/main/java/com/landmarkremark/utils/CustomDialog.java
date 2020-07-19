package com.landmarkremark.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.landmarkremark.models.MarkedNote;
import com.landmarkremark.R;

public class CustomDialog implements ICustomDialog {
    Context context;
    TextView address, name;
    TextView title_tv, desc_tv;
    Button dialogButton;
    Dialog dialog;

    public CustomDialog(Context context) {
        this.context = context;
        createDialog();
    }

    @Override
    public void viewNotes(MarkedNote markedNote) {

        //updating view pref
        name.setVisibility(View.VISIBLE);
        //setting values
        name.setText(String.format("%s: %s", context.getString(R.string.name), markedNote.getName()));
        address.setText(String.format("%s\n%s", context.getString(R.string.address), markedNote.getAddress()));
        title_tv.setText(String.format("%s\n%s", context.getString(R.string.title), markedNote.getTitle()));
        desc_tv.setText(String.format("%s\n%s", context.getString(R.string.notes), markedNote.getDescription()));
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void createDialog() {

        // custom dialog
        dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.fragment_addmarker);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT); //<--Controlling width and height.
        //Dialog view 0bjects
// set the custom dialog components
        name = dialog.findViewById(R.id.addMarker_name_tv);
        address = dialog.findViewById(R.id.addMarker_addr_tv);
        title_tv = dialog.findViewById(R.id.addMarker_title_tv);
        desc_tv = dialog.findViewById(R.id.addMarker_desc_tv);
        dialogButton = dialog.findViewById(R.id.addMarker_btn);
    }
}
