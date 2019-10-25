package com.nishantboro.splititeasy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class ExpensesTabFragment extends Fragment {
    private String gName;

    public ExpensesTabFragment(String gName) {this.gName = gName;}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // retrieve bills from database
        View view = inflater.inflate(R.layout.expenses_fragment,container,false);
        RecyclerView recyclerView = view.findViewById(R.id.expensesRecyclerView);
        recyclerView.setHasFixedSize(true);
        final ExpensesTabViewAdapter adapter = new ExpensesTabViewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);

        // if data in database(Bill) changes, call the onChanged() below
        BillViewModel billViewModel = ViewModelProviders.of(this,new BillViewModelFactory(this.getActivity().getApplication(),gName)).get(BillViewModel.class);
        billViewModel.getAllBills().observe(this, new Observer<List<BillEntity>>() {
            @Override
            public void onChanged(List<BillEntity> billEntities) {
                adapter.storeToList(billEntities);
            }
        });


        // Implement Add new member function
        FloatingActionButton addFloating = view.findViewById(R.id.expensesFragmentAdd);
        addFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddBillActivity.class);
                intent.putExtra(GroupListActivity.EXTRA_TEXT_GNAME,gName);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }
}
