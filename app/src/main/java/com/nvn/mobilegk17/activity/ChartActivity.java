package com.nvn.mobilegk17.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.database.DbSanPham;
import com.nvn.mobilegk17.model.SanPham;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {
    BarChart barChart;
    PieChart pieChart;
    DbSanPham dbSanPham;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setControl();
        actionToolBar();
        setEvent();
    }
    private void actionToolBar() {
        toolbar.setTitle("    Biểu đồ sản phẩm");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //tạo nút home
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void setEvent() {
        //Initialize array list
        dbSanPham = new DbSanPham(getApplicationContext());
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        //Use for loop
        ArrayList<SanPham> arrs = dbSanPham.laySanPham();
        int A=0,B=0,D=0,N=0,Q=0,sum=0;
        ArrayList<Integer> a = new ArrayList();
        for ( SanPham sp : arrs
             ) {
                switch (sp.getMaSP().charAt(0)){
                    case 'A': {
                        A++;
                        break;
                    }
                    case 'B': {
                        B++;
                        break;
                    }
                    case 'D': {
                        D++;
                        break;
                    }
                    case 'N': {
                        N++;
                        break;
                    }
                    case 'Q': {
                        Q++;
                        break;
                    }
                }
        }
        sum = A+B+D+N+Q;
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, (A* 100 /sum )));
        barEntries.add(new BarEntry(2,(B* 100 /sum )));
        barEntries.add(new BarEntry(3, (D* 100 /sum )));
        barEntries.add(new BarEntry(4,(N* 100 /sum )));
        barEntries.add(new BarEntry(5,(Q* 100 /sum )));

        pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(A,"Áo sơ mi"));
        pieEntries.add(new PieEntry(B, "Bao tay"));
        pieEntries.add(new PieEntry(D, "Dép"));
        pieEntries.add(new PieEntry(N,"Nón"));
        pieEntries.add(new PieEntry(Q,"Quần tây"));

        //Initialize bar data set
        BarDataSet barDataSet = new BarDataSet(barEntries, "Sản phẩm");
        //Set colors
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //Hide draw value
        barDataSet.setDrawValues(false);
        //Set bar data
        barChart.setData(new BarData(barDataSet));
        //Set animation
        barChart.animateY(5000);
        //Set description text and color
        barChart.getDescription().setText("");

        //Initialize pie data set
        PieDataSet pieDataSet = new PieDataSet(pieEntries, ""); //Biểu đồ sản phẩm
        //Set colors
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //Set pie data
        pieChart.setData(new PieData(pieDataSet));
        //Set animation
        pieChart.animateXY(5000, 5000);
        //Hide description
        pieChart.getDescription().setEnabled(false);
    }

    private void setControl() {
        barChart = findViewById(R.id.bar_chart);
        pieChart = findViewById(R.id.pie_chart);
        toolbar = findViewById(R.id.toolbar_chart);
    }
}