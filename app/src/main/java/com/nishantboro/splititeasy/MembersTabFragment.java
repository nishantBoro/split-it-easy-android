package com.nishantboro.splititeasy;

import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MembersTabFragment extends Fragment {
    private String gName;

    public MembersTabFragment(String gName) {
        this.gName = gName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("tag2", "onCreateView() called");
        View view = inflater.inflate(R.layout.members_fragment,container,false);
        // prepare recycler view
        RecyclerView recyclerView = view.findViewById(R.id.membersRecyclerView);
        recyclerView.setHasFixedSize(true);
        final MembersTabViewAdapter adapter = new MembersTabViewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);

        // if data in database(Member) changes, call the onChanged() below
        MemberViewModel memberViewModel = ViewModelProviders.of(this,new MemberViewModelFactory(this.getActivity().getApplication(),gName)).get(MemberViewModel.class);
        memberViewModel.getAllMembers().observe(this, new Observer<List<MemberEntity>>() {
            @Override
            public void onChanged(List<MemberEntity> memberEntities) {
                // Recreate the recycler view by notifying adapter with the changes
                Log.d("Tag x", "called inside onChanged");
                adapter.storeToList(memberEntities);
            }
        });


        // Implement Add new member function
        FloatingActionButton addFloating = view.findViewById(R.id.membersFragmentAdd);
        addFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddMemberActivity.class);
                intent.putExtra(GroupListActivity.EXTRA_TEXT_GNAME,gName);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }
}