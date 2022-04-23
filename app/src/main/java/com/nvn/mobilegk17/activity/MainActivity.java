package com.nvn.mobilegk17.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
//                    finish();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}