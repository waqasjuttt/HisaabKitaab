package com.example.waqas.hisaabkitaab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class List_Milk_Fragment extends AppCompatActivity {

    Milk_Items milk_items;
    ListView listView;
    List<Milk_Items> milk_items_list;
    TextView tv_Grand_Total_Price;

    Button btnUpdate, btnDelete;
    SqliteHelper sqliteHelper;
    MilkCursorAdapter milkCursorAdapter;

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
        btnUpdate = (Button) findViewById(R.id.btn_Edit);
        btnDelete = (Button) findViewById(R.id.btn_Delete);
        tv_Grand_Total_Price = (TextView) findViewById(R.id.tv_Grand_Total_Price);

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
    }
}