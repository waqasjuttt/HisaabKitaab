package com.example.waqas.hisaabkitaab;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main_Fragment extends Fragment {

    private View view;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private GridView gridView;
    private String[] values = {
            "Add Daily Milk"
            , "View List"};
    private int[] images = {
            R.drawable.add_milk, R.drawable.list_of_milk};

    public Main_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.main_fragment, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        getActivity().setTitle(" Home");

        initComponents();

        return view;
    }

    private void initComponents() {
        gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(getActivity(), values, images));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
//                Toast.makeText(
//                        getActivity().getApplicationContext(),
//                        ((TextView) v.findViewById(R.id.grid_item_label))
//                                .getText(), Toast.LENGTH_SHORT).show();

                if (position == 0) {
                    fragmentTransaction = fragmentManager
                            .beginTransaction()
                            .replace(R.id.container, new AddDailMilk_Fragment());
                    fragmentTransaction.addToBackStack(null).commit();
                } else if (position == 1) {
                    Intent intent = new Intent(getActivity(), List_Milk_Fragment.class);
                    startActivity(intent);
                }
            }
        });
    }
}

class ImageAdapter extends BaseAdapter {
    private Context context;
    private final String[] strValues;
    private final int[] imageValue;

    public ImageAdapter(Context context, String[] strValues, int[] imageValue) {
        this.context = context;
        this.strValues = strValues;
        this.imageValue = imageValue;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;

        if (convertView == null) {

            view = new View(context);

            // get layout from mobile.xml
            view = inflater.inflate(R.layout.grid_items, null);

            // set value into textview
            TextView textView = (TextView) view
                    .findViewById(R.id.grid_item_label);
            textView.setText(strValues[position]);

            // set image based on selected text
            ImageView imageView = (ImageView) view
                    .findViewById(R.id.grid_item_image);
            imageView.setImageResource(imageValue[position]);

        } else {
            view = (View) convertView;
        }

        return view;
    }

    @Override
    public int getCount() {
        return strValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}