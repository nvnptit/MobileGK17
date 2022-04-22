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
import com.nvn.mobilegk17.activity.ChiTietChamCongActivity;
import com.nvn.mobilegk17.model.ChamCong;
import com.nvn.mobilegk17.model.ChiTietChamCong;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterChiTietChamCong extends ArrayAdapter<ChiTietChamCong> implements Filterable {

    private final Context context;
    private final int resource;
    private List<ChiTietChamCong> data;
    private List<ChiTietChamCong> dataOld= new ArrayList<>();

    public CustomAdapterChiTietChamCong(@NonNull Context context, int resource, List<ChiTietChamCong> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.dataOld.addAll(data);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);

        TextView maSP = convertView.findViewById(R.id.tvMaSP);
        TextView tenSP = convertView.findViewById(R.id.tvTenSP);
        TextView soTP = convertView.findViewById(R.id.tvSotp);
        TextView soPP = convertView.findViewById(R.id.tvsopp);
        ImageView btnXoa = convertView.findViewById(R.id.ivXoaCTCC);

        ChiTietChamCong chiTietChamCong = data.get(position);

        maSP.setText(chiTietChamCong.getMaSanPham());
        tenSP.setText(chiTietChamCong.getTenSanPham());
        soTP.setText(String.valueOf(chiTietChamCong.getSoLuongThanhPham()));
        soPP.setText(String.valueOf(chiTietChamCong.getSoLuongPhePham()));

        btnXoa.setOnClickListener(view -> {
            ((ChiTietChamCongActivity) context).xoaCTCC(chiTietChamCong);
        });

        return convertView;
    }

    public  void filter(String text) {
        data.clear();
        text = text.toLowerCase();
        if (text.length() == 0) {
            data.addAll(dataOld);
        } else {
            for (ChiTietChamCong cc : dataOld) {
                String thongtin = cc.getMaSanPham()+" "+ cc.getTenSanPham();
                if (thongtin.toLowerCase().contains(text)) {
                    data.add(cc);
                }
            }
        }
        notifyDataSetChanged();
    }



}
