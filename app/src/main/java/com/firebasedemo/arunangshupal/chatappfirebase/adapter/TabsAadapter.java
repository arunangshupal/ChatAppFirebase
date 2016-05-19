package com.firebasedemo.arunangshupal.chatappfirebase.adapter;

import android.content.Context;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.firebasedemo.arunangshupal.chatappfirebase.fragment.ChatHistory;
import com.firebasedemo.arunangshupal.chatappfirebase.fragment.ContactList;

/**
 * Created by Arunangshu Pal on 5/15/2016.
 */
public class TabsAadapter extends FragmentPagerAdapter {

    //private Context mContext;
   // private  ViewPager mViewPager;
    //private ActionBar mActionBar;
    public TabsAadapter(FragmentManager fm) {
        super(fm);
     //   mContext = activity;
       // mActionBar = activity.getActionBar();
      //  mViewPager = pager;
      //  mViewPager.setAdapter(this);
     //   mViewPager.setOnPageChangeListener((ViewPager.OnPageChangeListener) pager);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String fragmentName;
        switch (position){
            case 0: fragmentName="Contacts";break;
            case 1:fragmentName="History";break;
            default:fragmentName="";
        }
        return  fragmentName;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position){
            case 0: fragment=new ContactList();break;
            case 1:fragment=new ChatHistory();break;
            default:fragment=new ContactList();
        }
        return  fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
