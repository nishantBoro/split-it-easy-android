package com.nishantboro.splititeasy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public class AddEditMemberAvatarSpinnerAdapter extends ArrayAdapter<Integer> {

    public AddEditMemberAvatarSpinnerAdapter(@NonNull Context context, @NonNull List<Integer> objects) {
        super(context, 0, objects);
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
        int avatarResource = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_member_avatar_spinner_row, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.addMemberAvatarSpinnerImage);
        imageView.setImageResource(avatarResource);

        return convertView;
    }

//    @Override
//    public int getPosition(@Nullable MemberEntity item) {
//        Log.d("size", Integer.toString(this.list.size()));
//        for(int i=0;i<this.list.size();++i) {
//            Log.d("spinner_member_id", Integer.toString(this.list.get(i).id));
//            Log.d("item_id", Integer.toString(item.id));
//            if(this.list.get(i).id == item.id) {
//                return i;
//            }
//        }
//        return -1;
//    }
}
