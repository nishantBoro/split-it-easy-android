package com.nishantboro.splititeasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class AddBillActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText editTextItem;
    private EditText editTextCost;
    private String currency;
    private String gName;
    private String paidBy;
    private List<MemberEntity> members = new ArrayList<>();


    private void saveExpense() {
        String item = this.editTextItem.getText().toString();
        String cost = this.editTextCost.getText().toString();

        // check if the name is empty
        if (item.trim().isEmpty() || cost.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
            return;
        }

        // store to database
        BillViewModel billViewModel = ViewModelProviders.of(this,new BillViewModelFactory(this.getApplication(),this.gName)).get(BillViewModel.class);
        billViewModel.insert(new BillEntity(item,cost,this.currency,this.gName,this.paidBy));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_bill);

        // get the data(group name) from the intent that started this activity
        Intent intent = getIntent();
        this.gName = intent.getStringExtra(GroupListActivity.EXTRA_TEXT_GNAME);

        this.editTextItem = findViewById(R.id.addBillItemName);
        this.editTextCost = findViewById(R.id.addBillItemCost);

        // spinner for select currency
        Spinner spinner = findViewById(R.id.addBillItemCurrencySpinner);
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.currencySymbols,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        // spinner for selecting paidBy
        Spinner spinnerPaidBy = findViewById(R.id.addBillItemPaidBy);
        final AddBillPaidBySpinnerAdapter spinnerPaidByAdapter = new AddBillPaidBySpinnerAdapter(this,this.members);
        spinnerPaidByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaidBy.setAdapter(spinnerPaidByAdapter);
        spinnerPaidBy.setOnItemSelectedListener(this);

        // get all current members of the group
        MemberViewModel memberViewModel = ViewModelProviders.of(this,new MemberViewModelFactory(this.getApplication(),this.gName)).get(MemberViewModel.class);
        memberViewModel.getAllMembers().observe(this, new Observer<List<MemberEntity>>() {
            @Override
            public void onChanged(List<MemberEntity> memberEntities) {
                // Recreate the recycler view by notifying adapter with the changes
                Log.d("Tag x", "called inside onChanged of add bill");
                spinnerPaidByAdapter.clear();
                spinnerPaidByAdapter.addAll(memberEntities);
                spinnerPaidByAdapter.notifyDataSetChanged();
            }
        });

        Toolbar toolbar = findViewById(R.id.addBillToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add an Expense");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_bill_action_bar_menu,menu);
        return true;
    }

    // call this method when an option in the menu is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.addBillToolbarMenu) {
            saveExpense();
        }
        finish();
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.addBillItemCurrencySpinner:Currency curr = Currency.getInstance(parent.getItemAtPosition(position).toString());
                this.currency = curr.getSymbol();
                Log.d("p", "selected currency");
                Toast.makeText(parent.getContext(),this.currency,Toast.LENGTH_SHORT).show();break;
            case R.id.addBillItemPaidBy:
                Log.d("t", "selected paidBy");
                MemberEntity member = (MemberEntity) parent.getItemAtPosition(position);
                this.paidBy = member.name;
                Toast.makeText(parent.getContext(),this.paidBy,Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
