package com.nvn.mobilegk17.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.nvn.mobilegk17.model.SanPham;

import java.util.ArrayList;

public class DbSanPham extends SQLiteOpenHelper {


    private static final String _TABLE = "DbSanPham";

    public DbSanPham(@Nullable Context context) {
        super(context, _TABLE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE tbSanPham (MASP text, TENSP text, DONGIA text, HINHSP text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL ("DROP TABLE IF EXISTS "+ _TABLE);
        onCreate(sqLiteDatabase);
    }

    public void themSanPham(SanPham sanPham){
        String sql = "INSERT INTO tbSanPham VALUES(?,?,?,?)";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql,new String []{sanPham.getMaSP(),sanPham.getTenSP(), sanPham.getDonGia(), sanPham.getHinhSP()});
        database.close();
    }

    public ArrayList<SanPham> laySanPham(){
        ArrayList<SanPham> sanPhamArrayList = new ArrayList<>();
        String sql = "SELECT * FROM tbSanPham";
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            do {
                SanPham sanPham = new SanPham();
                sanPham.setMaSP(cursor.getString(0));
                sanPham.setTenSP(cursor.getString(1));
                sanPham.setDonGia(cursor.getString(2));
                sanPham.setHinhSP(cursor.getString(3));
                sanPhamArrayList.add(sanPham);
            }
            while (cursor.moveToNext());
        }
        return sanPhamArrayList;
    }

    public void suaSanPham(SanPham sanPham)
    {
        String sql="UPDATE tbSanPham SET TENSP=?, DONGIA=?, HINHSP=? where MASP=?";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql, new String[]{sanPham.getTenSP(), sanPham.getDonGia(),sanPham.getHinhSP(),sanPham.getMaSP()});
        database.close();
    }

    public void xoaSanPham(SanPham sanPham)
    {
        String sql="DELETE FROM tbSanPham  WHERE MASP=?";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql, new String[]{sanPham.getMaSP()});
        database.close();
    }

}
