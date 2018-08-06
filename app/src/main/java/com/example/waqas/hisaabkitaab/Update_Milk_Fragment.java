package com.example.waqas.hisaabkitaab;


import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sdsmdg.tastytoast.TastyToast;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.Calendar;

public class Update_Milk_Fragment extends Fragment implements View.OnClickListener {

    List_Milk_Fragment list_milk_fragment;

    int per_kg_value;

    private Dialog MyDialog;
    private Button btnOK, btn_Cancel;
    private ImageView btnClose;

    private SqliteHelper sqliteHelper;
//    private MilkCursorAdapter milkCursorAdapter;

    private LinearLayout linearLayout;
    private ArrayAdapter<String> adapter_Milk_Quantity, adapter_HH, adapter_MM, adapter_AM;
    private TextView tv_Date, tv_Total, tv_PerKG;
    private Button btnSave, btn_Time;
    private FragmentManager fragmentManager;
    private View view;
    private BetterSpinner betterSpinnerHH, betterSpinnerMM, betterSpinnerAM;
    //    private BetterSpinner Milk_Quantity;
    private Spinner Milk_Quantity;
    private String[] str_Milk_Quantity = {"کتنے کلو دودھ؟", "4 کلو", "5 کلو", "6 کلو", "7 کلو", "8 کلو", "9 کلو", "10 کلو", "11 کلو", "12 کلو", "13 کلو", "14 کلو", "15 کلو",};
    private String[] str_HH = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    private String[] str_MM = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "00"};
    private String[] str_Am = {"am", "pm"};

    public Update_Milk_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.update_milk_fragment, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        getActivity().setTitle(" Update Milk");

        initComponents();
        setListners();
        return view;
    }

    private void initComponents() {
        tv_Date = (TextView) view.findViewById(R.id.tv_DatePicker);
        tv_PerKG = (TextView) view.findViewById(R.id.tv_PerKG);
        tv_Total = (TextView) view.findViewById(R.id.tv_TotalMilkPrice);
        btnSave = (Button) view.findViewById(R.id.btn_Save);
        Milk_Quantity = (Spinner) view.findViewById(R.id.MilkQuantity);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        sqliteHelper = new SqliteHelper(getActivity());
    }

    private void setListners() {
        btnSave.setOnClickListener(this);
        btn_Time.setOnClickListener(this);

        tv_PerKG.setText(list_milk_fragment.milk_items.getMilk_Quantity());
        tv_Total.setText(list_milk_fragment.milk_items.getTotal_Milk());
        tv_Date.setText("Date: " + list_milk_fragment.milk_items.getDateNTime());
        linearLayout.setBackgroundColor(Color.parseColor("#f08080"));

        adapter_Milk_Quantity = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, str_Milk_Quantity) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter_Milk_Quantity.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Milk_Quantity.setAdapter(adapter_Milk_Quantity);

        Milk_Quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    per_kg_value = position + 3;
                    tv_PerKG.setText(per_kg_value + " کلو");
                    tv_Total.setText(per_kg_value * 75 + " روپے");
                    linearLayout.setBackgroundColor(Color.parseColor("#f08080"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_Save:
                CheckValidation();
                break;
        }
    }

    private void CheckValidation() {
        if (list_milk_fragment.listView.getCheckedItemPosition() == -1) {
            TastyToast.makeText(getActivity(), "List is not selected", TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
        } else {
//            Milk_Items milk_items = new Milk_Items();
//            Cursor c = (Cursor) list_milk_fragment.milkCursorAdapter.getItem(list_milk_fragment.listView.getCheckedItemPosition());
//            Milk_Items cust = (Milk_Items) SqliteHelper.getObjectFromCursor(c, Milk_Items.class);
//
//            milk_items.setID(cust.getID());
//            milk_items.setMilk_Quantity(String.valueOf(per_kg_value));
//            milk_items.setTotal_Milk(String.valueOf(per_kg_value * 75));
//            milk_items.setDateNTime(" " + tv_Date.getText().toString());
        }
    }
}
