package com.landmarkremark.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.landmarkremark.Models.MarkedNote;
import com.landmarkremark.R;
import com.landmarkremark.Utils.CustomDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdpaterAllMarkers extends RecyclerView.Adapter<AdpaterAllMarkers.ViewHolder> implements Filterable {

    ArrayList<MarkedNote> markedNoteArrayList;
    ArrayList<MarkedNote> searchNoteArrayList;
    Context context;

    public AdpaterAllMarkers(Context con, ArrayList<MarkedNote> markedNotes) {
        this.context = con;
        this.markedNoteArrayList = markedNotes;
        this.searchNoteArrayList = markedNotes;
    }

    @Override
    public AdpaterAllMarkers.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.recview_items, parent, false));
    }

    @Override
    public void onBindViewHolder(AdpaterAllMarkers.ViewHolder holder, final int position) {
        holder.title_tv.setText("Title: " + markedNoteArrayList.get(position).getTitle());
        holder.address_tv.setText("Address: " + markedNoteArrayList.get(position).getAddress());
        holder.name_tv.setText("Name: " + markedNoteArrayList.get(position).getName());

        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog customDialog = new CustomDialog(context, markedNoteArrayList.get(position));
                customDialog.viewNotes();
            }
        });

    }

    @Override
    public int getItemCount() {
        return markedNoteArrayList.size();
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
                filteredList.addAll(markedNoteArrayList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MarkedNote item : markedNoteArrayList) {
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
            if (searchNoteArrayList.size() > 0) {
                searchNoteArrayList.clear();
                searchNoteArrayList.addAll((ArrayList) results.values);
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "No item for search result", Toast.LENGTH_SHORT).show();
            }
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name_tv, address_tv, title_tv;
        public CardView itemCard;

        public ViewHolder(View itemView) {
            super(itemView);
            itemCard = itemView.findViewById(R.id.itemCardView);
            name_tv = itemView.findViewById(R.id.itemUsername);
            address_tv = itemView.findViewById(R.id.itemAddress);
            title_tv = itemView.findViewById(R.id.itemTitle);
        }
    }


}


