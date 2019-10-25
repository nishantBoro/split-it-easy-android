package com.nishantboro.splititeasy;

// Objective: To prepare an adapter that could throw all the items from expensesList Array
// to a recycler view

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;


public class ExpensesTabViewAdapter extends RecyclerView.Adapter<ExpensesTabViewAdapter.ExpenseDetailViewHolder> {

    private List<BillEntity> list = new ArrayList<>();

    // Provide a reference to the views for each name in our expenseList Array:
    static class ExpenseDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewItem;
        private TextView textViewCost;
        private TextView textViewCurrency;

        ExpenseDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.expenseDetailItem); // get the textView View component from expense_detail.xml and attach it to our holder
            textViewCost = itemView.findViewById(R.id.expenseDetailCost); // get the textView View component from expense_detail.xml and attach it to our holder
            textViewCurrency = itemView.findViewById(R.id.expenseDetailCurrency); // get the textView View component from expense_detail.xml and attach it to our holder
        }
    }


    // Create new viewHolder (invoked by the layout manager)
    @NonNull
    @Override
    public ExpenseDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_detail, parent, false);
        return new ExpenseDetailViewHolder(v); // pass the inflated view, create a holder with textView component attached to it and return
    }

    // Bind the data in ExpenseList[position] to the holder created by Layout manager
    @Override
    public void onBindViewHolder(@NonNull ExpenseDetailViewHolder holder, int position) {
        holder.textViewItem.setText(list.get(position).item);
        holder.textViewCost.setText(list.get(position).cost);
        holder.textViewCurrency.setText(list.get(position).currency);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void storeToList(List<BillEntity> billEntities) {
        this.list = billEntities;
        notifyDataSetChanged();
    }

}
