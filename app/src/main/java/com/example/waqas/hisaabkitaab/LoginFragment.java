package com.example.waqas.hisaabkitaab;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;


public class LoginFragment extends Fragment {

    View view;
    Pinview pinview;
    TextView tv_Forgor, tv_Signup;
    FragmentManager fragmentManager;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.login_fragment, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Hisab Kitab");

        initComponents();
        setListners();

        return view;
    }

    private void initComponents() {
        pinview = (Pinview) view.findViewById(R.id.pinView);
        tv_Forgor = (TextView) view.findViewById(R.id.tv_forgot);
        tv_Signup = (TextView) view.findViewById(R.id.tv_signup);
    }

    private void setListners() {
        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean b) {
                if (pinview.getValue().equals("1234")) {
                    fragmentManager.beginTransaction().replace(R.id.container, new Main_Fragment(), Utils.Mian_Fragment).commit();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(pinview.getWindowToken(), 0);

                } else {
                    Toast.makeText(getActivity(), "You enter wrong Pincode", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_Forgor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tv_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}