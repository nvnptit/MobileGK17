package com.nvn.mobilegk17.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.activity.ChamCongActivity;
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
            com.apachat.loadingbutton.core.customViews.CircularProgressButton btnDongY, btnHuy;
            final Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_dialog_confirm);
            btnDongY = dialog.findViewById(R.id.btnDongY);
            btnHuy = dialog.findViewById(R.id.btnHuy);
            TextView noidung = dialog.findViewById(R.id.noidung);
            noidung.setText("Bạn có chắc chắn muốn xoá không?");
            Window window = dialog.getWindow();
            if (window == null) {
                return;
            } else {
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                windowAttributes.gravity = Gravity.CENTER;
                window.setAttributes(windowAttributes);
                dialog.setCancelable(false);
                dialog.show();
                btnDongY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ChiTietChamCongActivity) context).xoaCTCC(chiTietChamCong);
                    }
                });
                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
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
