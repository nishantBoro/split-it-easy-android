package com.nishantboro.splititeasy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // define button variables
        View listGroups = findViewById(R.id.listGroups);
        View addNewGroup = findViewById(R.id.addNewGroup);

        // attach click listener to buttons
        listGroups.setOnClickListener(this);
        addNewGroup.setOnClickListener(this);
    }

    // run this method on every click event
    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()) {
            case R.id.listGroups : intent = new Intent(this,GroupListActivity.class);startActivity(intent);break;
            case R.id.addNewGroup : intent = new Intent(this,CreateNewGroupActivity.class);startActivity(intent);break;
            default:break;
        }
    }
}
