package com.example.waqas.hisaabkitaab;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddDailMilk_Fragment extends Fragment implements View.OnClickListener {

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

    //Get Current Date
//    String mydate = java.text.DateFormat.getDateInstance().format(Calendar.getInstance().getTime());

    int per_kg_value;
    List_Milk_Fragment list_milk_fragment;
    private SqliteHelper sqliteHelper;
    private LinearLayout linearLayout;
    private ArrayAdapter<String> adapter_Milk_Quantity;
    private TextView tv_Total, tv_PerKG;
    static TextView tv_Date, tv_Time;
    private Button btnSave;
    private FragmentManager fragmentManager;
    private View view;
    private Spinner Milk_Quantity;
    private String[] str_Milk_Quantity = {"کتنے کلو دودھ؟", "4 کلو", "5 کلو", "6 کلو", "7 کلو", "8 کلو", "9 کلو", "10 کلو", "11 کلو", "12 کلو", "13 کلو", "14 کلو", "15 کلو",};

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

    private void initComponents() {
        tv_Date = (TextView) view.findViewById(R.id.tv_DatePicker);
        tv_Time = (TextView) view.findViewById(R.id.tv_Time);
        tv_PerKG = (TextView) view.findViewById(R.id.tv_PerKG);
        tv_Total = (TextView) view.findViewById(R.id.tv_TotalMilkPrice);
        btnSave = (Button) view.findViewById(R.id.btn_Save);
        Milk_Quantity = (Spinner) view.findViewById(R.id.MilkQuantity);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        sqliteHelper = new SqliteHelper(getActivity());
        list_milk_fragment = new List_Milk_Fragment();
    }

    private void setListners() {
        btnSave.setOnClickListener(this);

        Date today = new Date();
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        strDate = simpleDate.format(today);
        tv_Date.setText("Date: " + strDate);
        tv_Date.setOnClickListener(this);

        //For Time
        final Calendar c = Calendar.getInstance();
        hr = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        updateTime(hr, min);
        tv_Time.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_Save:
                CheckValidation();
                break;

            case R.id.tv_DatePicker:
                datePickerFragment = new DatePickerFragmentIncome();
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "Date");
                break;

            case R.id.tv_Time:
                createdDialog(TIME_DIALOG_ID).show();
                break;
        }
    }

    private void CheckValidation() {
        String DateNTime = "null", Per_KG;
        int Total;

        if (!strDate.toString().contains("null")) {
            DateNTime = strDate + "\n" + aTime.toString();
        }
        Per_KG = String.valueOf(per_kg_value);
        Total = per_kg_value * 75;

        if (per_kg_value == 0 || Milk_Quantity.getSelectedItem().toString() == "0") {
            TastyToast.makeText(getActivity(), "دودھ کی مقدار سلکٹ کریں!", Toast.LENGTH_SHORT, TastyToast.ERROR).show();
        } else if (tv_Time.getText().toString().isEmpty()) {
            TastyToast.makeText(getActivity(), "آپ نے وقت سلکٹ نہیں کیا!", Toast.LENGTH_SHORT, TastyToast.ERROR).show();
        } else if (sqliteHelper.checkDate(strDate.toString().trim())) {
            Snackbar snackbar = Snackbar
                    .make(view, "تاریخ پہلے سے مجود ہے!", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (per_kg_value != 0
                && !DateNTime.toString().isEmpty()
                && !sqliteHelper.checkDate(strDate.toString().trim())) {

            Milk_Items milk_items = new Milk_Items(DateNTime, Per_KG, Total);
            sqliteHelper.add_Milk(milk_items);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, new Main_Fragment()).commit();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    ///////////////////////////////////////////////////////////// For Time //////////////////////////////////////////////////////////////////////////////
    protected Dialog createdDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(getActivity(), timePickerListener, hr, min, false);
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
        tv_Time.setText("Time: " + aTime);
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
            tv_Date.setText("Date: " + strDate);
        }
    }
}