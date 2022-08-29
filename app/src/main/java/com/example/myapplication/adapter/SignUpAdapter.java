package com.example.myapplication.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.fragment.LoginFragment;
import com.example.myapplication.fragment.SignupFragment;

public class SignUpAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    public SignUpAdapter(FragmentManager fm, Context context, int totalTabs){
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title= "";
        switch(position) {
            case 0:
                title = "Login";
                break;
            case 1:
                title = "Sign up";
                break;
        }
        return title;
    }

    public Fragment getItem(int position) {
        switch (position){
            case 0:
                LoginFragment LoginFragment = new LoginFragment();
                return LoginFragment;
            case 1:
                SignupFragment SigupFragment = new SignupFragment();
                return SigupFragment;
        }
        return null;
    }
}
