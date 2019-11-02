package com.nishantboro.splititeasy;

// Objective: To prepare an adapter that could throw all the items from expensesList Array
// to a recycler view

import android.app.Application;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;


public class ExpensesTabViewAdapter extends RecyclerView.Adapter<ExpensesTabViewAdapter.ExpenseDetailViewHolder> {
    private OnItemClickListener listener;
    private List<BillEntity> list = new ArrayList<>();
    public boolean multiSelect = false;
    public List<BillEntity> selectedItems = new ArrayList<>();
    public ActionMode actionMode;
    private String gName;
    private Application application;
    private ExpensesTabFragment thisOfExpenseFragment;

    ExpensesTabViewAdapter(String gName, Application application, ExpensesTabFragment thisOfExpenseFragment) {
        this.gName = gName;
        this.application = application;
        this.thisOfExpenseFragment = thisOfExpenseFragment;
    }

    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            ExpensesTabViewAdapter.this.multiSelect = true;
            ExpensesTabViewAdapter.this.actionMode = mode;
            menu.add("Delete");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            for(BillEntity bill: ExpensesTabViewAdapter.this.selectedItems) {
                ExpensesTabViewAdapter.this.list.remove(bill);
                ExpensesTabViewAdapter.this.deleteFromDatabase(bill);
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            ExpensesTabViewAdapter.this.multiSelect = false;
            ExpensesTabViewAdapter.this.selectedItems.clear();
            ExpensesTabViewAdapter.this.notifyDataSetChanged();
        }
    };

    // Provide a reference to the views for each name in our expenseList Array:
    class ExpenseDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewItem;
        private TextView textViewCost;
        private TextView textViewCurrency;
        private RelativeLayout relativeLayout;

        ExpenseDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewItem = itemView.findViewById(R.id.expenseDetailItem); // get the textView View component from expense_detail.xml and attach it to our holder
            this.textViewCost = itemView.findViewById(R.id.expenseDetailCost); // get the textView View component from expense_detail.xml and attach it to our holder
            this.textViewCurrency = itemView.findViewById(R.id.expenseDetailCurrency); // get the textView View component from expense_detail.xml and attach it to our holder
            this.relativeLayout = itemView.findViewById(R.id.expenseDetail);
        }

        void update(final BillEntity bill) {

            // if the user clicks on back button while an item was selected(gray colour), notifyDataSetChanged is called, hence update() is called again for every viewHolder. So, at this point
            // we need to make sure that the item that was selected(gray colour) previously, needs to be white(unselected) now.
            if (ExpensesTabViewAdapter.this.selectedItems.contains(bill)) {
                this.relativeLayout.setBackgroundColor(Color.LTGRAY);
            } else {
                this.relativeLayout.setBackgroundColor(Color.WHITE);
            }

            // attach a long click listener
            this.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((AppCompatActivity)v.getContext()).startSupportActionMode(ExpensesTabViewAdapter.this.actionModeCallbacks);
                    ExpensesTabViewAdapter.ExpenseDetailViewHolder.this.selectItem(bill);
                    return true;
                }
            });

            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ExpensesTabViewAdapter.ExpenseDetailViewHolder.this.selectItem(bill);
                }
            });
        }

        void selectItem(BillEntity bill) {
            if (ExpensesTabViewAdapter.this.multiSelect) {
                if (ExpensesTabViewAdapter.this.selectedItems.contains(bill)) {
                    ExpensesTabViewAdapter.this.selectedItems.remove(bill);
                    this.relativeLayout.setBackgroundColor(Color.WHITE);
                } else {
                    ExpensesTabViewAdapter.this.selectedItems.add(bill);
                    this.relativeLayout.setBackgroundColor(Color.LTGRAY);
                }
            }
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
        holder.textViewItem.setText(this.list.get(position).item);
        holder.textViewCost.setText(this.list.get(position).cost);

        String curr = this.list.get(position).currency;
        holder.textViewCurrency.setText(Character.toString(curr.charAt(5)));
        holder.update(this.list.get(position));

        final int pos = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ExpensesTabViewAdapter.this.listener != null) {
                    ExpensesTabViewAdapter.this.listener.onItemClick(ExpensesTabViewAdapter.this.list.get(pos));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public void storeToList(List<BillEntity> billEntities) {
        this.list = billEntities;
        this.notifyDataSetChanged();
    }

    public void deleteFromDatabase(BillEntity bill) {
        BillViewModel billViewModel = ViewModelProviders.of(ExpensesTabViewAdapter.this.thisOfExpenseFragment,new BillViewModelFactory(ExpensesTabViewAdapter.this.application,ExpensesTabViewAdapter.this.gName)).get(BillViewModel.class);
        billViewModel.delete(bill);
    }

    public interface OnItemClickListener {
        void onItemClick(BillEntity bill);
    }

    public void setOnItemClickListener(ExpensesTabViewAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

}
