package com.landmarkremark;

import androidx.appcompat.app.AppCompatActivity;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.landmarkremark.databinding.ActivityAddNewNoteBinding;
import com.landmarkremark.models.MarkedNote;
import com.landmarkremark.repository.UserRepo;
import com.landmarkremark.utils.Utils;

public class AddNewNoteActivity extends AppCompatActivity {
    private UserRepo userRepo;
    private Location location;
    private String address;
    private ActivityAddNewNoteBinding activityAddNewNoteBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddNewNoteBinding = ActivityAddNewNoteBinding.inflate(getLayoutInflater());
        View view = activityAddNewNoteBinding.getRoot();
        setContentView(view);
        //set title on toolbar
        AddNewNoteActivity.this.setTitle(getString(R.string.add_new_note));

        userRepo = new UserRepo();
        getMarkedLocation();
        setNameAndAddress();
        onSubmitClick();
    }

    private void onSubmitClick() {

        activityAddNewNoteBinding.addMarkerSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = activityAddNewNoteBinding.addMarkerTitleEt.getText().toString().trim();
                String desc = activityAddNewNoteBinding.addMarkerDescEt.getText().toString();
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
        finish();
    }

    private void setNameAndAddress() {
        address = getAddress(location);
        activityAddNewNoteBinding.addMarkerNameHeader.setText(String.format("%s: %s", getString(R.string.name), Utils.username));
        activityAddNewNoteBinding.addMarkerAddressTv.setText(String.format("%s: %s", getString(R.string.address), address));
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
        note.setUserName(Utils.username);
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