package com.nvn.mobilegk17.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.nvn.mobilegk17.fragment.CongNhanFragment;
import com.nvn.mobilegk17.fragment.QuanLySanPhamFragment;
import com.nvn.mobilegk17.R;

//#NVN
public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    QuanLySanPhamFragment quanLySanPhamFragment;
    CongNhanFragment congNhanFragment;
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
                } else if (id == R.id.nav_chamcong) {
                 //   setFragment(ChamCongFragment);
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