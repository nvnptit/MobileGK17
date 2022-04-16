package com.nvn.mobilegk17.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.activity.ChiTietSanPhamActivity;
import com.nvn.mobilegk17.model.SanPham;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ItemHolder> {

    Context context;
    ArrayList<SanPham> arrayList;
    ArrayList<SanPham> dataTemp = new ArrayList<>();

    public SanPhamAdapter(Context context, ArrayList<SanPham> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.dataTemp.addAll(arrayList);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp view sản phẩm
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_qlsanpham, null);
        return new ItemHolder(view);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void filter(String text) {
        arrayList.clear();
        text = text.toLowerCase();
        if (text.length() == 0) {
            System.out.println("TEMPSIZE: "+dataTemp.size());
            arrayList.addAll(dataTemp);
        } else {
            System.out.println("LISTENER TMP SIZE: " + dataTemp.size());
            for (SanPham sp : dataTemp) {
                if (sp.getTenSP().toLowerCase().contains(text)) {
                    arrayList.add(sp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        SanPham sanPham = arrayList.get(position);
        if (sanPham == null) {
            return;
        }
        DecimalFormat df = new DecimalFormat("###,###,###");
        holder.nameProduct.setText(sanPham.getTenSP());
        holder.priceProduct.setText(df.format(Integer.parseInt(sanPham.getDonGia())) + "VNĐ");
        Picasso.get().load(Uri.fromFile(new File(sanPham.getHinhSP())))
                .error(R.drawable.no_image)
                .into(holder.imageProduct);
    }

    public void release() {
        context = null;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView nameProduct, priceProduct;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            nameProduct = itemView.findViewById(R.id.tvnameproduct);
            priceProduct = itemView.findViewById(R.id.tvpriceproduct);
            imageProduct = itemView.findViewById(R.id.imgProduct);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChiTietSanPhamActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("sanpham", arrayList.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }

    }

}
