package com.nishantboro.splititeasy;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BalancesTabViewAdapter extends RecyclerView.Adapter<BalancesTabViewAdapter.BalanceDetailViewHolder> {

    private List<HashMap<String,Object>> list = new ArrayList<>();

    // Provide a reference to the views for each name in our expenseList Array:
    static class BalanceDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewSender;
        private TextView textViewRecipient;
        private TextView textViewAmount;

        BalanceDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSender = itemView.findViewById(R.id.balanceDetailSender); // get the textView View component from expense_detail.xml and attach it to our holder
            textViewRecipient = itemView.findViewById(R.id.balanceDetailRecipient); // get the textView View component from expense_detail.xml and attach it to our holder
            textViewAmount = itemView.findViewById(R.id.balanceDetailAmount); // get the textView View component from expense_detail.xml and attach it to our holder
        }
    }


    // Create new viewHolder (invoked by the layout manager)
    @NonNull
    @Override
    public BalanceDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.balances_detail, parent, false);
        return new BalanceDetailViewHolder(v); // pass the inflated view, create a holder with textView component attached to it and return
    }

    // Bind the data in ExpenseList[position] to the holder created by Layout manager
    @Override
    public void onBindViewHolder(@NonNull BalanceDetailViewHolder holder, int position) {
        holder.textViewSender.setText((String)list.get(position).get("sender"));
        holder.textViewRecipient.setText((String)list.get(position).get("recipient"));
        holder.textViewAmount.setText((String)list.get(position).get("amount"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void storeToList(List<HashMap<String,Object>> balances) {
        this.list = balances;
        notifyDataSetChanged();
    }

}
