package com.landmarkremark.ui.allmarkers;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.landmarkremark.adapter.AdpaterAllMarkers;
import com.landmarkremark.MainActivity;
import com.landmarkremark.databinding.FragmentAllmarkersBinding;
import com.landmarkremark.repository.UserRepo;

public class FragmentAllMarkers extends Fragment {

    public AdpaterAllMarkers adpaterAllMarkers;
    private UserRepo userRepo;
    private FragmentAllmarkersBinding fragmentAllMarkersBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentAllMarkersBinding = FragmentAllmarkersBinding.inflate(inflater, container, false);
        View root = fragmentAllMarkersBinding.getRoot();

        userRepo = new UserRepo();
        initRecyclerView();

        //if not home Fragment hide the Add button
        Activity activity = getActivity();
        MainActivity mainActivity = (MainActivity) activity;
        //showing search menu
        mainActivity.getSearchMenuItem().setVisible(true);
        adpaterAllMarkers = new AdpaterAllMarkers(getActivity());
        loadAllNotes(adpaterAllMarkers);
        fragmentAllMarkersBinding.allMarkerRec.setAdapter(adpaterAllMarkers);

        return root;
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        fragmentAllMarkersBinding.allMarkerRec.setHasFixedSize(true);
        fragmentAllMarkersBinding.allMarkerRec.setLayoutManager(layoutManager);
    }

    private void loadAllNotes(AdpaterAllMarkers adpaterAllMarkers) {
        userRepo.findAllNotes(note -> {
            adpaterAllMarkers.addNote(note);
            adpaterAllMarkers.notifyDataSetChanged();
        });
    }

    public void searchItem(String query) {
        if (query.length() > 0) {
            adpaterAllMarkers.getFilter().filter(query);
        } else {
            adpaterAllMarkers.markedNotes.clear();
            loadAllNotes(adpaterAllMarkers);
        }
    }
}
