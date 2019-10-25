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

public class CreateNewGroupActivity extends AppCompatActivity {
    private EditText editText;
    private GroupViewModel groupViewModel;

    private void saveGroup() {
        String name = editText.getText().toString();

        // check if the name is empty
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return;
        }
        // take care no duplicate group name is inserted here

        // store the group name in database
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
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


