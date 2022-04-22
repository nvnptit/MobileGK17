package com.nvn.mobilegk17.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.nvn.mobilegk17.model.CongNhan;
import com.nvn.mobilegk17.model.SanPham;

import java.util.ArrayList;

public class DbCongNhan extends SQLiteOpenHelper {
    private static final String _TABLE = "DbCongNhan";
    public DbCongNhan(@Nullable Context context) {
        super(context, _TABLE, null, 1);
    };
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE tbCongNhan (MACN text , HOCN text, TENCN text, PHANXUONG text,NGAYSINH text,IMAGE text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL ("DROP TABLE IF EXISTS "+ _TABLE);
        onCreate(sqLiteDatabase);
    }

    public void themCN(CongNhan congNhan){
        String sql = "INSERT INTO tbCongNhan VALUES(?,?,?,?,?,?)";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql,new String []{congNhan.getMaCN(),congNhan.getHoCN(),congNhan.getTenCN(), congNhan.getPhanXuong(),congNhan.getNgaySinh(),congNhan.getImageSrc()});
        database.close();
    }

    public ArrayList<CongNhan> layCongNhan(){
        ArrayList<CongNhan> congNhanArrayList = new ArrayList<>();
        String sql = "SELECT * FROM tbCongNhan";
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            do {
               CongNhan congNhan=new CongNhan();
                congNhan.setMaCN(cursor.getString(0));
                congNhan.setHoCN(cursor.getString(1));
                congNhan.setTenCN(cursor.getString(2));
                congNhan.setPhanXuong(cursor.getString(3));
                congNhan.setNgaySinh(cursor.getString(4));
                congNhan.setImageSrc(cursor.getString(5));
                congNhanArrayList.add(congNhan);
            }
            while (cursor.moveToNext());
        }
        return congNhanArrayList;
    }

    public void suaCongNhan(CongNhan congNhan)
    {
        String sql="UPDATE tbCongNhan SET HOCN=?, TENCN=?, PHANXUONG=? ,NGAYSINH=?,IMAGE=? where MACN=?";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql, new String[]{congNhan.getHoCN(),congNhan.getTenCN(), congNhan.getPhanXuong(),congNhan.getNgaySinh(),congNhan.getImageSrc(),congNhan.getMaCN()});
        database.close();
    }

    public void xoaCongNhan(CongNhan congNhan)
    {
        String sql="DELETE FROM tbCongNhan  WHERE MACN=?";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql, new String[]{congNhan.getMaCN()});
        database.close();
    }
}
