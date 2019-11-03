package com.nishantboro.splititeasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;


public class CreateNewGroupActivity extends AppCompatActivity {
    private EditText editText;
    private GroupViewModel groupViewModel;

    private void saveGroup() {
        final String name = editText.getText().toString();

        // check if the name is empty
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return;
        }

        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        GroupEntity group = new GroupEntity(name);

        // if database already contains group name return and do not save
        List<GroupEntity> groups = groupViewModel.getAllGroupsNonLiveData();
        for(GroupEntity item:groups) {
            if(item.gName.equals(group.gName)) {
                Toast.makeText(this, "Group already exists", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // else store the group name in database
        groupViewModel.insert(new GroupEntity(name));
        Toast.makeText(this, "Group created successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_group_activity);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.createNewGroupToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Create Group");

        // get reference to edit text-"Enter group name"
        editText = findViewById(R.id.createNewGroupName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_group_bar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // save the group if user clicks on save group item
        if(item.getItemId() == R.id.createNewGroupToolbarSaveGroupItem) {
            saveGroup();
        }

        finish();
        return true;
    }
}


