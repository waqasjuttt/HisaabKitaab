package com.example.waqas.hisaabkitaab;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class List_Milk_Fragment extends AppCompatActivity {

    //For Date
    private DatePickerFragmentIncome datePickerFragment;
    private static Calendar dateTime = Calendar.getInstance();
    protected static int mYear;
    protected static int mMonth;
    protected static int mDay;
    static String strDate = "null";

    //For Time
    static String aTime;
    private int hr;
    private int min;
    static final int TIME_DIALOG_ID = 1111;

    int d, m, y;

    private Dialog MyDialogForMilk;
    Milk_Items milk_items;
    ListView listView;
    List<Milk_Items> milk_items_list;
    TextView tv_Grand_Total_Price, tv_Per_KG, tv_Total;
    static TextView tv_date, tv_time;
    Button btnAdd, btnSave;
    private Button btn_Cancel;
    private Spinner Milk_Quantity;
    private LinearLayout linearLayout;
    List_Milk_Fragment list_milk_fragment;
    private ArrayAdapter<String> adapter_Milk_Quantity;
    private String[] str_Milk_Quantity = {"کتنے کلو دودھ؟", "4 کلو", "5 کلو", "6 کلو", "7 کلو", "8 کلو", "9 کلو", "10 کلو", "11 کلو", "12 کلو", "13 کلو", "14 کلو", "15 کلو",};
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

        int number = Integer.parseInt(String.valueOf(sqliteHelper.Grand_Total));
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String result = numberFormat.format(number);
        tv_Grand_Total_Price.setText("ٹوٹل بل: " + result);
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

        Toast.makeText(List_Milk_Fragment.this, String.valueOf(tv_time.getText().toString()), Toast.LENGTH_SHORT).show();

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
                datePickerFragment = new DatePickerFragmentIncome();
                datePickerFragment.show(getSupportFragmentManager(), "Date");
            }
        });

        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createdDialog(TIME_DIALOG_ID).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DateNTime = "null", Per_KG;
                int Total;

                if (!strDate.toString().contains("null")) {
                    DateNTime = strDate + " " + aTime.toString();
                }

                Per_KG = String.valueOf(per_kg_value);
                Total = per_kg_value * 75;

                if (per_kg_value == 0 || Milk_Quantity.getSelectedItem().toString() == "0") {
                    TastyToast.makeText(List_Milk_Fragment.this, "دودھ کی مقدار سلکٹ کریں!", Toast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (tv_date.getText().toString().contains("تاریخ درج کریں")) {
                    TastyToast.makeText(List_Milk_Fragment.this, "آپ نے تاریخ سلکٹ نہیں کی!", Toast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (tv_time.getText().toString().contains("وقت درج کریں")) {
                    TastyToast.makeText(List_Milk_Fragment.this, "آپ نے وقت سلکٹ نہیں کیا!", Toast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (per_kg_value != 0
                        && !tv_date.getText().toString().contains("تاریخ درج کریں")
                        && !tv_time.getText().toString().contains("وقت درج کریں")) {
                    Milk_Items milk_items = new Milk_Items(DateNTime, Per_KG, Total);
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
        MyDialogForMilk.show();
    }

    ///////////////////////////////////////////////////////////// For Time //////////////////////////////////////////////////////////////////////////////
    protected Dialog createdDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(List_Milk_Fragment.this, timePickerListener, hr, min, false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            hr = hourOfDay;
            min = minutes;
            updateTime(hr, min);
        }
    };

    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        aTime = new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();
        tv_time.setText("وقت: " + aTime);
    }

    /////////////////////////////////////////////////////////// For Date /////////////////////////////////////////////////////////////////////////////////////////////////
    public static class DatePickerFragmentIncome extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Using current date as start Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Get DatePicker Dialog
            return new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            dateTime.set(mYear, mMonth, mDay);
            long selectDateInMilliSeconds = dateTime.getTimeInMillis();

            Calendar currentDate = Calendar.getInstance();
            long currentDateInMilliSeconds = currentDate.getTimeInMillis();

            SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            strDate = simpleDate.format(dateTime.getTime());

            if (selectDateInMilliSeconds > currentDateInMilliSeconds) {
                Toast.makeText(getActivity(), "Invalid date entered", Toast.LENGTH_LONG).show();
                strDate = "Date:";
                return;
            }
            long diffDate = currentDateInMilliSeconds - selectDateInMilliSeconds;
            Calendar yourAge = Calendar.getInstance();
            yourAge.setTimeInMillis(diffDate);
            tv_date.setText("Date: " + strDate);
        }
    }
}