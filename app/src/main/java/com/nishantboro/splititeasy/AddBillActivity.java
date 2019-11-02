package com.nishantboro.splititeasy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class AddBillActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText editTextItem;
    private EditText editTextCost;
    private String currency;
    private String gName;
    private String paidBy;
    private int memberId;


    private void saveExpense() {
        String item = this.editTextItem.getText().toString();
        String cost = this.editTextCost.getText().toString();

        // check if the name is empty
        if (item.trim().isEmpty() || cost.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
            return;
        }

        BigDecimal decimal = new BigDecimal(cost);
        BigDecimal res = decimal.setScale(2);

        // store to database
        BillViewModel billViewModel = ViewModelProviders.of(this,new BillViewModelFactory(this.getApplication(),this.gName)).get(BillViewModel.class);
        billViewModel.insert(new BillEntity(this.memberId,item,res.toString(),this.currency,this.gName,this.paidBy));
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

        this.editTextCost.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void afterTextChanged(Editable arg0) {
                String str = AddBillActivity.this.editTextCost.getText().toString();
                if (str.isEmpty()) return;
                String str2 = PerfectDecimal(str, 20, 2);

                if (!str2.equals(str)) {
                    AddBillActivity.this.editTextCost.setText(str2);
                    int pos = AddBillActivity.this.editTextCost.getText().length();
                    AddBillActivity.this.editTextCost.setSelection(pos);
                }
            }
        });

        // spinner for select currency
        Spinner spinner = findViewById(R.id.addBillItemCurrencySpinner);
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.currencySymbols,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        // spinner for selecting paidBy Member
        Spinner spinnerPaidBy = findViewById(R.id.addBillItemPaidBy);
        final AllMembersSpinnerAdapter allMembersSpinnerAdapter = new AllMembersSpinnerAdapter(this,new ArrayList<MemberEntity>());
        allMembersSpinnerAdapter.setDropDownViewResource(0);
        spinnerPaidBy.setAdapter(allMembersSpinnerAdapter);
        spinnerPaidBy.setOnItemSelectedListener(this);

        // get all current members of the group
        MemberViewModel memberViewModel = ViewModelProviders.of(this,new MemberViewModelFactory(this.getApplication(),this.gName)).get(MemberViewModel.class);
        memberViewModel.getAllMembers().observe(this, new Observer<List<MemberEntity>>() {
            @Override
            public void onChanged(List<MemberEntity> memberEntities) {
                // Recreate the recycler view by notifying adapter with the changes
                Log.d("Tag x", "called inside onChanged of add bill activity");
                allMembersSpinnerAdapter.clear();
                allMembersSpinnerAdapter.addAll(memberEntities);
                allMembersSpinnerAdapter.notifyDataSetChanged();
            }
        });

        Toolbar toolbar = findViewById(R.id.addBillToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add an Expense");
    }

    public String PerfectDecimal(String str, int MAX_BEFORE_POINT, int MAX_DECIMAL){
        if(str.charAt(0) == '.') str = "0"+str;
        int max = str.length();

        String rFinal = "";
        boolean after = false;
        int i = 0, up = 0, decimal = 0; char t;
        while(i < max){
            t = str.charAt(i);
            if(t != '.' && after == false){
                up++;
                if(up > MAX_BEFORE_POINT) return rFinal;
            }else if(t == '.'){
                after = true;
            }else{
                decimal++;
                if(decimal > MAX_DECIMAL)
                    return rFinal;
            }
            rFinal = rFinal + t;
            i++;
        }return rFinal;
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
                this.memberId = member.id;
                Toast.makeText(parent.getContext(),this.paidBy,Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
