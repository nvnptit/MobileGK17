package com.nvn.mobilegk17.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;


import androidx.appcompat.widget.AppCompatButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.adapter.CustomAdapterChamCong;
import com.nvn.mobilegk17.database.ChamCongDB;
import com.nvn.mobilegk17.database.ChiTietChamCongDB;
import com.nvn.mobilegk17.model.ChamCong;
import com.nvn.mobilegk17.model.ChiTietChamCong;

import java.util.ArrayList;
import java.util.List;

public class ChamCongActivity extends AppCompatActivity {

    private ListView lvDanhSach;
    private List<ChamCong> data = new ArrayList<>();
    private CustomAdapterChamCong customAdapterChamCong;
    private ChamCongDB database;
    private ChiTietChamCongDB chiTietChamCongDB;
    private AppCompatButton btnThem, btnSua;
    private EditText edtMaChamCong, edtNgayChamCong;
    private TextView tvMaCongNhan;
    private SearchView searchMenu;
    private String maChamCongOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cham_cong);
        setControl();
        tvMaCongNhan.setText(getIntent().getStringExtra("macn"));
        setEvent();
    }

    private void setEvent() {
        database = new ChamCongDB(this);
        chiTietChamCongDB = new ChiTietChamCongDB(this);
        customAdapterChamCong = new CustomAdapterChamCong(this, R.layout.cham_cong_item, data);
        loadAll();
        lvDanhSach.setAdapter(customAdapterChamCong);

        btnThem.setOnClickListener(this::btnThemOnClickEvent);
        btnSua.setOnClickListener(this::btnSuaOnClickEvent);
        lvDanhSach.setOnItemClickListener(this::lvDanhSachOnItemClickEvent);
        lvDanhSach.setLongClickable(true);
        lvDanhSach.setOnItemLongClickListener(this::lvDanhSachOnLongItemClickEvent);
    }

    private void setControl() {
        lvDanhSach = findViewById(R.id.lvDanhSachChamCong);
        edtMaChamCong = findViewById(R.id.edtMaChamcong);
        edtNgayChamCong = findViewById(R.id.edtNgayChamCong);
        tvMaCongNhan = findViewById(R.id.tvMaCongNhan);
        btnThem = findViewById(R.id.btnThem);
        btnSua = findViewById(R.id.btnSuaCC);
    }

    private void loadAll() {
        data.clear();
        data.addAll(database.docDuLieu());
        customAdapterChamCong.notifyDataSetChanged();
    }


    private void btnThemOnClickEvent(View view) {


        ChamCong chamCong = getChamCongFromEditText();
        if (chamCong.getMaChamCong().isEmpty() || chamCong.getNgayChamCong().isEmpty()) {
            displayToast("Vui lòng điền đầy đủ thông tin chấm công");
            return;
        }
        database.themChamCong(chamCong);
        data.add(chamCong);
        customAdapterChamCong.notifyDataSetChanged();
        displayToast("Đã thêm chấm công!");

    }

    @NonNull
    private ChamCong getChamCongFromEditText() {
        ChamCong chamCong = new ChamCong();
        chamCong.setMaChamCong(edtMaChamCong.getText().toString());
        chamCong.setNgayChamCong(edtNgayChamCong.getText().toString());
        chamCong.setMaCongNhan(tvMaCongNhan.getText().toString());
        return chamCong;
    }

    public void xoaChamCong(ChamCong chamCong) {
        if (hasChiTietChamCong(chamCong.getMaChamCong()) == true) {
            displayToast("Chấm công đã có chi tiết chấm công. Không thể xoá");
            return;
        }

        //Tạo đối tượng
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        //Thiết lập tiêu đề
        b.setTitle("Xác nhận");
        b.setMessage("Bạn muốn xoá chấm công không?");
        // Nút Ok
        b.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                database.xoaDuLieu(chamCong);
                loadAll();
                displayToast("Đã xoá chấm công: " + chamCong.getMaChamCong());

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
        ChamCong chamCong = data.get(i);

        tvMaCongNhan.setText(chamCong.getMaCongNhan());
        edtMaChamCong.setText(chamCong.getMaChamCong());
        maChamCongOld = chamCong.getMaChamCong();
        edtNgayChamCong.setText(chamCong.getNgayChamCong());
    }

    private boolean lvDanhSachOnLongItemClickEvent(AdapterView<?> adapterView, View view, int i, long l) {
        ChamCong chamCong = data.get(i);
        Intent intent = new Intent(getApplicationContext(), ChiTietChamCongActivity.class);
        intent.putExtra("MaChamCong", chamCong.getMaChamCong());  // Truyền một String
        startActivity(intent);
        return true;
    }

    private boolean hasChiTietChamCong(String maChamCong) {
        List<ChiTietChamCong> ds = chiTietChamCongDB.docDuLieu(maChamCong);
        if (ds.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void btnSuaOnClickEvent(View view) {
        ChamCong chamCong = findChamCong(tvMaCongNhan.getText().toString(), maChamCongOld);
        if (!edtMaChamCong.getText().toString().equals(chamCong.getMaChamCong())) {
            displayToast("Không thể chỉnh sửa mã chấm công");
            return;
        } else {
            chamCong.setNgayChamCong(edtNgayChamCong.getText().toString());
            database.suaNgay(chamCong);
        }
        loadAll();
        displayToast("Chỉnh sửa chấm công thành công");
    }

    private void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public ChamCong findChamCong(String maCongNhan, String maChamCong) {
        for (int i = 0; i < data.size(); i++) {
            if (maCongNhan.equals(data.get(i).getMaCongNhan()) && maChamCong.equals(data.get(i).getMaChamCong())) {
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
                customAdapterChamCong.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapterChamCong.getFilter().filter(newText);
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