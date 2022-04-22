package com.nvn.mobilegk17.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.nvn.mobilegk17.model.SanPham;
import com.nvn.mobilegk17.model.User;
import com.nvn.mobilegk17.util.Password;

import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public class DBLogin extends SQLiteOpenHelper {
    public static final String DBNAME="login12.db";
    public DBLogin(Context context) {
        super(context, DBNAME,null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(username TEXT primary key,password TEXT,salt TEXT, name TEXT, phone TEXT, isVerify INTEGER, image BLOB )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");

    }
    public Boolean insertData(User user){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put("username",user.getEmail());
        values.put("password",user.getPassword());
        values.put("name",user.getName());
        values.put("isVerify", user.getIsVerify());
        values.put("image",user.getImage());
        values.put("phone",user.getPhone());
        values.put("salt",user.getSalt());


        long result=db.insert("users",null,values);
        if(result==-1){
            return false;
        }
        else
        {
            return true;
        }
    }
    public Boolean checkUsername(String user){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from users where username=?",new String[]{user});
        if(cursor.getCount()>0){
            return true;
        }
        else
        {
            return false;
        }
    }
    public Boolean checkPhone(String mobilePhone){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from users where phone=?",new String[]{mobilePhone});
        if(cursor.getCount()>0){
            return true;
        }
        else
        {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Boolean checkUsernamePassword(String user, String providedPass){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql = "SELECT * FROM users where username=?";
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(sql,new String[]{user});
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
        }
        else
            return false;
        String password=cursor.getString(1);
        String salt=cursor.getString(2);
        Boolean result=true;
        try {
            result= Password.verifyUserPassword(providedPass,password,salt);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return result;
    }
    public User getUser(String email){
        User u=new User();
        SQLiteDatabase db=this.getWritableDatabase();
        String sql = "SELECT * FROM users where username=?";
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(sql,new String[]{email});
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
        }
        else
            return null;
        u.setEmail(cursor.getString(0));
        u.setPassword(cursor.getString(1));
        u.setName(cursor.getString(3));
        u.setImage(cursor.getString(6));
        u.setPhone(cursor.getString(4));
        u.setSalt(cursor.getString(2));
        u.setIsVerify(cursor.getInt(5));
        return u;
    }
    public User getUserByPhone(String mobilePhone){
        User u=new User();
        SQLiteDatabase db=this.getWritableDatabase();
        String sql = "SELECT * FROM users where phone=?";
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(sql,new String[]{mobilePhone});
        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
        }
        else
            return null;
        u.setEmail(cursor.getString(0));
        u.setPassword(cursor.getString(1));
        u.setName(cursor.getString(3));
        u.setImage(cursor.getString(6));
        u.setPhone(cursor.getString(4));
        u.setSalt(cursor.getString(2));
        u.setIsVerify(cursor.getInt(5));
        return u;
    }
    public void update(User user)
    {
        String sql="UPDATE Users SET username=?, password=?, image=? ,isVerify=? where username=?";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql, new String[]{user.getEmail(), user.getPassword(),user.getImage(),user.getIsVerify()+"",user.getEmail()});
        database.close();
    }
    public void updatePass(String email,String password, String salt){
        String sql="UPDATE Users SET password=?,salt=? where username=?";
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql, new String[]{password,salt,email});
        database.close();

    }
    @SuppressLint("Range")
    public int checkVerify(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Users",
                new String[]{"isVerify"}, "username" + "=?",
                new String[]{username}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        int verify = cursor.getInt(cursor.getInt(0));
        cursor.close();
        return verify;
    }
}
