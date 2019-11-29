package com.nishantboro.splititeasy;

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

/* Objective: Prepare a custom adapter that could create/update the view for every item in the recycler view */
public class GroupListActivityViewAdapter extends RecyclerView.Adapter<GroupListActivityViewAdapter.GroupListActivityViewHolder>{
    private OnGroupClickListener listener;
    private List<GroupEntity> list  = new ArrayList<>(); // maintain a list of all the existing groups in the database
    ActionMode actionMode;
    boolean multiSelect = false; // true if user has selected any item
    List<GroupEntity> selectedItems = new ArrayList<>();
    private GroupListActivity thisOfGroupListActivity;

    /*
    handles all kinds of action when ActionMode is active.
    In our case, when the user does a long click on any recycler view item, ActionMode is activated
    and the following actionModeCallbacks object is created: */
    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        // method is called right after the user does a long click on any item
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            menu.add("Delete");
            actionMode = mode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        // method is called when user clicks on "Delete" option in the menu
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // for every selected item remove it from the recycler view list and also delete it from database
            for(GroupEntity group: selectedItems) {
                list.remove(group);
                deleteFromDatabase(group);
            }
            mode.finish(); // close the ActionMode bar
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            notifyDataSetChanged(); // notify the recycler view about the changes and hence re-render its list
        }
    };

    // A holder for every item in our recycler view is created
    class GroupListActivityViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private RelativeLayout relativeLayout;

        GroupListActivityViewHolder(@NonNull View itemView) {
            super(itemView);

            // store all references from our layout for future use
            textView = itemView.findViewById(R.id.groupListDetailName);
            relativeLayout = itemView.findViewById(R.id.groupListDetail);
        }


        void update(final GroupEntity group) {

            /* if the user clicks on back button while an item was selected(gray colour), notifyDataSetChanged is called, hence update() is called again for every viewHolder. So, at this point
               we need to make sure that the item which was selected(gray colour) previously, needs to be white(unselected) now. */
            if (selectedItems.contains(group)) {
                relativeLayout.setBackgroundColor(Color.LTGRAY);
            } else {
                relativeLayout.setBackgroundColor(Color.WHITE);
            }

            // attach a long click listener to itemView
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // at this point the user has successfully initiated a long click and hence we need to activate ActionMode now to handle multiple select and delete items
                    ((AppCompatActivity)v.getContext()).startSupportActionMode(actionModeCallbacks); // activate ActionMode and let actionModeCallback handle action
                    selectItem(group); // here group is the initially selected item after the long click event
                    return true;
                }
            });
        }

        void selectItem(GroupEntity group) {
            if (multiSelect) {
                if (selectedItems.contains(group)) { // if the user selects a group which is already selected(light gray), deselect it(change colour to white) and remove from selectedItems list
                    selectedItems.remove(group);
                    relativeLayout.setBackgroundColor(Color.WHITE);
                } else { // else add the group to our selection list and change colour to light gray
                    selectedItems.add(group);
                    relativeLayout.setBackgroundColor(Color.LTGRAY);
                }
            }
        }
    }

    GroupListActivityViewAdapter(GroupListActivity thisOfGroupListActivity) {
        this.thisOfGroupListActivity = thisOfGroupListActivity;
    }

    // Create new viewHolder (invoked by the layout manager). Note that this method is called for creating every GroupListActivityViewHolder required for our recycler view items
    @NonNull
    @Override
    public GroupListActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_list_detail, parent, false);
        return new GroupListActivityViewHolder(v);
    }

    // note that this method is called for every GroupListActivityViewHolder
    @Override
    public void onBindViewHolder(@NonNull GroupListActivityViewHolder holder, int position) {
        final GroupListActivityViewHolder hold = holder;
        holder.textView.setText(list.get(position).gName); // set group name to holder
        holder.update(list.get(position));

        final int pos = position;

        // attach a click listener to the GroupListActivityViewHolder
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Note that if the user is in multiSelect mode, the function for individual click on any item shouldn't be initiated
                if(multiSelect) { // if multiSelect is On, clicking on any item should only highlight it and add it to our selectedItems list
                    hold.selectItem(list.get(pos));
                }
                if(listener != null && !multiSelect) { // if multiSelect is Off, clicking on any item should initiate HandleOnGroupClickActivity
                    listener.onGroupClick(pos); // onGroupClick method defined in GroupListActivity[line 61]
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    void saveToList(List<GroupEntity> groupNames) {
        list = groupNames;
        notifyDataSetChanged();
    }

    private void deleteFromDatabase(GroupEntity group) {
        GroupViewModel groupViewModel = ViewModelProviders.of(thisOfGroupListActivity).get(GroupViewModel.class);
        groupViewModel.delete(group);
    }

    public interface OnGroupClickListener {
        void onGroupClick(int position);
    }

    // store a reference(as a private variable) to the OnItemClickListener object passed on as a parameter
    void setOnItemClickListener(OnGroupClickListener listener) {
        this.listener = listener;
    }
}
