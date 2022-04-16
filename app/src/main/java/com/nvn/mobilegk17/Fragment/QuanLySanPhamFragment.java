package com.nvn.mobilegk17.Fragment;

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
import com.nvn.mobilegk17.activity.ThemMoiActivity;
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
                Intent intent = new Intent(getContext(), ThemMoiActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initData() {
//        sanPhamArrayList.add( new SanPham("A01","Áo sơ mi nữ","30000","content://com.android.providers.media.documents/document/image%3A18"));
//        sanPhamArrayList.add( new SanPham("A02","Áo sơ mi nam","45000","1"));
//        sanPhamArrayList.add( new SanPham("B03","Bao tay","6000","1"));
//        sanPhamArrayList.add( new SanPham("D06","Dép","15000","1"));
//        sanPhamArrayList.add( new SanPham("N04","Nón","8000","1"));
//        sanPhamArrayList.add( new SanPham("Q05","Quần Tây","60000","1"));
        dbSanPham = new DbSanPham(getContext());
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