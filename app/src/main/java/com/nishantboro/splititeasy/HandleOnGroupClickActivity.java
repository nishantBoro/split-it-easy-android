package com.nishantboro.splititeasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

public class HandleOnGroupClickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handle_on_group_click_activity);

        // get the data(group name) from the intent that started this activity
        Intent intent = getIntent();
        String gName = intent.getStringExtra(GroupListActivity.EXTRA_TEXT_GNAME);

        TabLayout tabLayout = findViewById(R.id.tablayout_id);
        ViewPager viewPager = findViewById(R.id.viewpager_id);
        Toolbar toolbar = findViewById(R.id.handleOnGroupClickToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        setTitle(gName);

        AddNewGroupFragmentsViewAdapter adapter = new AddNewGroupFragmentsViewAdapter(getSupportFragmentManager(),0);
        adapter.addFragment(new MembersTabFragment(gName),"Members");
        adapter.addFragment(new ExpensesTabFragment(gName),"Expenses");
        adapter.addFragment(new BalancesTabFragment(gName),"Balances");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
