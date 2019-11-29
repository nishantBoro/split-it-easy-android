package com.nishantboro.splititeasy;

import android.app.Application;
import android.graphics.Color;
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

/* Objective: Prepare a custom adapter that could create/update the view for every item in the recycler view */
public class ExpensesTabViewAdapter extends RecyclerView.Adapter<ExpensesTabViewAdapter.ExpenseDetailViewHolder> {
    private OnItemClickListener listener;
    private List<BillEntity> list = new ArrayList<>(); // maintain a list of all the existing bills in the database
    boolean multiSelect = false; // true if user has selected any item
    List<BillEntity> selectedItems = new ArrayList<>();
    ActionMode actionMode;
    private String gName;
    private String currency;
    private Application application;
    private ExpensesTabFragment thisOfExpenseFragment;

    ExpensesTabViewAdapter(String gName, Application application, ExpensesTabFragment thisOfExpenseFragment) {
        this.gName = gName;
        this.application = application;
        this.thisOfExpenseFragment = thisOfExpenseFragment;
    }

    /*
    handles all kinds of action when ActionMode is active.
    In our case, when the user does a long click on any recycler view item, ActionMode is activated
    and the following actionModeCallbacks object is created: */
    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {

        // method is called right after the user does a long click on any item
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            actionMode = mode;
            menu.add("Delete");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        // method is called when user clicks on "Delete" option in the menu
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // for every selected item remove it from the recycler view list and also delete it from database
            for(BillEntity bill: selectedItems) {
                list.remove(bill);
                deleteFromDatabase(bill);
            }
            mode.finish(); // close the ActionMode bar
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            notifyDataSetChanged(); // notify the recycler view about the changes and hence re-render its list
        }
    };

    // A holder for every item in our recycler view is created
    class ExpenseDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewItem;
        private TextView textViewCost;
        private TextView textViewCurrency;
        private TextView textViewPaidBy;
        private RelativeLayout relativeLayout;

        ExpenseDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            // store all the references from our layout for future use
            textViewItem = itemView.findViewById(R.id.expenseDetailItem);
            textViewCost = itemView.findViewById(R.id.expenseDetailCost);
            textViewCurrency = itemView.findViewById(R.id.expenseDetailCurrency);
            textViewPaidBy = itemView.findViewById(R.id.expenseDetailPaidBy);
            relativeLayout = itemView.findViewById(R.id.expenseDetail);
        }

        void update(final BillEntity bill) {

            /* if the user clicks on back button while an item was selected(gray colour), notifyDataSetChanged is called, hence update() is called again for every viewHolder. So, at this point
               we need to make sure that the item which was selected(gray colour) previously, needs to be white(unselected) now. */
            if (selectedItems.contains(bill)) {
                relativeLayout.setBackgroundColor(Color.LTGRAY);
            } else {
                relativeLayout.setBackgroundColor(Color.WHITE);
            }

            // attach a long click listener to itemView
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // at this point the user has successfully initiated a long click and hence we need to activate ActionMode now to handle multiple select and delete items
                    ((AppCompatActivity) v.getContext()).startSupportActionMode(actionModeCallbacks); // activate ActionMode and let actionModeCallback handle actions now
                    selectItem(bill); // here bill is the initially selected item after the long click event
                    return true;
                }
            });
        }


        void selectItem(BillEntity bill) {
            if (multiSelect) {
                if (selectedItems.contains(bill)) { // if the user selects a bill which is already selected(light gray), deselect it(change colour to white) and remove from selectedItems list
                    selectedItems.remove(bill);
                    relativeLayout.setBackgroundColor(Color.WHITE);
                } else { // else add the bill to our selection list and change colour to light gray
                    selectedItems.add(bill);
                    relativeLayout.setBackgroundColor(Color.LTGRAY);
                }
            }
        }

    }


    // Create new viewHolder (invoked by the layout manager). Note that this method is called for creating every ExpenseDetailViewHolder required for our recycler view items
    @NonNull
    @Override
    public ExpenseDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_detail, parent, false);
        return new ExpenseDetailViewHolder(v);
    }

    // note that this method is called for every ExpenseDetailViewHolder
    @Override
    public void onBindViewHolder(@NonNull ExpenseDetailViewHolder holder, int position) {
        StringBuilder justText = new StringBuilder();
        justText.append("Paid By: ");
        final ExpenseDetailViewHolder hold = holder;
        holder.textViewItem.setText(list.get(position).item); // set bill item to holder
        holder.textViewCost.setText(list.get(position).cost); // set bill cost to holder
        holder.textViewPaidBy.setText(justText.append(list.get(position).paidBy)); // set bill paidBy to holder
        holder.textViewCurrency.setText(Character.toString(currency.charAt(5))); // set bill currency to holder. charAt(5) holds the currency symbol(like in USD-($))
        holder.update(list.get(position));

        final int pos = position;

        // attach a click listener to the ExpenseDetailViewHolder
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Note that if the user is in multiSelect mode, the function for individual click on any item shouldn't be initiated
                if(multiSelect) { // if multiSelect is On, clicking on any item should only highlight it and add it to our selectedItems list
                    hold.selectItem(list.get(pos));
                }
                if(listener != null && !multiSelect) { // if multiSelect is Off, clicking on any item should initiate edit expense intent
                    listener.onItemClick(list.get(pos)); // onItemClick method defined in ExpensesTabFragment[line 84]
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void storeToList(List<BillEntity> billEntities, String currency) {
        list = billEntities;
        this.currency = currency;
        notifyDataSetChanged();
    }

    private void deleteFromDatabase(BillEntity bill) {
        BillViewModel billViewModel = ViewModelProviders.of(ExpensesTabViewAdapter.this.thisOfExpenseFragment,new BillViewModelFactory(ExpensesTabViewAdapter.this.application,ExpensesTabViewAdapter.this.gName)).get(BillViewModel.class);
        billViewModel.delete(bill);
    }

    public interface OnItemClickListener {
        void onItemClick(BillEntity bill);
    }

    // store a reference(as a private variable) to the OnItemClickListener object passed on as a parameter
    void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
