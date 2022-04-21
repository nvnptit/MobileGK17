package com.nvn.mobilegk17;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemTouchHelperListener{
    private RecyclerView rcvUsers;
    private    CongNhanAdapter congNhanAdapter;
    private SearchView searchView;
    public   List<CongNhan> mListCongNhan;
    private FloatingActionButton btnThem;
    private int indexCN=-2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnThem=findViewById(R.id.btnThem);
        rcvUsers=findViewById(R.id.rcv_users);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        rcvUsers.setLayoutManager(linearLayoutManager);
        mListCongNhan=getListCongNhan();

        congNhanAdapter=new CongNhanAdapter(this,mListCongNhan);

        rcvUsers.setAdapter(congNhanAdapter);




        RecyclerView.ItemDecoration itemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcvUsers.addItemDecoration(itemDecoration);
        rcvUsers.setClickable(true);
        ItemTouchHelper.SimpleCallback simpleCallback=new RecycleViewItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvUsers);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, ThemCNActivity.class), 1);
            }


        });

        Bundle bundle = getIntent().getExtras();
        if(bundle==null)
        {
            return;
        }
        CongNhan congNhan= (CongNhan)bundle.get("user_update");

        indexCN=bundle.getInt("vi_tri");
        System.out.println("vi trí Cong nhan: "+indexCN);
        System.out.println("Ho sau khi sua :"+congNhan.getHoCN());
        congNhanAdapter.updateItem(congNhan,indexCN);
        congNhanAdapter.notifyDataSetChanged();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {


        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            Bundle bundle=intent.getExtras();
            CongNhan congNhan= (CongNhan) bundle.get("cnMoi");
            System.out.println("Ten CN"+congNhan.getTenCN());
            congNhanAdapter.addItem(congNhan);
            congNhanAdapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }


    private List<CongNhan> getListCongNhan() {
        List<CongNhan> list=new ArrayList<>();
        list.add(new CongNhan("CN1","Nguyen", "Thu","Ha Noi","28/02/2000","https://kenh14cdn.com/thumb_w/660/203336854389633024/2021/8/15/photo-1-1629034096768473359967.jpg"));
        list.add(new CongNhan("CN2","Trần", "Hoa","Ha Noi","28/02/2000","https://htran844.github.io/hihi/img/ig/i7.jpg"));
        list.add(new CongNhan("CN3","Lê", "Tien","HCM","28/02/2000","https://htran844.github.io/hihi/img/ig/i13.jpg"));
        list.add(new CongNhan("CN4","Hồng", "Vân","HCM","28/02/2000","https://htran844.github.io/hihi/img/ig/i15.jpg"));
        list.add(new CongNhan("CN5","Nguyen", "Long","HCM","28/02/2000","https://htran844.github.io/hihi/img/ig/i17.jpg"));
        list.add(new CongNhan("CN6","Nguyen", "Minh","HCM","28/02/2000","https://htran844.github.io/hihi/img/ig/i27.jpg"));
        list.add(new CongNhan("CN7","Nguyen", "Vũ","HCM","28/02/2000","https://htran844.github.io/hihi/img/ig/i30.jpg"));

        return list;
    }
    //Set chiều dài thanh search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_cn,menu);
        SearchManager searchManager=(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView= (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                congNhanAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                congNhanAdapter.getFilter().filter(newText);

                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified())
        {
            searchView.setIconified(true);
            return;
        }

        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(congNhanAdapter!=null){
            congNhanAdapter.release();
        }
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof CongNhanAdapter.UserViewHolder){
            String nameUserDelete=mListCongNhan.get(viewHolder.getBindingAdapterPosition()).hoCN+" "+mListCongNhan.get(viewHolder.getBindingAdapterPosition()).tenCN;
            final  CongNhan congNhanDelete=mListCongNhan.get(viewHolder.getBindingAdapterPosition());
           final int indexDelete=viewHolder.getBindingAdapterPosition();
            congNhanAdapter.removeItem(indexDelete);

            Snackbar snackbar=Snackbar.make(rcvUsers,nameUserDelete+" Đã xoá !",Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    congNhanAdapter.undoItem(congNhanDelete,indexDelete);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}