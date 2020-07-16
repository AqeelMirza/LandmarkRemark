package com.landmarkremark.ui.allmarkers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.landmarkremark.adapter.AdpaterAllMarkers;
import com.landmarkremark.MainActivity;
import com.landmarkremark.models.MarkedNote;
import com.landmarkremark.R;
import com.landmarkremark.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;

public class Fragment_AllMarkers extends Fragment {

    private RecyclerView recyclerView;
    public AdpaterAllMarkers adpaterAllMarkers;
    private UserRepo userRepo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_allmarkers, container, false);

        userRepo = new UserRepo();
        initRecyclerView(root);

        //if not home Fragment hide the Add button
        Activity activity = getActivity();
        MainActivity mainActivity = (MainActivity) activity;
        //Hiding the FAB
        mainActivity.hideFab();
        //showing search menu
        mainActivity.getSearchMenuItem().setVisible(true);
        adpaterAllMarkers = new AdpaterAllMarkers(getActivity());
        loadAllNotes(adpaterAllMarkers);
        recyclerView.setAdapter(adpaterAllMarkers);

        return root;
    }

    private void initRecyclerView(View root) {
        recyclerView = root.findViewById(R.id.allMarker_Rec);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadAllNotes(AdpaterAllMarkers adpaterAllMarkers) {
        userRepo.findAllNotes(note -> {
            adpaterAllMarkers.addNote(note);
            adpaterAllMarkers.notifyDataSetChanged();
        });
    }

    public void searchItem(String query) {

        adpaterAllMarkers.getFilter().filter(query);
    }

}
