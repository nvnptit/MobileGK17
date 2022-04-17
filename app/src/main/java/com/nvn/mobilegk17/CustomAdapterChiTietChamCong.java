package com.nvn.mobilegk17;

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

import java.util.ArrayList;
import java.util.List;

public class CustomAdapterChiTietChamCong extends ArrayAdapter<ChiTietChamCong> implements Filterable {

    private final Context context;
    private final int resource;
    private List<ChiTietChamCong> data;
    private List<ChiTietChamCong> dataOld;

    public CustomAdapterChiTietChamCong(@NonNull Context context, int resource, List<ChiTietChamCong> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.dataOld= data;
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

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if(strSearch.isEmpty()){
                    data= dataOld;
                }else{
                    List<ChiTietChamCong> chiTietChamCongs= new ArrayList<>();
                    for(ChiTietChamCong cc: dataOld){
                        if(cc.getTenSanPham().toLowerCase().contains(strSearch.toLowerCase())|| cc.getMaSanPham().toLowerCase().contains(strSearch.toLowerCase())){
                            chiTietChamCongs.add(cc);
                        }
                    }
                    data= chiTietChamCongs;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = data;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                data= (List<ChiTietChamCong>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
