package com.nishantboro.splititeasy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MembersTabFragment extends Fragment {
    private String gName;
    private MemberViewModel memberViewModel;
    private MembersTabViewAdapter adapter;
    private List<MemberEntity> members = new ArrayList<>();
    public MembersTabFragment(String gName) {
        this.gName = gName;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("x", "onCreateView() called of members");
        View view = inflater.inflate(R.layout.members_fragment,container,false);

        // prepare recycler view
        RecyclerView recyclerView = view.findViewById(R.id.membersRecyclerView);
        recyclerView.setHasFixedSize(true);
        this.adapter = new MembersTabViewAdapter(this.gName,this.getActivity().getApplication(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);

        // if data in database(Member) changes, call the onChanged() below
        memberViewModel = ViewModelProviders.of(this,new MemberViewModelFactory(this.getActivity().getApplication(),this.gName)).get(MemberViewModel.class);
        memberViewModel.getAllMembers().observe(this, new Observer<List<MemberEntity>>() {
            @Override
            public void onChanged(List<MemberEntity> memberEntities) {
                // Recreate the recycler view by notifying adapter with the changes
                Log.d("x", "called inside onChanged of members");
                MembersTabFragment.this.adapter.storeToList(memberEntities);
                MembersTabFragment.this.members = memberEntities;
            }
        });


        // Implement Add new member function
        FloatingActionButton addFloating = view.findViewById(R.id.membersFragmentAdd);
        addFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddEditMemberActivity.class);
                intent.putExtra(GroupListActivity.EXTRA_TEXT_GNAME,gName);
                getActivity().startActivity(intent);
            }
        });

        this.adapter.setOnItemClickListener(new MembersTabViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MemberEntity member) {
                Intent intent = new Intent(MembersTabFragment.this.getActivity(), AddEditMemberActivity.class);
                
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.members_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAllMembers:
                if(!MembersTabFragment.this.members.isEmpty()) {
                    MembersTabFragment.this.memberViewModel.deleteAll(this.gName);
                    Toast.makeText(MembersTabFragment.this.getActivity(), "All Members Deleted", Toast.LENGTH_SHORT).show();
                    return true;
                }
                Toast.makeText(MembersTabFragment.this.getActivity(), "Nothing To Delete", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        if(this.adapter.multiSelect) {
            this.adapter.actionMode.finish();
            this.adapter.multiSelect = false;
            this.adapter.selectedItems.clear();
            this.adapter.notifyDataSetChanged();
        }
        super.onPause();
    }
}