package com.example.waqas.hisaabkitaab;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MilkCursorAdapter extends ArrayAdapter<Milk_Items> {

    private Button btn_yes, btn_no;
    private Context context;
    private SqliteHelper sqliteHelper;
    FragmentManager fragmentManager;
    List<Milk_Items> milk_itemses;

    public MilkCursorAdapter(Context context, int resource, List<Milk_Items> objects, SqliteHelper helper) {
        super(context, resource, objects);
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        this.context = context;
        this.sqliteHelper = helper;
        this.milk_itemses = objects;
    }

    @Override
    public int getCount() {
        return milk_itemses.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_milk_items, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_date.setText(getItem(position).getDateNTime().toString());
        holder.tv_KG.setText(getItem(position).getMilk_Quantity() + " کلو");

        final int number = Integer.parseInt(String.valueOf(getItem(position).getTotal_Milk()));
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String result = numberFormat.format(number);

        holder.tv_Total.setText(result + " روپے");

        //Delete an item
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.delete_warning_layout);
                dialog.setCancelable(false);
                dialog.show();

                btn_yes = dialog.findViewById(R.id.btn_yes);
                btn_no = dialog.findViewById(R.id.btn_no);

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sqliteHelper.deleteMilkData(getItem(position)); //delete in db
                        Toast.makeText(getContext(), "ختم ہوچکا ہے!", Toast.LENGTH_LONG).show();

                        //reload the database to view
                        sqliteHelper.Grand_Total = 0;
                        fragmentManager
                                .beginTransaction()
                                .replace(R.id.container, new Tablayout_Fragment())
                                .commit();
                        dialog.dismiss();
                    }
                });

                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }

                });
            }
        });

        //Edit/Update an item
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Update Milk's Quantity");
                final LinearLayout layout = new LinearLayout(getContext());
                layout.setPadding(10, 10, 10, 10);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText et_milk_quantity = new EditText(getContext());
                et_milk_quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                et_milk_quantity.setFocusable(true);
                et_milk_quantity.setFocusableInTouchMode(true);
                et_milk_quantity.setHint("Milk Quantity");
                layout.addView(et_milk_quantity);

                et_milk_quantity.setText(getItem(position).getMilk_Quantity());
                et_milk_quantity.setSelection(getItem(position).getMilk_Quantity().toString().length());

                alertDialog.setView(layout);

                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int value = Integer.parseInt(et_milk_quantity.getText().toString());
                        if (value > 15) {
                            Toast.makeText(getContext(), "دودھ کی مقدار 1 سے 15 تک ہونی چاہی", Toast.LENGTH_SHORT).show();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.container, new Tablayout_Fragment())
                                    .commit();
                        } else {
                            int total = Integer.parseInt(et_milk_quantity.getText().toString());
                            total = total * 75;
                            Milk_Items milkItems = new Milk_Items((getItem(position).getDateNTime().toString()), et_milk_quantity.getText().toString(), total);
                            milkItems.setID(getItem(position).getID());
                            sqliteHelper.updateMilkData(milkItems); //update to db
                            Toast.makeText(getContext(), "Data updated at " + String.valueOf(getItem(position).getDateNTime().toString().substring(0, 11)), Toast.LENGTH_LONG).show();

                            //reload the database to view
                            sqliteHelper.Grand_Total = 0;
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.container, new Tablayout_Fragment())
                                    .commit();
                        }
                    }
                });

                alertDialog.setNegativeButton("Cancel", null);

                //show alert dialog
                alertDialog.show();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        private TextView tv_date, tv_KG, tv_Total;
        private Button btnDelete, btnEdit;

        public ViewHolder(View v) {
            tv_date = (TextView) v.findViewById(R.id.tv_date_list);
            tv_KG = (TextView) v.findViewById(R.id.tv_milk_list);
            tv_Total = (TextView) v.findViewById(R.id.tv_total_list);
            btnDelete = v.findViewById(R.id.btn_Delete);
            btnEdit = v.findViewById(R.id.btn_Edit);
        }
    }
}