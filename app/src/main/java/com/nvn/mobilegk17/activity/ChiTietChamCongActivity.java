package com.nvn.mobilegk17.activity;

import android.Manifest;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.nvn.mobilegk17.BuildConfig;
import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.adapter.CustomAdapterChiTietChamCong;
import com.nvn.mobilegk17.adapter.CustomAdapterSpinner;
import com.nvn.mobilegk17.database.ChiTietChamCongDB;
import com.nvn.mobilegk17.database.DbSanPham;
import com.nvn.mobilegk17.model.ChiTietChamCong;
import com.nvn.mobilegk17.model.SanPham;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private AppCompatButton btnThem, btnSua, btnPdf;
    private String tenSPOld;
    private SearchView searchView;
    private int pageWidth = 1200, pageHeight = 2010;
    Date dateobj;
    DateFormat dateFormat;
    private String hotenNV,phanxuongNV;
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
        btnPdf.setOnClickListener(this::btnPdfOnClickEvent);
    }

    private void btnPdfOnClickEvent(View view) {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, PackageManager.PERMISSION_GRANTED);


        dateobj = new Date();
        if (data.size() <= 0) {
            Toast.makeText(this, "Không có thông tin để in !", Toast.LENGTH_SHORT).show();
        } else {

            PdfDocument pdfDocument = new PdfDocument();
            Paint Titlepaint = new Paint();
            Paint myPaint = new Paint();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
            PdfDocument.Page page = pdfDocument.startPage((pageInfo));
            Canvas canvas = page.getCanvas();
            Titlepaint.setTextSize(60f);
            Titlepaint.setColor(Color.BLUE);
            Titlepaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Danh sách chi tiết chấm công ", pageWidth / 2, 300, Titlepaint);
            myPaint.setTextAlign(Paint.Align.LEFT);
            myPaint.setTextSize(35f);
            myPaint.setColor(Color.BLACK);
            canvas.drawText("Tên Công Nhân: "+hotenNV,20,500,myPaint);
            canvas.drawText("Phân xưởng: "+phanxuongNV.toString(),20,640,myPaint);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("Mã chấm công: "+maChamCong.getText().toString(),pageWidth-20,500,myPaint);

            dateFormat = new SimpleDateFormat("dd/MM/yy");
            canvas.drawText("Date: " + dateFormat.format(dateobj), pageWidth - 20, 640, myPaint);
            dateFormat = new SimpleDateFormat("HH:mm:ss");
            canvas.drawText("Time: " + dateFormat.format(dateobj), pageWidth - 20, 690, myPaint);
            myPaint.setStyle(Paint.Style.STROKE);
            myPaint.setStrokeWidth(2);
            canvas.drawRect(20, 780, pageWidth - 20, 860, myPaint);
            myPaint.setTextAlign(Paint.Align.LEFT);
            myPaint.setStyle(Paint.Style.FILL);

            canvas.drawText("Mã SP", 40, 830, myPaint);
            canvas.drawText("Tên sản phẩm", 200, 830, myPaint);
            canvas.drawText("Số TP", 700, 830, myPaint);
            canvas.drawText("Số phế phẩm", 900, 830, myPaint);


            canvas.drawLine(180, 790, 180, 840, myPaint);
            canvas.drawLine(680, 790, 680, 840, myPaint);
            canvas.drawLine(880, 790, 880, 840, myPaint);
            int SoTP = 0, SoPP = 0;
            int yChuan = 950;
            int yThem = 100;
            int rowNumber = -2;
            for (int i = 0; i < data.size(); i++) {
                SoTP = SoTP + data.get(i).getSoLuongThanhPham();
                SoPP = SoPP + data.get(i).getSoLuongPhePham();
                rowNumber++;
                canvas.drawText(data.get(i).getMaSanPham().toString(), 40, yChuan, myPaint);
                canvas.drawText(data.get(i).getTenSanPham(), 200, yChuan, myPaint);
                canvas.drawText(data.get(i).getSoLuongThanhPham() + "", 700, yChuan, myPaint);
                canvas.drawText(data.get(i).getSoLuongPhePham() + "", 900, yChuan, myPaint);
                yChuan += yThem;


            }
            int TongSP = SoTP - SoPP;
            canvas.drawLine(680, 1200 + rowNumber * 100, pageWidth - 20, 1200 + rowNumber * 100, myPaint);
            canvas.drawText("Tổng Thành Phẩm  :", 700, 1250 + rowNumber * 100, myPaint);

            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.valueOf(SoTP), pageWidth - 40, 1250 + rowNumber * 100, myPaint);
            myPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Tổng Phế Phẩm      :", 700, 1300 + rowNumber * 100, myPaint);

            canvas.drawText(String.valueOf(SoPP), pageWidth - 60, 1300 + rowNumber * 100, myPaint);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            myPaint.setTextAlign(Paint.Align.LEFT);
            myPaint.setColor(Color.rgb(247, 147, 30));
            canvas.drawRect(680, 1350 + rowNumber * 100, pageWidth - 20, 1450 + rowNumber * 100, myPaint);
            myPaint.setColor(Color.BLACK);
            myPaint.setTextSize(50f);
            myPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Sản Phẩm", 700, 1415 + rowNumber * 100, myPaint);
            myPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(String.valueOf(TongSP), pageWidth - 40, 1415 + rowNumber * 100, myPaint);
            pdfDocument.finishPage(page);

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    File.separator + "Report.pdf");
            try {
                pdfDocument.writeTo(new FileOutputStream(file));
            } catch (Exception e) {
                Log.d("PDFERROR",e.toString());
            }
            pdfDocument.close();
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(FileProvider.getUriForFile(ChiTietChamCongActivity.this, BuildConfig.APPLICATION_ID + ".provider", file), "application/pdf");
            target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|
                    Intent.FLAG_ACTIVITY_NO_HISTORY);


            Intent intent = Intent.createChooser(target, "Open File");

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
        }


    }

    public ArrayList khoiTao() {
        DbSanPham dbSanPham = new DbSanPham(this);
        return dbSanPham.laySanPham();
    }

    private void setControl() {
        phanxuongNV=getIntent().getStringExtra("phanxuongNV").toString();
        hotenNV=getIntent().getStringExtra("hotenNV").toString();
        spinner = findViewById(R.id.spnSP);
        maChamCong = findViewById(R.id.tvMCC);
        tenSanPham = findViewById(R.id.tvTenSanPham);
        soThanhPham = findViewById(R.id.edtSoThanhPham);
        soPhePham = findViewById(R.id.edtSoPhePham);
        lvDanhSach = findViewById(R.id.lvCTCC);
        btnThem = findViewById(R.id.btnThemCTCC);
        btnSua = findViewById(R.id.btnSuaCTCC);
        searchView = findViewById(R.id.timkiem_congnhan);
        btnPdf = findViewById(R.id.btnPdf);
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