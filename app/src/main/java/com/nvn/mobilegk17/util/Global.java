package com.nvn.mobilegk17.util;

import android.app.Application;

import com.nvn.mobilegk17.model.User;

public class Global extends Application {
    public static String codeVerify;
    public static User user;

    public static String getCodeVerify() {
        return codeVerify;
    }
    public static void setCodeVerify(String code){
        codeVerify=code;
    }

}
