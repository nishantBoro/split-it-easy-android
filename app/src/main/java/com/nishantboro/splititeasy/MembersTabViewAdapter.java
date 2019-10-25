package com.nishantboro.splititeasy;

// Objective: To prepare an adapter that could throw all the items from membersList Array(MembersTabFragment class)
// to a recycler view

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MembersTabViewAdapter extends RecyclerView.Adapter<MembersTabViewAdapter.MemberDetailViewHolder> {

    private ArrayList<String> list = new ArrayList<>();

    // Provide a reference to the views for each name in our membersList Array:
    static class MemberDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        MemberDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.memberDetailName); // get the textView View component from member_detail.xml and attach it to our holder
        }
    }

    MembersTabViewAdapter(ArrayList<String> names) {
        list = names;
    }

    // Create new viewHolder (invoked by the layout manager)
    @NonNull
    @Override
    public MemberDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_detail, parent, false);
        return new MemberDetailViewHolder(v); // pass the inflated view, create a holder with textView component attached to it and return
    }

    // Bind the name in membersList[position] to the holder created by Layout manager
    @Override
    public void onBindViewHolder(@NonNull MemberDetailViewHolder holder, int position) {
        holder.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
