package com.nvn.mobilegk17.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.adapter.CongNhanAdapter;
import com.nvn.mobilegk17.model.CongNhan;
import com.nvn.mobilegk17.util.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ThemCNActivity extends AppCompatActivity {
    String[] items = {"Ha Noi", "HCM", "DA NANG"};
    CongNhanAdapter congNhanAdapter;
    ArrayAdapter<String> adapterPhanXuong;
    Button btnLuu;
    EditText txtHo, txtTen, edtDate;
    AutoCompleteTextView cbbPhanXuong;
    TextView tvMaCN;
    ImageView imageCreate;
    String selectedImage;
    FloatingActionButton btnSelectPhoto;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_cnactivity);
        setControl();
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        String MaCNMoi=bundle.getString("Mamoi");
        tvMaCN.setText(MaCNMoi);
        CongNhan congNhan = (CongNhan) bundle.get("user_update");
        adapterPhanXuong = new ArrayAdapter<String>(this, R.layout.dropdown_item, items);
        cbbPhanXuong.setAdapter(adapterPhanXuong);
        setEvent();
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Ho, Ten, PhanXuong, MaCN, NgaySinh;
                String HinhAnh;
                Ho = txtHo.getText().toString();
                Ten = txtTen.getText().toString();
                PhanXuong = cbbPhanXuong.getText().toString();
                MaCN = tvMaCN.getText().toString();

                NgaySinh = edtDate.getText().toString();
                if (Ho.isEmpty() || Ten.isEmpty() || PhanXuong.isEmpty() || NgaySinh.isEmpty()) {
                    alert("Vui l??ng nh???p ?????y ????? th??ng tin");
                } else {
                    alert("Th??m th??nh c??ng !");
                    CongNhan congNhanNew = new CongNhan(MaCN, Ho, Ten, PhanXuong, NgaySinh, selectedImage);
                    Intent intent = new Intent();
                    System.out.println("Ten NV" + Ten);
                    intent.putExtra("cnMoi", congNhanNew);
                    setResult(RESULT_OK, intent);


                }

            }


        });


        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check runtime PERMISSION
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //Permision not Granted.request it
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //Show popup for runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                }
            }

        });
    }




    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageFromGallery();
                } else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
//            imageCreate.setImageURI(data.getData());
//            selectedImage = data.getData().toString();
            Picasso.get().load(data.getData())
                    .into(imageCreate);
            String path= Utils.getRealPathFromURI(getApplicationContext(),data.getData())+"";
            selectedImage=path;
            System.out.println("selectedImage"+selectedImage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setEvent() {
        edtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                chonNgay();
            }
        });
    }

    private void chonNgay() {
        Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy");
                edtDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }

    private void alert(String mess) {
        openConfirmDialog();

    }
    private void openConfirmDialog() {
        com.apachat.loadingbutton.core.customViews.CircularProgressButton btnXacNhan;
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_update);
        btnXacNhan = dialog.findViewById(R.id.btnxacnhan);
        TextView noidung = dialog.findViewById(R.id.tvcapnhat);
        noidung.setText("Th??m m???i th??nh c??ng!");
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        } else {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);
            dialog.setCancelable(false);
            dialog.show();
            btnXacNhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    onBackPressed();
                }
            });
        }
    }


    private void setControl() {
        btnLuu = findViewById(R.id.btnLuu);
        txtTen = findViewById(R.id.txtTenCR);
        txtHo = findViewById(R.id.txtHoCR);
        cbbPhanXuong = findViewById(R.id.cbbPhanXuong);
        edtDate = findViewById(R.id.edtNgaySinhCR);
        imageCreate = findViewById(R.id.profile_imageCR);
        btnSelectPhoto = findViewById(R.id.btnChoosedImage);
        tvMaCN=findViewById(R.id.tvmaCNCR);
    }
}
