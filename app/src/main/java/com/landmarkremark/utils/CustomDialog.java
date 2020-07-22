package com.landmarkremark.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.landmarkremark.models.MarkedNote;
import com.landmarkremark.R;

public class CustomDialog implements IDialog {
    private Context context;
    private TextView address, title_tv, desc_tv;
    private Button dialogButton;
    private Dialog dialog;

    public CustomDialog(Context context) {
        this.context = context;
        createDialog();
    }

    @Override
    public void viewNotes(MarkedNote markedNote) {
        //setting values
        dialog.setTitle(markedNote.getUserName());
        address.setText(markedNote.getAddress());
        title_tv.setText(markedNote.getTitle());
        desc_tv.setText(markedNote.getDescription());
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
        dialog = new Dialog(context, R.style.Dialog);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT); //<--Controlling width and height.
        // set the custom dialog components
        address = dialog.findViewById(R.id.addMarker_addr_tv);
        title_tv = dialog.findViewById(R.id.addMarker_title_tv);
        desc_tv = dialog.findViewById(R.id.addMarker_desc_tv);
        dialogButton = dialog.findViewById(R.id.addMarker_btn);
    }
}
