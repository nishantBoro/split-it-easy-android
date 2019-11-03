package com.nishantboro.splititeasy;

// Objective: To prepare an adapter that could throw all the items from membersList Array(MembersTabFragment class)
// to a recycler view

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class GroupListActivityViewAdapter extends RecyclerView.Adapter<GroupListActivityViewAdapter.GroupListActivityViewHolder>{
    private OnGroupClickListener listener;
    private List<GroupEntity> list  = new ArrayList<>();
    public ActionMode actionMode;
    public boolean multiSelect = false;
    public List<GroupEntity> selectedItems = new ArrayList<>();
    private GroupListActivity thisOfGroupListActivity;

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            GroupListActivityViewAdapter.this.multiSelect = true;
            menu.add("Delete");
            GroupListActivityViewAdapter.this.actionMode = mode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            for(GroupEntity group: GroupListActivityViewAdapter.this.selectedItems) {
                GroupListActivityViewAdapter.this.list.remove(group);
                GroupListActivityViewAdapter.this.deleteFromDatabase(group);
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            GroupListActivityViewAdapter.this.multiSelect = false;
            GroupListActivityViewAdapter.this.selectedItems.clear();
            GroupListActivityViewAdapter.this.notifyDataSetChanged();
        }
    };

    // Provide a reference to the views for each name in our membersList Array:
    class GroupListActivityViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private RelativeLayout relativeLayout;

        GroupListActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.groupListDetailName); // get the textView View component from member_detail.xml and attach it to our holder
            this.relativeLayout = itemView.findViewById(R.id.groupListDetail);
        }


        void update(final GroupEntity group) {

            // if the user clicks on back button while an item was selected(gray colour), notifyDataSetChanged is called, hence update() is called again for every viewHolder. So, at this point
            // we need to make sure that the item that was selected(gray colour) previously, needs to be white(unselected) now.
            if (GroupListActivityViewAdapter.this.selectedItems.contains(group)) {
                this.relativeLayout.setBackgroundColor(Color.LTGRAY);
            } else {
                this.relativeLayout.setBackgroundColor(Color.WHITE);
            }

            // attach a long click listener
            this.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((AppCompatActivity)v.getContext()).startSupportActionMode(GroupListActivityViewAdapter.this.actionModeCallbacks);
                    GroupListActivityViewAdapter.GroupListActivityViewHolder.this.selectItem(group);
                    return true;
                }
            });
        }

        void selectItem(GroupEntity group) {
            if (GroupListActivityViewAdapter.this.multiSelect) {
                if (GroupListActivityViewAdapter.this.selectedItems.contains(group)) {
                    GroupListActivityViewAdapter.this.selectedItems.remove(group);
                    this.relativeLayout.setBackgroundColor(Color.WHITE);
                } else {
                    GroupListActivityViewAdapter.this.selectedItems.add(group);
                    this.relativeLayout.setBackgroundColor(Color.LTGRAY);
                }
            }
        }
    }

    GroupListActivityViewAdapter(GroupListActivity thisOfGroupListActivity) {
        this.thisOfGroupListActivity = thisOfGroupListActivity;
    }

    // Create new viewHolder (invoked by the layout manager)
    @NonNull
    @Override
    public GroupListActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_detail, parent, false);
        return new GroupListActivityViewHolder(v); // pass the inflated view, create a holder with textView component attached to it and return
    }

    // Bind the name in membersList[position] to the holder created by Layout manager
    @Override
    public void onBindViewHolder(@NonNull GroupListActivityViewHolder holder, int position) {
        final GroupListActivityViewHolder hold = holder;
        holder.textView.setText(list.get(position).gName);
        holder.update(this.list.get(position));

        final int pos = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GroupListActivityViewAdapter.this.multiSelect) {
                    hold.selectItem(GroupListActivityViewAdapter.this.list.get(pos));
                }
                if(GroupListActivityViewAdapter.this.listener != null && !GroupListActivityViewAdapter.this.multiSelect) {
                    GroupListActivityViewAdapter.this.listener.onGroupClick(pos);
                }
            }
        });
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

    public void deleteFromDatabase(GroupEntity group) {
        GroupViewModel groupViewModel = ViewModelProviders.of(this.thisOfGroupListActivity).get(GroupViewModel.class);
        groupViewModel.delete(group);
    }

    public interface OnGroupClickListener {
        void onGroupClick(int position);
    }

    public void setOnItemClickListener(OnGroupClickListener listener) {
        this.listener = listener;
    }


}
