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

public class AddEditBillActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText editTextItem;
    private EditText editTextCost;
    private String currency;
    private String gName;
    private String paidBy;
    private int memberId;
    private int requestCode;
    private int billId;

    private void saveExpense() {
        String item = this.editTextItem.getText().toString();
        String cost = this.editTextCost.getText().toString();

        // check if the name is empty
        if (item.trim().isEmpty() || cost.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
            return;
        }

        BillViewModel billViewModel = ViewModelProviders.of(this,new BillViewModelFactory(this.getApplication(),this.gName)).get(BillViewModel.class);


        if(this.requestCode == 1) {
            BigDecimal decimal = new BigDecimal(cost);
            BigDecimal res = decimal.setScale(2);

            // store to database
            Log.d("1", Integer.toString(this.memberId));
            billViewModel.insert(new BillEntity(AddEditBillActivity.this.memberId,item,res.toString(),AddEditBillActivity.this.currency,AddEditBillActivity.this.gName,AddEditBillActivity.this.paidBy));
        }

        if(this.requestCode == 2) {
            BillEntity bill = new BillEntity(AddEditBillActivity.this.memberId,item,cost,AddEditBillActivity.this.currency,AddEditBillActivity.this.gName,AddEditBillActivity.this.paidBy);
            bill.setId(this.billId);

            billViewModel.update(bill);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_bill);

        this.editTextItem = findViewById(R.id.addBillItemName);
        this.editTextCost = findViewById(R.id.addBillItemCost);
        this.editTextCost.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void afterTextChanged(Editable arg0) {
                String str = AddEditBillActivity.this.editTextCost.getText().toString();
                if (str.isEmpty()) return;
                String str2 = PerfectDecimal(str, 20, 2);

                if (!str2.equals(str)) {
                    AddEditBillActivity.this.editTextCost.setText(str2);
                    int pos = AddEditBillActivity.this.editTextCost.getText().length();
                    AddEditBillActivity.this.editTextCost.setSelection(pos);
                }
            }
        });



        // get the data(group name) from the intent that started this activity
        Intent intent = getIntent();
        this.gName = intent.getStringExtra(GroupListActivity.EXTRA_TEXT_GNAME);
        this.requestCode = intent.getIntExtra("requestCode",0);
        this.memberId = intent.getIntExtra("billMemberId",-1);
        this.billId = intent.getIntExtra("billId",-1);

        // spinner for select currency
        Spinner spinner = findViewById(R.id.addBillItemCurrencySpinner);
        final ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.currencySymbols,android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);


        // spinner for selecting paidBy Member
        final Spinner spinnerPaidBy = findViewById(R.id.addBillItemPaidBy);
        final AllMembersSpinnerAdapter allMembersSpinnerAdapter = new AllMembersSpinnerAdapter(this,new ArrayList<MemberEntity>());
        allMembersSpinnerAdapter.setDropDownViewResource(0);
        spinnerPaidBy.setAdapter(allMembersSpinnerAdapter);
        spinnerPaidBy.setOnItemSelectedListener(this);

        // get all current members of the group
        MemberViewModel memberViewModel = ViewModelProviders.of(this,new MemberViewModelFactory(this.getApplication(),this.gName)).get(MemberViewModel.class);
        memberViewModel.getAllMembers().observe(this, new Observer<List<MemberEntity>>() {
            @Override
            public void onChanged(List<MemberEntity> memberEntities) {
                allMembersSpinnerAdapter.clear();
                allMembersSpinnerAdapter.addAll(memberEntities);
                allMembersSpinnerAdapter.notifyDataSetChanged();

                if(AddEditBillActivity.this.requestCode == 2) {
                    MemberEntity member = new MemberEntity(AddEditBillActivity.this.paidBy,AddEditBillActivity.this.gName);
                    member.setId(AddEditBillActivity.this.memberId);
                    int spinnerPositionPaidBy = allMembersSpinnerAdapter.getPosition(member);
                    Log.d("kla", Integer.toString(spinnerPositionPaidBy));
                    spinnerPaidBy.setSelection(spinnerPositionPaidBy);
                }
            }

        });


        Toolbar toolbar = findViewById(R.id.addBillToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(intent.hasExtra("billId")) {
            setTitle("Edit expense");
            this.editTextItem.setText(intent.getStringExtra("billName"));
            this.editTextCost.setText(intent.getStringExtra("billCost"));
            this.paidBy = intent.getStringExtra("billPaidBy");
            this.currency = intent.getStringExtra("billCurrency");

            int spinnerPositionCurrency = spinnerAdapter.getPosition(this.currency);
            spinner.setSelection(spinnerPositionCurrency);

        } else {
            setTitle("Add an Expense");
        }

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
            case R.id.addBillItemCurrencySpinner:
                this.currency = parent.getItemAtPosition(position).toString();
                Log.d("p", "selected currency");
                Toast.makeText(parent.getContext(),this.currency,Toast.LENGTH_SHORT).show();break;
            case R.id.addBillItemPaidBy:
                Log.d("t", "selected paidBy");
                MemberEntity member = (MemberEntity) parent.getItemAtPosition(position);
                this.paidBy = member.name;
                this.memberId = member.id;
                Toast.makeText(parent.getContext(),this.paidBy,Toast.LENGTH_SHORT).show();
                break;
            default:break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
