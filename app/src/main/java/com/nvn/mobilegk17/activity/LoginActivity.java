package com.nvn.mobilegk17.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.database.DBLogin;
import com.nvn.mobilegk17.model.User;
import com.nvn.mobilegk17.util.EmailService;
import com.nvn.mobilegk17.util.Global;
import com.nvn.mobilegk17.util.LoadingDialog;
import com.nvn.mobilegk17.util.Utils;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    DBLogin DB;
    com.apachat.loadingbutton.core.customViews.CircularProgressButton login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon color
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        DB= new DBLogin(this);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.editTextEmailLogin);
        password=findViewById(R.id.editTextPasswordLogin);
        login=findViewById(R.id.cirLoginButtonLogin);
        final LoadingDialog loadingDialog=new LoadingDialog(LoginActivity.this);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;

        if (SDK_INT>8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String user = email.getText().toString().trim();
                String pass=password.getText().toString().trim();
                if(TextUtils.isEmpty(user)||TextUtils.isEmpty(pass)){
                    Toast.makeText(LoginActivity.this,"Không được để trống email/ mật khẩu",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(!Utils.isEmailValid(user)){
                        Toast.makeText(LoginActivity.this,"Địa chỉ email không hợp lệ",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Boolean checkUser=DB.checkUsernamePassword(user,pass);
                    if(!checkUser){
                        Toast.makeText(LoginActivity.this,"Email hoặc mật khẩu không đúng",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        if(DB.checkVerify(user)==0){
                            Global.user=DB.getUser(user);
                            Random rand = new Random();
                            Global global=(Global)getApplicationContext();
                            global.codeVerify=rand.nextInt(10000)+"";
                            EmailService.send(user,"MÃ XÁC THỰC TÀI KHOẢN","Mã xác thực tài khoản của bạn là: "+Global.codeVerify);
                            Toast.makeText(LoginActivity.this,"Một mã xác nhận vừa được gửi qua email của bạn",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),VerifyEmailActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            loadingDialog.startLoadingDialog();
                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialog.dismissDialog();
                                        User u=new User();
                                        u=DB.getUser(user);
                                        Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);
                                    }
                                },3000);

                        }
                    }
                }

            }
        });

    }
    public void onLoginClick(View view){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }
    public void onForgotPassClick(View view){
        startActivity(new Intent(getApplicationContext(),ResetPasswordActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }
}