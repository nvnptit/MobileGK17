package com.nvn.mobilegk17.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.activity.ChamCongActivity;
import com.nvn.mobilegk17.model.ChamCong;
import com.nvn.mobilegk17.model.CongNhan;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomAdapterChamCong extends ArrayAdapter<ChamCong> implements Filterable {
    private final Context context;
    private final int resource;
    private List<ChamCong> data;
    private List<ChamCong> dataOld= new ArrayList<>();

    public CustomAdapterChamCong(@NonNull Context context, int resource, List<ChamCong> data1) {
        super(context, resource, data1);
        this.context = context;
        this.resource = resource;
        this.data = data1;
        this.dataOld.addAll(data) ;
    }

    @Override
    public int getCount() {
        if(data!=null)
        {
            return  data.size();
        }
        return 0;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);

        TextView tvMaChamCong = convertView.findViewById(R.id.tvMaChamCong);
        TextView tvNgayChamCong = convertView.findViewById(R.id.tvNgayChamCong);
        ImageView imXoaChamCong = convertView.findViewById(R.id.ivXoaCC);

        ChamCong chamCong = data.get(position);

        tvMaChamCong.setText(chamCong.getMaChamCong());
        tvNgayChamCong.setText(chamCong.getNgayChamCong());

        imXoaChamCong.setOnClickListener(view -> {
            ((ChamCongActivity) context).xoaChamCong(chamCong);
        });

        return convertView;
    }

    public  void filter(String text) {
        data.clear();
        text = text.toLowerCase();
        if (text.length() == 0) {
            data.addAll(dataOld);
        } else {
            for (ChamCong cc : dataOld) {
                String thongtin = cc.getMaChamCong()+" "+cc.getNgayChamCong();
                if (thongtin.toLowerCase().contains(text)) {
                    data.add(cc);
                }
            }
        }
        notifyDataSetChanged();
    }

}
