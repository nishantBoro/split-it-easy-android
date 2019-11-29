package com.nishantboro.splititeasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;


public class CreateNewGroupActivity extends AppCompatActivity {
    private EditText editText;

    private void saveGroup() {
        final String name = editText.getText().toString();

        // check if the name is empty
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return;
        }

        GroupViewModel groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);

        GroupEntity group = new GroupEntity(name); // create group object that needs to be inserted into the database
        group.gCurrency = "USD-($)"; // set default currency

        // if database already contains group object return and do not save
        List<GroupEntity> groups = groupViewModel.getAllGroupsNonLiveData();
        for(GroupEntity item:groups) {
            if(item.gName.equals(group.gName)) {
                Toast.makeText(this, "Group already exists", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // else store the group object in database
        groupViewModel.insert(group);
        Toast.makeText(this, "Group created successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_group_activity);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.createNewGroupToolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("Create New Group");

        // get reference to edit text-"Enter group name"
        editText = findViewById(R.id.createNewGroupName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // fill toolbar menu with save group item
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_group_bar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // save the group to database if user clicks on save group item in the toolbar
        if(item.getItemId() == R.id.createNewGroupToolbarSaveGroupItem) {
            saveGroup();
        }

        finish(); // initiate finish if user clicks on back button
        return true;
    }
}


