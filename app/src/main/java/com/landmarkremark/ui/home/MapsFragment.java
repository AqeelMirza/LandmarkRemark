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
import com.landmarkremark.MainActivity;
import com.landmarkremark.models.MarkedNote;
import com.landmarkremark.R;
import com.landmarkremark.repository.UserRepo;
import com.landmarkremark.utils.CustomDialog;
import com.landmarkremark.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MainActivity mainActivity;
    private UserRepo userRepo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        userRepo = new UserRepo();
        initGoogleMaps();

        //if not home Fragment hide the Add button
        mainActivity = (MainActivity) getActivity();
        try {
            if (mainActivity.getSearchMenuItem().isVisible()) {
                mainActivity.getSearchMenuItem().setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mainActivity.getNavController().getCurrentDestination().getLabel().equals(SharedPref.home_label)) {
            //showing the FAB Button on Home Screen
            mainActivity.showFab();
        } else {
            //Hiding the FAB
            mainActivity.hideFab();
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
        this.googleMap = googleMap;
        if (mainActivity.getNavController().getCurrentDestination().getLabel().equals(SharedPref.home_label)) {
            if (SharedPref.isLocationEnabled(getActivity())) {
                setMarkerOnCurrentLocation();
            } else {
                Toast.makeText(getActivity(), "Please switch on Location services.", Toast.LENGTH_SHORT).show();
                //navigating to Settings
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        } else if (mainActivity.getNavController().getCurrentDestination().getLabel().equals(SharedPref.myLandmarks_label)) {
            populateNotesAndSetupGoogleMap(SharedPref.getLoggedInUserId(), googleMap);
        }
    }

    @SuppressLint("MissingPermission")
    private void setMarkerOnCurrentLocation() {
        //getting current location
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            LatLng myLoc = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 5));
            googleMap.addMarker(new MarkerOptions().position(myLoc).title("You are here!"));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            //setting the details of the location
            setCurrentLocation(location);


        }
    }

    private void setCurrentLocation(Location location) {
        mainActivity.setMarkedLocation(location);
    }

    private void populateNotesAndSetupGoogleMap(String userId, GoogleMap googleMap) {
        userRepo.findUserNotes(userId, note -> {
            addMarkerOnMap(note, googleMap);
            setGoogleMapClickListener(googleMap, note);
        });
    }

    private void setGoogleMapClickListener(GoogleMap googleMap, final MarkedNote note) {
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                CustomDialog customDialog = new CustomDialog(getActivity());
                customDialog.viewNotes(note);
                return true;
            }
        });
    }

    //setting users marker on Map
    public Marker addMarkerOnMap(MarkedNote note, GoogleMap googleMap) {
        Double lat = Double.valueOf(note.getLatitude());
        Double lon = Double.valueOf(note.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lon))      // Sets the center of the map to location user
                .zoom(15)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        return googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .anchor(0.5f, 0.5f)
                .title(note.getTitle())
                .snippet(note.getDescription()));
    }
}
