package com.nvn.mobilegk17.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.nvn.mobilegk17.model.ChiTietChamCong;

import java.util.ArrayList;
import java.util.List;

public class ChiTietChamCongDB extends SQLiteOpenHelper {
    public ChiTietChamCongDB(@Nullable Context context) {
        super(context, "dbChiTietChamCong", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "Create Table ChiTietChamCong (MaChamCong text, MaSanPham text, TenSanPham text, SoThanhPham int, SoPhePham int)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void themDuLieu(ChiTietChamCong chiTietChamCong) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "Insert Into ChiTietChamCong values(?, ?, ?,?,?)";
        database.execSQL(sql, new Object[]{chiTietChamCong.getMaChamCong(), chiTietChamCong.getMaSanPham(), chiTietChamCong.getTenSanPham(), chiTietChamCong.getSoLuongThanhPham(), chiTietChamCong.getSoLuongPhePham()});
    }

    public List<ChiTietChamCong> docDuLieu(String maChamCong) {
        List<ChiTietChamCong> data = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String sql = "Select * from ChiTietChamCong where MaChamCong= ?";
        Cursor cursor = database.rawQuery(sql, new String[]{maChamCong});

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ChiTietChamCong chiTietChamCong = new ChiTietChamCong();
                chiTietChamCong.setMaChamCong(cursor.getString(0));
                chiTietChamCong.setMaSanPham(cursor.getString(1));
                chiTietChamCong.setTenSanPham(cursor.getString(2));
                chiTietChamCong.setSoLuongThanhPham(cursor.getInt(3));
                chiTietChamCong.setSoLuongPhePham(cursor.getInt(4));
                data.add(chiTietChamCong);
                cursor.moveToNext();
            }
        }

        return data;
    }

    public void suaDuLieu(ChiTietChamCong chiTietChamCong) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "update ChiTietChamCong set TenSanPham=?, SoThanhPham=?, SoPhePham=? where MaChamCong = ? and MaSanPham=?";
        database.execSQL(sql, new Object[]{chiTietChamCong.getTenSanPham(), chiTietChamCong.getSoLuongThanhPham(), chiTietChamCong.getSoLuongPhePham(), chiTietChamCong.getMaChamCong(), chiTietChamCong.getMaSanPham()});
    }

    public void xoaDuLieu(ChiTietChamCong chiTietChamCong) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "delete from ChiTietChamCong where MaChamCong = ? and MaSanPham= ? ";
        database.execSQL(sql, new Object[]{chiTietChamCong.getMaChamCong(), chiTietChamCong.getMaSanPham()});

    }
}
