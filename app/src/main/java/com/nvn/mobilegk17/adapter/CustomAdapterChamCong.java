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
import com.nvn.mobilegk17.model.ChamCong;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomAdapterChamCong extends ArrayAdapter<ChamCong> implements Filterable {
    private final Context context;
    private final int resource;
    private List<ChamCong> data;
    private List<ChamCong> dataOld;

    public CustomAdapterChamCong(@NonNull Context context, int resource, List<ChamCong> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.dataOld = data;
    }

    @Override
    public int getCount() {
        return data.size();
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

//        imXoaChamCong.setOnClickListener(view -> {
//            ((getContext(ChamCongFragment)) context).xoaChamCong(chamCong);
//        });

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
                    List<ChamCong> chamCongs= new ArrayList<>();
                    for(ChamCong cc: dataOld){
                        if(cc.getMaChamCong().toLowerCase().contains(strSearch.toLowerCase()) || cc.getNgayChamCong().toLowerCase().contains(strSearch.toLowerCase()) ){
                            chamCongs.add(cc);
                        }
                    }
                    data= chamCongs;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = data;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                data= (List<ChamCong>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
