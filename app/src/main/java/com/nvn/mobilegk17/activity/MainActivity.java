package com.nvn.mobilegk17.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.nvn.mobilegk17.fragment.BieuDoFragment;
import com.nvn.mobilegk17.fragment.CongNhanFragment;
import com.nvn.mobilegk17.fragment.QuanLySanPhamFragment;
import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.fragment.SettingFragment;

//#NVN
public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    QuanLySanPhamFragment quanLySanPhamFragment;
    CongNhanFragment congNhanFragment;
    BieuDoFragment bieuDoFragment;
    SettingFragment settingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setEvent();
    }

    private void setControl() {
        bottomNavigationView = findViewById(R.id.bottom_nav);
        quanLySanPhamFragment = new QuanLySanPhamFragment();
        congNhanFragment = new CongNhanFragment();
        bieuDoFragment = new BieuDoFragment();
        settingFragment = new SettingFragment();
    }

    private void setEvent() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_sanpham) {
                    setFragment(quanLySanPhamFragment);
                    return true;
                } else if (id == R.id.nav_congnhan) {
                    setFragment(congNhanFragment);
                    return true;
                } else if (id == R.id.nav_bieudo) {
                    setFragment(bieuDoFragment);
                    return true;
                } else if (id == R.id.nav_setting) {
                    openConfirmDialog();
//                    setFragment(settingFragment);
                    return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_sanpham);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }
    private void openConfirmDialog(){
        com.apachat.loadingbutton.core.customViews.CircularProgressButton btnDongY, btnHuy;
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_confirm);
        btnDongY=dialog.findViewById(R.id.btnDongY);
        btnHuy=dialog.findViewById(R.id.btnHuy);
        Window window=dialog.getWindow();
        if(window==null){
            return;
        }
        else
        {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributes=window.getAttributes();
            windowAttributes.gravity=Gravity.CENTER;
            window.setAttributes(windowAttributes);
            dialog.setCancelable(false);
            dialog.show();
            btnDongY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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

    @Override
    public void onBackPressed() {
    }
}