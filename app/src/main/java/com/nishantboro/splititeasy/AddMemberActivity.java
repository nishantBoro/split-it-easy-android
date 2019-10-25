package com.nishantboro.splititeasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddMemberActivity extends AppCompatActivity {
    private EditText editText;
    private MemberViewModel memberViewModel;
    private String gName;

    private void saveMember() {
        String name = editText.getText().toString();

        // check if the name is empty
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return;
        }

        // store the name in database
        memberViewModel = ViewModelProviders.of(this,new MemberViewModelFactory(this.getApplication(),gName)).get(MemberViewModel.class);
        memberViewModel.insert(new MemberEntity(name,gName));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member_activity);

        // get the data(group name) from the intent that started this activity
        Intent intent = getIntent();
        this.gName = intent.getStringExtra(GroupListActivity.EXTRA_TEXT_GNAME);

        this.editText = findViewById(R.id.addMemberNameText);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.addMemberToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add Member to Group");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_member_action_bar_menu,menu);
        return true;
    }

    // call this method when an option in the menu is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.addMemberToolbarMenu) {
            saveMember();
        }

        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
