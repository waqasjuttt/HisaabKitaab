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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.Calendar;

public class AddDailMilk_Fragment extends Fragment implements View.OnClickListener {

    //    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    String mydate = java.text.DateFormat.getDateInstance().format(Calendar.getInstance().getTime());

    String str;
    int per_kg_value;

    List_Milk_Fragment list_milk_fragment;
    private Dialog MyDialog;
    private Button btnOK, btn_Cancel;
    private ImageView btnClose;

    private SqliteHelper sqliteHelper;
//    private MilkCursorAdapter milkCursorAdapter;

    private LinearLayout linearLayout;
    private ArrayAdapter<String> adapter_Milk_Quantity, adapter_HH, adapter_MM, adapter_AM;
    private TextView tv_Date, tv_Total, tv_PerKG, tv_Time;
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

    public AddDailMilk_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.add_dail_milk_fragment, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        getActivity().setTitle(" Add Daily Milk");

        initComponents();
        setListners();

        return view;
    }

    private void setListners() {
        btnSave.setOnClickListener(this);
        btn_Time.setOnClickListener(this);

        tv_Date.setText("Date: " + mydate);

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
//                    Toast.makeText(getActivity(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

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

    private void initComponents() {
        tv_Date = (TextView) view.findViewById(R.id.tv_DatePicker);
        tv_Time = (TextView) view.findViewById(R.id.tv_Time);
        tv_PerKG = (TextView) view.findViewById(R.id.tv_PerKG);
        tv_Total = (TextView) view.findViewById(R.id.tv_TotalMilkPrice);
        btnSave = (Button) view.findViewById(R.id.btn_Save);
        btn_Time = (Button) view.findViewById(R.id.btnTime);
        Milk_Quantity = (Spinner) view.findViewById(R.id.MilkQuantity);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        sqliteHelper = new SqliteHelper(getActivity());
        list_milk_fragment = new List_Milk_Fragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_Save:
                CheckValidation();
                break;

            case R.id.btnTime:
                MyCustomAlertDialog();
                break;
        }
    }

    private void CheckValidation() {
        String DateNTime, Per_KG, Total_Price;
        int Total;

        DateNTime = mydate + " " + tv_Time.getText().toString();
        Per_KG = String.valueOf(per_kg_value);
        Total = per_kg_value * 75;

        if (Per_KG.contains("0") || per_kg_value == 0 || Milk_Quantity.getSelectedItem().toString() == "0") {
            TastyToast.makeText(getActivity(), "دودھ کی مقدار سلکٹ کریں!", Toast.LENGTH_SHORT, TastyToast.ERROR).show();
        } else if (tv_Time.getText().toString().isEmpty()) {
            TastyToast.makeText(getActivity(), "آپ نے وقت سلکٹ نہیں کیا!", Toast.LENGTH_SHORT, TastyToast.ERROR).show();
        } else if (!Per_KG.contains("0") && !DateNTime.toString().isEmpty()) {
            Milk_Items milk_items = new Milk_Items(DateNTime, Per_KG, Total);
            sqliteHelper.add_Milk(milk_items);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Main_Fragment()).commit();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void MyCustomAlertDialog() {
        MyDialog = new Dialog(getActivity());
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.custum_dialog_time);
        MyDialog.setCanceledOnTouchOutside(false);

        btnOK = (Button) MyDialog.findViewById(R.id.btn_Agree);
        btn_Cancel = (Button) MyDialog.findViewById(R.id.btn_Not_Agree);
        btnClose = (ImageView) MyDialog.findViewById(R.id.btnClose);
        betterSpinnerHH = (BetterSpinner) MyDialog.findViewById(R.id.spinnerHH);
        betterSpinnerMM = (BetterSpinner) MyDialog.findViewById(R.id.spinnerMM);
        betterSpinnerAM = (BetterSpinner) MyDialog.findViewById(R.id.spinnerAM);

        btnOK.setEnabled(true);
        btn_Cancel.setEnabled(true);


        adapter_HH = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, str_HH);
        betterSpinnerHH.setAdapter(adapter_HH);

        adapter_MM = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, str_MM);
        betterSpinnerMM.setAdapter(adapter_MM);

        adapter_AM = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, str_Am);
        betterSpinnerAM.setAdapter(adapter_AM);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDialog.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!betterSpinnerHH.getText().toString().isEmpty() && !betterSpinnerMM.getText().toString().isEmpty() && !betterSpinnerAM.getText().toString().isEmpty()) {
                    tv_Time.setText(betterSpinnerHH.getText().toString() + ":" + betterSpinnerMM.getText().toString() + " " + betterSpinnerAM.getText().toString());
                    MyDialog.dismiss();
                } else if (betterSpinnerHH.getText().toString().isEmpty() || betterSpinnerMM.getText().toString().isEmpty() || betterSpinnerAM.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "آپ نے وقت سلکٹ نہیں کیا!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.dismiss();
            }
        });

        MyDialog.show();
    }
}