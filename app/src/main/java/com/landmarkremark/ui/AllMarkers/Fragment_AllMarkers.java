package com.landmarkremark.ui.AllMarkers;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.landmarkremark.Adapter.AdpaterAllMarkers;
import com.landmarkremark.MainActivity;
import com.landmarkremark.Models.MarkedNote;
import com.landmarkremark.Models.User;
import com.landmarkremark.R;
import com.landmarkremark.Utils.SharedPref;

import java.util.ArrayList;

public class Fragment_AllMarkers extends Fragment {

    static ArrayList<MarkedNote> noteArrayList;
    RecyclerView recyclerView;
    public static AdpaterAllMarkers adpaterAllMarkers;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_allmarkers, container, false);

        recyclerView = root.findViewById(R.id.allMarker_Rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //if not home Fragment hide the Add button
        Activity activity = getActivity();
        MainActivity mainActivity = (MainActivity) activity;
        //Hiding the FAB
        mainActivity.hideFAB();
        //showing search menu
        mainActivity.search.setVisible(true);
        //getting the data from the database
        getUserData();


        return root;
    }

    void getUserData() {
        //Arraylist to add all the notes
        noteArrayList = new ArrayList<>();
//Firebase initialization
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference database = firebaseDatabase.getReference().child("users");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = (String) ds.getKey();
                    Log.d("TAG", "Key" + key);
                    DatabaseReference keyReference = FirebaseDatabase.getInstance().getReference().child("users").child(key);
                    Log.d("TAG", "Key reference: " + keyReference);
                    keyReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot : dataSnapshot.child("markedNote").getChildren()) {
                                MarkedNote markedNote = childSnapshot.getValue(MarkedNote.class);

                                // adding the notes to arraylist
                                noteArrayList.add(markedNote);
                                System.out.println("noteArrayList Size ---   " + noteArrayList.size());
                            }
                            //checking for data
                            if (noteArrayList.size() > 0) {
                                adpaterAllMarkers = new AdpaterAllMarkers(getActivity(), noteArrayList);
                                recyclerView.setAdapter(adpaterAllMarkers);
                            } else {
                                Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("TAG", "Read failed");
                        }
                    }); // [End of keyReference]

                } // END of for Loop
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "Read failed");
            }
        }); // [End of DatabaseReference]


    }
}
