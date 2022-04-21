package com.nvn.mobilegk17.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.nvn.mobilegk17.model.ChamCong;

import java.util.ArrayList;
import java.util.List;

public class ChamCongDB extends SQLiteOpenHelper {

    public ChamCongDB(@Nullable Context context) {
        super(context, "dbChamCong", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "Create Table CHAMCONG (MaChamCong text,MaCongNhan text, NgayChamCong text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void themChamCong(ChamCong chamCong) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "Insert Into CHAMCONG values(?, ?, ?)";
        database.execSQL(sql, new Object[]{chamCong.getMaChamCong(), chamCong.getMaCongNhan(), chamCong.getNgayChamCong()});
    }

    public List<ChamCong> docDuLieu() {
        List<ChamCong> data = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String sql = "Select * from CHAMCONG";
        Cursor cursor = database.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ChamCong chamCong = new ChamCong();
                chamCong.setMaChamCong(cursor.getString(0));
                chamCong.setMaCongNhan(cursor.getString(1));
                chamCong.setNgayChamCong(cursor.getString(2));
                data.add(chamCong);
                cursor.moveToNext();
            }
        }

        return data;
    }


    public void suaNgay(ChamCong chamCong) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "update CHAMCONG set  NgayChamCong = ? where MaCongNhan = ? and MaChamCong = ?";
        database.execSQL(sql, new Object[]{chamCong.getNgayChamCong(), chamCong.getMaCongNhan(), chamCong.getMaChamCong()});
    }

    public void xoaDuLieu(ChamCong chamCong) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "delete from CHAMCONG where MaChamCong = ?";
        database.execSQL(sql, new Object[]{chamCong.getMaChamCong()});
    }
}
