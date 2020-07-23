package com.landmarkremark.ui.searchmarkers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.landmarkremark.adapter.AdapterAllMarkers;
import com.landmarkremark.MainActivity;
import com.landmarkremark.databinding.FragmentAllmarkersBinding;
import com.landmarkremark.repository.UserRepo;

public class FragmentSearchMarkers extends Fragment {

    public AdapterAllMarkers adapterAllMarkers;
    private UserRepo userRepo;
    private FragmentAllmarkersBinding fragmentAllMarkersBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentAllMarkersBinding = FragmentAllmarkersBinding.inflate(inflater, container, false);
        View root = fragmentAllMarkersBinding.getRoot();

        userRepo = new UserRepo();

        initRecyclerView();
        //showing search menu
        showSearchMenu();

        //Setting adapter to RecyclerView
        adapterAllMarkers = new AdapterAllMarkers(getActivity());
        loadAllNotes(adapterAllMarkers);
        fragmentAllMarkersBinding.allMarkerRec.setAdapter(adapterAllMarkers);

        return root;
    }

    private void showSearchMenu() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.getSearchMenuItem().setVisible(true);
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        fragmentAllMarkersBinding.allMarkerRec.setHasFixedSize(true);
        fragmentAllMarkersBinding.allMarkerRec.setLayoutManager(layoutManager);
    }

    //Loading All notes from Database
    private void loadAllNotes(AdapterAllMarkers adapterAllMarkers) {
        userRepo.findAllNotes(note -> {
            adapterAllMarkers.addNote(note);
            adapterAllMarkers.notifyDataSetChanged();
        });
    }

    //For search landmark
    public void searchItem(String query) {
        if (query.length() > 0) {
            adapterAllMarkers.getFilter().filter(query);
        } else {
            //loading all Notes
            adapterAllMarkers.markedNotes.clear();
            loadAllNotes(adapterAllMarkers);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentAllMarkersBinding = null;
    }
}
