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

public class AdapterAllMarkers extends RecyclerView.Adapter<AdapterAllMarkers.ViewHolder> implements Filterable {

    public List<MarkedNote> markedNotes = new ArrayList<>();
    private Context context;

    public AdapterAllMarkers(Context context) {
        this.context = context;
    }

    public void addNote(MarkedNote markedNote) {
        this.markedNotes.add(markedNote);
    }

    @Override
    public AdapterAllMarkers.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(RecviewItemsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(AdapterAllMarkers.ViewHolder holder, final int position) {
        holder.recviewItemsBinding.itemTitle.setText(String.format("%s %s", context.getString(R.string.title), markedNotes.get(position).getTitle()));
        holder.recviewItemsBinding.itemAddress.setText(String.format("%s %s", context.getString(R.string.address), markedNotes.get(position).getAddress()));
        holder.recviewItemsBinding.itemUsername.setText(markedNotes.get(position).getUserName());
        holder.recviewItemsBinding.itemCardView.setOnClickListener(view -> {
            //calling custom dialog to display Notes
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
        protected FilterResults performFiltering(CharSequence filterChar) {
            ArrayList<MarkedNote> filteredList = new ArrayList<>();
            //checking for filterChar value
            if (filterChar == null || filterChar.length() == 0) {
                filteredList.addAll(markedNotes);
            } else {
                String filterPattern = filterChar.toString().toLowerCase().trim();
                for (MarkedNote item : markedNotes) {
                    //checking for UserName or Title of the Note
                    if (item.getUserName().toLowerCase().contains(filterPattern) || item.getTitle().toLowerCase().contains(filterPattern)) {
                        //add to filtered list
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //checking if it contains notes
            if (markedNotes.size() > 0) {
                markedNotes.clear();
                markedNotes.addAll((ArrayList) results.values);
                notifyDataSetChanged();
            } else {
                //Message for no Items Found
                Toast.makeText(context, "No items for search result", Toast.LENGTH_SHORT).show();
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


