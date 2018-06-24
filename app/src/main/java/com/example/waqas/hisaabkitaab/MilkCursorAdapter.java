package com.example.waqas.hisaabkitaab;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MilkCursorAdapter extends ArrayAdapter<Milk_Items> {

    private Button btn_yes, btn_no;
    private List_Milk_Fragment listMilkFragment;
    private Context context;
    private SqliteHelper sqliteHelper;
    private List<Milk_Items> milk_itemses;

    public MilkCursorAdapter(List_Milk_Fragment list_milk_fragment, int resource, List<Milk_Items> objects, SqliteHelper helper) {
        super(list_milk_fragment, resource, objects);
        this.listMilkFragment = list_milk_fragment;
        this.sqliteHelper = helper;
        this.milk_itemses = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_milk_items, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_date.setText(getItem(position).getDateNTime());
        holder.tv_KG.setText(getItem(position).getMilk_Quantity() + " کلو");
        holder.tv_Total.setText(String.valueOf(getItem(position).getTotal_Milk()) + " روپے");

        //Delete an item
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(listMilkFragment);
                dialog.setContentView(R.layout.delete_warning_layout);
                dialog.setCancelable(false);
                dialog.show();

                btn_yes = dialog.findViewById(R.id.btn_yes);
                btn_no = dialog.findViewById(R.id.btn_no);

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sqliteHelper.deleteMilkData(getItem(position)); //delete in db
                        Toast.makeText(listMilkFragment, "ختم ہوچکا ہے!", Toast.LENGTH_LONG).show();

                        //reload the database to view
                        sqliteHelper.Grand_Total = 0;
                        listMilkFragment.reloadingDatabase();
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
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(listMilkFragment);
                alertDialog.setTitle("Update a Friend");

                LinearLayout layout = new LinearLayout(listMilkFragment);
                layout.setPadding(10, 10, 10, 10);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText nameBox = new EditText(listMilkFragment);
                nameBox.setHint("Name");
                layout.addView(nameBox);

                final EditText jobBox = new EditText(listMilkFragment);
                jobBox.setHint("job");
                layout.addView(jobBox);

                nameBox.setText(getItem(position).getMilk_Quantity());
//                jobBox.setText(getItem(position).get());

                alertDialog.setView(layout);

                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Milk_Items milkItems = new Milk_Items(nameBox.getText().toString(), null, 0);
                        milkItems.setID(getItem(position).getID());
                        sqliteHelper.updateMilkData(milkItems); //update to db
                        Toast.makeText(listMilkFragment, "Updated!", Toast.LENGTH_SHORT).show();

                        //reload the database to view
                        listMilkFragment.reloadingDatabase();
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