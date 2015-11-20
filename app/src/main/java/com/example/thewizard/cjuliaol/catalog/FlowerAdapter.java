package com.example.thewizard.cjuliaol.catalog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.thewizard.cjuliaol.catalog.model.Flower;

import java.util.List;

/**
 * Created by cjuliaol on 19-Nov-15.
 */
public class FlowerAdapter extends ArrayAdapter<Flower> {

    private Context mContext;
    private List<Flower> mFlowerList;

    public FlowerAdapter(Context context, int resource, List<Flower> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mFlowerList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_flower, parent, false);

        // Display flower name in the textview widget

        Flower flower = mFlowerList.get(position);
        TextView flowerName = (TextView) view.findViewById(R.id.flower_name);
        flowerName.setText(flower.getName());

        return view;


    }
}
