package com.nvn.mobilegk17.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.activity.ThemMoiSPActivity;
import com.nvn.mobilegk17.adapter.SanPhamAdapter;
import com.nvn.mobilegk17.database.DbSanPham;
import com.nvn.mobilegk17.model.SanPham;

import java.util.ArrayList;

public class QuanLySanPhamFragment extends Fragment {
    private View view;
    Toolbar toolbar;
    Button btnThemMoi;
    RecyclerView recyclerView;
    SearchView timKiem;

    DbSanPham dbSanPham;
    SanPhamAdapter sanPhamAdapter;
    ArrayList<SanPham> sanPhamArrayList = new ArrayList<>();

    public QuanLySanPhamFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_quan_ly_san_pham, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl();
        setEvent();
    }

    private void setEvent() {
        initData();
        timKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timKiem.setIconified(false);
            }
        });
        timKiem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sanPhamAdapter.filter(newText);
                return false;
            }
        });

        btnThemMoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ThemMoiSPActivity.class);
                startActivity(intent);
            }
        });

    }
    void initDataDB(){
        dbSanPham.themSanPham(new SanPham("A01","??o s?? mi n???",
                "100000","/storage/emulated/0/Download/aosominu.png"));
        dbSanPham.themSanPham(new SanPham("A02","??o s?? mi nam 1",
                "80000","/storage/emulated/0/Download/aosominam.png"));
        dbSanPham.themSanPham(new SanPham("A03","??o s?? mi nam 2",
                "180000","/storage/emulated/0/Download/aosominam1.png"));
        dbSanPham.themSanPham(new SanPham("B01","Bao tay",
                "6000","/storage/emulated/0/Download/baotay.png"));
        dbSanPham.themSanPham(new SanPham("D01","D??p 1",
                "15000","/storage/emulated/0/Download/dep1.png"));
        dbSanPham.themSanPham(new SanPham("D02","D??p 2",
                "15000","/storage/emulated/0/Download/dep2.png"));
        dbSanPham.themSanPham(new SanPham("N01","N??n 1",
                "20000","/storage/emulated/0/Download/non1.png"));
        dbSanPham.themSanPham(new SanPham("N02","N??n 2",
                "30000","/storage/emulated/0/Download/non2.png"));
        dbSanPham.themSanPham(new SanPham("N03","N??n 3",
                "40000","/storage/emulated/0/Download/non3.png"));
        dbSanPham.themSanPham(new SanPham("Q01","Qu???n t??y nam",
                "88888","/storage/emulated/0/Download/quantaynam.png"));
        dbSanPham.themSanPham(new SanPham("Q02","Qu???n t??y n???",
                "99999","/storage/emulated/0/Download/quantaynu.png"));
    }

    private void initData() {
        dbSanPham = new DbSanPham(getContext());
//        initDataDB();
        sanPhamArrayList.addAll(dbSanPham.laySanPham());

        sanPhamAdapter = new SanPhamAdapter(getContext(),sanPhamArrayList);
        recyclerView.setAdapter(sanPhamAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSanPhamDB();
    }

    private void loadSanPhamDB() {
        sanPhamArrayList.clear();
        sanPhamArrayList.addAll(dbSanPham.laySanPham());

        sanPhamAdapter = new SanPhamAdapter(getContext(),sanPhamArrayList);
        recyclerView.swapAdapter(sanPhamAdapter,false);
    }
    private void setControl() {
        toolbar = getActivity().findViewById(R.id.toolbar_sanpham);
        btnThemMoi = getActivity().findViewById(R.id.btn_them);
        recyclerView = getActivity().findViewById(R.id.recyclerview);
        timKiem = getActivity().findViewById(R.id.timkiem);
    }
}