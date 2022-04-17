package com.nvn.mobilegk17;

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
    private SearchView searchMenu;
    private String tenSPOld;

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
        customAdapterChiTietChamCong = new CustomAdapterChiTietChamCong(this, R.layout.chi_tiet_cham_cong_item, data);
        lvDanhSach.setAdapter(customAdapterChiTietChamCong);

        Intent intent = getIntent();
        maChamCong.setText(intent.getStringExtra("MaChamCong"));

        loadAll();

        customAdapterSpinner = new CustomAdapterSpinner(this, R.layout.spinner_row, dssp);
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
        ArrayList data = new ArrayList<>();
        SanPham sp1 = new SanPham("1", "Áo ngắn tay",R.drawable.ao1);
        data.add(sp1);

        SanPham sp2 = new SanPham("2", "Sơ mi", R.drawable.somi);
        data.add(sp2);
        SanPham sp3 = new SanPham("3", "Quần short", R.drawable.quanshort);
        data.add(sp3);
        return (ArrayList) data;
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
    }

    private void loadAll() {
        data.clear();
        data.addAll(database.docDuLieu(maChamCong.getText().toString()));
        customAdapterChiTietChamCong.notifyDataSetChanged();
    }

    private void btnThemOnClickEvent(View view) {
        ChiTietChamCong chiTietChamCong = getCTCCFromEditText();
        if (findChiTietChamCong(chiTietChamCong.getMaSanPham(), chiTietChamCong.getMaChamCong()) != null) {
            displayToast("Sản phẩm đã tồn tại trong danh sách chấm công");
            return;
        }
        database.themDuLieu(chiTietChamCong);
        data.add(chiTietChamCong);
        customAdapterChiTietChamCong.notifyDataSetChanged();
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

        //Tạo đối tượng
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        //Thiết lập tiêu đề
        b.setTitle("Xác nhận");
        b.setMessage("Bạn muốn xoá chi tiết chấm công.");
        // Nút Ok
        b.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                database.xoaDuLieu(chiTietChamCong);
                loadAll();
                displayToast("Xoá thành công chi tiết chấm công!" );

            }
        });
        //Nút Cancel
        b.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        //Tạo dialog
        AlertDialog al = b.create();
        //Hiển thị
        al.show();

    }

    private void lvDanhSachOnItemClickEvent(AdapterView<?> adapterView, View view, int i, long l) {
        ChiTietChamCong chiTietChamCong = data.get(i);
        maChamCong.setText(chiTietChamCong.getMaChamCong());
        spinner.setSelection(getPositionSP(chiTietChamCong.getTenSanPham()));
        tenSanPham.setText(chiTietChamCong.getTenSanPham());
        tenSPOld= chiTietChamCong.getTenSanPham();
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
        if(!tenSPOld.equals(sp.getTenSP())){
            displayToast("Mã sản phẩm không thể thay đổi!");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        getMenuInflater().inflate(R.menu.search_menu, menu);
        searchMenu = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchMenu.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchMenu.setMaxWidth(Integer.MAX_VALUE);
        searchMenu.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                customAdapterChiTietChamCong.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapterChiTietChamCong.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!searchMenu.isIconified()) {
            searchMenu.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}