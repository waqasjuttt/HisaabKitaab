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

    LinearLayout linearLayout_Tab;
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
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Hisab Kitab List");
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ((MainActivity) getActivity()).item.setVisible(true);

        initComopnents();
        return view;
    }

    private void initComopnents() {
        linearLayout_Tab = (LinearLayout) view.findViewById(R.id.linearLayout_Tab);
        sqliteHelper = new SqliteHelper(getActivity());
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        addTabs(viewPager);

        if (tabLayout.getTabCount() == 0) {
            linearLayout_Tab.setBackgroundResource(R.color.backgroundColor);
        } else {
            linearLayout_Tab.setBackgroundResource(R.color.TabBackground);
        }

        viewPager.setCurrentItem(Integer.parseInt(String.valueOf(tabLayout.getTabCount())));

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
            if (str.toString().equals("1")) {
                adapter.addFrag(new List_Milk_Fragment("-01-"), "Jan");
            } else if (str.toString().equals("2")) {
                adapter.addFrag(new List_Milk_Fragment("-02-"), "Feb");
            } else if (str.toString().equals("3")) {
                adapter.addFrag(new List_Milk_Fragment("-03-"), "Mar");
            } else if (str.toString().equals("4")) {
                adapter.addFrag(new List_Milk_Fragment("-04-"), "Apr");
            } else if (str.toString().equals("5")) {
                adapter.addFrag(new List_Milk_Fragment("-05-"), "May");
            } else if (str.toString().equals("6")) {
                adapter.addFrag(new List_Milk_Fragment("-06-"), "Jun");
            } else if (str.toString().equals("7")) {
                adapter.addFrag(new List_Milk_Fragment("-07-"), "Jul");
            } else if (str.toString().equals("8")) {
                adapter.addFrag(new List_Milk_Fragment("-08-"), "Aug");
            } else if (str.toString().equals("9")) {
                adapter.addFrag(new List_Milk_Fragment("-09-"), "Sep");
            } else if (str.toString().equals("10")) {
                adapter.addFrag(new List_Milk_Fragment("-10-"), "Oct");
            } else if (str.toString().equals("11")) {
                adapter.addFrag(new List_Milk_Fragment("-11-"), "Nov");
            } else if (str.toString().equals("12")) {
                adapter.addFrag(new List_Milk_Fragment("-12-"), "Dec");
            }
        }

        if (str1.length == 0) {
            Toast.makeText(getActivity(), "List is Empty", Toast.LENGTH_LONG).show();
        }
        viewPager.setAdapter(adapter);
    }
}