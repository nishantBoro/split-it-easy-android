package com.nishantboro.splititeasy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;


public class ExpensesTabFragment extends Fragment {
    private String gName;
    private List<MemberEntity> members = new ArrayList<>();
    private List<BillEntity> bills = new ArrayList<>();
    private BillViewModel billViewModel;
    private ExpensesTabViewAdapter adapter;
    public ExpensesTabFragment(String gName) {this.gName = gName;}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // retrieve bills from database
        View view = inflater.inflate(R.layout.expenses_fragment,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.expensesRecyclerView);
        recyclerView.setHasFixedSize(true);
        ExpensesTabFragment.this.adapter = new ExpensesTabViewAdapter(gName,this.getActivity().getApplication(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);

        Log.d("y", "on createView called of expenses()");

        // if data in database(Bill) changes, call the onChanged() below
        billViewModel = ViewModelProviders.of(this,new BillViewModelFactory(this.getActivity().getApplication(),this.gName)).get(BillViewModel.class);
        billViewModel.getAllBills().observe(this, new Observer<List<BillEntity>>() {
            @Override
            public void onChanged(List<BillEntity> billEntities) {
                Log.d("y", "inside of onchanged expenses");
                ExpensesTabFragment.this.adapter.storeToList(billEntities);
                ExpensesTabFragment.this.bills = billEntities;
            }
        });

        MemberViewModel memberViewModel = ViewModelProviders.of(this,new MemberViewModelFactory(this.getActivity().getApplication(),this.gName)).get(MemberViewModel.class);
        memberViewModel.getAllMembers().observe(this, new Observer<List<MemberEntity>>() {
            @Override
            public void onChanged(List<MemberEntity> memberEntities) {
                ExpensesTabFragment.this.members = memberEntities;
            }
        });

        // Implement Add new member function
        FloatingActionButton addFloating = view.findViewById(R.id.expensesFragmentAdd);
        addFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ExpensesTabFragment.this.members.isEmpty()) {
                    Intent intent = new Intent(getActivity(), AddEditBillActivity.class);
                    intent.putExtra(GroupListActivity.EXTRA_TEXT_GNAME,gName);
                    intent.putExtra("requestCode",1);
                    ExpensesTabFragment.this.getActivity().startActivityFromFragment(ExpensesTabFragment.this,intent,1);
                } else {
                    Toast.makeText(ExpensesTabFragment.this.getActivity(), "No members found. Please add some members.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.adapter.setOnItemClickListener(new ExpensesTabViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BillEntity bill) {
                Intent intent = new Intent(ExpensesTabFragment.this.getActivity(), AddEditBillActivity.class);
                intent.putExtra("billId",bill.id);
                intent.putExtra("billPaidBy",bill.paidBy);
                intent.putExtra("billCost",bill.cost);
                intent.putExtra("billMemberId",bill.mid);
                intent.putExtra("billName",bill.item); //
                intent.putExtra("billCurrency",bill.currency);
                intent.putExtra(GroupListActivity.EXTRA_TEXT_GNAME,bill.gName);
                intent.putExtra("requestCode",2);
                ExpensesTabFragment.this.getActivity().startActivityFromFragment(ExpensesTabFragment.this,intent,2);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.expenses_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAllBills:
                if(!ExpensesTabFragment.this.bills.isEmpty()) {
                    ExpensesTabFragment.this.billViewModel.deleteAll(this.gName);
                    Toast.makeText(ExpensesTabFragment.this.getActivity(), "All Expenses Deleted", Toast.LENGTH_SHORT).show();
                    return true;
                }
                Toast.makeText(ExpensesTabFragment.this.getActivity(), "Nothing To Delete", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        if(this.adapter.multiSelect) {
            this.adapter.actionMode.finish();
            this.adapter.multiSelect = false;
            this.adapter.selectedItems.clear();
            this.adapter.notifyDataSetChanged();
        }
        super.onPause();
    }
}
