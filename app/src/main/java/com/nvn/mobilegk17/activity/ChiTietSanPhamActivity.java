package com.nvn.mobilegk17.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ChiTietSanPhamActivity.this);
                builder.setTitle("Xác nhận xoá sản phẩm");
                builder.setMessage("Bạn có chắc xoá sản phẩm này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbSanPham.xoaSanPham(sanPham);
                        Intent intent = new Intent();
                        intent.putExtra("sanpham", sanPham);
                        Toast.makeText(getApplicationContext(), "Xoá thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
                    }
                });
                builder.show();
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

}