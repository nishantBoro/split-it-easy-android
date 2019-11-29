package com.nishantboro.splititeasy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

/* Custom Adapter that stores all the fragments/tabs and returns a fragment when user clicks on any tab/fragment.
   In our case, the fragments/tabs are Balances,Expenses and Members
*/
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentListTitles = new ArrayList<>();

    ViewPagerAdapter(FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentListTitles.get(position);
    }

    void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentListTitles.add(title);
    }
}
