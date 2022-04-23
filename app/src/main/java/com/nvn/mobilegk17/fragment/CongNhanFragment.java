package com.nvn.mobilegk17.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.nvn.mobilegk17.R;
import com.nvn.mobilegk17.activity.ThemCNActivity;
import com.nvn.mobilegk17.adapter.CongNhanAdapter;
import com.nvn.mobilegk17.adapter.RecycleViewItemTouchHelper;
import com.nvn.mobilegk17.adapter.SanPhamAdapter;
import com.nvn.mobilegk17.database.DbCongNhan;
import com.nvn.mobilegk17.database.DbSanPham;
import com.nvn.mobilegk17.model.CongNhan;

import java.util.ArrayList;
import java.util.List;

public class CongNhanFragment extends Fragment {
    private RecyclerView rcvUsers;
    private CongNhanAdapter congNhanAdapter;
    private SearchView searchView;
    public List<CongNhan> mListCongNhan=new ArrayList<>();
    private FloatingActionButton btnThem;
    private int indexCN = -2,check=0;
    CongNhan congNhanXoa;
    DbCongNhan dbCongNhan;
    public CongNhanFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnThem = getActivity().findViewById(R.id.btnThem);
        rcvUsers = getActivity().findViewById(R.id.rcv_users);
        searchView = getActivity().findViewById(R.id.timkiem_congnhan);
        setEvent();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvUsers.setLayoutManager(linearLayoutManager);


        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcvUsers.addItemDecoration(itemDecoration);
        rcvUsers.setClickable(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecycleViewItemTouchHelper(0, ItemTouchHelper.LEFT, this::onSwipe);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvUsers);


        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        CongNhan congNhan = (CongNhan) bundle.get("user_update");
        indexCN = bundle.getInt("vi_tri");
        congNhanAdapter.updateItem(congNhan, indexCN);
        dbCongNhan.suaCongNhan(congNhan);
        congNhanAdapter.notifyDataSetChanged();
    }

    private String  taoMaCN(){
        if (mListCongNhan.size()>0){
            CongNhan congNhan= mListCongNhan.get(mListCongNhan.size()-1);
            String maCNHienTai=congNhan.getMaCN();
            System.out.println(maCNHienTai);
            String[] part = maCNHienTai.split("(?<=\\D)(?=\\d)");
            int maMoi=Integer.valueOf(part[1])+1;
            String maCNmoi="CN"+ maMoi;
            return maCNmoi ;
        }
       return "CN1";
    }
    private void setEvent() {
        initData();
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String maCNMoi=taoMaCN();
                Intent intent=new Intent(getContext(), ThemCNActivity.class);
                intent.putExtra("Mamoi",maCNMoi);
                startActivityForResult(intent,1);

            }



        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                congNhanAdapter.filter(newText);
                return false;
            }
        });
    }

    //Set chiều dài thanh search
    private void initDataCN() {

//        dbCongNhan.themCN(new CongNhan("CN1", "Nguyen", "Thu", "Ha Noi", "28/02/2000", "https://kenh14cdn.com/thumb_w/660/203336854389633024/2021/8/15/photo-1-1629034096768473359967.jpg"));
//        dbCongNhan.themCN(new CongNhan("CN2", "Trần", "Hoa", "Ha Noi", "28/02/2000", "https://htran844.github.io/hihi/img/ig/i7.jpg"));
//        dbCongNhan.themCN(new CongNhan("CN3", "Lê", "Tien", "HCM", "28/02/2000", "https://htran844.github.io/hihi/img/ig/i13.jpg"));
//        dbCongNhan.themCN(new CongNhan("CN4", "Hồng", "Vân", "HCM", "28/02/2000", "https://htran844.github.io/hihi/img/ig/i15.jpg"));
//        dbCongNhan.themCN(new CongNhan("CN5", "Nguyen", "Long", "HCM", "28/02/2000", "https://htran844.github.io/hihi/img/ig/i17.jpg"));
//        dbCongNhan.themCN(new CongNhan("CN6", "Nguyen", "Minh", "HCM", "28/02/2000", "https://htran844.github.io/hihi/img/ig/i27.jpg"));
//        dbCongNhan.themCN(new CongNhan("CN7", "Nguyen", "Vũ", "HCM", "28/02/2000", "https://htran844.github.io/hihi/img/ig/i30.jpg"));

    }
    @Override
    public void onResume() {
        super.onResume();
        loadCNDB();
    }
    private void loadCNDB() {
        mListCongNhan.clear();
        mListCongNhan.addAll(dbCongNhan.layCongNhan());
        congNhanAdapter = new CongNhanAdapter(getContext(),mListCongNhan);
        rcvUsers.swapAdapter(congNhanAdapter,false);
    }

    private void initData() {


        dbCongNhan = new DbCongNhan(getContext());
        initDataCN();
        mListCongNhan.addAll(dbCongNhan.layCongNhan());
        congNhanAdapter = new CongNhanAdapter(getContext(), mListCongNhan);
        rcvUsers.setAdapter(congNhanAdapter);
        rcvUsers.setHasFixedSize(true);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {


        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            Bundle bundle=intent.getExtras();
            CongNhan congNhan= (CongNhan) bundle.get("cnMoi");
            congNhanAdapter.addItem(congNhan);
            dbCongNhan.themCN(congNhan);
            congNhanAdapter.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }
    public void onSwipe(RecyclerView.ViewHolder viewHolder) {

        if (viewHolder instanceof CongNhanAdapter.UserViewHolder) {

            String nameUserDelete = mListCongNhan.get(viewHolder.getBindingAdapterPosition()).getHoCN() + " " + mListCongNhan.get(viewHolder.getBindingAdapterPosition()).getTenCN();
            final CongNhan congNhanDelete = mListCongNhan.get(viewHolder.getBindingAdapterPosition());
                congNhanXoa=congNhanDelete;
            final int indexDelete = viewHolder.getBindingAdapterPosition();
            congNhanAdapter.removeItem(indexDelete);

            Snackbar snackbar = Snackbar.make(rcvUsers, nameUserDelete + " Đã xoá !", Snackbar.LENGTH_SHORT);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    congNhanAdapter.undoItem(congNhanDelete, indexDelete);
                    check=1;

                }

            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();



        }
        if(check==1)
        {
            check=0;
            return;
        }
        else
        {
            dbCongNhan.xoaCongNhan(congNhanXoa);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cong_nhan, container, false);
    }
}