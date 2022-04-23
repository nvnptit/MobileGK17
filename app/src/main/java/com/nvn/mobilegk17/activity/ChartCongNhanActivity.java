package com.nvn.mobilegk17.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.database.DbCongNhan;
import com.nvn.mobilegk17.database.DbSanPham;
import com.nvn.mobilegk17.model.CongNhan;
import com.nvn.mobilegk17.model.SanPham;

import java.util.ArrayList;

public class ChartCongNhanActivity extends AppCompatActivity {
    PieChart pieChart;
    DbCongNhan dbCongNhan;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_cong_nhan);
        setControl();
        actionToolBar();
        setEvent();
    }

    private void actionToolBar() {
        toolbar.setTitle("    Biểu đồ Công Nhân");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //tạo nút home
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void setControl() {
        pieChart = findViewById(R.id.pie_chartCN);
        toolbar = findViewById(R.id.toolbar_chartCN);
    }
    private void setEvent() {
        //Initialize array list
        dbCongNhan = new DbCongNhan(getApplicationContext());
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        //Use for loop
        ArrayList<CongNhan> arrs = dbCongNhan.layCongNhan();
        int HN=0,HCM=0,DN=0,sum=0;
        ArrayList<Integer> a = new ArrayList();
        for ( CongNhan cn : arrs
        ) {
            switch (cn.getPhanXuong().charAt(cn.getPhanXuong().length()-1)){
                case 'i': {
                    HN++;
                    break;
                }
                case 'M': {
                    HCM++;
                    break;
                }
                case 'G': {
                    DN++;
                    break;
                }

            }
        }
        sum = HN+HCM+DN;

        pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(HN,"Hà Nội"));
        pieEntries.add(new PieEntry(HCM, "Hồ Chí Minh"));
        pieEntries.add(new PieEntry(DN, "Đà Nẵng"));




        //Initialize pie data set
        PieDataSet pieDataSet = new PieDataSet(pieEntries, ""); //Biểu đồ công nhân
        //Set colors
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //Set pie data
        pieChart.setData(new PieData(pieDataSet));
        //Set animation
        pieChart.animateXY(5000, 5000);
        //Hide description
        pieChart.getDescription().setEnabled(false);
    }
}