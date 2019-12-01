package com.nishantboro.splititeasy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
    public String gName; // group name
    private String currency;
    private List<MemberEntity> members = new ArrayList<>();
    private List<HashMap<String,Object>> results = new ArrayList<>();
    private BalancesTabViewAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private TextView header;

    private void calculateBalances(PriorityQueue<Balance> debtors,PriorityQueue<Balance> creditors) {
        if(getActivity() == null) {
            return;
        }
        BillViewModel billViewModel = ViewModelProviders.of(this,new BillViewModelFactory(getActivity().getApplication(),gName)).get(BillViewModel.class);
        List<BigDecimal> preBalances = new ArrayList<>();
        BigDecimal sum = new BigDecimal("0");

        // Now, we can iterate through every member i(say) and do a query(in BillEntity) to get all the expenses for every i-th member
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

        /* Find the balance of everyone in the group. Balance is the net amount of money someone owes or is owed from the group.
        * Balance of someone = eachPay - total money paid by the member.
        * If someone has a -ve balance, it means he is owed money and hence is added to the debtors list
        * If someone has a +ve balance, it means he owes money to the group and hence is added to the creditors list*/

        for (int i=0;i<preBalances.size();++i) {
            BigDecimal balance = eachPay.subtract(preBalances.get(i));
            int compare = 1;
            int compareNegate = -1;
            if(balance.compareTo(new BigDecimal("-0.49")) == compareNegate) {
                debtors.add(new Balance(balance.negate(),members.get(i).name)); // -ve members go to debtors list, note that we negate the balance to get the absolute value
            } else if (balance.compareTo(new BigDecimal("0.49")) == compare) {
                creditors.add(new Balance(balance,members.get(i).name)); // +ve members go to creditors list
            }
        }
    }

    private void calculateTransactions() {
        results.clear(); // remove previously calculated transactions before calculating again
        PriorityQueue<Balance> debtors = new PriorityQueue<>(members.size(),new BalanceComparator()); // debtors are members of the group who are owed money
        PriorityQueue<Balance> creditors = new PriorityQueue<>(members.size(),new BalanceComparator()); // creditors are members who have to pay money to the group

        calculateBalances(debtors,creditors);

        /*Algorithm: Pick the largest element from debtors and the largest from creditors. Ex: If debtors = {4,3} and creditors={2,7}, pick 4 as the largest debtor and 7 as the largest creditor.
        * Now, do a transaction between them. The debtor with a balance of 4 receives $4 from the creditor with a balance of 7 and hence, the debtor is eliminated from further
        * transactions. Repeat the same thing until and unless there are no creditors and debtors.
        *
        * The priority queues help us find the largest creditor and debtor in constant time. However, adding/removing a member takes O(log n) time to perform it.
        * Optimisation: This algorithm produces correct results but the no of transactions is not minimum. To minimize it, we could use the subset sum algorithm which is a NP problem.
        * The use of a NP solution could really slow down the app! */
        while(!creditors.isEmpty() && !debtors.isEmpty()) {
            Balance rich = creditors.peek(); // get the largest creditor
            Balance poor = debtors.peek(); // get the largest debtor
            if(rich == null || poor == null) {
                return;
            }
            String richName = rich.name;
            BigDecimal richBalance = rich.balance;
            creditors.remove(rich); // remove the creditor from the queue

            String poorName = poor.name;
            BigDecimal poorBalance = poor.balance;
            debtors.remove(poor); // remove the debtor from the queue

            BigDecimal min = richBalance.min(poorBalance);

            // calculate the amount to be send from creditor to debtor
            richBalance = richBalance.subtract(min);
            poorBalance = poorBalance.subtract(min);

            HashMap<String,Object> values = new HashMap<>(); // record the transaction details in a HashMap
            values.put("sender",richName);
            values.put("recipient",poorName);
            values.put("amount",currency.charAt(5) + min.toString());

            results.add(values);

            // Consider a member as settled if he has an outstanding balance between 0.00 and 0.49 else add him to the queue again
            int compare = 1;
            if(poorBalance.compareTo(new BigDecimal("0.49")) == compare) {
                // if the debtor is not yet settled(has a balance between 0.49 and inf) add him to the priority queue again so that he is available for further transactions to settle up his debts
                debtors.add(new Balance(poorBalance,poorName));
            }

            if(richBalance.compareTo(new BigDecimal("0.49")) == compare) {
                // if the creditor is not yet settled(has a balance between 0.49 and inf) add him to the priority queue again so that he is available for further transactions
                creditors.add(new Balance(richBalance,richName));
            }
        }
    }

    static BalancesTabFragment newInstance(String gName) {
        Bundle args = new Bundle();
        args.putString("group_name", gName);
        BalancesTabFragment f = new BalancesTabFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balances_fragment,container,false);
        if(getArguments() == null || getActivity() == null) {
            return view;
        }
        gName = getArguments().getString("group_name"); // get group name from bundle

        recyclerView = view.findViewById(R.id.balancesRecyclerView);
        emptyView = view.findViewById(R.id.no_data);
        header = view.findViewById(R.id.balancesHeader);
        recyclerView.setHasFixedSize(true);
        adapter = new BalancesTabViewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        GroupViewModel groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        if(getActivity() == null) {
            return;
        }
        MemberViewModel memberViewModel = ViewModelProviders.of(this,new MemberViewModelFactory(getActivity().getApplication(),gName)).get(MemberViewModel.class);
        members =  memberViewModel.getAllMembersNonLiveData(gName);

        // get latest currency picked by the user
        currency = groupViewModel.getGroupCurrencyNonLive(gName);
        runCalculations(); // run the algorithm
        super.onResume();
    }

    private void resultEmptyCheck() {
        // if results[] is empty display"No one is owed money"
        if(results.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            header.setVisibility(View.GONE);
        } else  {
            recyclerView.setVisibility(View.VISIBLE);
            header.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            adapter.storeToList(results); // update the recycler view with the new results
        }
    }

    private void runCalculations() {
        if(!members.isEmpty()) {
            calculateTransactions();
            resultEmptyCheck();
        } else {
            results.clear();
            resultEmptyCheck();
        }
    }
}