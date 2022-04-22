package com.nvn.mobilegk17.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;

import android.widget.EditText;
import android.widget.ListView;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;

import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.adapter.CustomAdapterChiTietChamCong;
import com.nvn.mobilegk17.adapter.CustomAdapterSpinner;
import com.nvn.mobilegk17.database.ChiTietChamCongDB;
import com.nvn.mobilegk17.database.DbSanPham;
import com.nvn.mobilegk17.model.ChiTietChamCong;
import com.nvn.mobilegk17.model.SanPham;

import java.util.ArrayList;
import java.util.List;

public class ChiTietChamCongActivity extends AppCompatActivity {

    private ListView lvDanhSach;
    private List<ChiTietChamCong> data = new ArrayList<>();
    private ArrayList<SanPham> dssp;
    private CustomAdapterChiTietChamCong customAdapterChiTietChamCong;
    private CustomAdapterSpinner customAdapterSpinner;
    private ChiTietChamCongDB database;
    private Spinner spinner;
    private TextView maChamCong, tenSanPham;
    private EditText soThanhPham, soPhePham;
    private AppCompatButton btnThem, btnSua;
    private String tenSPOld;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_cham_cong);
        dssp = khoiTao();
        setControl();
        setEvent();
    }

    private void setEvent() {

        database = new ChiTietChamCongDB(ChiTietChamCongActivity.this);
        Intent intent = getIntent();
        maChamCong.setText(intent.getStringExtra("MaChamCong"));

        data.addAll(database.docDuLieu(maChamCong.getText().toString()));
        customAdapterChiTietChamCong = new CustomAdapterChiTietChamCong(this, R.layout.chi_tiet_cham_cong_item, data);
        lvDanhSach.setAdapter(customAdapterChiTietChamCong);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapterChiTietChamCong.filter(newText);
                return false;
            }
        });

        customAdapterSpinner = new CustomAdapterSpinner(this, R.layout.spinner_row, khoiTao());
        this.spinner.setAdapter(customAdapterSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SanPham sp = dssp.get(i);
                tenSanPham.setText(sp.getTenSP());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnThem.setOnClickListener(this::btnThemOnClickEvent);
        btnSua.setOnClickListener(this::btnSuaOnClickEvent);
        lvDanhSach.setOnItemClickListener(this::lvDanhSachOnItemClickEvent);
    }

    public ArrayList khoiTao() {
        DbSanPham dbSanPham = new DbSanPham(this);
        return dbSanPham.laySanPham();
    }

    private void setControl() {
        spinner = findViewById(R.id.spnSP);
        maChamCong = findViewById(R.id.tvMCC);
        tenSanPham = findViewById(R.id.tvTenSanPham);
        soThanhPham = findViewById(R.id.edtSoThanhPham);
        soPhePham = findViewById(R.id.edtSoPhePham);
        lvDanhSach = findViewById(R.id.lvCTCC);
        btnThem = findViewById(R.id.btnThemCTCC);
        btnSua = findViewById(R.id.btnSuaCTCC);
        searchView = findViewById(R.id.timkiem_congnhan);
    }

    private void loadAll() {
        data.clear();
        data.addAll(database.docDuLieu(maChamCong.getText().toString()));
        customAdapterChiTietChamCong = new CustomAdapterChiTietChamCong(this, R.layout.chi_tiet_cham_cong_item, data);
        lvDanhSach.setAdapter(customAdapterChiTietChamCong);
    }

    private void btnThemOnClickEvent(View view) {
        ChiTietChamCong chiTietChamCong = getCTCCFromEditText();
        if (findChiTietChamCong(chiTietChamCong.getMaSanPham(), chiTietChamCong.getMaChamCong()) != null) {
            displayToast("Sản phẩm đã tồn tại trong danh sách chấm công");
            return;
        }
        database.themDuLieu(chiTietChamCong);
        loadAll();
        displayToast("Thêm thành công chi tiết chấm công!");
    }

    @NonNull
    private ChiTietChamCong getCTCCFromEditText() {
        SanPham sp = (SanPham) customAdapterSpinner.getItem(spinner.getSelectedItemPosition());
        ChiTietChamCong chiTietChamCong = new ChiTietChamCong();
        chiTietChamCong.setMaChamCong(maChamCong.getText().toString());
        chiTietChamCong.setMaSanPham(sp.getMaSP());
        chiTietChamCong.setTenSanPham(sp.getTenSP());
        if (soThanhPham.getText().toString().equals("")) {
            chiTietChamCong.setSoLuongThanhPham(0);
        } else if (soPhePham.getText().toString().equals("")) {
            chiTietChamCong.setSoLuongPhePham(0);
        } else {
            chiTietChamCong.setSoLuongThanhPham(Integer.parseInt(soThanhPham.getText().toString()));
            chiTietChamCong.setSoLuongPhePham(Integer.parseInt(soPhePham.getText().toString()));
        }
        return chiTietChamCong;
    }

    public void xoaCTCC(ChiTietChamCong chiTietChamCong) {

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Xác nhận");
        b.setMessage("Bạn muốn xoá chi tiết chấm công.");
        b.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                database.xoaDuLieu(chiTietChamCong);
                loadAll();
                displayToast("Xoá thành công chi tiết chấm công!");
            }
        });
        b.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog al = b.create();
        al.show();

    }

    private void lvDanhSachOnItemClickEvent(AdapterView<?> adapterView, View view, int i, long l) {
        ChiTietChamCong chiTietChamCong = data.get(i);
        maChamCong.setText(chiTietChamCong.getMaChamCong());
        spinner.setSelection(getPositionSP(chiTietChamCong.getTenSanPham()));
        tenSanPham.setText(chiTietChamCong.getTenSanPham());
        tenSPOld = chiTietChamCong.getTenSanPham();
        soThanhPham.setText(String.valueOf(chiTietChamCong.getSoLuongThanhPham()));
        soPhePham.setText(String.valueOf(chiTietChamCong.getSoLuongPhePham()));
    }

    private int getPositionSP(String tenSP) {
        for (int i = 0; i < dssp.size(); i++) {
            if (tenSP.equals(dssp.get(i).getTenSP())) {
                return i;
            }
        }
        return -1;
    }


    private void btnSuaOnClickEvent(View view) {
        SanPham sp = (SanPham) customAdapterSpinner.getItem(spinner.getSelectedItemPosition());
        if (!tenSPOld.equals(sp.getTenSP())) {
            displayToast("Mã sản phẩm không thể thay đổi!");
            spinner.setSelection(getPositionSP(tenSPOld));
            return;
        }
        ChiTietChamCong chiTietChamCong = findChiTietChamCong(sp.getMaSP(), maChamCong.getText().toString());
        chiTietChamCong.setSoLuongThanhPham(Integer.parseInt(soThanhPham.getText().toString()));
        chiTietChamCong.setSoLuongPhePham(Integer.parseInt(soPhePham.getText().toString()));
        database.suaDuLieu(chiTietChamCong);
        loadAll();
        displayToast("Chỉnh sửa chi tiết chấm công thành công!");
    }

    private void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public ChiTietChamCong findChiTietChamCong(String maSanPham, String maChamCong) {
        for (int i = 0; i < data.size(); i++) {
            if (maChamCong.equals(data.get(i).getMaChamCong()) && maSanPham.equals(data.get(i).getMaSanPham())) {
                return data.get(i);
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}