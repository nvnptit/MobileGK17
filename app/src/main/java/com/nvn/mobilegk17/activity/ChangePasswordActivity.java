package com.nvn.mobilegk17.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.database.DBLogin;
import com.nvn.mobilegk17.model.User;
import com.nvn.mobilegk17.util.Global;
import com.nvn.mobilegk17.util.Password;

import java.security.spec.InvalidKeySpecException;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText password, repass;
    DBLogin db;
    String username;
    com.apachat.loadingbutton.core.customViews.CircularProgressButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        password=findViewById(R.id.editTextPassword1);
        repass=findViewById(R.id.editTextRepas1);
        button=findViewById(R.id.cirContButton);
        db=new DBLogin(this);
        username=getIntent().getStringExtra("phone_number");
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(!password.getText().toString().equals(repass.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Mật khẩu không khớp",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String salt=Password.getSalt(30);
                    try {
                        User u=new User();
                        Global.user=db.getUserByPhone(username);
                        db.updatePass(Global.user.getEmail(), Password.generateSecurePassword(password.getText().toString(),salt),salt);
                        Toast.makeText(getApplicationContext(),"Khôi phục mật khẩu thành công!",Toast.LENGTH_SHORT).show();
                        gotoMainActivity();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void gotoMainActivity() {
        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }
}