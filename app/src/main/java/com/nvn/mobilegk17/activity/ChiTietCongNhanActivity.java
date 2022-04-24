package com.nvn.mobilegk17.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.fragment.CongNhanFragment;
import com.nvn.mobilegk17.model.CongNhan;
import com.nvn.mobilegk17.util.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChiTietCongNhanActivity extends AppCompatActivity {


    String[] items={"Ha Noi","HCM","DA NANG"};
    EditText txtHo,txtTen,edtDate;
    TextView tvMaCN;
    ImageView hinhAnh;
    AutoCompleteTextView autoCompleteText;
    ArrayAdapter<String> adapterPhanXuong;
    Button btnSua,btnChamCong;
    FloatingActionButton btnEditImage;
    private  static final int IMAGE_PICK_CODE=1000;
    private  static  final  int PERMISSION_CODE=1001;
    String selectedImage,ImageOld;
    int viTri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_cong_nhan);

        autoCompleteText=findViewById(R.id.auto_complete_text);
        adapterPhanXuong=new ArrayAdapter<String>(this,R.layout.dropdown_item,items);
        autoCompleteText.setAdapter(adapterPhanXuong);
        setControl();
        setEvent();
        Bundle bundle=getIntent().getExtras();
        if(bundle==null)
        {
            return;
        }
        CongNhan congNhan= (CongNhan)bundle.get("object_user");
        viTri=bundle.getInt("vi_tri");
        tvMaCN.setText(congNhan.getMaCN());
        txtHo.setText(congNhan.getHoCN());
        txtTen.setText(congNhan.getTenCN());
        ImageOld=congNhan.getImageSrc();
        System.out.println("HINH_Nhat:"+ ImageOld);
//        Glide.with(getApplicationContext())
//                .load(congNhan.getImageSrc())
//                .into(hinhAnh);
        Picasso.get().load(Uri.fromFile(new File(congNhan.getImageSrc()+"")))
                .error(R.drawable.no_image)
                .into(hinhAnh);
        edtDate.setText(congNhan.getNgaySinh());
        for(int i=0;i< adapterPhanXuong.getCount();i++)
        {
            if(congNhan.getPhanXuong().equals(adapterPhanXuong.getItem(i)))
            {
                autoCompleteText.setText(adapterPhanXuong.getItem(i).toString(), false);

            }
        }
        btnEditImage.setVisibility(View.INVISIBLE);
        txtHo.setEnabled(false);
        txtTen.setEnabled(false);
        edtDate.setEnabled(false);
        autoCompleteText.setEnabled(false);
    }

    private void setEvent() {
        edtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                chonNgay();
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnSua.getText().toString().equals("Cập Nhật"))
                {
                    txtHo.setEnabled(true);
                    txtTen.setEnabled(true);
                    edtDate.setEnabled(true);
                    autoCompleteText.setEnabled(true);
                    btnSua.setText("Lưu");
                    btnEditImage.setVisibility(View.VISIBLE);
                }
                else
                {
                    String MaCN,Ho,Ten,PhanXuong,NgaySinh;
                    String imgSrc="";
                    Ho=txtHo.getText().toString();
                    Ten=txtTen.getText().toString();
                    PhanXuong=autoCompleteText.getText().toString();
                    NgaySinh=edtDate.getText().toString();
                    MaCN=tvMaCN.getText().toString();
                    System.out.println("Vị trí sửa:"+viTri);

                    if(selectedImage!=null)
                    {
                        imgSrc=selectedImage;
                    }
                    else
                    {
                        imgSrc=ImageOld;
                    }
                    CongNhan congNhanUpdate=new CongNhan(MaCN,Ho,Ten,PhanXuong,NgaySinh,imgSrc);
                    AlertDialog dlg=new AlertDialog
                            .Builder(ChiTietCongNhanActivity.this)
                            .setIcon(R.drawable.baseline_build_black_24dp)
                            .setTitle("Notification")
                            .setMessage("\n                  Cập nhật thành công!")

                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    getIntent().putExtra("user_update", congNhanUpdate);
                                    FragmentManager fragmentManager=getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("vi_tri", viTri);
                                    CongNhanFragment fragobj = new CongNhanFragment();

                                    fragobj.setArguments(bundle);
                                    fragmentTransaction.replace(R.id.frame_CTCN,fragobj);
                                    fragmentTransaction.commit();
                                    finish();

                                }
                            }).create() ;
                    dlg.show();
                }
            }
        });



        btnEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check runtime PERMISSION
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        //Permision not Granted.request it
                        String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                        //Show popup for runtime permission
                        requestPermissions(permissions,PERMISSION_CODE);
                    }
                    else
                    {
                        //permission already granted
                        pickImageFromGallery();
                    }
                }
            }

        });
        btnChamCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChamCongActivity.class);
                intent.putExtra("macn",tvMaCN.getText().toString());
                intent.putExtra("hoTen",txtHo.getText().toString()+ " "+txtTen.getText().toString());
                intent.putExtra("phanxuong",autoCompleteText.getText().toString());
                startActivity(intent);
            }
        });

    }


    private void pickImageFromGallery() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
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
//            hinhAnh.setImageURI(data.getData());
//            selectedImage=data.getData().toString();
            Picasso.get().load(data.getData())
                    .into(hinhAnh);
            String path= Utils.getRealPathFromURI(getApplicationContext(),data.getData())+"";
            selectedImage=path;
            System.out.println("selectedImage"+selectedImage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void chonNgay(){
        Calendar calendar=Calendar.getInstance();
        int ngay=calendar.get(Calendar.DATE);
        int thang=calendar.get(Calendar.MONTH);
        int nam=calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i,i1,i2);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyy");
                edtDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },nam,thang,ngay);
        datePickerDialog.show();
    }
    private void setControl() {
        txtHo=findViewById(R.id.txtHo);
        txtTen=findViewById(R.id.txtTen);
        hinhAnh=findViewById(R.id.profile_image);
        edtDate=findViewById(R.id.edtNgaySinh);
        tvMaCN=findViewById(R.id.tvmaCN);
        btnSua=findViewById(R.id.btnCapNhat);
        btnChamCong=findViewById(R.id.btnChamCong);
        btnEditImage=findViewById(R.id.btnEditImage);


    }
}