package com.nishantboro.splititeasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditMemberActivity extends AppCompatActivity {
    private EditText editText;
    private MemberViewModel memberViewModel;
    private String gName;
    private int requestCode;
    private int userId;

    private void saveEditMember() {
        String name = editText.getText().toString();

        // check if the name is empty
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return;
        }

        memberViewModel = ViewModelProviders.of(this,new MemberViewModelFactory(this.getApplication(),this.gName)).get(MemberViewModel.class);

        if(this.requestCode == 1) {
            // prevent duplicate member names
            


            // store the name in database
            memberViewModel.insert(new MemberEntity(name,gName));
            return;
        }

        if(this.requestCode == 2) {
            // update the database
            MemberEntity member = new MemberEntity(name,this.gName);

            if(this.userId == -1) {
                Log.d("userIdError", "member cannot be updated");
                return;
            }

            member.setId(this.userId);
            Log.d("userID", Integer.toString(this.userId));
            memberViewModel.update(member);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member_activity);

        // get the data(group name) from the intent that started this activity
        Intent intent = getIntent();
        this.gName = intent.getStringExtra(GroupListActivity.EXTRA_TEXT_GNAME);
        this.editText = findViewById(R.id.addMemberNameText);
        this.requestCode = intent.getIntExtra("requestCode",0);
        this.userId = intent.getIntExtra(MembersTabFragment.EXTRA_ID,-1);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.addMemberToolbar);
        setSupportActionBar(toolbar);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(intent.hasExtra(MembersTabFragment.EXTRA_TEXT)) {
            setTitle("Edit Member");
            this.editText.setText(intent.getStringExtra(MembersTabFragment.EXTRA_TEXT));
        } else {
            setTitle("Add Member to Group");
        }
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
            saveEditMember();
        }

        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
