package com.landmarkremark.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.landmarkremark.MainActivity;
import com.landmarkremark.Models.MarkedNote;
import com.landmarkremark.Models.User;
import com.landmarkremark.R;
import com.landmarkremark.Utils.AddCustomDialog;
import com.landmarkremark.Utils.CustomDialog;
import com.landmarkremark.Utils.SharedPref;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static MarkedNote note;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<MarkedNote> markedNoteArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        //if not home Fragment hide the Add button
        Activity activity = getActivity();
        MainActivity mainActivity = (MainActivity) activity;
        try {
            if (mainActivity.search.isVisible()) {
                mainActivity.search.setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (MainActivity.navController.getCurrentDestination().getLabel().equals("Home")) {
            //showing the FAB Button on Home Screen
            mainActivity.showFAB();
        } else {
            //Hiding the FAB
            mainActivity.hideFAB();
        }
        return view;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (MainActivity.navController.getCurrentDestination().getLabel().equals("Home")) {
//calling Home, to display current location and allowing to add notes
            if (SharedPref.isLocationEnabled(getActivity())) {
                callHome();
            } else {
                Toast.makeText(getActivity(), "Please switch on Location services.", Toast.LENGTH_SHORT).show();
                //navigating to Settings
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        } else if (MainActivity.navController.getCurrentDestination().getLabel().equals("My Landmarks")) {
//calling my Landmarks to display all Markers
            myLandmarks();
        }
    }

    //getting the current address of the user
    private String getAddrs(double lat, double lon) {
        String addrs = "";
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (geocoder.isPresent()) {
                StringBuilder stringBuilder = new StringBuilder();
                if (addresses.size() > 0) {
                    Address returnAddress = addresses.get(0);

                    Log.i("Return Address: ", returnAddress.getAddressLine(0));
                    addrs = returnAddress.getAddressLine(0);
                }
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addrs;
    }

    //Home page
    @SuppressLint("MissingPermission")
    private void callHome() {

        //getting current location
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            LatLng myLoc = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 5));
            mMap.addMarker(new MarkerOptions().position(myLoc).title("You are here!"));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            //setting the details of the location
            note = new MarkedNote();
            // note.setName(user.getName());
            note.setAddress(getAddrs(location.getLatitude(), location.getLongitude()));
            note.setLatitude(String.valueOf(location.getLatitude()));
            note.setLongitude(String.valueOf(location.getLongitude()));

        }

    }

    //user's markers
    private void myLandmarks() {
        //Firebase initialization
        firebaseDatabase = FirebaseDatabase.getInstance();
        //getting the reference of all users
        databaseReference = firebaseDatabase.getReference("users").getRef().child(SharedPref.getloggedId()).child("markedNote");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //  user = dataSnapshot.getValue(User.class);
                markedNoteArrayList = new ArrayList<>();
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    Log.i("User notes", noteSnapshot.toString());
                    note = noteSnapshot.getValue(MarkedNote.class);
                    //adding the notes to arraylist
                    markedNoteArrayList.add(note);
                    //  }
                }
                //adding all markers to cluster
                for (MarkedNote note : markedNoteArrayList)
                    setupMap(note);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "" + databaseError.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //displaying customDialog when marker is clicked
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                CustomDialog customDialog = new CustomDialog(getActivity(), note);
                customDialog.viewNotes();

                return true;
            }
        });
    }

    //setting users marker on Map
    public Marker setupMap(MarkedNote note) {
        Double lat = Double.valueOf(note.getLatitude());
        Double lon = Double.valueOf(note.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lon))      // Sets the center of the map to location user
                .zoom(15)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .anchor(0.5f, 0.5f)
                .title(note.getTitle())
                .snippet(note.getDescription()));


    }


}
