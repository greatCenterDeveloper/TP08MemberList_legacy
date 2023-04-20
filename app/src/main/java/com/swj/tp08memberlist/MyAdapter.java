package com.swj.tp08memberlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    Context context;
    ArrayList<Item> itemArrayList;

    public MyAdapter(Context context, ArrayList<Item> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @Override
    public int getCount() {
        return itemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.listview_item, null);
        }

        Item item = itemArrayList.get(i);
        TextView tvName = view.findViewById(R.id.tv_name);
        TextView tvNation = view.findViewById(R.id.tv_nation);
        TextView tvGender = view.findViewById(R.id.tv_gender);
        ImageView ivNationalFlag = view.findViewById(R.id.iv_national_flag);

        tvName.setText(item.name);
        tvNation.setText(item.nation);
        tvGender.setText(item.gender);
        ivNationalFlag.setImageResource(item.imgId);
        return view;
    }
}
