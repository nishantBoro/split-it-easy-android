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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;


public class BalancesTabFragment extends Fragment {
    public String gName;
    private List<MemberEntity> members = new ArrayList<>();
    private List<HashMap<String,Object>> results = new ArrayList<>();
    private BalancesTabViewAdapter adapter;

    public void calculateBalances() {
        /* Algorithm:


        */
        results.clear();
        BigDecimal sum = new BigDecimal("0");
        PriorityQueue<Balance> debtors = new PriorityQueue<Balance>(members.size(),new BalanceComparator()); // debtors = -ve
        PriorityQueue<Balance> creditors = new PriorityQueue<Balance>(members.size(),new BalanceComparator()); // creditors = +ve
        List<BigDecimal> preBalances = new ArrayList<>();
        BillViewModel billViewModel = ViewModelProviders.of(this,new BillViewModelFactory(this.getActivity().getApplication(),gName)).get(BillViewModel.class);

        /*Now, we can iterate through every member i(say) and do a query(in BillEntity) to get all the expenses for every i-th member*/
        for(MemberEntity member:members) {
            List<BillEntity> memberBills = billViewModel.getAllMemberBills(this.gName,member.id);
            BigDecimal sumOfAllBills = new BigDecimal("0");
            for(BillEntity memberBill:memberBills) {
                sumOfAllBills = sumOfAllBills.add(new BigDecimal(memberBill.cost));
            }
            preBalances.add(sumOfAllBills);
            sum = sum.add(sumOfAllBills);
        }

        // Each member in the group is supposed to pay an amount of sum/(members.size()) = eachPay
        BigDecimal eachPay = sum.divide(new BigDecimal(members.size()),2, RoundingMode.HALF_EVEN);

        /* Find the balance of everyone in the group. Balance is the amount of money someone owes or is owed from the group.
        Balance of someone = eachPay - total money paid by the member. If the balance is -ve it means he is owed money, else he owes money to the group */

        for (int i=0;i<preBalances.size();++i) {
            BigDecimal balance = eachPay.subtract(preBalances.get(i));
            if(balance.compareTo(new BigDecimal("-0.49")) == -1) {
                Log.d("debtors added", balance.toString());
                debtors.add(new Balance(balance.negate(),members.get(i).name)); // -ve members go to debtors list
            } else if (balance.compareTo(new BigDecimal("0.49")) == 1) {
                Log.d("creditors added", balance.toString());
                creditors.add(new Balance(balance,members.get(i).name)); // +ve members go to creditors list
            }
        }

        while(!creditors.isEmpty() && !debtors.isEmpty()) {

            Balance rich = creditors.peek();
            String richName = rich.name;
            BigDecimal richBalance = rich.balance;
            creditors.remove(rich);

            Balance poor = debtors.peek();
            BigDecimal poorBalance = poor.balance;
            String poorName = poor.name;
            debtors.remove(poor);

            BigDecimal min = richBalance.min(poorBalance);

            richBalance = richBalance.subtract(min);
            poorBalance = poorBalance.subtract(min);

            HashMap<String,Object> values = new HashMap<>();
            values.put("sender",richName);
            values.put("recipient",poorName);
            values.put("amount",min.toString());

            this.results.add(values);

            if(poorBalance.compareTo(new BigDecimal("0.49")) == 1) {
                debtors.add(new Balance(poorBalance,poorName));
            }

            if(richBalance.compareTo(new BigDecimal("0.49")) == 1) {
                creditors.add(new Balance(richBalance,richName));
            }
        }
    }



    public BalancesTabFragment(String gName) {
        this.gName = gName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balances_fragment,container,false);

        Log.d("p", "i called balances fragment");

        /* Collect all the expenses for every member of the given group. Hence, we get all the members first:*/
        MemberViewModel memberViewModel = ViewModelProviders.of(this,new MemberViewModelFactory(this.getActivity().getApplication(),gName)).get(MemberViewModel.class);
        memberViewModel.getAllMembers().observe(this, new Observer<List<MemberEntity>>() {
            @Override
            public void onChanged(List<MemberEntity> memberEntities) {
                BalancesTabFragment.this.members = memberEntities;
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.balancesRecyclerView);
        recyclerView.setHasFixedSize(true);
        this.adapter = new BalancesTabViewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        if(!members.isEmpty()) {
            calculateBalances();
        }
        this.adapter.storeToList(results);
        super.onResume();
    }
}