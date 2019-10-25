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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Currency;

public class AddBillActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText editTextItem;
    private EditText editTextCost;
    private String currency;
    private String gName;


    private void saveExpense() {
        String item = editTextItem.getText().toString();
        String cost = editTextCost.getText().toString();

        // check if the name is empty
        if (item.trim().isEmpty() || cost.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
            return;
        }

        // store to database
        BillViewModel billViewModel = ViewModelProviders.of(this,new BillViewModelFactory(this.getApplication(),gName)).get(BillViewModel.class);
        billViewModel.insert(new BillEntity(item,cost,currency,gName));
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

        Spinner spinner = findViewById(R.id.addBillItemCurrencySpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.currencySymbols,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

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
        Currency curr = Currency.getInstance(parent.getItemAtPosition(position).toString());
        currency = curr.getSymbol();
        Toast.makeText(parent.getContext(),currency,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
