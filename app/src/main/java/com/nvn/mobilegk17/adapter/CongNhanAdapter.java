package com.nvn.mobilegk17.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.activity.ChiTietCongNhanActivity;
import com.nvn.mobilegk17.model.CongNhan;
import com.nvn.mobilegk17.model.SanPham;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CongNhanAdapter extends  RecyclerView.Adapter<CongNhanAdapter.UserViewHolder> implements Filterable {
    private List<CongNhan> mListCongNhan;
    private List<CongNhan> mListCongNhanCu = new ArrayList<>();;
    private Context mContext;

    public CongNhanAdapter(Context context,List<CongNhan> mCongNhan) {
        this.mListCongNhan = mCongNhan;
        this.mListCongNhanCu.addAll(mListCongNhan);
        this.mContext=context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cn,parent,false);
        return new UserViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        int vitri=position;
        CongNhan congNhan=mListCongNhan.get(position);
        if(congNhan==null)
        {
            return;
        }
        System.out.println("HINH_Nhat:"+ holder.imgUser);
//        Glide.with(mContext)
//                .load(congNhan.getImageSrc())
//                .into(holder.imgUser);
        Picasso.get().load(Uri.fromFile(new File(congNhan.getImageSrc()+"")))
                .error(R.drawable.no_image)
                .into(holder.imgUser);
        holder.tvName.setText(congNhan.getHoCN()+" "+congNhan.getTenCN());
        holder.tvPhanXuong.setText(congNhan.getPhanXuong());
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickGoToDetail(congNhan);

            }

            private void onClickGoToDetail(CongNhan congNhan) {

                Intent intent=new Intent(mContext, ChiTietCongNhanActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("object_user",congNhan);
                intent.putExtras(bundle);
                intent.putExtra("vi_tri",vitri);
                mContext.startActivity(intent);
            }

        });
    }
    public  void release(){
        mContext=null;
    }



    @Override
    public int getItemCount() {
        if(mListCongNhan!=null)
        {
            return  mListCongNhan.size();
        }
        return 0;
    }



    public  class UserViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView imgUser;

        private TextView tvName;
        private TextView tvPhanXuong;
        private CardView layoutItem;
        LinearLayout layoutForeGround;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutItem=itemView.findViewById(R.id.layout_item);
            imgUser=itemView.findViewById(R.id.img_user);
            tvName=itemView.findViewById(R.id.tvName);
            tvPhanXuong=itemView.findViewById(R.id.tvphanXuong);
            layoutForeGround=itemView.findViewById(R.id.layoutItem);
        }



    }
    public void removeItem(int index){
        mListCongNhan.remove(index);
        notifyItemRemoved(index);
    }
    public void addItem(CongNhan congNhan){
        mListCongNhan.add(congNhan);

    }

    public void undoItem(CongNhan congNhan,int index)
    {
        mListCongNhan.add(index,congNhan);
        notifyItemInserted(index);
    }
    public void updateItem(CongNhan congNhan,int index)
    {
        mListCongNhan.set(index,congNhan);
        notifyItemChanged(index);
    }

    public void filter(String text) {
        mListCongNhan.clear();
        text = text.toLowerCase();
        if (text.length() == 0) {
            System.out.println("TEMPSIZE: "+mListCongNhanCu.size());
            mListCongNhan.addAll(mListCongNhanCu);
        } else {
            System.out.println("LISTENER TMP SIZE: " + mListCongNhanCu.size());
            for (CongNhan cn : mListCongNhanCu) {
                String hoten= cn.getHoCN()+" "+cn.getTenCN();
                if (hoten.toLowerCase().contains(text)) {
                    mListCongNhan.add(cn);
                }
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch=charSequence.toString();
                if(strSearch.isEmpty())
                {
                    mListCongNhan=mListCongNhanCu;
                }
                else
                {
                    List<CongNhan> list=new ArrayList<>();
                    for(CongNhan cn:mListCongNhanCu)
                    {
                        String hotenNV=cn.getHoCN()+" "+cn.getTenCN();
                        if(hotenNV.toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(cn);
                        }
                    }
                    mListCongNhan=list;
                }


                FilterResults filterResults=new FilterResults();
                filterResults.values=mListCongNhan;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListCongNhan= (List<CongNhan>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



}
