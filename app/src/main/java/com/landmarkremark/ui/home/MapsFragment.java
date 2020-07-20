package com.landmarkremark.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.landmarkremark.AddNewNoteActivity;
import com.landmarkremark.MainActivity;
import com.landmarkremark.models.MarkedNote;
import com.landmarkremark.R;
import com.landmarkremark.repository.UserRepo;
import com.landmarkremark.utils.CustomDialog;
import com.landmarkremark.utils.ErrorDialog;
import com.landmarkremark.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MainActivity mainActivity;
    private UserRepo userRepo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        userRepo = new UserRepo();
        initGoogleMaps();

        //if home Fragment hide the Search button
        mainActivity = (MainActivity) getActivity();
        try {
            if (mainActivity.getSearchMenuItem().isVisible()) {
                mainActivity.getSearchMenuItem().setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void initGoogleMaps() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (googleMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (mainActivity.getNavController().getCurrentDestination().getLabel().equals(Utils.home_label)) {
            if (Utils.isLocationEnabled(getActivity())) {
                setMarkerOnCurrentLocation();
            } else {
                Toast.makeText(getActivity(), "Please switch on Location services.", Toast.LENGTH_SHORT).show();
                //navigating to Settings
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        } else if (mainActivity.getNavController().getCurrentDestination().getLabel().equals(Utils.myLandmarks_label)) {
            List<MarkedNote> myMarked = new ArrayList<>();
            populateNotesAndSetupGoogleMap(Utils.getLoggedInUserId(), googleMap, myMarked);
        }
    }

    @SuppressLint("MissingPermission")
    private void setMarkerOnCurrentLocation() {
        //getting current location
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            LatLng myLoc = new LatLng(location.getLatitude(), location.getLongitude());
            setUpMarker(getString(R.string.click_marker), googleMap, myLoc);
            //setting onClick to Add Marker
            setGoogleMapClickListener(googleMap, null, location, 1);
        }
    }

    private void addNewNotes(Location location) {
        Intent in = new Intent(getActivity(), AddNewNoteActivity.class);
        in.putExtra("location", location);
        startActivity(in);
    }

    private void populateNotesAndSetupGoogleMap(String userId, GoogleMap googleMap, List<MarkedNote> myMarked) {
        userRepo.findUserNotes(userId, note -> {
            myMarked.add(note);
            setUpMarker(note.getTitle(), googleMap, getLatLng(note));
            setGoogleMapClickListener(googleMap, myMarked, null, 2);
        }, () -> openDialog(getString(R.string.alert_title), getString(R.string.no_note_alert_message)));
    }

    //onClick on Markers
    private void setGoogleMapClickListener(GoogleMap googleMap, final List<MarkedNote> notes, Location location, int type) {
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (type == 2) {
                    CustomDialog customDialog = new CustomDialog(getActivity());
                    for (MarkedNote markedNote : notes) {
                        if (marker.getPosition().equals(getLatLng(markedNote)))
                            customDialog.viewNotes(markedNote);
                    }
                } else if (type == 1) {
                    addNewNotes(location);
                }
                return true;
            }
        });
    }

    //getting LatLng value
    private LatLng getLatLng(MarkedNote markedNote) {
        Double lat = Double.valueOf(markedNote.getLatitude());
        Double lon = Double.valueOf(markedNote.getLongitude());
        return new LatLng(lat, lon);
    }

    private void setUpMarker(String title, GoogleMap googleMap, LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to location user
                .zoom(15)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.addMarker(new MarkerOptions().position(latLng).title(title)).showInfoWindow();
    }

    //Display Error Dialog
    public void openDialog(String title, String message) {
        ErrorDialog alertDialog = new ErrorDialog(title, message);
        alertDialog.show(getChildFragmentManager(), this.getClass().getName());
    }

}
