package com.nvn.mobilegk17.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.database.DBLogin;
import com.nvn.mobilegk17.model.User;
import com.nvn.mobilegk17.util.EmailService;
import com.nvn.mobilegk17.util.Global;
import com.nvn.mobilegk17.util.LoadingDialog;
import com.nvn.mobilegk17.util.Password;
import com.nvn.mobilegk17.util.Utils;

import java.security.spec.InvalidKeySpecException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RegisterActivity extends AppCompatActivity {
    DBLogin DB;
    EditText email, password, repassword, phone,name;
    com.apachat.loadingbutton.core.customViews.CircularProgressButton signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();
        DB =new DBLogin(this);
        email=findViewById(R.id.editTextEmailRegister);
        password=findViewById(R.id.editTextPasswordRegister);
        repassword=findViewById(R.id.editTextRePassRegister);
        phone=findViewById(R.id.editTextMobileRegister);
        signup=findViewById(R.id.cirRegisterButtonRegister);
        name=findViewById(R.id.editTextNameRegister);
        final LoadingDialog loadingDialog=new LoadingDialog(this);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;

        if (SDK_INT>8){

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        signup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String user=email.getText().toString().trim();
                String pass=password.getText().toString().trim();
                String repass=repassword.getText().toString().trim();
                String mobilePhoneGet=phone.getText().toString().trim();
                String nameNV=name.getText().toString();
                String salt= Password.getSalt(30);
                if(TextUtils.isEmpty(user)||TextUtils.isEmpty(pass)||TextUtils.isEmpty(repass)||TextUtils.isEmpty(pass)|| TextUtils.isEmpty(mobilePhoneGet)|| TextUtils.isEmpty(nameNV))
                {
                    Toast.makeText(RegisterActivity.this,"Không được để trống dữ liệu",Toast.LENGTH_SHORT).show();
                    return;
                }
                String passSecure="";
                try {
                    passSecure=Password.generateSecurePassword(pass,salt);
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
                String mobilePhone="+84"+phone.getText().toString().substring(1,phone.getText().toString().length());
                User userAdd=new User(user,passSecure,nameNV,null,mobilePhone,salt,0);
                Global.user=userAdd;
                    if(!Utils.isEmailValid(user)){
                        Toast.makeText(getApplicationContext(),"Địa chỉ email không hợp lệ",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!Utils.isPhoneValid(mobilePhoneGet))
                    {
                        Toast.makeText(getApplicationContext(),"Số điện thoại không hợp lệ",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(pass.equals(repass)){
                        Boolean checkEmail=DB.checkUsername(user);
                        if(!checkEmail){
                            Boolean checkPhone=DB.checkPhone(mobilePhone);
                            if(!checkPhone){
                                loadingDialog.startLoadingDialog();
                                Handler handler=new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialog.dismissDialog();
                                        Boolean insert= DB.insertData(userAdd);
                                        if(insert){
                                            Random rand = new Random();
                                            Global global=(Global)getApplicationContext();
                                            global.codeVerify=rand.nextInt(10000)+"";
                                            EmailService.send(user,"MÃ XÁC THỰC TÀI KHOẢN","Mã xác thực tài khoản của bạn là: "+Global.codeVerify);
                                            Toast.makeText(RegisterActivity.this,"Một mã xác nhận vừa được gửi qua email của bạn",Toast.LENGTH_LONG).show();
                                            Intent intent= new Intent(getApplicationContext(),VerifyEmailActivity.class);
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            Toast.makeText(RegisterActivity.this,"Đăng ký thất bại",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },3000);
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this,"Số điện thoại này đã được sử dụng",Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this,"Email này đã được sử dụng",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this,"Mật khẩu không khớp",Toast.LENGTH_SHORT).show();
                    }
                }
        });
    }
    public void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }
    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right, android.R.anim.slide_out_right);
    }
}