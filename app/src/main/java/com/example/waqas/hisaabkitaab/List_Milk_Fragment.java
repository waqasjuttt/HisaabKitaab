package com.example.waqas.hisaabkitaab;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;

public class List_Milk_Fragment extends AppCompatActivity {

    int d, m, y;

    private Dialog MyDialogForMilk, MyDialog;
    Milk_Items milk_items;
    ListView listView;
    List<Milk_Items> milk_items_list;
    TextView tv_Grand_Total_Price, tv_date, tv_time, tv_Per_KG, tv_Total;
    Button btnAdd, btnSave, btnCancel, btnTime;
    private Button btnOK, btn_Cancel;
    private ImageView btnClose;
    private Spinner Milk_Quantity;
    private LinearLayout linearLayout;
    List_Milk_Fragment list_milk_fragment;
    private BetterSpinner betterSpinnerHH, betterSpinnerMM, betterSpinnerAM;
    private ArrayAdapter<String> adapter_Milk_Quantity, adapter_HH, adapter_MM, adapter_AM;
    private String[] str_Milk_Quantity = {"کتنے کلو دودھ؟", "4 کلو", "5 کلو", "6 کلو", "7 کلو", "8 کلو", "9 کلو", "10 کلو", "11 کلو", "12 کلو", "13 کلو", "14 کلو", "15 کلو",};
    private String[] str_HH = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    private String[] str_MM = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "00"};
    private String[] str_Am = {"am", "pm"};
    SqliteHelper sqliteHelper;
    MilkCursorAdapter milkCursorAdapter;
    private int per_kg_value;

    public List_Milk_Fragment() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_milk_fragment);
        initComponents();
        setListners();
    }

    private void initComponents() {
        listView = (ListView) findViewById(R.id.listView);
        tv_Grand_Total_Price = (TextView) findViewById(R.id.tv_Grand_Total_Price);
        btnAdd = (Button) findViewById(R.id.btn_Add_Extra_Milk);

        sqliteHelper = new SqliteHelper(this);
        milk_items_list = new ArrayList<>();
    }

    public void reloadingDatabase() {
        milk_items_list = sqliteHelper.getAllData();
        if (milk_items_list.size() == 0) {
            Toast.makeText(this, "No record found in database!", Toast.LENGTH_SHORT).show();
        }
        milkCursorAdapter = new MilkCursorAdapter(this, R.layout.list_milk_items, milk_items_list, sqliteHelper);
        listView.setAdapter(milkCursorAdapter);
        tv_Grand_Total_Price.setText("ٹوٹل بل: " + String.valueOf(sqliteHelper.Grand_Total));
    }

    private void setListners() {
        reloadingDatabase();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomAlertDialog();
            }
        });
    }

    public void MyCustomAlertDialog() {
        MyDialogForMilk = new Dialog(this);
        MyDialogForMilk.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialogForMilk.setContentView(R.layout.add_missing_milk_layout);
        MyDialogForMilk.setCanceledOnTouchOutside(false);

        btnTime = (Button) MyDialogForMilk.findViewById(R.id.btnTime);
        btnSave = (Button) MyDialogForMilk.findViewById(R.id.btn_Save);
        btn_Cancel = (Button) MyDialogForMilk.findViewById(R.id.btn_Cancel);
        tv_date = (TextView) MyDialogForMilk.findViewById(R.id.tv_DatePicker);
        tv_time = (TextView) MyDialogForMilk.findViewById(R.id.tv_Time);
        tv_Per_KG = (TextView) MyDialogForMilk.findViewById(R.id.tv_PerKG);
        tv_Total = (TextView) MyDialogForMilk.findViewById(R.id.tv_TotalMilkPrice);
        Milk_Quantity = (Spinner) MyDialogForMilk.findViewById(R.id.MilkQuantity);
        linearLayout = (LinearLayout) MyDialogForMilk.findViewById(R.id.linearLayout);

        sqliteHelper = new SqliteHelper(this);
        list_milk_fragment = new List_Milk_Fragment();

        adapter_Milk_Quantity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str_Milk_Quantity) {
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

                    tv_Per_KG.setText(per_kg_value + " کلو");
                    tv_Total.setText(per_kg_value * 75 + " روپے");
                    linearLayout.setBackgroundColor(Color.parseColor("#f08080"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnSave.setEnabled(true);
        btn_Cancel.setEnabled(true);

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(List_Milk_Fragment.this, "You Click on Date", Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DateNTime, Per_KG;
                int Total;

//                DateNTime = mydate + " " + tv_time.getText().toString();
                Per_KG = String.valueOf(per_kg_value);
                Total = per_kg_value * 75;

                if (per_kg_value == 0 || Milk_Quantity.getSelectedItem().toString() == "0") {
                    TastyToast.makeText(List_Milk_Fragment.this, "دودھ کی مقدار سلکٹ کریں!", Toast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (tv_time.getText().toString().isEmpty()) {
                    TastyToast.makeText(List_Milk_Fragment.this, "آپ نے وقت سلکٹ نہیں کیا!", Toast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (per_kg_value != 0) {
                    Milk_Items milk_items = new Milk_Items(null, Per_KG, Total);
                    sqliteHelper.add_Milk(milk_items);

                    reloadingDatabase();
                    MyDialogForMilk.dismiss();
                }
            }
        });

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialogForMilk.dismiss();
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomAlertDialogForTime();
            }
        });

        MyDialogForMilk.show();
    }

    public void MyCustomAlertDialogForTime() {
        MyDialog = new Dialog(this);
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


        adapter_HH = new ArrayAdapter<String>(List_Milk_Fragment.this, android.R.layout.simple_dropdown_item_1line, str_HH);
        betterSpinnerHH.setAdapter(adapter_HH);

        adapter_MM = new ArrayAdapter<String>(List_Milk_Fragment.this, android.R.layout.simple_dropdown_item_1line, str_MM);
        betterSpinnerMM.setAdapter(adapter_MM);

        adapter_AM = new ArrayAdapter<String>(List_Milk_Fragment.this, android.R.layout.simple_dropdown_item_1line, str_Am);
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
                    tv_time.setText(betterSpinnerHH.getText().toString() + ":" + betterSpinnerMM.getText().toString() + " " + betterSpinnerAM.getText().toString());
                    MyDialog.dismiss();
                } else if (betterSpinnerHH.getText().toString().isEmpty() || betterSpinnerMM.getText().toString().isEmpty() || betterSpinnerAM.getText().toString().isEmpty()) {
                    Toast.makeText(List_Milk_Fragment.this, "آپ نے وقت سلکٹ نہیں کیا!", Toast.LENGTH_SHORT).show();
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