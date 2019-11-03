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
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddEditMemberActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText editText;
    private ImageView imageView;
    private MemberViewModel memberViewModel;
    private String gName;
    private int requestCode;
    private int userId;
    private int avatarResource;


    private void saveEditMember() {
        String name = editText.getText().toString();

        // check if the name is empty
        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return;
        }

        memberViewModel = ViewModelProviders.of(this,new MemberViewModelFactory(this.getApplication(),this.gName)).get(MemberViewModel.class);

        if(this.requestCode == 1) {
            // store the name in database
            MemberEntity member = new MemberEntity(name,gName);
            member.setMAvatar(this.avatarResource);
            memberViewModel.insert(member);
        }

        if(this.requestCode == 2) {
            // update the database
            MemberEntity member = new MemberEntity(name,gName);

            if(this.userId == -1) {
                Log.d("userIdError", "member cannot be updated");
                return;
            }
            member.setId(this.userId);
            member.setMAvatar(this.avatarResource);
            Log.d("userID", Integer.toString(this.userId));
            memberViewModel.update(member);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Log.d("look", "i ran oncreate addeditmember");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member_activity);

        // get the data(group name) from the intent that started this activity
        Intent intent = getIntent();
        this.gName = intent.getStringExtra(GroupListActivity.EXTRA_TEXT_GNAME);
        this.editText = findViewById(R.id.addMemberNameText);
        this.imageView = findViewById(R.id.memberDetailAvatar);
        this.requestCode = intent.getIntExtra("requestCode",0);
        this.userId = intent.getIntExtra(MembersTabFragment.EXTRA_ID,-1);

        // set toolbar
        Toolbar toolbar = findViewById(R.id.addMemberToolbar);
        setSupportActionBar(toolbar);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set spinner for avatar
        final Spinner spinnerAvatar = findViewById(R.id.addMemberActivityAvatarSpinner);
        final AddEditMemberAvatarSpinnerAdapter addEditMemberAvatarSpinnerAdapter = new AddEditMemberAvatarSpinnerAdapter(this,new ArrayList<Integer>());
        addEditMemberAvatarSpinnerAdapter.setDropDownViewResource(0);
        spinnerAvatar.setAdapter(addEditMemberAvatarSpinnerAdapter);
        spinnerAvatar.setOnItemSelectedListener(this);

        // populate the spinner list
        List<Integer> avatarOptions = new ArrayList<>();
        this.populateAvatarList(avatarOptions);
        addEditMemberAvatarSpinnerAdapter.addAll(avatarOptions);

        if(intent.hasExtra(MembersTabFragment.EXTRA_TEXT)) {
            setTitle("Edit Member");
            this.editText.setText(intent.getStringExtra(MembersTabFragment.EXTRA_TEXT));
            Log.d("asd", Integer.toString(intent.getIntExtra("avatarResource",-1)));
            int spinnerAvatarPosition = addEditMemberAvatarSpinnerAdapter.getPosition(intent.getIntExtra("avatarResource",-1));
            spinnerAvatar.setSelection(spinnerAvatarPosition);
        } else {
            setTitle("Add Member to Group");
        }
    }

    private void populateAvatarList(List<Integer> avatarOptions) {
        avatarOptions.add(R.drawable.member);
        avatarOptions.add(R.drawable.member_female_one);
        avatarOptions.add(R.drawable.member_female_two);
        avatarOptions.add(R.drawable.member_female_three);
        avatarOptions.add(R.drawable.member_female_four);
        avatarOptions.add(R.drawable.member_female_five);
        avatarOptions.add(R.drawable.member_male_one);
        avatarOptions.add(R.drawable.member_male_two);
        avatarOptions.add(R.drawable.member_male_three);
        avatarOptions.add(R.drawable.member_male_four);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.addMemberActivityAvatarSpinner:
                this.avatarResource = Integer.parseInt(parent.getItemAtPosition(position).toString());
                Log.d("p", "selected avatar");
            default:break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
