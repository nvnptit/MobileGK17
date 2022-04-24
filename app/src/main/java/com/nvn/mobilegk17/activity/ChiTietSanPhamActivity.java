package com.nvn.mobilegk17.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.database.DbSanPham;
import com.nvn.mobilegk17.model.SanPham;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btn_CapNhat, btn_Xoa;

    ImageView imageView;
    TextView tvMaSP, tvTenSP, tvDonGiaSP;
    SanPham sanPham;

    DbSanPham dbSanPham;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        setControl();
        actionToolBar();
        setEvent();
    }

    private void setEvent() {
        sanPham = (SanPham) getIntent().getSerializableExtra("sanpham");
        tvMaSP.setText(sanPham.getMaSP());
        tvTenSP.setText(sanPham.getTenSP());

        DecimalFormat df = new DecimalFormat("###,###,###");
        tvDonGiaSP.setText(df.format(Long.parseLong(sanPham.getDonGia())) + "VNĐ");
        Picasso.get().load(Uri.fromFile(new File(sanPham.getHinhSP())))
                .error(R.drawable.no_image)
                .into(imageView);

        btn_CapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), CapNhatActivity.class);
                intent.putExtra("sanpham", sanPham);
                startActivity(intent);
            }
        });

        btn_Xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfirmDialog();
            }
        });

    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbar_chitietsanpham);
        btn_CapNhat = findViewById(R.id.btn_capnhat);
        btn_Xoa = findViewById(R.id.btn_xoa);
        imageView = findViewById(R.id.viewimage);

        tvMaSP = findViewById(R.id.tv_viewmaproduct);
        tvTenSP = findViewById(R.id.tv_viewnameproduct);
        tvDonGiaSP = findViewById(R.id.tv_viewpriceproduct);
        dbSanPham = new DbSanPham(this);
    }

    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //tạo nút home
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void openConfirmDialog() {
        com.apachat.loadingbutton.core.customViews.CircularProgressButton btnDongY, btnHuy;
        final Dialog dialog = new Dialog(this);
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
                    dbSanPham.xoaSanPham(sanPham);
                    Intent intent = new Intent();
                    intent.putExtra("sanpham", sanPham);
                    Toast.makeText(getApplicationContext(), "Xoá thành công!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                }
            });
            btnHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
    }
}