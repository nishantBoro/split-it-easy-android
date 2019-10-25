package com.nishantboro.splititeasy;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;


public class GroupListActivity extends AppCompatActivity implements GroupListActivityViewAdapter.OnGroupListener {
    public static final String EXTRA_TEXT_GNAME = "com.nishantboro.splititeasy.EXTRA_TEXT_GNAME";
    private List<GroupEntity> groupNames = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list_activity);

        // prepare recycler view
        RecyclerView recyclerView = findViewById(R.id.group_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        // second parameter -> attach this activity as a listener to every item in the group list
        final GroupListActivityViewAdapter adapter = new GroupListActivityViewAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // if data in database(Group) changes, call the onChanged() below
        GroupViewModel groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        groupViewModel.getAllGroups().observe(this, new Observer<List<GroupEntity>>() {
            @Override
            public void onChanged(List<GroupEntity> groupEntities) {
                // Recreate the recycler view by notifying adapter with the changes
                // here groupEntities is the list of current items in the groupList
                groupNames = groupEntities;
                adapter.saveToList(groupEntities);
            }
        });
    }

    // run this when an item in the group list is clicked
    @Override
    public void onGroupClick(int position) {
        // get group name of the item the user clicked on from groupNames array
        String gName = groupNames.get(position).gName;

        // create an intent to launch the HandleOnGroupClickActivity, pass the gName along
        Intent intent = new Intent(this,HandleOnGroupClickActivity.class);
        intent.putExtra(EXTRA_TEXT_GNAME,gName);
        startActivity(intent);
    }
}
