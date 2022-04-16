package com.nvn.mobilegk17.activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.database.DbSanPham;
import com.nvn.mobilegk17.model.SanPham;
import com.nvn.mobilegk17.util.Utils;
import com.squareup.picasso.Picasso;

public class ThemMoiActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 18;
    private static final String MY_TAG = ThemMoiActivity.class.toString();
    Toolbar toolbar;
    Button btnThem, btnDatLai;

    ImageView imageView;
    EditText tvTenSP, tvDonGiaSP;
    TextView tvMaSP, tvHinhSP;
    DbSanPham dbSanPham;

    private TextInputLayout textInputLayoutMaSP;
    private TextInputLayout textInputLayoutTenSP;
    private TextInputLayout textInputLayoutDonGiaSP;
    private TextInputLayout textInputLayoutHinhSP;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_moi);
        setControl();
        tvMaSP.setFocusable(true);
        setEvent();
        actionToolBar();
    }


    private void onRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] permission = {READ_EXTERNAL_STORAGE};
            requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        activityResultLauncher.launch(Intent.createChooser(intent, "Mời lựa chọn ảnh"));
    }

    private void setEvent() {
        catchData();
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkData()){
                    SanPham sanPham = new SanPham(
                            tvMaSP.getText().toString(),
                            tvTenSP.getText().toString(),
                            tvDonGiaSP.getText().toString(),
                            tvHinhSP.getText().toString()
                    );
                    dbSanPham.themSanPham(sanPham);
                    Toast.makeText(getApplicationContext(), "Thêm mới thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        btnDatLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvMaSP.setText("");
                tvTenSP.setText("");
                tvDonGiaSP.setText("");
                tvHinhSP.setText("Đính kèm ảnh");
                imageView.setImageDrawable(null);
            }
        });

        tvHinhSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRequestPermission();
            }
        });
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), (activityResult) -> {
                    Log.e(MY_TAG, "activityResult");
                    if (activityResult.getResultCode() == RESULT_OK) {
                        Intent dataIntent = activityResult.getData();
                        if (dataIntent != null) {
                            Picasso.get().load(dataIntent.getData())
                                    .into(imageView);
                            String path= Utils.getRealPathFromURI(getApplicationContext(),dataIntent.getData());
                            tvHinhSP.setText(path);
                        }
                    }
                }
        );
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbar_themsanpham);
        btnThem = findViewById(R.id.btnthem);
        btnDatLai = findViewById(R.id.btndatlai);
        imageView = findViewById(R.id.newimage);

        tvMaSP = findViewById(R.id.tv_newmaproduct);
        tvTenSP = findViewById(R.id.tv_newnameproduct);
        tvDonGiaSP = findViewById(R.id.tv_newpriceproduct);
        tvHinhSP = findViewById(R.id.tv_newimage);

        dbSanPham = new DbSanPham(this);

        textInputLayoutMaSP = findViewById(R.id.til_newmaproduct);
        textInputLayoutTenSP = findViewById(R.id.til_newnameproduct);
        textInputLayoutDonGiaSP = findViewById(R.id.til_newpriceproduct);
//        textInputLayoutHinhSP = findViewById(R.id.til_newimageproduct);
    }
    private boolean checkData() {
        boolean check = true;
        if (tvMaSP.getText().toString().trim().equals("")) {
            textInputLayoutMaSP.setError("Không được để trống!");
            return false;
        }
        if (tvTenSP.getText().toString().trim().equals("")) {
            textInputLayoutTenSP.setError("Không được để trống!");
            return false;
        }
        if (tvDonGiaSP.getText().toString().trim().equals("")) {
            textInputLayoutDonGiaSP.setError("Không được để trống!");
            return false;
        }
        if (!(textInputLayoutMaSP.getError() ==null)) {
            return false;
        }
        if (!(textInputLayoutTenSP.getError() ==null)) {
            return false;
        }
        if (!(textInputLayoutDonGiaSP.getError() ==null)) {
            return false;
        }
        return check;
    }

    private void catchData() {
        tvMaSP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String regexMaSP="\\w+";
                if (!charSequence.toString().isEmpty() && !charSequence.toString().matches(regexMaSP)) {
                    textInputLayoutMaSP.setError("Chỉ dùng các chữ cái và số");
                } else {
                    textInputLayoutMaSP.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        tvTenSP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String regexTenSP="\\w+";
                if (!charSequence.toString().isEmpty() && !charSequence.toString().matches(regexTenSP)) {
                    textInputLayoutTenSP.setError("Chỉ dùng các chữ cái và số");
                } else {
                    textInputLayoutTenSP.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        tvDonGiaSP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String regexPrice="\\d+";
                if (!charSequence.toString().isEmpty() && !charSequence.toString().matches(regexPrice)) {
                    textInputLayoutDonGiaSP.setError("Chỉ dùng số");
                } else {
                    textInputLayoutDonGiaSP.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //tạo nút home
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}