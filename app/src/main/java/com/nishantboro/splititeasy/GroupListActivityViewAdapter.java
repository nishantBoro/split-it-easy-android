package com.nishantboro.splititeasy;

// Objective: To prepare an adapter that could throw all the items from membersList Array(MembersTabFragment class)
// to a recycler view

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class GroupListActivityViewAdapter extends RecyclerView.Adapter<GroupListActivityViewAdapter.GroupListActivityViewHolder>{

    private List<GroupEntity> list  = new ArrayList<>();
    private OnGroupListener onGroupListener;

    // Provide a reference to the views for each name in our membersList Array:
    static class GroupListActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;
        private OnGroupListener onGroupListener;


        GroupListActivityViewHolder(@NonNull View itemView, OnGroupListener onGroupListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.groupListDetailName); // get the textView View component from member_detail.xml and attach it to our holder
            this.onGroupListener = onGroupListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onGroupListener.onGroupClick(getAdapterPosition());
        }
    }

    GroupListActivityViewAdapter(OnGroupListener onGroupListener) {
        Log.d("tag", "got the on group listener");
        this.onGroupListener = onGroupListener;
    }

    // Create new viewHolder (invoked by the layout manager)
    @NonNull
    @Override
    public GroupListActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_detail, parent, false);
        return new GroupListActivityViewHolder(v,onGroupListener); // pass the inflated view, create a holder with textView component attached to it and return
    }

    // Bind the name in membersList[position] to the holder created by Layout manager
    @Override
    public void onBindViewHolder(@NonNull GroupListActivityViewHolder holder, int position) {
        holder.textView.setText(list.get(position).gName);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public interface OnGroupListener {
        void onGroupClick(int position);
    }

    public void saveToList(List<GroupEntity> groupNames) {
        this.list = groupNames;
        notifyDataSetChanged();
    }

}
