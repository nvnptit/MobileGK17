package com.nvn.mobilegk17.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.nvn.mobilegk17.model.CongNhan;

import java.util.ArrayList;
import java.util.List;

public class CongNhanFragment extends Fragment {
    private RecyclerView rcvUsers;
    private CongNhanAdapter congNhanAdapter;
    private SearchView searchView;
    public List<CongNhan> mListCongNhan;
    private FloatingActionButton btnThem;
    private int indexCN = -2;

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
        mListCongNhan = getListCongNhan();
        congNhanAdapter = new CongNhanAdapter(getContext(), mListCongNhan);
        rcvUsers.setAdapter(congNhanAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcvUsers.addItemDecoration(itemDecoration);
        rcvUsers.setClickable(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecycleViewItemTouchHelper(0, ItemTouchHelper.LEFT, this::onSwipe);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rcvUsers);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), ThemCNActivity.class), 1);

            }


        });
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        CongNhan congNhan = (CongNhan) bundle.get("user_update");
        indexCN = bundle.getInt("vi_tri");
        System.out.println("vi trí Cong nhan: " + indexCN);
        System.out.println("Ho sau khi sua :" + congNhan.getHoCN());
        congNhanAdapter.updateItem(congNhan, indexCN);
        congNhanAdapter.notifyDataSetChanged();
    }

    private void setEvent() {
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
    private List<CongNhan> getListCongNhan() {
        List<CongNhan> list = new ArrayList<>();
        list.add(new CongNhan("CN1", "Nguyen", "Thu", "Ha Noi", "28/02/2000", "https://kenh14cdn.com/thumb_w/660/203336854389633024/2021/8/15/photo-1-1629034096768473359967.jpg"));
        list.add(new CongNhan("CN2", "Trần", "Hoa", "Ha Noi", "28/02/2000", "https://htran844.github.io/hihi/img/ig/i7.jpg"));
        list.add(new CongNhan("CN3", "Lê", "Tien", "HCM", "28/02/2000", "https://htran844.github.io/hihi/img/ig/i13.jpg"));
        list.add(new CongNhan("CN4", "Hồng", "Vân", "HCM", "28/02/2000", "https://htran844.github.io/hihi/img/ig/i15.jpg"));
        list.add(new CongNhan("CN5", "Nguyen", "Long", "HCM", "28/02/2000", "https://htran844.github.io/hihi/img/ig/i17.jpg"));
        list.add(new CongNhan("CN6", "Nguyen", "Minh", "HCM", "28/02/2000", "https://htran844.github.io/hihi/img/ig/i27.jpg"));
        list.add(new CongNhan("CN7", "Nguyen", "Vũ", "HCM", "28/02/2000", "https://htran844.github.io/hihi/img/ig/i30.jpg"));
        return list;
    }

    public void onSwipe(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof CongNhanAdapter.UserViewHolder) {
            String nameUserDelete = mListCongNhan.get(viewHolder.getBindingAdapterPosition()).getHoCN() + " " + mListCongNhan.get(viewHolder.getBindingAdapterPosition()).getTenCN();
            final CongNhan congNhanDelete = mListCongNhan.get(viewHolder.getBindingAdapterPosition());
            final int indexDelete = viewHolder.getBindingAdapterPosition();
            congNhanAdapter.removeItem(indexDelete);

            Snackbar snackbar = Snackbar.make(rcvUsers, nameUserDelete + " Đã xoá !", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    congNhanAdapter.undoItem(congNhanDelete, indexDelete);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cong_nhan, container, false);
    }
}