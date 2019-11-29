package com.nishantboro.splititeasy;

import android.app.Application;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class MembersTabViewAdapter extends RecyclerView.Adapter<MembersTabViewAdapter.MemberDetailViewHolder> {
    private OnItemClickListener listener;
    private List<MemberEntity> list = new ArrayList<>(); // maintain a list of all the existing members in the database
    boolean multiSelect = false; // true if user has selected any item
    List<MemberEntity> selectedItems = new ArrayList<>();
    private String gName;
    private Application application;
    private MembersTabFragment thisOfMemberFragment;
    ActionMode actionMode;

    // constructor
    MembersTabViewAdapter(String gName, Application application, MembersTabFragment thisOfMemberFragment) {
        this.gName = gName;
        this.application = application;
        this.thisOfMemberFragment = thisOfMemberFragment;
    }

    /*
    handles all kinds of action when ActionMode is active.
    In our case, when the user does a long click on any recycler view item, ActionMode is activated
    and the following actionModeCallbacks object is created: */
    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {

        // method is called right after the user does a long click on any item
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true; // since user has already selected one item after the long click
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
            for(MemberEntity member: selectedItems) {
                list.remove(member);
                deleteFromDatabase(member);
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
    class MemberDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private RelativeLayout relativeLayout;
        private ImageView imageView;

        MemberDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            // store all references from our layout for future use
            textView = itemView.findViewById(R.id.memberDetailName); // get the textView view component reference from member_detail.xml and attach it to our holder
            relativeLayout = itemView.findViewById(R.id.memberDetail);
            imageView = itemView.findViewById(R.id.memberDetailAvatar); // member avatar image reference
        }

        void update(final MemberEntity member) {

            /* if the user clicks on back button while an item was selected(gray colour), notifyDataSetChanged is called, hence update() is called again for every viewHolder. So, at this point
               we need to make sure that the item that was selected(gray colour) previously, needs to be white(unselected) now. */
            if (selectedItems.contains(member)) {
                relativeLayout.setBackgroundColor(Color.LTGRAY);
            } else {
                relativeLayout.setBackgroundColor(Color.WHITE);
            }

            // attach a long click listener to itemView
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // at this point the user has successfully initiated a long click and hence we need to activate ActionMode now to handle multiple select and delete items
                    ((AppCompatActivity)v.getContext()).startSupportActionMode(actionModeCallbacks); // activate ActionMode and let actionModeCallback handle actions now
                    selectItem(member); // here member is the initially selected item after the long click event
                    return true;
                }
            });
        }

        void selectItem(MemberEntity member) {
            if (multiSelect) {
                if (selectedItems.contains(member)) { // if  we select a member that is already selected(light gray), deselect it(change colour to white) and remove from selectedItems list
                    selectedItems.remove(member);
                    relativeLayout.setBackgroundColor(Color.WHITE);
                } else { // else add the member to our selection list and change colour to light gray
                    selectedItems.add(member);
                    relativeLayout.setBackgroundColor(Color.LTGRAY);
                }
            }
        }

    }

    // Create new viewHolder (invoked by the layout manager). Note that this method is called for creating every MemberDetailViewHolder required for our recycler view items
    @NonNull
    @Override
    public MemberDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_detail, parent, false);
        return new MemberDetailViewHolder(v); // pass the inflated view, create a holder with textView and avatar components attached to it and return
    }

    // note that this method is called for every MemberDetailViewHolder
    @Override
    public void onBindViewHolder(@NonNull MemberDetailViewHolder holder, int position) {
        final MemberDetailViewHolder hold = holder;
        holder.textView.setText(list.get(position).name); // set member name to holder
        holder.imageView.setImageResource(list.get(position).mAvatar); // set member avatar to holder
        holder.update(list.get(position));

        final int pos = position;

        // attach a on click listener to the MemberDetailViewHolder
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Note that if the user is in multiSelect mode, the function for individual click on any item(multiSelect off) shouldn't be initiated*/
                if(multiSelect) {  // if multiSelect is on and user clicks on any other item, run selectItem function for that item
                    hold.selectItem(list.get(pos));
                }
                if(listener != null && !multiSelect) { // if multiSelect is Off, clicking on any item should initiate edit member intent
                    listener.onItemClick(list.get(pos)); // onItemClick method defined in MembersTabFragment[line 69]
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void storeToList(List<MemberEntity> memberEntities) {
        list = memberEntities;
        notifyDataSetChanged();
    }

    private void deleteFromDatabase(MemberEntity member) {
        MemberViewModel memberViewModel = ViewModelProviders.of(thisOfMemberFragment,new MemberViewModelFactory(application,gName)).get(MemberViewModel.class);
        memberViewModel.delete(member);
    }

    public interface OnItemClickListener {
        void onItemClick(MemberEntity member);
    }

    // store a reference(as a private variable) to the OnItemClickListener object passed on as a parameter
    void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
