package com.landmarkremark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.landmarkremark.models.MarkedNote;
import com.landmarkremark.repository.UserRepo;
import com.landmarkremark.utils.Utils;

public class AddNewNoteActivity extends AppCompatActivity {
    private UserRepo userRepo;
    private Location location;
    private EditText titleEdittext, descEdittext;
    private TextView nameText, addressText;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        AddNewNoteActivity.this.setTitle(getString(R.string.add_new_note));

        userRepo = new UserRepo();
        getMarkedLocation();
        init();
        onSubmitClick();
    }

    private void onSubmitClick() {
        Button submitBtn = findViewById(R.id.addMarker_SubmitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEdittext.getText().toString().trim();
                String desc = descEdittext.getText().toString();
                address = addressText.getText().toString();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(desc)) {
                    Toast.makeText(AddNewNoteActivity.this, R.string.title_cannot_be_empty, Toast.LENGTH_SHORT).show();
                } else {
                    addNotesToDb(setNote(location, title, desc, address));
                    moveToMain();
                }
            }
        });
    }

    private void moveToMain() {
        /*Intent in = new Intent(AddNewNoteActivity.this, MainActivity.class);
        startActivity(in);*/
        finish();
    }

    private void init() {
        address = getAddress(location);
        titleEdittext = findViewById(R.id.addMarker_title_et);
        descEdittext = findViewById(R.id.addMarker_desc_et);
        nameText = findViewById(R.id.addMarker_name_header);
        nameText.setText(String.format("%s: %s", getString(R.string.name), Utils.username));
        addressText = findViewById(R.id.addMarker_address_tv);
        addressText.setText(String.format("%s: %s", getString(R.string.address), address));
    }

    //getting the location Object from prev fragment
    private Location getMarkedLocation() {
        location = getIntent().getParcelableExtra("location");
        return location;
    }

    //adding notes at current location
    private void addNotesToDb(MarkedNote markedNote) {
        userRepo.addNotesToDb(markedNote);
        Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
    }

    //setting the Note obj
    private MarkedNote setNote(Location location, String title, String desc, String addrs) {
        MarkedNote note = new MarkedNote();
        note.setTitle(title);
        note.setDescription(desc);
        note.setName(Utils.username);
        note.setAddress(addrs);
        note.setLatitude(String.valueOf(location.getLatitude()));
        note.setLongitude(String.valueOf(location.getLongitude()));
        return note;
    }

    //getting the address from location
    private String getAddress(Location location) {
        return Utils.convertLatLngToAddress(this, location.getLatitude(), location.getLongitude());
    }


}