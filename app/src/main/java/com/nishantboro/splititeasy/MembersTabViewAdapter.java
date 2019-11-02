package com.nishantboro.splititeasy;

// Objective: To prepare an adapter that could throw all the items from membersList Array(MembersTabFragment class)
// to a recycler view

import android.app.Application;
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


public class MembersTabViewAdapter extends RecyclerView.Adapter<MembersTabViewAdapter.MemberDetailViewHolder> {
    private OnItemClickListener listener;
    private List<MemberEntity> list = new ArrayList<>();
    public boolean multiSelect = false;
    public List<MemberEntity> selectedItems = new ArrayList<>();
    private String gName;
    private Application application;
    private MembersTabFragment thisOfMemberFragment;
    public ActionMode actionMode;

    MembersTabViewAdapter(String gName, Application application, MembersTabFragment thisOfMemberFragment) {
        this.gName = gName;
        this.application = application;
        this.thisOfMemberFragment = thisOfMemberFragment;
    }

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MembersTabViewAdapter.this.multiSelect = true;
            menu.add("Delete");
            MembersTabViewAdapter.this.actionMode = mode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            for(MemberEntity member: MembersTabViewAdapter.this.selectedItems) {
                MembersTabViewAdapter.this.list.remove(member);
                MembersTabViewAdapter.this.deleteFromDatabase(member);
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            MembersTabViewAdapter.this.multiSelect = false;
            MembersTabViewAdapter.this.selectedItems.clear();
            MembersTabViewAdapter.this.notifyDataSetChanged();
        }
    };

    // Provide a reference to the views for each name in our membersList Array:
    class MemberDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private RelativeLayout relativeLayout;

        MemberDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.memberDetailName); // get the textView View component from member_detail.xml and attach it to our holder
            this.relativeLayout = itemView.findViewById(R.id.memberDetail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(MembersTabViewAdapter.this.listener != null && position != RecyclerView.NO_POSITION) {
                        MembersTabViewAdapter.this.listener.onItemClick(MembersTabViewAdapter.this.list.get(position));
                    }
                }
            });
        }

        void update(final MemberEntity member) {

            // if the user clicks on back button while an item was selected(gray colour), notifyDataSetChanged is called, hence update() is called again for every viewHolder. So, at this point
            // we need to make sure that the item that was selected(gray colour) previously, needs to be white(unselected) now.
            if (MembersTabViewAdapter.this.selectedItems.contains(member)) {
                this.relativeLayout.setBackgroundColor(Color.LTGRAY);
            } else {
                this.relativeLayout.setBackgroundColor(Color.WHITE);
            }

            // attach a long click listener
            this.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((AppCompatActivity)v.getContext()).startSupportActionMode(MembersTabViewAdapter.this.actionModeCallbacks);
                    MembersTabViewAdapter.MemberDetailViewHolder.this.selectItem(member);
                    return true;
                }
            });

            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MembersTabViewAdapter.MemberDetailViewHolder.this.selectItem(member);
                }
            });
        }

        void selectItem(MemberEntity member) {
            if (MembersTabViewAdapter.this.multiSelect) {
                if (MembersTabViewAdapter.this.selectedItems.contains(member)) {
                    MembersTabViewAdapter.this.selectedItems.remove(member);
                    this.relativeLayout.setBackgroundColor(Color.WHITE);
                } else {
                    MembersTabViewAdapter.this.selectedItems.add(member);
                    this.relativeLayout.setBackgroundColor(Color.LTGRAY);
                }
            }
        }

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
        holder.textView.setText(this.list.get(position).name);
        holder.update(this.list.get(position));
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public void storeToList(List<MemberEntity> memberEntities) {
        this.list = memberEntities;
        this.notifyDataSetChanged();
    }

    public void deleteFromDatabase(MemberEntity member) {
        MemberViewModel memberViewModel = ViewModelProviders.of(MembersTabViewAdapter.this.thisOfMemberFragment,new MemberViewModelFactory(MembersTabViewAdapter.this.application,MembersTabViewAdapter.this.gName)).get(MemberViewModel.class);
        memberViewModel.delete(member);
    }

    public interface OnItemClickListener {
        void onItemClick(MemberEntity member);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
