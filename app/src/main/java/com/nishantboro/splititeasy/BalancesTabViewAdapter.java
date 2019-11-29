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

    static class BalanceDetailViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewSender;
        private TextView textViewRecipient;
        private TextView textViewAmount;

        BalanceDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSender = itemView.findViewById(R.id.balanceDetailSender);
            textViewRecipient = itemView.findViewById(R.id.balanceDetailRecipient);
            textViewAmount = itemView.findViewById(R.id.balanceDetailAmount);
        }
    }


    // Create new viewHolder (invoked by the layout manager)
    @NonNull
    @Override
    public BalanceDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.balances_detail, parent, false);
        return new BalanceDetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BalanceDetailViewHolder holder, int position) {
        String myString = (String) list.get(position).get("sender");
        if(myString == null) {
            return;
        }
        StringBuilder sender = new StringBuilder(myString);
//      System.out.println("before insert" + sender.toString());
        // if sender's name is longer than 18 characters, print in multi lines
        if(sender.length() >= 18) {
            sender.insert(18,"\n");
            if(sender.charAt(19) == ' ') {
                sender.deleteCharAt(19);
            }
//      System.out.println("after insert" + sender.toString());
        }

        holder.textViewSender.setText(sender.toString());
        holder.textViewRecipient.setText((String) list.get(position).get("recipient"));
        holder.textViewAmount.setText((String)list.get(position).get("amount"));
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    void storeToList(List<HashMap<String,Object>> balances) {
        list = balances;
        notifyDataSetChanged();
    }

}
