package com.landmarkremark.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.landmarkremark.R;
import com.landmarkremark.databinding.RecviewItemsBinding;
import com.landmarkremark.models.MarkedNote;
import com.landmarkremark.utils.CustomDialog;

import java.util.ArrayList;
import java.util.List;

public class AdpaterAllMarkers extends RecyclerView.Adapter<AdpaterAllMarkers.ViewHolder> implements Filterable {

    public List<MarkedNote> markedNotes = new ArrayList<>();
    private Context context;

    public AdpaterAllMarkers(Context context) {
        this.context = context;
    }

    public void addNote(MarkedNote markedNote) {
        this.markedNotes.add(markedNote);
    }

    @Override
    public AdpaterAllMarkers.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(RecviewItemsBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(AdpaterAllMarkers.ViewHolder holder, final int position) {

        holder.recviewItemsBinding.itemTitle.setText(String.format("%s: %s", context.getString(R.string.title), markedNotes.get(position).getTitle()));
        holder.recviewItemsBinding.itemAddress.setText(String.format("Address: %s", markedNotes.get(position).getAddress()));
        holder.recviewItemsBinding.itemUsername.setText(String.format("Name: %s", markedNotes.get(position).getName()));
        holder.recviewItemsBinding.itemCardView.setOnClickListener(view -> {
            CustomDialog customDialog = new CustomDialog(context);
            customDialog.viewNotes(markedNotes.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return markedNotes.size();
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<MarkedNote> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(markedNotes);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MarkedNote item : markedNotes) {
                    if (item.getName().toLowerCase().contains(filterPattern) || item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (markedNotes.size() > 0) {
                markedNotes.clear();
                markedNotes.addAll((ArrayList) results.values);
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "No item for search result", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecviewItemsBinding recviewItemsBinding;

        public ViewHolder(RecviewItemsBinding recviewItemsBinding) {
            super(recviewItemsBinding.getRoot());
            this.recviewItemsBinding = recviewItemsBinding;
        }
    }


}


