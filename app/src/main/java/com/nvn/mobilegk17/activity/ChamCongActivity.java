package com.nvn.mobilegk17.activity;

import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;


import androidx.appcompat.widget.AppCompatButton;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.nvn.mobilegk17.adapter.SanPhamAdapter;
import com.nvn.mobilegk17.database.ChamCongDB;
import com.nvn.mobilegk17.database.ChiTietChamCongDB;
import com.nvn.mobilegk17.model.ChamCong;
import com.nvn.mobilegk17.model.ChiTietChamCong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChamCongActivity extends AppCompatActivity {

    private ListView lvDanhSach;
    private List<ChamCong> data = new ArrayList<>();
    private List<ChamCong> alldata = new ArrayList<>();
    private CustomAdapterChamCong customAdapterChamCong;
    private ChamCongDB database;
    private ChiTietChamCongDB chiTietChamCongDB;
    private AppCompatButton btnThem, btnSua;
    private EditText edtMaChamCong;
    private TextView tvMaCongNhan;
    private SearchView searchMenu;
    private String maChamCongOld;
    private ImageView ivDatePicker;
    private EditText editNgayChamCong;
    private SearchView searchView;

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


        data.addAll(database.docDuLieu(tvMaCongNhan.getText().toString()));
        customAdapterChamCong = new CustomAdapterChamCong(this, R.layout.cham_cong_item, data);
        lvDanhSach.setAdapter(customAdapterChamCong);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapterChamCong.filter(newText);
                return false;
            }
        });
        catchData();
        btnThem.setOnClickListener(this::btnThemOnClickEvent);
        btnSua.setOnClickListener(this::btnSuaOnClickEvent);
        ivDatePicker.setOnClickListener(this::CalendarSetOnClickListener);
        lvDanhSach.setOnItemClickListener(this::lvDanhSachOnItemClickEvent);
        lvDanhSach.setLongClickable(true);
        lvDanhSach.setOnItemLongClickListener(this::lvDanhSachOnLongItemClickEvent);
    }

    private void CalendarSetOnClickListener(View view) {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        calendar.set(year, month, dayOfMonth);
                        editNgayChamCong.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void setControl() {
        lvDanhSach = findViewById(R.id.lvDanhSachChamCong);
        edtMaChamCong = findViewById(R.id.edtMaChamcong);
        ivDatePicker = findViewById(R.id.ivDatePicker);
        editNgayChamCong = findViewById(R.id.addNgaySinhBTV);
        tvMaCongNhan = findViewById(R.id.tvMaCongNhan);
        btnThem = findViewById(R.id.btnThem);
        btnSua = findViewById(R.id.btnSuaCC);
        searchView = findViewById(R.id.timkiem_congnhan);
    }

    private void loadAll() {
        data.clear();
        data.addAll(database.docDuLieu(tvMaCongNhan.getText().toString()));
        customAdapterChamCong = new CustomAdapterChamCong(this, R.layout.cham_cong_item, data);
        lvDanhSach.setAdapter(customAdapterChamCong);

    }


    private boolean isExist(String maChamCong) {
        boolean check = false;
        alldata.clear();
        alldata.addAll(database.docAllDuLieu());
        for (int i = 0; i < alldata.size(); i++) {
            if (alldata.get(i).getMaChamCong().equals(maChamCong)) {
                check = true;
            }
        }
        return check;
    }

    private void btnThemOnClickEvent(View view) {
        ChamCong chamCong = getChamCongFromEditText();
        if(chamCong == null){
            return;
        }
        if (isExist(chamCong.getMaChamCong()) == true) {
            displayToast("Đã tồn tại thông tin mã chấm công");
            return;
        }

        database.themChamCong(chamCong);
        loadAll();
        displayToast("Đã thêm chấm công!");

    }
    private void catchData() {
        edtMaChamCong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String regexMaSP="\\w+";
                if (!charSequence.toString().isEmpty() && !charSequence.toString().matches(regexMaSP)) {
                    edtMaChamCong.setError("Chỉ dùng các chữ cái và số");

                } else {
                    edtMaChamCong.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editNgayChamCong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String regexMaSP="\\d{2}/\\d{2}/\\d{4}";
                if (!charSequence.toString().isEmpty() && !charSequence.toString().matches(regexMaSP)) {
                    editNgayChamCong.setError("Ngày chấm công không để trống, ngày chấm công có định dạng dd/MM/yyyy");

                } else {
                    editNgayChamCong.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    @NonNull
    private ChamCong getChamCongFromEditText() {

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String[] tach= currentDate.split("-");
        int day= Integer.parseInt(tach[0]) ;
        int month= Integer.parseInt(tach[1]) ;
        int year= Integer.parseInt(tach[2]) ;

        String[] tachTest= editNgayChamCong.getText().toString().split("/");
        int dayTest= Integer.parseInt(tachTest[0]) ;
        int monthTest= Integer.parseInt(tachTest[1]) ;
        int yearTest= Integer.parseInt(tachTest[2]) ;

        if ( (year < yearTest)||
                (year == yearTest && month == monthTest && day < dayTest)  ||
                (year == yearTest && month < monthTest)  ) {
            Toast.makeText(this, "Ngày chấm công có thể là ngày hôm nay hoặc những ngày trước đó, Không chấm công trước.", Toast.LENGTH_SHORT).show();
            return null;
        }else{
            ChamCong chamCong = new ChamCong();
            chamCong.setMaChamCong(edtMaChamCong.getText().toString());
            chamCong.setNgayChamCong(editNgayChamCong.getText().toString());
            chamCong.setMaCongNhan(tvMaCongNhan.getText().toString());
            return chamCong;
        }

    }

    public void xoaChamCong(ChamCong chamCong) {
        if (hasChiTietChamCong(chamCong.getMaChamCong()) == true) {
            displayToast("Chấm công đã có chi tiết chấm công. Không thể xoá");
            return;
        }

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Xác nhận");
        b.setMessage("Bạn muốn xoá chấm công không?");
        b.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                database.xoaDuLieu(chamCong);
                loadAll();
                displayToast("Đã xoá chấm công: " + chamCong.getMaChamCong());

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
        ChamCong chamCong = data.get(i);

        tvMaCongNhan.setText(chamCong.getMaCongNhan());
        edtMaChamCong.setText(chamCong.getMaChamCong());
        maChamCongOld = chamCong.getMaChamCong();
        editNgayChamCong.setText(chamCong.getNgayChamCong());
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
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String[] tach= currentDate.split("-");
        int day= Integer.parseInt(tach[0]) ;
        int month= Integer.parseInt(tach[1]) ;
        int year= Integer.parseInt(tach[2]) ;

        String[] tachTest= editNgayChamCong.getText().toString().split("/");
        int dayTest= Integer.parseInt(tachTest[0]) ;
        int monthTest= Integer.parseInt(tachTest[1]) ;
        int yearTest= Integer.parseInt(tachTest[2]) ;

        ChamCong chamCong = findChamCong(tvMaCongNhan.getText().toString(), maChamCongOld);

        if (!edtMaChamCong.getText().toString().equals(chamCong.getMaChamCong())) {
            displayToast("Không thể chỉnh sửa mã chấm công");
            edtMaChamCong.setText(maChamCongOld);
            return;
        }else if ( (year < yearTest)||
                (year == yearTest && month == monthTest && day < dayTest)  ||
                (year == yearTest && month < monthTest)  ) {
            Toast.makeText(this, "Ngày chấm công có thể là ngày hôm nay hoặc những ngày trước đó, Không chấm công trước.", Toast.LENGTH_SHORT).show();

        }
        else {
            chamCong.setNgayChamCong(editNgayChamCong.getText().toString());
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
            if (maCongNhan.trim().toLowerCase().equals(data.get(i).getMaCongNhan().toLowerCase().trim()) && maChamCong.toLowerCase().trim().equals(data.get(i).getMaChamCong().trim().toLowerCase())) {
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