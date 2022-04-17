package com.nvn.mobilegk17;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapterSpinner extends BaseAdapter {
    private final Activity context;
    private final int resource;
    private List<SanPham> data;

    private LayoutInflater flater;

    public CustomAdapterSpinner(Activity context, int resource, List<SanPham> data) {
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.flater = context.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return this.data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SanPham sp = (SanPham) getItem(i);
        View rowview = this.flater.inflate(R.layout.spinner_row, null, true);
        TextView masp = rowview.findViewById(R.id.masp);
     //   TextView tensp = rowview.findViewById(R.id.tensp);
        ImageView imv = rowview.findViewById(R.id.ivSP);
        masp.setText(sp.getMaSP());
       // tensp.setText(sp.getTenSP());
        imv.setImageResource(sp.getHinhAnh());
        return rowview;
    }
}
