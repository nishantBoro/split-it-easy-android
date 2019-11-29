package com.nishantboro.splititeasy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class AllMembersSpinnerAdapter extends ArrayAdapter<MemberEntity> {
    private List<MemberEntity> list;

    AllMembersSpinnerAdapter(@NonNull Context context, @NonNull List<MemberEntity> objects) {
        super(context, 0, objects);
        list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position,convertView,parent);
    }

    private View getCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MemberEntity member = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_member, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.spinner_item_member_name);

        if(member != null) {
            textView.setText(member.name);
        }

        return convertView;
    }

    @Override
    public int getPosition(@Nullable MemberEntity item) {
        for(int i=0;i<list.size();++i) {
            if(item != null && list.get(i).id == item.id) {
                return i;
            }
        }
        return -1;
    }
}
