package com.example.waqas.hisaabkitaab;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Tablayout_Fragment extends Fragment {

    View view;
    TabLayout tabLayout;
    ViewPager viewPager;
    SqliteHelper sqliteHelper;

    public Tablayout_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.tablayout_fragment, container, false);

        initComopnents();
        return view;
    }

    private void initComopnents() {
        sqliteHelper = new SqliteHelper(getActivity());
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        addTabs(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LinearLayout tabLayout1 = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabTextView = (TextView) tabLayout1.getChildAt(1);
                tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.BOLD);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                LinearLayout tabLayout1 = (LinearLayout) ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
                TextView tabTextView = (TextView) tabLayout1.getChildAt(1);
                tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.NORMAL);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void addTabs(ViewPager viewPager) {
        TablayoutAdapter adapter = new TablayoutAdapter(getActivity().getSupportFragmentManager());
        String[] str1 = sqliteHelper.getDates();
        for (String str : str1) {
            if (str.toString().startsWith("1")) {
                adapter.addFrag(new List_Milk_Fragment(), "Jan");
            } else if (str.toString().startsWith("2")) {
                adapter.addFrag(new List_Milk_Fragment(), "Feb");
            } else if (str.toString().contains("3")) {
                adapter.addFrag(new List_Milk_Fragment(), "Mar");
            } else if (str.toString().contains("4")) {
                adapter.addFrag(new List_Milk_Fragment(), "Apr");
            } else if (str.toString().contains("5")) {
                adapter.addFrag(new List_Milk_Fragment(), "May");
            } else if (str.toString().contains("6")) {
                adapter.addFrag(new List_Milk_Fragment(), "Jun");
            } else if (str.toString().contains("7")) {
                adapter.addFrag(new List_Milk_Fragment(), "Jul");
            } else if (str.toString().contains("8")) {
                adapter.addFrag(new List_Milk_Fragment(), "Aug");
            } else if (str.toString().contains("9")) {
                adapter.addFrag(new List_Milk_Fragment(), "Sep");
            } else if (str.toString().contains("10")) {
                adapter.addFrag(new List_Milk_Fragment(), "Oct");
            } else if (str.toString().contains("11")) {
                adapter.addFrag(new List_Milk_Fragment(), "Nov");
            } else if (str.toString().contains("12")) {
                adapter.addFrag(new List_Milk_Fragment(), "Dec");
            }
        }
        viewPager.setAdapter(adapter);
    }
}