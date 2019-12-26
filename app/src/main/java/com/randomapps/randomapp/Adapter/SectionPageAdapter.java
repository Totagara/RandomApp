package com.randomapps.randomapp.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionPageAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragmentList = new ArrayList<>();
    List<String> titlesList = new ArrayList<>();
    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

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
        return titlesList.get(position);
    }

    public void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        titlesList.add(title);
    }
}

