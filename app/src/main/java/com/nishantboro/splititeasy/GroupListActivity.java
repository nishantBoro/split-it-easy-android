package com.nishantboro.splititeasy;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class GroupListActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT_GNAME = "com.nishantboro.splititeasy.EXTRA_TEXT_GNAME";
    private List<GroupEntity> groupNames = new ArrayList<>();
    private GroupListActivityViewAdapter adapter;
    private GroupViewModel groupViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list_activity);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.groupListToolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("Groups");

        // prepare recycler view
        RecyclerView recyclerView = findViewById(R.id.group_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        // second parameter below -> attach this activity as a listener to every item in the group list
        adapter = new GroupListActivityViewAdapter(GroupListActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // if data in database(Group) changes, call the onChanged() below
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        groupViewModel.getAllGroups().observe(this, new Observer<List<GroupEntity>>() {
            @Override
            public void onChanged(List<GroupEntity> groupEntities) {
                // Recreate the recycler view by notifying adapter with the changes
                // here groupEntities is the list of current items in the groupList
                groupNames = groupEntities;
                adapter.saveToList(groupEntities);
                // check if there are no groups
                TextView emptyListMsgTV = (TextView) findViewById(R.id.noGroupsMsg);
                if (adapter.getItemCount() == 0) {
                    emptyListMsgTV.setText("No groups found :(\nPlease create a new group");
                }
            }
        });

        adapter.setOnItemClickListener(new GroupListActivityViewAdapter.OnGroupClickListener() {
            @Override
            public void onGroupClick(int position) {
                // get group name of the item the user clicked on from groupNames array
                String gName = groupNames.get(position).gName;

                // create an intent to launch the HandleOnGroupClickActivity, pass the gName along
                Intent intent = new Intent(GroupListActivity.this,HandleOnGroupClickActivity.class);
                intent.putExtra(EXTRA_TEXT_GNAME,gName);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.group_list_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.deleteAllGroups) {
            if(!groupNames.isEmpty()) {
                groupViewModel.deleteAll();
                Toast.makeText(this, "All Groups Deleted", Toast.LENGTH_SHORT).show();
                return true;
            }
            Toast.makeText(this, "Nothing To Delete", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
        // finish if user clicks on back button
        finish();
        return true;
    }

    @Override
    public void onPause() {
        if(adapter.multiSelect) {
            adapter.actionMode.finish();
            adapter.multiSelect = false;
            adapter.selectedItems.clear();
            adapter.notifyDataSetChanged();
        }
        super.onPause();
    }
}
