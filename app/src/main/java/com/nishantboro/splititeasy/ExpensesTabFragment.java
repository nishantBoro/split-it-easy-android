package com.nishantboro.splititeasy;

import android.content.Intent;
import android.os.Bundle;
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
    private String gName; // group name
    private List<BillEntity> bills = new ArrayList<>(); // maintain a list of all the existing bills of the group from the database
    private List<MemberEntity> members = new ArrayList<>(); // maintain a list of all the existing members of the group from the database
    private BillViewModel billViewModel;
    private ExpensesTabViewAdapter adapter;
    private StringBuilder currency = new StringBuilder();

    static ExpensesTabFragment newInstance(String gName) {
        Bundle args = new Bundle();
        args.putString("group_name", gName);
        ExpensesTabFragment f = new ExpensesTabFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expenses_fragment,container,false);
        if(getArguments() == null) {
            return view;
        }
        gName = getArguments().getString("group_name"); // get group name from bundle

        // prepare recycler view for displaying all expenses of the group
        RecyclerView recyclerView = view.findViewById(R.id.expensesRecyclerView);
        recyclerView.setHasFixedSize(true);
        if(getActivity() != null) {
            adapter = new ExpensesTabViewAdapter(gName, getActivity().getApplication(), this);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        // if data in database(BillEntity) changes, call the onChanged() below
        billViewModel = ViewModelProviders.of(this,new BillViewModelFactory(getActivity().getApplication(),gName)).get(BillViewModel.class);
        billViewModel.getAllBills().observe(this, new Observer<List<BillEntity>>() {
            @Override
            public void onChanged(List<BillEntity> billEntities) {
                GroupViewModel groupViewModel = ViewModelProviders.of(ExpensesTabFragment.this).get(GroupViewModel.class);
                // get latest currency picked by the user
                currency.setLength(0); // delete previous currency
                currency.append(groupViewModel.getGroupCurrencyNonLive(gName));

                adapter.storeToList(billEntities, currency.toString()); // Recreate the recycler view by passing the new List<BillEntity> and currency to the adapter
                bills = billEntities;
            }
        });

        // get all the existing members from the database
        MemberViewModel memberViewModel = ViewModelProviders.of(this,new MemberViewModelFactory(getActivity().getApplication(),gName)).get(MemberViewModel.class);
        memberViewModel.getAllMembers().observe(this, new Observer<List<MemberEntity>>() {
            @Override
            public void onChanged(List<MemberEntity> memberEntities) {
                members = memberEntities;
            }
        });

        // Implement Add new expense function
        FloatingActionButton addFloating = view.findViewById(R.id.expensesFragmentAdd);
        addFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create an add expense intent
                if(!members.isEmpty() && getActivity() != null) { // condition prevents launching an add bill activity if there are no existing members of the group
                    Intent intent = new Intent(getActivity(), AddEditBillActivity.class);
                    intent.putExtra(GroupListActivity.EXTRA_TEXT_GNAME,gName);
                    intent.putExtra("groupCurrency", currency.toString());
                    intent.putExtra("requestCode",1); // using requestCode(value = 1) to identify add expense intent
                    getActivity().startActivityFromFragment(ExpensesTabFragment.this,intent,1);
                } else {
                    Toast.makeText(getActivity(), "No members found. Please add some members.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // implement edit expense function
        // create new ExpensesTabViewAdapter.OnItemClickListener interface object and pass it as a parameter to ExpensesTabViewAdapter.setOnItemClickListener method
        adapter.setOnItemClickListener(new ExpensesTabViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BillEntity bill) {
                // create an edit expense intent
                Intent intent = new Intent(getActivity(), AddEditBillActivity.class);
                intent.putExtra("billId",bill.id);
                intent.putExtra("billPaidBy",bill.paidBy);
                intent.putExtra("billCost",bill.cost);
                intent.putExtra("billMemberId",bill.mid);
                intent.putExtra("billName",bill.item);
                intent.putExtra("groupCurrency", currency.toString());
                intent.putExtra(GroupListActivity.EXTRA_TEXT_GNAME,bill.gName);
                intent.putExtra("requestCode",2); // using requestCode(value = 2) to identify edit expense intent

                if(getActivity() != null) {
                    getActivity().startActivityFromFragment(ExpensesTabFragment.this, intent, 2); // launch the intent
                }
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
        if(item.getItemId() == R.id.deleteAllBills) {
            if(!bills.isEmpty()) { // condition prevents initiating a deleteAll operation if there are no bills to delete
                billViewModel.deleteAll(gName);
                Toast.makeText(getActivity(), "All Expenses Deleted", Toast.LENGTH_SHORT).show();
                return true;
            }
            Toast.makeText(getActivity(), "Nothing To Delete", Toast.LENGTH_SHORT).show();
            return super.onOptionsItemSelected(item);
        }
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        // close ActionMode if the user decides to leave the fragment while multiSelect is ON
        if(adapter.multiSelect) {
            adapter.actionMode.finish();
            adapter.multiSelect = false;
            adapter.selectedItems.clear();
            adapter.notifyDataSetChanged();
        }
        super.onPause();
    }
}
