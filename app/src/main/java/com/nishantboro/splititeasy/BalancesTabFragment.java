package com.nishantboro.splititeasy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

public class BalancesTabFragment extends Fragment {
    public String gName;
    List<BillEntity> bills = new ArrayList<>();

    public BalancesTabFragment(String gName) {
        this.gName = gName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balances_fragment,container,false);

        // get all expenses from database(Bill)
        BillViewModel billViewModel = ViewModelProviders.of(this,new BillViewModelFactory(this.getActivity().getApplication(),this.gName)).get(BillViewModel.class);
        billViewModel.getAllBills().observe(this, new Observer<List<BillEntity>>() {
            @Override
            public void onChanged(List<BillEntity> billEntities) {
                bills = billEntities;
            }
        });

        return view;
    }
}
