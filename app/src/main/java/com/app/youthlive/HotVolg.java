package com.app.youthlive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.app.youthlive.vlogListPOJO.vlogListBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HotVolg extends Fragment {

    RecyclerView recyclerView;
    GridLayoutManager manager;
    HotAdapter adapter;
    List<com.app.youthlive.vlogListPOJO.Datum> list;
    ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.hot, container, false);

        progress = view.findViewById(R.id.progress);

        list = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        manager = new GridLayoutManager(getContext(), 2);
        adapter = new HotAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        loadData();

    }

    public void loadData() {
        progress.setVisibility(View.VISIBLE);
        final bean b = (bean) getContext().getApplicationContext();

        Call<vlogListBean> call = b.getRetrofit().getVlogList(SharePreferenceUtils.getInstance().getString("userId"));

        Log.d("userId", SharePreferenceUtils.getInstance().getString("userId"));

        call.enqueue(new Callback<vlogListBean>() {
            @Override
            public void onResponse(Call<vlogListBean> call, Response<vlogListBean> response) {

                try {
                    adapter.setGridData(response.body().getData());
                    b.vlist = response.body().getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<vlogListBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });
    }
}
