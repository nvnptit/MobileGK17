package com.nvn.mobilegk17.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.database.DBLogin;
import com.nvn.mobilegk17.util.Global;

public class VerifyEmailActivity extends AppCompatActivity {
    EditText digit1, digit2, digit3, digit4;
    DBLogin db= new DBLogin(this);
    com.apachat.loadingbutton.core.customViews.CircularProgressButton verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        digit1=findViewById(R.id.digit1);
        digit2=findViewById(R.id.digit2);
        digit3=findViewById(R.id.digit3);
        digit4=findViewById(R.id.digit4);
        verify=findViewById(R.id.cirVerifyButton);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num1=digit1.getText().toString();
                String num2=digit2.getText().toString();
                String num3=digit3.getText().toString();
                String num4=digit4.getText().toString();
                Global global=(Global) getApplicationContext();
                if((num1+num2+num3+num4).equals(global.codeVerify)){
                    Global.user.setIsVerify(1);
                    db.update(Global.user);
                    Toast.makeText(getApplicationContext(),"Xác thực tài khoản thành công",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Mã xác thực không đúng, mời bạn thử lại",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void onLoginClick(View view){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }
}